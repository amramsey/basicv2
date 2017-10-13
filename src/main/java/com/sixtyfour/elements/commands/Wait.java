package com.sixtyfour.elements.commands;

import java.util.List;

import com.sixtyfour.Logger;
import com.sixtyfour.cbmnative.Util;
import com.sixtyfour.elements.Type;
import com.sixtyfour.parser.Atom;
import com.sixtyfour.parser.Parser;
import com.sixtyfour.parser.cbmnative.CodeContainer;
import com.sixtyfour.system.BasicProgramCounter;
import com.sixtyfour.system.Machine;
import com.sixtyfour.util.VarUtils;

/**
 * The WAIT command.
 */
public class Wait extends AbstractCommand {

	/** The pars. */
	private List<Atom> pars;

	/** The stop. */
	private boolean stop = false;

	/**
	 * Instantiates a new wait.
	 */
	public Wait() {
		super("Wait");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sixtyfour.elements.commands.AbstractCommand#parse(java.lang.String,
	 * int, int, int, boolean, sixtyfour.system.Machine)
	 */
	@Override
	public String parse(String linePart, int lineCnt, int lineNumber, int linePos, boolean lastPos, Machine machine) {
		super.parse(linePart, lineCnt, lineNumber, linePos, lastPos, machine);
		term = Parser.getTerm(this, linePart, machine, true);
		pars = Parser.getParameters(term);

		if (pars.size() < 2 || pars.size() > 3) {
			syntaxError(linePart);
		}

		checkTypes(pars, linePart, Type.STRING, Type.STRING, Type.STRING);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sixtyfour.elements.commands.AbstractCommand#stopExecution()
	 */
	@Override
	public void stopExecution() {
		stop = true;
	}
	
	@Override
  public List<CodeContainer> evalToCode(Machine machine) {
    Logger.log("WARNING: WAIT not implemented in native compiler!");
    return Util.createSingleCommand("NOP");
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sixtyfour.elements.commands.AbstractCommand#execute(sixtyfour.system.
	 * Machine)
	 */
	@Override
	public BasicProgramCounter execute(Machine machine) {
		Atom addr = pars.get(0);
		Atom waitFor = pars.get(1);
		int memAddr = VarUtils.getInt(addr.eval(machine));
		int vally = VarUtils.getInt(waitFor.eval(machine));
		if (vally < 0 || vally > 255 || memAddr < 0 || memAddr > 65535) {
			throw new RuntimeException("Illegal quantity error: " + this);
		}

		int invert = 0;
		boolean invertFound = false;
		if (pars.size() == 3) {
			invertFound = true;
			Atom inverted = pars.get(2);
			invert = VarUtils.getInt(inverted.eval(machine));
			if (invert < 0 || invert > 255) {
				throw new RuntimeException("Illegal quantity error: " + this);
			}
		}

		if (!invertFound && memAddr == 6502 && vally == 1) {
			machine.getOutputChannel().systemPrintln(0, "EgonOlsen!");
		}

		while (machine.getMemoryListener().wait(memAddr, vally, invert) && !stop) {
			Thread.yield();
		}
		stop = false;

		return null;

	}
}
