package com.sixtyfour.parser.assembly;

import java.util.List;

import com.sixtyfour.elements.mnemonics.Mnemonic;
import com.sixtyfour.elements.mnemonics.MnemonicList;
import com.sixtyfour.parser.Parser;

/**
 *
 */
public class AssemblyParser {
	public static Mnemonic getMnemonic(String linePart) {
		List<Mnemonic> mnemonics = MnemonicList.getMnemonics();
		Mnemonic mne = null;

		for (Mnemonic mnee : mnemonics) {
			if (mnee.isMnemonic(linePart)) {
				mne = mnee;
				break;
			}
		}

		return mne;
	}

	public static String truncateComments(String linePart) {
		StringBuilder sb = new StringBuilder();
		boolean inString = false;
		for (int i = 0; i < linePart.length(); i++) {
			char c = linePart.charAt(i);
			if (c == '"') {
				inString = !inString;
			}
			if (!inString && c == ';') {
				return sb.toString();
			}
			sb.append(c);
		}
		return sb.toString().trim();
	}

	public static LabelAndCode getLabel(String linePart) {
		String linePart2 = Parser.replaceStrings(linePart, '_').trim();
		int pos = linePart2.indexOf(" ");
		if (pos != -1) {
			return new LabelAndCode(linePart.substring(0, pos), linePart.substring(pos + 1).trim());
		}
		return null;
	}

	public static int getValue(String number, int addr, ConstantsContainer ccon, LabelsContainer lcon) {
		number = number.trim();
		if (!number.startsWith("$") && !number.startsWith("%") && !Character.isDigit(number.charAt(0))) {
			ConstantValue cv = ccon.get(number);
			if (cv != null) {
				return cv.getValue();
			}

			Integer labelAddr = lcon.get(number);
			if (labelAddr != null) {
				return labelAddr;
			}

			// No constant and no label found...might be a delayed label...
			lcon.addDelayedLabelRef(addr, number);
			return addr;

		}
		return getValue(number);
	}

	public static int getLowByte(int val) {
		return val % 256;
	}

	public static int getHighByte(int val) {
		return val / 256;
	}

	public static ConstantValue getConstant(String linePart) {
		String linePart2 = Parser.replaceStrings(linePart, '_').trim();
		int pos = linePart2.indexOf("=");
		if (pos != -1) {
			String left = linePart.substring(0, pos).trim();
			String right = linePart.substring(pos + 1).trim();

			int val = getValue(right);

			if (val < 256) {
				return new ConstantByte(left, val);
			}
			return new ConstantInt(left, val);
		}
		return null;
	}

	private static int getValue(String number) {
		number = number.trim();
		int val = 0;
		try {
			if (number.startsWith("$")) {
				val = Integer.parseInt(number.substring(1), 16);
			} else {
				if (number.startsWith("%")) {
					val = Integer.parseInt(number.substring(1), 2);
				} else {
					val = Integer.parseInt(number);
				}
			}

			if (val < -32768 || val > 65535) {
				throw new RuntimeException("Value out of range: " + val);
			}

			return val;
		} catch (Exception e) {
			throw new RuntimeException("Invalid number: " + number);
		}
	}
}
