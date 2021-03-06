/*
 * 
 */
package com.sixtyfour.elements.systemvars;

import com.sixtyfour.elements.Variable;

/**
 * A system variable that represents PI.
 */
public class Pie extends Variable implements SystemVariable {

	/**
	 * Instantiates a new pie.
	 */
	public Pie() {
		super("Π", (float) Math.PI);
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

}
