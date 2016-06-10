package sixtyfour.elements.functions;

import java.util.List;

import sixtyfour.Memory;
import sixtyfour.Parser;
import sixtyfour.elements.Atom;
import sixtyfour.elements.Constant;
import sixtyfour.elements.Type;
import sixtyfour.elements.Variable;

public class ArrayAccess extends AbstractFunction {

	// Just a placeholder. A string to store then name into would have done it
	// as well....anyway...
	private Variable variable;

	public ArrayAccess() {
		super("[]");
	}

	@Override
	public Type getType() {
		return variable.getType();
	}

	@Override
	public Object eval(Memory memory) {
		List<Atom> pars = Parser.getParameters(term);
		int[] pis = new int[pars.size()];
		int cnt = 0;
		for (Atom par : pars) {
			pis[cnt++] = ((Number) par.eval(memory)).intValue();
		}
		Variable vary = memory.getVariable(variable.getName());
		if (vary == null) {
			// No such array...revert to a constant
			if (variable.getType().equals(Type.REAL)) {
				return new Constant<Float>(0f);
			}
			if (variable.getType().equals(Type.INTEGER)) {
				return new Constant<Integer>(0);
			}
			if (variable.getType().equals(Type.STRING)) {
				return new Constant<String>("");
			}
		}

		return vary.getValue(pis);
	}

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}
}