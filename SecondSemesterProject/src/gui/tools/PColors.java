package gui.tools;

import java.awt.Color;

public enum PColors {
		
		GREEN(new Color(15, 113, 115)),
		RED(new Color(240, 93, 94)),
		YELLOW(new Color(204,204,0)),
		GREY(new Color(231, 236, 239)),
		BLACK(new Color(39, 41, 50));
		
		java.awt.Color color;
		
		PColors(Color color){
			this.color = color;
		}
		
		public static Color get(PColors name) {
			return name.color;
		}
	}

