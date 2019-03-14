package com.sixtyfour.cbmnative.javascript.generators;

import java.util.List;

import com.sixtyfour.cbmnative.Generator;
import com.sixtyfour.cbmnative.Operand;
import com.sixtyfour.elements.Type;

public abstract class GeneratorBaseJs implements Generator {

	protected boolean checkSpecialWriteVars(List<String> nCode, Operand target, Operand source) {
		if (target.getAddress().equals("VAR_TI$")) {
			String val = source.isRegister() ? source.getRegisterName() : source.getAddress();
			nCode.add("this.WRITETID(this." + val + ");");
			return true;
		}
		return false;
	}

	protected boolean checkSpecialReadVars(List<String> nCode, Operand target, Operand source) {
		if (source.getAddress() != null) {
			if (source.getAddress().equals("VAR_ST")) {
				nCode.add("this.READSTATUS();");
				return true;
			} else if (source.getAddress().equals("VAR_TI")) {
				nCode.add("this.READTI();");
				return true;
			} else if (source.getAddress().equals("VAR_TI$")) {
				nCode.add("this.READTID();");
				return true;
			}
		}
		return false;
	}

	protected String getOpName(Operand op) {
		String name = op.isRegister() ? op.getRegisterName()
				: op.getAddress().replace("%", "_int").replace("[]", "_array");
		if ((name.endsWith("_array") || name.endsWith("_int")) && !name.startsWith("VAR_")) {
			name = "VAR_" + name;
		}
		if (isNumber(name)) {
			return name;
		}
		return "this." + name;
	}

	protected boolean isNumber(String line) {
		try {
			Integer.parseInt(line);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected void truncInteger(List<String> nCode, Operand target) {
		if (target.getType() == Type.INTEGER && !target.isRegister()) {
			String to = getOpName(target);
			nCode.add(to + "=Math.floor(" + to + ");");
		}
	}

}
