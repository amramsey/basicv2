package com.sixtyfour.cbmnative.mos6502;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sixtyfour.Logger;
import com.sixtyfour.cbmnative.PlatformProvider;
import com.sixtyfour.cbmnative.Util;
import com.sixtyfour.config.CompilerConfig;

/**
 * Bundles Integer optimizations, because they look quite messy...
 * 
 * @author EgonOlsen
 *
 */
public class IntOptimizer {

	public List<String> applyIntOptimizations(CompilerConfig conf, PlatformProvider platform, List<String> input,
			int[] startAndEnd) {

		// if (true) return input;

		Map<String, Number> const2Value = extractConstants(input);
		Map<String, String> strConst2Value = extractStringConstants(input);
		int[] ps = startAndEnd;
		int codeStart = ps[0];
		int codeEnd = ps[1];
		List<IntPattern> intPatterns = new ArrayList<>();

		// intvar+<const> - Variant 1
		intPatterns.add(new IntPattern(true, "Optimized code for adding INTs (1)",
				new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}", "JSR COPY2_XYA_YREG", "LDY {*}", "LDA {*}",
						"JSR INTFAC", "JSR FACXREG", "JSR YREGFAC", "LDA #<X_REG", "LDY #>X_REG", "JSR FASTFADDMEM" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						double numd = num.doubleValue();
						if (numd == (int) numd && numd >= 0 && numd <= 16383) {
							String numHex = getHex(numd);
							List<String> rep = new ArrayList<>();
							rep.add("LDY #$" + numHex.substring(0, 2));
							rep.add("LDA #$" + numHex.substring(2));
							rep.add("STA TMP3_ZP");
							rep.add("STY TMP3_ZP+1");
							rep.add(cleaned.get(3));
							rep.add(cleaned.get(4));
							rep.add("JSR INTADD");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// intvar+<const> - Variant 2
		intPatterns.add(new IntPattern(true, "Optimized code for adding INTs (2)",
				new String[] { "LDY {*}", "LDA {*}", "JSR INTFAC", "LDA #<{CONST0}", "LDY #>{CONST0}",
						"JSR COPY2_XYA_XREG", "LDA #<X_REG", "LDY #>X_REG", "JSR FASTFADDMEM" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(3);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						double numd = num.doubleValue();
						if (numd == (int) numd && numd >= 0 && numd <= 16383) {
							String numHex = getHex(numd);
							List<String> rep = new ArrayList<>();
							rep.add("LDY #$" + numHex.substring(0, 2));
							rep.add("LDA #$" + numHex.substring(2));
							rep.add("STA TMP3_ZP");
							rep.add("STY TMP3_ZP+1");
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							rep.add("JSR INTADD");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// intvar+-intvar
		intPatterns.add(new IntPattern(true, "Optimized code for adding/subtracting INT variables",
				new String[] { "LDY {MEM0}", "LDA {MEM0}", "JSR INTFAC", "JSR FACYREG", "LDY {MEM1}", "LDA {MEM1}",
						"JSR INTFAC", "JSR FACXREG", "JSR YREGFAC", "LDA #<X_REG", "LDY #>X_REG", "JSR FAST{*}" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String func = cleaned.get(11);
						if (func.contains("FASTFADDMEM") || func.contains("FASTFSUBMEM")) {
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(4));
							rep.add(cleaned.get(5));
							rep.add("STY TMP3_ZP");
							rep.add("STA TMP3_ZP+1");
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							if (func.contains("FASTFADDMEM")) {
								rep.add("JSR INTADDVAR");
							} else {
								rep.add("JSR INTSUBVAR");
							}
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));
		
		// if l%=h% etc.
		intPatterns.add(new IntPattern(true, "Optimized code for Integer(1)",
				new String[] { "LDY {*}", "LDA {*}", "JSR INTFAC", "JSR FACYREG", "LDY {*}", "LDA {*}", "JSR INTFAC",
						"JSR FACXREG", "JSR YREGFAC", "LDA #<X_REG", "LDY #>X_REG", "JSR CMPFAC" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						List<String> rep = new ArrayList<>();
						rep.add(cleaned.get(0));
						rep.add(cleaned.get(1));
						rep.add("STY TMP_ZP");
						rep.add("STA TMP_ZP+1");
						rep.add(cleaned.get(4));
						rep.add(cleaned.get(5));
						rep.add("JSR ICMP");
						return combine(pattern, rep);
					}
				}));

		// if l%=123 etc.
		intPatterns.add(new IntPattern(true, "Optimized code for Integer(2)",
				new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}", "JSR COPY2_XYA_YREG", "LDY {*}", "LDA {*}",
						"JSR INTFAC", "JSR FACXREG", "JSR YREGFAC", "LDA #<X_REG", "LDY #>X_REG", "JSR CMPFAC" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						double numd = num.doubleValue();
						if (numd == (int) numd && numd >= -32768 && numd < 32768) {
							String numHex = getHex(numd);
							List<String> rep = new ArrayList<>();
							rep.add("LDA #$" + numHex.substring(2));
							rep.add("LDY #$" + numHex.substring(0, 2));
							rep.add("STA TMP_ZP");
							rep.add("STY TMP_ZP+1");
							rep.add(cleaned.get(3));
							rep.add(cleaned.get(4));
							rep.add("JSR ICMP");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// if 123=l% etc.
		intPatterns.add(new IntPattern(true, "Optimized code for Integer(3)",
				new String[] { "LDY {*}", "LDA {*}", "JSR INTFAC", "JSR FACYREG", "LDA #<{CONST0}", "LDY #>{CONST0}",
						"JSR COPY2_XYA_XREG", "JSR YREGFAC", "LDA #<X_REG", "LDY #>X_REG", "JSR CMPFAC" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(4);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						double numd = num.doubleValue();
						if (numd == (int) numd && numd >= -32768 && numd < 32768) {
							String numHex = getHex(numd);
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							rep.add("STY TMP_ZP");
							rep.add("STA TMP_ZP+1");
							rep.add("LDY #$" + numHex.substring(2));
							rep.add("LDA #$" + numHex.substring(0, 2));
							rep.add("JSR ICMP");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// if len(a$)=12 etc.
		intPatterns.add(new IntPattern(true, "Optimized code for LEN(1)",
				new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}", "JSR REALFACPUSH", "LDA {*}", "LDY {*}", "STA B_REG",
						"STY B_REG+1", "JSR LEN", "JSR POPREAL", "JSR FACYREG", "LDA #<X_REG", "LDY #>X_REG",
						"JSR CMPFAC" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						double numd = num.doubleValue();
						if (numd == (int) numd && numd >= -32768 && numd < 32768) {
							String numHex = getHex(numd);
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(3));
							rep.add(cleaned.get(4));
							rep.add(cleaned.get(5));
							rep.add(cleaned.get(6));
							rep.add(cleaned.get(7));
							rep.add("LDA #$" + numHex.substring(2));
							rep.add("LDY #$" + numHex.substring(0, 2));
							rep.add("STA TMP_ZP");
							rep.add("STY TMP_ZP+1");
							rep.add("LDY TMP2_ZP");
							rep.add("LDA #0");
							rep.add("JSR ICMP");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// if 12=len(a$) etc.
		intPatterns.add(new IntPattern(true, "Optimized code for LEN(2)",
				new String[] { "LDA {*}", "LDY {*}", "STA B_REG", "STY B_REG+1", "JSR LEN", "JSR COPY_XREG2YREG",
						"LDA #<{CONST0}", "LDY #>{CONST0}", "JSR COPY2_XYA_XREG", "JSR YREGFAC", "LDA #<X_REG",
						"LDY #>X_REG", "JSR CMPFAC" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(6);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						double numd = num.doubleValue();
						if (numd == (int) numd && numd >= -32768 && numd < 32768) {
							String numHex = getHex(numd);
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							rep.add(cleaned.get(2));
							rep.add(cleaned.get(3));
							rep.add(cleaned.get(4));
							rep.add("LDA TMP2_ZP");
							rep.add("STA TMP_ZP");
							rep.add("LDA #0");
							rep.add("STA TMP_ZP+1");
							rep.add("LDY #$" + numHex.substring(2));
							rep.add("LDA #$" + numHex.substring(0, 2));
							rep.add("JSR ICMP");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// if l%(6)=h% etc.
		intPatterns.add(new IntPattern(true, "Optimized code for Integer(4)", new String[] { "JSR {*}", "LDY {*}",
				"LDA {*}", "JSR INTFAC", "LDA #<X_REG", "LDY #>X_REG", "JSR CMPFAC" }, new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String jumpy = cleaned.get(0);
						if (jumpy.contains("ARRAYACCESS_INTEGER")) {
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							rep.add(cleaned.get(2));
							rep.add("STY TMP_ZP");
							rep.add("STA TMP_ZP+1");
							rep.add("LDY TMP2_ZP");
							rep.add("LDA TMP2_ZP+1");
							rep.add("JSR ICMP");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// if h%=l%(6) etc.
		intPatterns.add(new IntPattern(true, "Optimized code for Integer(5)",
				new String[] { "JSR {*}", "LDA #<X_REG", "LDY #>X_REG", "STY TMP3_ZP+1", "LDX #<Y_REG", "LDY #>Y_REG",
						"JSR COPY2_XYA", "LDY {*}", "LDA {*}", "JSR INTFAC", "JSR FACXREG", "JSR YREGFAC",
						"LDA #<X_REG", "LDY #>X_REG", "JSR CMPFAC" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String jumpy = cleaned.get(0);
						if (jumpy.contains("ARRAYACCESS_INTEGER")) {
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(0));
							rep.add("LDY TMP2_ZP");
							rep.add("LDA TMP2_ZP+1");
							rep.add("STY TMP_ZP");
							rep.add("STA TMP_ZP+1");
							rep.add(cleaned.get(7));
							rep.add(cleaned.get(8));
							rep.add("JSR ICMP");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// if i% then...
		intPatterns.add(new IntPattern(true, "Optimized code for Integer(6)",
				new String[] { "LDY {*}", "LDA {*}", "JSR INTFAC", "JSR FACYREG", "LDA Y_REG", "{LABEL}", "BEQ {*}" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						List<String> rep = new ArrayList<>();
						rep.add(cleaned.get(0).replace("LDY", "LDA"));
						rep.add(cleaned.get(1).replace("LDA", "ORA"));
						rep.add(cleaned.get(5));
						rep.add(cleaned.get(6));
						return combine(pattern, rep);
					}
				}));

		// mid$(a$,i,<const>)
		intPatterns.add(new IntPattern(true, "Optimized code for MID",
				new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}", "STY TMP3_ZP+1", "LDX #<D_REG", "LDY #>D_REG",
						"JSR COPY2_XYA", "LDA {*}", "LDY {*}", "STA B_REG", "STY B_REG+1", "JSR MID" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						int numd = num.intValue();
						List<String> rep = new ArrayList<>();
						// While this isn't what the interpreter would do, it's bs anyway, so we handle
						// it like this...
						if (numd < 0) {
							rep.add(cleaned.get(6));
							rep.add(cleaned.get(7));
							rep.add(cleaned.get(8));
							rep.add(cleaned.get(9));
							rep.add("JSR MIDNEGC");
						} else {
							if (numd > 255) {
								numd = 255;
							}
							rep.add(cleaned.get(6));
							rep.add(cleaned.get(7));
							rep.add(cleaned.get(8));
							rep.add(cleaned.get(9));
							rep.add("LDY #" + numd);
							rep.add("JSR MIDCONST");
						}
						return combine(pattern, rep);
					}
				}));

		// mid$(a$,i,<int>)
		intPatterns
				.add(new IntPattern(true, "Optimized code for MID (2)",
						new String[] { "LDY {MEM0}", "LDA {MEM0}", "JSR INTFAC", "LDX #<D_REG", "LDY #>D_REG",
								"JSR FACMEM", "LDA {*}", "LDY {*}", "STA B_REG", "STY B_REG+1", "JSR MID" },
						new AbstractCodeModifier() {
							@Override
							public List<String> modify(IntPattern pattern, List<String> input) {
								input = super.modify(pattern, input);
								List<String> rep = new ArrayList<>();
								// While this isn't what the interpreter would do, it's bs anyway, so we handle
								// it like this...
								rep.add(cleaned.get(6));
								rep.add(cleaned.get(7));
								rep.add(cleaned.get(8));
								rep.add(cleaned.get(9));
								rep.add(cleaned.get(0));
								rep.add(cleaned.get(1));
								rep.add("JSR MIDCONSTA");
								return combine(pattern, rep);
							}
						}));

		// left$(a$,<const>)/right$(a$, <const>)
		intPatterns.add(new IntPattern(
				true, "Optimized code for LEFT/RIGHT", new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}",
						"JSR COPY2_XYA_CREG", "LDA {*}", "LDY {*}", "STA B_REG", "STY B_REG+1", "JSR {*}" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						int numd = num.intValue();
						List<String> rep = new ArrayList<>();
						// While this isn't what the interpreter would do, it's bs anyway, so we handle
						// it like this...
						String func = cleaned.get(7).replace("JSR", "").trim();
						if (numd <= 0 || (!func.equals("LEFT") && !func.equals("RIGHT"))) {
							pattern.reset();
							return input;
						} else {
							if (numd > 255) {
								numd = 255;
							}
							rep.add(cleaned.get(3));
							rep.add(cleaned.get(4));
							rep.add(cleaned.get(5));
							rep.add(cleaned.get(6));
							rep.add("LDY #" + numd);
							rep.add("JSR " + func + "CONST");
						}
						return combine(pattern, rep);
					}
				}));

		// left$(a$,<int>)/right$(a$, <int>)
		intPatterns
				.add(new IntPattern(true, "Optimized code for LEFT/RIGHT (2)",
						new String[] { "LDY {MEM0}", "LDA {MEM0}", "JSR INTFAC", "LDX #<C_REG", "LDY #>C_REG",
								"JSR FACMEM", "LDA {*}", "LDY {*}", "STA B_REG", "STY B_REG+1", "JSR {*}" },
						new AbstractCodeModifier() {
							@Override
							public List<String> modify(IntPattern pattern, List<String> input) {
								input = super.modify(pattern, input);
								List<String> rep = new ArrayList<>();
								String func = cleaned.get(10).replace("JSR", "").trim();
								if (!func.equals("LEFT") && !func.equals("RIGHT")) {
									pattern.reset();
									return input;
								} else {
									rep.add(cleaned.get(6));
									rep.add(cleaned.get(7));
									rep.add(cleaned.get(8));
									rep.add(cleaned.get(9));
									rep.add(cleaned.get(0));
									rep.add(cleaned.get(1));
									rep.add("JSR " + func + "CONSTA");
								}
								return combine(pattern, rep);
							}
						}));

		// ON XX GOTO/GOSUB...
		intPatterns.add(new IntPattern(true, "Optimized code for ON XX GOYYY",
				new String[] { "JSR BASINT", "JSR {*}", "{LABEL}", "JSR ONETOFAC" }, new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String label = cleaned.get(2);

						// The result of BASINT can either be stored in X or Y, so we have to handle
						// both...
						if (label.startsWith("ON") && label.contains("SUB") && cleaned.get(1).contains("FAC")) {

							// Search for the end of the ON XX block and adjust the lists
							// accordingly...
							List<String> bet = new ArrayList<>(parts.get(1));
							List<String> last = new ArrayList<>(parts.get(2));

							for (Iterator<String> itty = last.iterator(); itty.hasNext();) {
								String line = itty.next();
								if (line.startsWith("GSKIPON")) {
									break;
								}
								itty.remove();
								bet.add(line);
							}

							// Set the new parts...
							parts.set(1, bet);
							parts.set(2, last);

							List<String> rep = new ArrayList<>();
							rep.add("JSR FACWORD");
							rep.add("STY TMP_ZP");
							int cnt = 0;
							int block = 0;
							boolean skip = false;

							// Finally replace the performance heavy section with something much simpler...
							for (String line : bet) {
								cnt++;
								if (cnt < 3) {
									continue;
								}
								if (!skip) {
									rep.add(line);
								}
								if (line.startsWith("ON") && line.contains("SUB")) {
									block++;
									skip = true;
								}
								if (line.equals("JSR CMPFAC")) {
									skip = false;
									rep.add("LDA #" + block);
									rep.add("CMP TMP_ZP");
								}
							}
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// PEEK(XXXX) with XXXX being a constant
		intPatterns.add(new IntPattern(true, "Optimized code for PEEK(<constant>)",
				new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}", "JSR REALFAC", "JSR FACWORD", "STY {*}", "STA {*}" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						int numd = num.intValue();

						List<String> rep = new ArrayList<>();
						rep.add("LDY #" + (numd & 0xff));
						rep.add("LDA #" + ((numd & 0xff00) >> 8));
						rep.add(cleaned.get(4));
						rep.add(cleaned.get(5));
						return combine(pattern, rep);
					}
				}));

		// CONST into Y/A
		intPatterns
				.add(new IntPattern(
						true, "Optimized code for CONST into Y/A", new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}",
								"JSR COPY2_XYA_YREG", "NOP", "JSR YREGFAC", "JSR FACWORD" },
						new AbstractCodeModifier() {
							@Override
							public List<String> modify(IntPattern pattern, List<String> input) {
								input = super.modify(pattern, input);
								String consty = cleaned.get(0);
								consty = consty.substring(consty.indexOf("<") + 1).trim();
								Number num = const2Value.get(consty);
								int numd = num.intValue();
								// Apply to constants only
								List<String> rep = new ArrayList<>();
								rep.add("LDY #" + (numd & 0xff));
								rep.add("LDA #" + ((numd & 0xff00) >> 8));
								return combine(pattern, rep);
							}
						}));

		// POKE I,PEEK(I) AND 234
		intPatterns
				.add(new IntPattern(
						true, "Optimized code for POKE,PEEK", new String[] { "JSR {*}", "JSR POPREAL", "JSR FACWORD",
								"STY {*}", "STA {*}", "JSR XREGFAC", "JSR FACWORD", "{LABEL}", "STY $FFFF" },
						new AbstractCodeModifier() {
							@Override
							public List<String> modify(IntPattern pattern, List<String> input) {
								input = super.modify(pattern, input);
								String jumpy = cleaned.get(0);
								if (jumpy.contains("PEEKBYTE")) {
									List<String> rep = new ArrayList<>();
									rep.add(cleaned.get(0));
									rep.add(cleaned.get(1));
									rep.add(cleaned.get(2));
									rep.add(cleaned.get(3));
									rep.add(cleaned.get(4));
									rep.add("LDY TMP2_ZP");
									rep.add(cleaned.get(7));
									rep.add(cleaned.get(8));
									return combine(pattern, rep);
								}
								pattern.reset();
								return input;
							}
						}));

		// POKE I,PEEK(J%)
		intPatterns.add(new IntPattern(true, "Optimized code for PEEK with Integer",
				new String[] { "LDY {MEM0}", "LDA {MEM0}", "JSR INTFAC", "JSR FACWORD", "STY {*}", "STA {*}" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String vary = cleaned.get(0);
						if (vary.contains("%") && cleaned.get(4).contains("MOVBSELF")) {
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							rep.add(cleaned.get(4));
							rep.add(cleaned.get(5));
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// POKE I,X%...(this one takes negative values as bytes instead of causing an
		// error...well, who cares...). It also omits the XREG-storage of the INT, but
		// that shouldn't matter here, because the block is done after the POKE anyway
		// and
		// there should be no further code referencing the value.
		intPatterns.add(new IntPattern(true, "Optimized code for POKE of Integer values",
				new String[] { "LDY {MEM0}", "LDA {MEM0}", "JSR INTFAC", "JSR FACXREG", "JSR POPREAL", "JSR FACWORD",
						"STY {*}", "STA {*}", "JSR XREGFAC", "JSR FACWORD", "{LABEL}", "STY $FFFF" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String vary = cleaned.get(0);
						String label = cleaned.get(10);
						if (vary.contains("%") && label.startsWith("MOVBSELF")) {
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(4));
							rep.add(cleaned.get(5));
							rep.add(cleaned.get(6));
							rep.add(cleaned.get(7));
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(10));
							rep.add(cleaned.get(11));
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// Integer array storage with contant index value
		intPatterns.add(new IntPattern(true, "Optimized code for fixed integer index",
				new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}", "JSR COPY2_XYA_XREG", "LDY {*}", "LDA {*}",
						"STY AS_TMP", "STA AS_TMP+1", "LDA #<{MEM0}", "LDY #>{MEM0}", "STA G_REG", "STY G_REG+1",
						"JSR ARRAYSTORE_INT_INTEGER" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						int numd = num.intValue();
						List<String> rep = new ArrayList<>();

						rep.add(cleaned.get(3));
						rep.add(cleaned.get(4));
						rep.add(cleaned.get(5));
						rep.add(cleaned.get(6));
						rep.add(cleaned.get(7));
						rep.add(cleaned.get(8));
						rep.add(cleaned.get(9));
						rep.add(cleaned.get(10));
						rep.add("LDY #" + (numd & 0xff));
						rep.add("LDA #" + ((numd & 0xff00) >> 8));
						rep.add("JSR ARRAYSTORE_INT_INTEGER_AC");
						return combine(pattern, rep);
					}
				}));

		// f%(l%)=f%(r%)...still semi-optimal, because the intermediate result is stored
		// as float, but anyway...
		intPatterns.add(new IntPattern(true, "Optimized code for copying from int-array to int-array(1)",
				new String[] { "LDY {MEM0}", "LDA {MEM0}", "JSR INTFAC", "JSR PUSHREAL", "NOP", "LDA #<{MEM1}",
						"LDY #>{MEM1}", "STA G_REG", "STY G_REG+1", "LDY {MEM2}", "LDA {MEM2}",
						"JSR ARRAYACCESS_INTEGER_INT", "JSR COPY_XREG2YREG", "JSR POPREALXREG", "LDA #<{MEM1}",
						"LDY #>{MEM1}", "STA G_REG", "STY G_REG+1", "JSR ARRAYSTORE_INTEGER_NX" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String vary = cleaned.get(0);
						if (vary.contains("%")) {
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(5));
							rep.add(cleaned.get(6));
							rep.add(cleaned.get(7));
							rep.add(cleaned.get(8));
							rep.add(cleaned.get(9));
							rep.add(cleaned.get(10));
							rep.add(cleaned.get(11));
							rep.add(cleaned.get(12));
							rep.add(cleaned.get(14));
							rep.add(cleaned.get(15));
							rep.add(cleaned.get(16));
							rep.add(cleaned.get(17));
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							rep.add("JSR ARRAYSTORE_INTEGER_INT");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// f%(l%+1)=f%(r%)...still semi-optimal, because the intermediate result is
		// stored as float, but anyway. This is quite a special case anyway...
		intPatterns.add(new IntPattern(true, "Optimized code for copying from int-array to int-array(2)",
				new String[] { "LDY {MEM0}", "LDA {MEM0}", "JSR FIINX", "LDA #<X_REG", "LDY #>X_REG", "JSR REALFACPUSH",
						"NOP", "LDA #<{MEM1}", "LDY #>{MEM1}", "STA G_REG", "STY G_REG+1", "LDY {MEM2}", "LDA {MEM2}",
						"JSR ARRAYACCESS_INTEGER_INT", "JSR COPY_XREG2YREG", "JSR POPREALXREG", "LDA #<{MEM1}",
						"LDY #>{MEM1}", "STA G_REG", "STY G_REG+1", "JSR ARRAYSTORE_INTEGER_NX" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String vary = cleaned.get(0);
						if (vary.contains("%")) {
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(7));
							rep.add(cleaned.get(8));
							rep.add(cleaned.get(9));
							rep.add(cleaned.get(10));
							rep.add(cleaned.get(11));
							rep.add(cleaned.get(12));
							rep.add(cleaned.get(13));
							rep.add(cleaned.get(14));
							rep.add(cleaned.get(16));
							rep.add(cleaned.get(17));
							rep.add(cleaned.get(18));
							rep.add(cleaned.get(19));
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							rep.add("JSR SUPERFIINX");
							rep.add("JSR ARRAYSTORE_INTEGER_INT");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// CHR$(X%+-CONST)...yes, that's quite a special and rare case...
		intPatterns.add(new IntPattern(true, "Optimized code CHR plus ADD/SUB",
				new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}", "JSR COPY2_XYA_YREG", "LDY {MEM0}", "LDA {MEM0}",
						"JSR INTFAC", "JSR FACXREG", "JSR YREGFAC", "LDA #<X_REG", "LDY #>X_REG", "JSR {*}",
						"JSR FACYREG", "JSR CHR" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						String vary = cleaned.get(3);
						String calcy = cleaned.get(10);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						int numd = num.intValue();
						if (vary.contains("%") && (calcy.contains("SUBMEM") || calcy.contains("ADDMEM"))) {
							List<String> rep = new ArrayList<>();
							rep.add("LDA #" + (numd & 0xff));
							rep.add("STA TMP2_ZP");
							if (calcy.contains("SUBMEM")) {
								rep.add("LDA #0");
							} else {
								rep.add("LDA #1");
							}
							rep.add("STA TMP2_ZP+1");
							rep.add(cleaned.get(3));
							rep.add("JSR CHRINTCALC");
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// p% and/or <const>0>
		intPatterns.add(new IntPattern(true, "Optimized code for AND/OR",
				new String[] { "LDA #<{CONST0}", "LDY #>{CONST0}", "JSR COPY2_XYA_YREG", "LDY {*}", "LDA {*}",
						"JSR INTFAC", "JSR FACXREG", "JSR YREGFAC", "JSR XREGARG", "JSR FAST{*}", "JSR {*}" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(0);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						double numd = num.doubleValue();
						boolean isFacInt = cleaned.get(10).contains("FACINT");
						boolean isXreg = cleaned.get(10).contains("FACXREG");
						if ((isFacInt || isXreg) && numd == (int) numd && numd >= -32767 && numd < 32768) {
							String op = cleaned.get(9).substring(8).replace("OR", "ORA");
							String numHex = getHex(numd);
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(4));
							rep.add(op + " #$" + numHex.substring(0, 2));
							rep.add("TAX");
							rep.add(cleaned.get(3));
							rep.add("TYA");
							rep.add(op + " #$" + numHex.substring(2));
							rep.add("TAY");
							rep.add("TXA");
							if (isXreg) {
								rep.add("JSR INTFAC");
								rep.add("JSR BASINT"); // Actually, this isn't needed but it triggers the ON
														// GOTO-Optimization in the second pass
								rep.add("JSR FACXREG");
							}
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		// p%+1 and/or <const>0>
		intPatterns.add(new IntPattern(
				true, "Optimized code for +1 AND/OR", new String[] { "LDY {*}", "LDA {*}", "JSR FI{*}",
						"LDA #<{CONST0}", "LDY #>{CONST0}", "JSR REALFAC", "JSR XREGARG", "JSR FAST{*}", "JSR {*}" },
				new AbstractCodeModifier() {
					@Override
					public List<String> modify(IntPattern pattern, List<String> input) {
						input = super.modify(pattern, input);
						String consty = cleaned.get(3);
						consty = consty.substring(consty.indexOf("<") + 1).trim();
						Number num = const2Value.get(consty);
						double numd = num.doubleValue();
						boolean isIncDec = cleaned.get(2).contains("FIINX") || cleaned.get(2).contains("FIDEX");
						boolean isFacInt = cleaned.get(8).contains("FACINT");
						boolean isXreg = cleaned.get(8).contains("FACXREG");
						if ((isFacInt || isXreg) && isIncDec && numd == (int) numd && numd >= -32767 && numd < 32768) {
							String op = cleaned.get(7).substring(8).replace("OR", "ORA");
							String numHex = getHex(numd);
							List<String> rep = new ArrayList<>();
							rep.add(cleaned.get(0));
							rep.add(cleaned.get(1));
							rep.add(cleaned.get(2).replace("JSR ", "JSR SUPER"));
							rep.add(op + " #$" + numHex.substring(0, 2));
							rep.add("TAX");
							rep.add("TYA");
							rep.add(op + " #$" + numHex.substring(2));
							rep.add("TAY");
							rep.add("TXA");
							if (isXreg) {
								rep.add("JSR INTFAC");
								rep.add("JSR BASINT"); // Actually, this isn't needed but it triggers the ON
														// GOTO-Optimization in the second pass
								rep.add("JSR FACXREG");
							}
							return combine(pattern, rep);
						}
						pattern.reset();
						return input;
					}
				}));

		for (int i = codeStart; i < codeEnd; i++) {
			String line = input.get(i);
			if (line.trim().startsWith(";")) {
				continue;
			}

			for (IntPattern pattern : intPatterns) {
				boolean matches = pattern.matches(line, i, const2Value, strConst2Value);
				if (matches) {
					input = pattern.modify(input);
				}
			}
		}

		int cnt = intPatterns.stream().map(p -> p.getUsage()).reduce(0, Integer::sum);
		if (cnt > 0) {
			Logger.log("Optimization Optimized code for Integer applied " + cnt + " times!");
		}

		return input;
	}

	private String getHex(double value) {
		int nummy = (((int) value) & 0x0000ffff);
		String numHex = Integer.toHexString(nummy);
		return "0000".substring(numHex.length()) + numHex;
	}

	private Map<String, Number> extractConstants(List<String> ret) {
		return Collections.unmodifiableMap(Util.extractNumberConstants(ret));
	}

	private Map<String, String> extractStringConstants(List<String> ret) {
		return Collections.unmodifiableMap(Util.extractStringConstants(ret));
	}

}