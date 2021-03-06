package com.sixtyfour.extensions.x16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sixtyfour.elements.Variable;
import com.sixtyfour.elements.commands.Command;
import com.sixtyfour.elements.functions.Function;
import com.sixtyfour.extensions.BasicExtension;
import com.sixtyfour.extensions.x16.commands.Char;
import com.sixtyfour.extensions.x16.commands.Cls;
import com.sixtyfour.extensions.x16.commands.Color;
import com.sixtyfour.extensions.x16.commands.Dos;
import com.sixtyfour.extensions.x16.commands.Frame;
import com.sixtyfour.extensions.x16.commands.Geos;
import com.sixtyfour.extensions.x16.commands.Line;
import com.sixtyfour.extensions.x16.commands.Mon;
import com.sixtyfour.extensions.x16.commands.Mouse;
import com.sixtyfour.extensions.x16.commands.Old;
import com.sixtyfour.extensions.x16.commands.Pset;
import com.sixtyfour.extensions.x16.commands.Rect;
import com.sixtyfour.extensions.x16.commands.Reset;
import com.sixtyfour.extensions.x16.commands.Screen;
import com.sixtyfour.extensions.x16.commands.Vload;
import com.sixtyfour.extensions.x16.commands.Vpoke;
import com.sixtyfour.extensions.x16.commands.Xload;
import com.sixtyfour.extensions.x16.functions.Joy;
import com.sixtyfour.extensions.x16.functions.Vpeek;
import com.sixtyfour.extensions.x16.systemvars.Mb;
import com.sixtyfour.extensions.x16.systemvars.Mx;
import com.sixtyfour.extensions.x16.systemvars.My;
import com.sixtyfour.system.Machine;

/**
 * @author EgonOlsen
 *
 */
public class X16Extensions implements BasicExtension {

	private final static List<Command> COMMANDS = Collections.unmodifiableList(new ArrayList<Command>() {
		private static final long serialVersionUID = 1L;
		{
			this.add(new Vpoke());
			this.add(new Dos());
			this.add(new Mon());
			this.add(new Vload());
			this.add(new Xload());
			this.add(new Geos());
			this.add(new Old());
			this.add(new Screen());
			this.add(new Mouse());
			this.add(new Pset());
			this.add(new Line());
			this.add(new Frame());
			this.add(new Rect());
			this.add(new Char());
			this.add(new Color());
			this.add(new Reset());
			this.add(new Cls());
		}
	});

	private final static List<Function> FUNCTIONS = Collections.unmodifiableList(new ArrayList<Function>() {
		private static final long serialVersionUID = 1L;
		{
			this.add(new Vpeek());
			this.add(new Joy());
		}
	});

	private final static List<Variable> VARS = Collections.unmodifiableList(new ArrayList<Variable>() {
		private static final long serialVersionUID = 1L;
		{
			this.add(new Mx());
			this.add(new My());
			this.add(new Mb());
		}
	});

	@Override
	public List<Command> getCommands() {
		return COMMANDS;
	}

	@Override
	public List<Function> getFunctions() {
		return FUNCTIONS;
	}

	@Override
	public void reset(Machine machine) {
		//
	}

	@Override
	public List<String> getAdditionalIncludes() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				this.add("x16");
			}
		};
	}

	@Override
	public Map<String, Integer> getLabel2Constant() {
		return new HashMap<String, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				this.put("VERAREG", Integer.parseInt("9F20", 16));
				this.put("VERAHI", Integer.parseInt("9F22", 16));
				this.put("VERAMID", Integer.parseInt("9F21", 16));
				this.put("VERALO", Integer.parseInt("9F20", 16));
				this.put("VERADAT", Integer.parseInt("9F23", 16));
				this.put("VERABNK", Integer.parseInt("9F61", 16));
				this.put("ROMSELECT", Integer.parseInt("9F60", 16));
				this.put("RAMSELECT", Integer.parseInt("9F61", 16));
			}
		};
	}

	@Override
	public List<Variable> getSystemVariables() {
		return VARS;
	}

	public static Map<Integer, String> getTokens() {
		return new HashMap<Integer, String>() {
			private static final long serialVersionUID = 1L;
			{
				this.put(0xCE86, "SCREEN");
				this.put(0xCE87, "PSET");
				this.put(0xCE8A, "RECT");
				this.put(0xCE88, "LINE");
				this.put(0xCE89, "FRAME");
				this.put(0xCE8B, "CHAR");
				this.put(0xCE8C, "MOUSE");
				this.put(0xCE92, "MX");
				this.put(0xCE93, "MY");
				this.put(0xCE94, "MB");
				this.put(0xCE84, "VPOKE");
				this.put(0xCE91, "VPEEK");
				this.put(0xCE85, "VLOAD");
				this.put(0xCE81, "DOS");
				this.put(0xCE80, "MON");
				this.put(0xCE82, "OLD");
				this.put(0xCE83, "GEOS");
				this.put(0xCE95, "JOY");
				this.put(0xCE8D, "COLOR");
				this.put(0xCE8F, "RESET");
				this.put(0xCE90, "CLS");
			}
		};
	}
}
