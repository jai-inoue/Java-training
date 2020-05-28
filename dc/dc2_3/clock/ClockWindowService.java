package dc2_3.clock;

import static dc2_3.clock.popup.PopupMenuUtility.*;

import java.awt.Color;

import main.service.Service;

public class ClockWindowService implements Service {
	private Color backgroundColor = Color.black;
	public static int SPACE_X = 20;
	public static int SPACE_Y = 20;

	public Color getBackGroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(final Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void convertCommand(String popupMenuName, String command) {
		switch (popupMenuName) {
		case "Background Color":
			this.backgroundColor = colorConverter(command);
			break;
		default:
		}
	}

}
