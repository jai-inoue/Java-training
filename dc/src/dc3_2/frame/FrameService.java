package dc3_2.frame;

import java.util.Calendar;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FrameService {
	private static final FrameService timeService = new FrameService();

	public static FrameService getInstance() {
		return timeService;
	}

//	public enum MenuKey {
//		FONT_STYLE("fontStyle"), FONT_SIZE("fontSize"), FONT_COLOR("fontColor"), BACKGROUND_COLOR("backgroundColor");
//
//		private String name;
//
//		MenuKey(final String name) {
//			this.name = name;
//		}
//	}

//	public enum ClockType {
//		ANALOG, DEGITAL, TETRIS;
//	}

	public enum ScreenMode {
		CLOCK, TETRIS, TETRIS_CLOCK;
	}

//	private static final String DEFAULT_TIMER_TEXT = "00:00:00";
//
//	private String backgroundColor;
//	private String fontColor;
//	private int fontSize;
//	private Font font;
//	//private ClockType clockType;
//
//	private int hour;
//	private int minute;
//	private int second;
//
//	private TimeService() {
//		font = Font.font(60);
//		fontColor = "green";
//		backgroundColor = "black";
//		//clockType = ClockType.DEGITAL;
//	}
//
//	public String getTime() {
//		String timeStr = new String();
//		if (hour < 10) {
//			timeStr += "0";
//		}
//		timeStr += hour + ":";
//		if (minute < 10) {
//			timeStr += "0";
//		}
//		timeStr += minute + ":";
//		if (second < 10) {
//			timeStr += "0";
//		}
//		timeStr += second;
//
//		return timeStr;
//	}
//
//	public void updateTime() {
//		hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//		minute = Calendar.getInstance().get(Calendar.MINUTE);
//		second = Calendar.getInstance().get(Calendar.SECOND);
//	}
//
//	public double plusFontSize(final Font font, final double width) {
//		final String family = font.getFamily();
//		double size = font.getSize();
//		final Text preText = new Text();
//		final Text text = new Text();
//		text.setText(DEFAULT_TIMER_TEXT);
//		preText.setText(DEFAULT_TIMER_TEXT);
//		while (true) {
//			text.setFont(Font.font(family, size));
//			preText.setFont(Font.font(family, size + 1));
//			if (width * 4 / 5 - text.getLayoutBounds().getWidth() < 0) {
//				break;
//			}
//			size++;
//		}
//		return size;
//	}
//
//	public double minusFontSize(final Font font, final double width) {
//		final String family = font.getFamily();
//		double size = font.getSize();
//		final Text preText = new Text();
//		final Text text = new Text();
//		text.setText(DEFAULT_TIMER_TEXT);
//		preText.setText(DEFAULT_TIMER_TEXT);
//		while (true) {
//			text.setFont(Font.font(family, size));
//			preText.setFont(Font.font(family, size - 1));
//			if (width * 4 / 5 - text.getLayoutBounds().getWidth() > 0) {
//				break;
//			}
//			size--;
//		}
//		return size;
//	}
//
//	public void setFontColor(final String fontColorName) {
//		this.fontColor = fontColorName;
//	}
//
//	public void setBackgroundColor(final String backgroundColorName) {
//		this.backgroundColor = backgroundColorName;
//	}
//
//	public String getFontColor() {
//		return fontColor;
//	}
//
//	public int getFontSize() {
//		return fontSize;
//	}
//
//	public String getBackgroundColor() {
//		return backgroundColor;
//	}
//
//	public Font getFont() {
//		return this.font;
//	}
//
//	public void setFont(final Font font) {
//		this.font = font;
//	}

//	public ClockType getClockType() {
//		return this.clockType;
//	}
//
//	public void setClockType(final ClockType clockType) {
//		this.clockType = clockType;
//	}
}
