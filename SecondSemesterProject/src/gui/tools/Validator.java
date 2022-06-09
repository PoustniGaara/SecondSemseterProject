package gui.tools;

import javax.swing.JTextField;

public class Validator {

	public static boolean isNumber(JTextField textField) {
		try {
			Long.parseLong(textField.getText().trim());
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isFilled(JTextField textField) {
		if (textField.getText().isBlank() || textField.getText().isEmpty() || textField.getText() == null) {
			return false;
		}
		return true;
	}

}
