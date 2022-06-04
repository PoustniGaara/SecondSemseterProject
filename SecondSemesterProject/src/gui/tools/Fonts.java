package gui.tools;

import java.awt.Font;

public enum Fonts {
	
	FONT20(new Font("Monaco", Font.BOLD, 20));
	
    private Font f;

    Fonts(Font f) {
        this.f = f;
    }

    public Font get() {
        return this.f;
    }

}
