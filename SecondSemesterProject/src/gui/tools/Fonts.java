package gui.tools;

import java.awt.Font;

public enum Fonts {

	FONT18(new Font("Dialog", Font.PLAIN, 18)),
	FONT15(new Font("Dialog", Font.PLAIN, 18)), 
	FONT20(new Font("Dialog", Font.PLAIN, 20)),
	TABLE_FONT(new Font("Segoe UI", Font.PLAIN, 17));

	private Font f;

	Fonts(Font f) {
		this.f = f;
	}

	public Font get() {
		return this.f;
		// FONT20(new Font("Monaco", Font.BOLD, 20)), FONT30(new Font("Monaco",
		// Font.BOLD, 30)),
		// FONT15(new Font("Monaco", Font.BOLD, 12))
	}

}
