package gui.tools;

import java.awt.Color;

public enum ProjectColors {
		
		GREEN(new Color(15, 113, 115)),
		RED(new Color(240, 93, 94)),
		YELLOW(new Color(204,204,0)),
		GREY(new Color(231, 236, 239)),
		BLACK(new Color(39, 41, 50)),
		WHITE(new Color(238,238,238,255)),
		SELECTED(new Color(232,242,254,255));
		
		java.awt.Color color;
		
		ProjectColors(Color color){
			this.color = color;
		}
		
		public Color get() {
			return color;
		}
}

