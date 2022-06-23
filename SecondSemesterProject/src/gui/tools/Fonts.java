package gui.tools;

import java.awt.Font;

public enum Fonts {
	
	FONT20(new Font("Monaco", Font.BOLD, 20)),
	FONT10(new Font("Monaco", Font.BOLD, 10)),
	FONT30(new Font("Monaco", Font.BOLD, 30)),
	FONT15(new Font("Monaco", Font.BOLD, 12));
	
    private Font f;

    Fonts(Font f) {
        this.f = f;
    }

    public Font get() {
        return this.f;
    }

}
