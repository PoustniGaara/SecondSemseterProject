package gui.tools;

import java.awt.Color;

public enum ProjectColors {

	GREEN(new Color(49, 131, 200)), RED(new Color(255, 255, 255)), YELLOW(new Color(204, 204, 0)),
	GREY(new Color(231, 236, 239)), BLACK(new Color(39, 41, 50)), WHITE(new Color(238, 238, 238, 255)),
	SELECTED(new Color(232, 242, 254, 255)), GGREEN(new Color(50, 168, 82)), GRED(new Color(168, 50, 54)),

	BLUE(new Color(49, 131, 200)), LIGHT_BLUE(new Color(170, 212, 245)), BG(new Color(237, 241, 245)),
	BLUEtable(new Color(247, 252, 255)), BLUE1(new Color(239, 248, 255)), BLUE3(new Color(99, 162, 216)),
	BLUE5(new Color(35, 104, 162)), BLUE6(new Color(26, 73, 113)), BLUE7(new Color(18, 40, 58));

	java.awt.Color color;

	ProjectColors(Color color) {
		this.color = color;
	}

	public Color get() {
		return color;
	}
}
