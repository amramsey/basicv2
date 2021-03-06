package com.sixtyfour.elements.commands;

import java.util.List;

import com.sixtyfour.config.CompilerConfig;
import com.sixtyfour.elements.Type;
import com.sixtyfour.parser.Atom;
import com.sixtyfour.parser.Parser;
import com.sixtyfour.parser.cbmnative.CodeContainer;
import com.sixtyfour.system.BasicProgramCounter;
import com.sixtyfour.system.Machine;
import com.sixtyfour.util.VarUtils;

/**
 * The CLOSE command.
 */
public class Close extends AbstractCommand {

	/** The pars. */
	private List<Atom> pars;

	/**
	 * Instantiates a new close.
	 */
	public Close() {
		super("CLOSE");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sixtyfour.elements.commands.AbstractCommand#parse(java.lang.String, int,
	 * int, int, boolean, sixtyfour.system.Machine)
	 */
	@Override
	public String parse(CompilerConfig config, String linePart, int lineCnt, int lineNumber, int linePos,
			boolean lastPos, Machine machine) {
		super.parse(config, linePart, lineCnt, lineNumber, linePos, lastPos, machine);
		term = Parser.getTerm(config, this, linePart, machine, true);
		pars = Parser.getParameters(term);

		if (pars.size() == 0) {
			syntaxError(this);
		}

		if (pars.size() > 1) {
			// If more parameters are given, use only the first one. This is
			// stupid, but the BASIC interpreter behaves in the same way.
			pars = pars.subList(0, 1);
		}
		checkTypes(pars, linePart, Type.STRING);
		return null;
	}

	@Override
	public List<CodeContainer> evalToCode(CompilerConfig config, Machine machine) {
		return this.createSingleParameterCall(config, machine, pars, "CLOSE");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sixtyfour.elements.commands.AbstractCommand#execute(sixtyfour.system.
	 * Machine)
	 */
	@Override
	public BasicProgramCounter execute(CompilerConfig config, Machine machine) {
		Atom fileNumber = pars.get(0);
		int fn = VarUtils.getInt(fileNumber.eval(machine));

		if (machine.getOutputChannel().getPrintConsumer() != null) {
			if (machine.getOutputChannel().getChannel() == fn) {
				machine.getOutputChannel().setPrintConsumer(null, 0);
			}
		}

		machine.getDeviceProvider().close(fn);
		return null;
	}

}
