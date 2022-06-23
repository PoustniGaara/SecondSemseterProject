package gui.tools;

import java.awt.Font;

public enum Fonts {
	
	FONT20(new Font("Monaco", Font.BOLD, 20)),
<<<<<<< HEAD
	FONT10(new Font("Monaco", Font.BOLD, 10)),
=======
>>>>>>> parent of 3c8f828 (GUI)
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
