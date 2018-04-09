package com.sixtyfour.cbmnative;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.sixtyfour.Basic;
import com.sixtyfour.Logger;
import com.sixtyfour.cbmnative.mos6502.C64Platform;
import com.sixtyfour.elements.commands.Command;
import com.sixtyfour.parser.Atom;
import com.sixtyfour.parser.Line;
import com.sixtyfour.parser.Term;
import com.sixtyfour.parser.cbmnative.CodeContainer;
import com.sixtyfour.parser.optimize.ConstantPropagator;
import com.sixtyfour.parser.optimize.DeadStoreEliminator;
import com.sixtyfour.system.CompilerConfig;
import com.sixtyfour.system.Machine;

/**
 * @author EgonOlsen
 * 
 */
public class NativeCompiler {

	private final static Set<String> SINGLES = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			this.add("!");
			this.add("SIN");
			this.add("COS");
			this.add("TAN");
			this.add("ATN");
			this.add("EXP");
			this.add("LOG");
			this.add("INT");
			this.add("ABS");
			this.add("SGN");
			this.add("SQR");
			this.add("RND");
			this.add("CHR");
			this.add("ASC");
			this.add("STR");
			this.add("VAL");
			this.add("POS");
			this.add("TAB");
			this.add("SPC");
			this.add("LEN");
			this.add("USR");
			this.add("PEEK");
			this.add("MID");
			this.add("PAR");
			this.add("SINGLEPAR");
			this.add("LEFT");
			this.add("RIGHT");
			this.add("ARRAYACCESS");
		}
	};

	private final static Set<String> STRING_OPERATORS = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			this.add("CHR");
			this.add(".");
			this.add("STR");
			this.add("MID");
			this.add("LEFT");
			this.add("RIGHT");
			this.add("TAB");
			this.add("SPC");
		}
	};

	private static NativeCompiler instance = new NativeCompiler();

	public static NativeCompiler getCompiler() {
		return instance;
	}

	public List<String> compile(Basic basic) {
		basic.compile();
		List<String> mCode = NativeCompiler.getCompiler().compileToPseudeCode(basic);

		PlatformProvider platform = new C64Platform();
		List<String> nCode = platform.getTransformer().transform(basic.getMachine(), platform, mCode);
		if (platform.getOptimizer() != null && CompilerConfig.getConfig().isNativeLanguageOptimizations()) {
			nCode = platform.getOptimizer().optimize(platform, nCode);
		}
		if (platform.getUnlinker() != null && CompilerConfig.getConfig().isOptimizedLinker()) {
			nCode = platform.getUnlinker().unlink(nCode);
		}
		return nCode;
	}

	public List<String> compileToPseudeCode(Basic basic) {
		long s = System.currentTimeMillis();
		Machine machine = basic.getMachine();
		PCode pCode = basic.getPCode();
		ConstantPropagator.propagateConstants(machine);
		DeadStoreEliminator.eliminateDeadStores(basic);
		
		List<String> mCode = new ArrayList<String>();
		for (Integer lineNumber : pCode.getLineNumbers()) {
			Line line = pCode.getLines().get(lineNumber);
			mCode.add(lineNumber + ":");
			boolean condi = false;
			for (Command cmd : line.getCommands()) {
				if (!condi) {
					mCode.addAll(compileToPseudoCode(machine, cmd));
				} else {
					// Place conditional code blocks inside of the conditional
					// area. This should now handle nested ifs as well.
					for (int i = mCode.size() - 1; i >= 0; i--) {
						if (!mCode.get(i).startsWith("SKIP")) {
							mCode.addAll(i + 1, compileToPseudoCode(machine, cmd));
							break;
						}
					}
					// mCode.addAll(mCode.size()-1, compileToPseudoCode(machine,
					// cmd));
				}
				if (cmd.isConditional()) {
					condi = true;
				}
			}
		}

		int os = mCode.size();
		mCode = optimize(mCode);

		Logger.log("Code optimized: " + os + " => " + mCode.size() + " lines!");

		if (!getLastEntry(mCode).equals("RTS")) {
			mCode.add("JSR END");
			mCode.add("RTS");
		}
		mCode.add(0, "JSR START");
		mCode.add(0, "PROGRAMSTART:");

		Logger.log("Compiled to pseudo code in: " + (System.currentTimeMillis() - s) + "ms");
		return mCode;
	}

	public List<String> compileToPseudoCode(Machine machine, Command command) {
		return compileToPseudoCodeInternal(machine, command);
	}

	public List<String> compileToPseudoCode(Machine machine, Term term) {
		Atom atom = TermHelper.linearize(machine, term);
		return compileToPseudoCode(machine, atom);
	}

	public List<String> compileToPseudoCode(Machine machine, Atom term) {
		String tr = null;
		String sr = null;
		boolean pointerMode = false;
		int contextMode = 0;
		int modeSwitchCnt = 0;
		Set<String> floatRegs = new HashSet<String>() {
			private static final long serialVersionUID = 1L;
			{
				this.add("X");
				this.add("Y");
			}
		};

		List<String> code = new ArrayList<String>();
		List<String> expr = term.evalToCode(machine).get(0).getExpression();

		Deque<String> stack = new LinkedList<String>();
		Deque<String> yStack = new LinkedList<String>();
		Deque<Boolean> stringStack = new LinkedList<Boolean>();
		boolean withStrings = false;
		boolean left = false;
		boolean right = false;
		boolean isArrayAccess = false;
		Set<Integer> fromAbove = new HashSet<Integer>();

		int expCnt = 0;
		for (String exp : expr) {
			expCnt++;
			boolean isOp = exp.startsWith(":");
			boolean isBreak = exp.equals("_");
			isArrayAccess = false;
			String osr = sr;
			if (exp.contains("{")) {
				if (exp.contains("{STRING") || exp.contains("[]")) {
					modeSwitchCnt++;
					String add = null;
					if (!pointerMode) {
						if (modeSwitchCnt > 1 && !code.isEmpty() && exp.contains("{STRING") && contextMode != 1) {
							add = "CHGCTX #1";
						}
					}
					contextMode = 1;
					pointerMode = true;
					if (!exp.contains("[]")) {
						tr = "A";
						sr = "B";
						withStrings = true;
					} else {
						tr = "G";
						sr = "G";
						isArrayAccess = true;
						if (exp.contains("{STRING")) {
							withStrings = true;
							stringStack.push(true);
						} else {
							stringStack.push(false);
						}

						if (this.getLastEntry(code).equals("PUSH X")) {
							yStack.pop();
							code.remove(code.size() - 1);
						}

						if (right && left) {
							code.add("PUSH " + osr);
							yStack.push(null);
							right = false;
						}
					}
					if (add != null) {
						if (right && !isArrayAccess) {
							code.add("PUSH Y");
							yStack.push(null);
							right = false;
						}
						if (left && !isArrayAccess) {
							code.add("PUSH X");
							yStack.push(null);
							left = false;
						}
						code.add(add);
					}
				} else {
					modeSwitchCnt++;
					if (pointerMode) {
						if (modeSwitchCnt > 1 && !code.isEmpty() && contextMode != 0) {
							code.add("CHGCTX #0");
							if (right) {
								code.add("PUSH B");
								yStack.push(null);
								right = false;
							}
							if (left) {
								code.add("PUSH A");
								yStack.push(null);
								left = false;
							}
						}
					}
					contextMode = 0;
					pointerMode = false;
					tr = "X";
					sr = "Y";
				}
			}

			if (!isBreak) {
				if (!isOp) {
					if (!right || isArrayAccess) {
						code.add("MOV " + (isArrayAccess ? "G" : sr) + "," + exp);
						right = true;
					} else if (!left) {
						code.add("MOV " + tr + "," + exp);
						left = true;
					} else {
						// throw new
						// RuntimeException("No free registers left to handle
						// "+exp);
					}

				}
			}

			if (isOp && right && !left) {
				String lc = getLastEntry(code);
				if (lc.startsWith("MOV " + sr + "") && !lc.contains("[]")) {
					yStack.push(lc);
					code.remove(code.size() - 1);
				} else {
					if (!isParameterRegister(sr)) {
						code.add("PUSH " + sr);
						yStack.push(null);
					}
				}
				right = false;
			}

			if (isBreak) {
				String ex = stack.pop();
				String op = ex.replace(":", "");
				boolean isSingle = isSingle(op);
				if (op.startsWith("ARRAYACCESS")) {
					if ("Y".equals(getLastFilledRegister(code, 1, floatRegs))) {
						// Move an array index from Y to x if needed
						code.add(code.size() - (code.get(code.size() - 2).startsWith("CHGCTX") ? 2 : 1), "MOV X,Y");
					}
				}

				boolean isStringArrayAccess = (!stringStack.isEmpty() && stringStack.peek() && contextMode == 1);

				if (!left && !isSingle) {
					if (code.size() >= 1 && getLastEntry(code).equals("PUSH " + tr)) {
						code.remove(code.size() - 1);
						yStack.pop();
					} else {
						if (code.size() >= 2 && code.get(code.size() - 2).equals("PUSH " + tr) && getLastEntry(code).startsWith("MOV " + sr)) {
							code.remove(code.size() - 2);
							yStack.pop();
						} else {
							if (popy(code, sr, tr, sr, tr, false)) {
								yStack.pop();
							}
						}
					}
					left = true;
				}

				if (!right) {

					String ntr = tr;
					String nsr = sr;

					if (STRING_OPERATORS.contains(op) || isStringArrayAccess) {
						if (!pointerMode || isStringArrayAccess) {
							if (modeSwitchCnt > 1 && !code.isEmpty()) {
								ntr = "A";
								nsr = "B";
								withStrings = true;
							}
						}

					} else {
						if (pointerMode) {
							if (modeSwitchCnt > 1 && !code.isEmpty()) {
								ntr = "X";
								nsr = "Y";
							}
						}
					}

					boolean mayPop = !op.equals("ARRAYACCESS");
					if (mayPop) {
						if (yStack.isEmpty()) {
							popy(code, tr, sr, ntr, nsr, true);
						} else {
							String v = yStack.pop();
							if (v == null) {
								if (!popy(code, tr, sr, ntr, nsr, false)) {
									yStack.push(null);
								}
							} else {
								code.add(v);
								fromAbove.add(code.size() - 1);
							}
						}
					}
					right = true;
				}

				if (!tr.equals(sr)) {
					if (!code.isEmpty() && getLastEntry(code).startsWith("MOV " + sr) && !getLastEntry(code).equals("MOV " + sr + "," + tr) && !fromAbove.contains(code.size() - 1)) {
						// code.add("SWAP X,Y");
						// Swap register usage if needed
						code.add(code.size() - 1, "MOV " + sr + "," + tr);
						code.set(code.size() - 1, getLastEntry(code).replace("MOV " + sr + ",", "MOV " + tr + ","));
					} else {
						// Fix wrong register order for single operand function
						// calls
						if (isSingle && !code.isEmpty() && getLastEntry(code).startsWith("MOV " + tr)) {
							String lmt = getLastMoveTarget(code, 2);
							code.add(code.size() - 1, "PUSH " + lmt);
							code.set(code.size() - 1, getLastEntry(code).replace("MOV " + tr + ",", "MOV " + sr + ","));
							yStack.push(null);
						}
					}
				}

				if (STRING_OPERATORS.contains(op) || isStringArrayAccess) {
					modeSwitchCnt++;
					if (!pointerMode) {
						if (modeSwitchCnt > 1 && !code.isEmpty() && contextMode != 1) {
							code.add("CHGCTX #1");
						}
					}
					contextMode = 1;
					pointerMode = true;
					tr = "A";
					sr = "B";
					withStrings = true;
				} else {
					modeSwitchCnt++;
					if (pointerMode) {
						if (modeSwitchCnt > 1 && !code.isEmpty() && contextMode != 0) {
							code.add("CHGCTX #0");
						}
					}
					contextMode = 0;
					pointerMode = false;
					tr = "X";
					sr = "Y";
				}
				String regs = pointerMode ? "A,B" : "X,Y";

				boolean dontPush = false;

				switch (op) {
				case "+":
					code.add("ADD " + regs);
					break;
				case "-":
					code.add("SUB " + regs);
					break;
				case "*":
					code.add("MUL " + regs);
					break;
				case "/":
					code.add("DIV " + regs);
					break;
				case "^":
					code.add("POW " + regs);
					break;
				case "|":
					code.add("OR " + regs);
					break;
				case "&":
					code.add("AND " + regs);
					break;
				case "!":
					code.add("NOT " + regs);
					break;
				case "SIN":
					code.add("SIN " + regs);
					break;
				case "COS":
					code.add("COS " + regs);
					break;
				case "LOG":
					code.add("LOG " + regs);
					break;
				case "SQR":
					code.add("SQR " + regs);
					break;
				case "INT":
					code.add("INT " + regs);
					break;
				case "ABS":
					code.add("ABS " + regs);
					break;
				case "SGN":
					code.add("SGN " + regs);
					break;
				case "TAN":
					code.add("TAN " + regs);
					break;
				case "ATN":
					code.add("ATN " + regs);
					break;
				case "EXP":
					code.add("EXP " + regs);
					break;
				case "RND":
					code.add("RND " + regs);
					break;
				case "PEEK":
					code.add("MOVB " + regs.replace(",", ",(") + ")");
					break;
				case ".":
					code.add("JSR CONCAT");
					break;
				case "USR":
					code.add("JSR USR");
					break;
				case "CHR":
					code.add("JSR CHR");
					break;
				case "STR":
					code.add("JSR STR");
					break;
				case "VAL":
					code.add("JSR VAL");
					break;
				case "ASC":
					code.add("JSR ASC");
					break;
				case "LEN":
					code.add("JSR LEN");
					break;
				case "TAB":
					code.add("JSR TAB");
					break;
				case "SPC":
					code.add("JSR SPC");
					break;
				case "POS":
					code.add("JSR POS");
					break;
				case "FRE":
					code.add("JSR FRE");
					break;
				case "ARRAYACCESS":
					code.add("JSR ARRAYACCESS");
					stringStack.pop();
					break;
				case "MID":
					code.add("POP D");
					code.add("POP C");
					code.add("JSR MID");
					break;
				case "LEFT":
					code.add("POP C");
					code.add("JSR LEFT");
					break;
				case "RIGHT":
					code.add("POP C");
					code.add("JSR RIGHT");
					break;
				case "CMP =":
					code.add("EQ " + regs);
					break;
				case "CMP >":
					code.add("GT " + regs);
					break;
				case "CMP <":
					code.add("LT " + regs);
					break;
				case "CMP >=":
					code.add("GTEQ " + regs);
					break;
				case "CMP <=":
					code.add("LTEQ " + regs);
					break;
				case "CMP <>":
					code.add("NEQ " + regs);
					break;
				case "SCMP =":
					code.add("JSR SEQ");
					break;
				case "SCMP >":
					code.add("JSR SGT");
					break;
				case "SCMP <":
					code.add("JSR SLT");
					break;
				case "SCMP >=":
					code.add("JSR SGTEQ");
					break;
				case "SCMP <=":
					code.add("JSR SLTEQ");
					break;
				case "SCMP <>":
					code.add("JSR SNEQ");
					break;
				case "PAR":
					if (getLastEntry(code).startsWith("MOV " + sr)) {
						code.add("MOV C," + sr);
					} else {
						code.add("MOV C," + tr);
					}
					code.add("PUSH C");
					// yStack.push(null);
					dontPush = true;
					break;
				default:
					if (op.startsWith("FN ")) {
						String label = op.substring(2).trim();
						code.add("PUSH " + sr);
						yStack.push(null);
						code.add("JSR " + label);
						if (expCnt >= expr.size()) {
							// POP the last result from the fn call, if that's
							// needed...Am I sure, that this is 100% water
							// proof...? Well...no, actually not...
							code.add("POP " + tr);
						}
						dontPush = true;
						break;
					} else {
						throw new RuntimeException("Unknown operator: " + op);
					}
				}
				if (!dontPush) {
					if (!isParameterRegister(tr)) {
						code.add("PUSH " + tr);
						yStack.push(null);
					}
				}
				dontPush = false;
				left = false;
				right = false;
			}

			if (isOp) {
				stack.push(exp);
			}

			// System.out.println(code.size() + ": " + this.getLastEntry(code) +
			// " / " + exp + "/" + sr);
		}

		if (!stack.isEmpty()) {
			throw new RuntimeException("Operator stack not empty, " + stack.size() + " element(s) remaining!");
		}

		// End simple expressions properly
		if (!code.isEmpty() && !getLastEntry(code).equals("PUSH " + tr)) {
			String cl = getLastEntry(code);
			if (cl.startsWith("MOV " + sr)) {
				if (!isParameterRegister(sr)) {
					code.add("PUSH " + sr);
				}
			} else {
				if (!isParameterRegister(tr)) {
					code.add("PUSH " + tr);
				}
			}
		}

		if (withStrings) {
			code.add(0, "JSR COMPACT");
		}
		/*
		 * if (!yStack.isEmpty()) { code.add("POP X"); }
		 */
		return optimize(code);
	}

	private List<String> optimize(List<String> code) {
		return NativeOptimizer.optimizeNative(code);
	}

	private String getLastMoveTarget(List<String> code, int offset) {
		for (int i = code.size() - offset; i >= 0; i--) {
			if (code.get(i).startsWith("MOV ")) {
				String reg = code.get(i).substring(4, code.get(i).indexOf(",")).trim();
				if (reg.length() == 1) {
					return reg;
				}
			}
		}
		return null;
	}

	private String getLastFilledRegister(List<String> code, int offset, Set<String> allowed) {
		for (int i = code.size() - offset; i >= 0; i--) {
			if (code.get(i).indexOf(" ") == 3) {
				int pos = code.get(i).indexOf(",");
				if (pos > 4) {
					String reg = code.get(i).substring(4, pos).trim();
					if (reg.length() == 1 && allowed.contains(reg)) {
						return reg;
					}
				}
			}
		}
		return null;
	}

	private String getLastEntry(List<String> code) {
		if (code.size() > 0) {
			return code.get(code.size() - 1);
		}
		return null;
	}

	private boolean popy(List<String> code, String tr, String sr, String ntr, String nsr, boolean stackEmpty) {
		if (getLastEntry(code) != null) {
			if (getLastEntry(code).equals("PUSH " + tr)) {
				code.set(code.size() - 1, "MOV " + sr + "," + tr);
			} else {
				if (getLastEntry(code).equals("PUSH " + nsr)) {
					code.remove(code.size() - 1);
				} else {
					if (!stackEmpty && !isParameterRegister(nsr)) {
						code.add("POP " + nsr);
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean isSingle(String op) {
		return SINGLES.contains(op.toUpperCase(Locale.ENGLISH)) || op.startsWith("FN ");
	}

	private boolean isParameterRegister(String reg) {
		return reg.equals("C") || reg.equals("D") || reg.equals("G");
	}

	private List<String> compileToPseudoCodeInternal(Machine machine, Atom atom) {
		List<String> mCode = new ArrayList<String>();
		List<CodeContainer> ccs = atom.evalToCode(machine);
		for (CodeContainer cc : ccs) {
			mCode.addAll(cc.getPseudoBefore());
			mCode.addAll(cc.getExpression());
			mCode.addAll(cc.getPseudoAfter());
		}
		return mCode;
	}

}
