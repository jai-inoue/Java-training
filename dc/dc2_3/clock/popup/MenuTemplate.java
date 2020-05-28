package dc2_3.clock.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dc2_3.view.MenuView;

public class MenuTemplate implements ActionListener {
	public final MenuView menuView;
	private final String popupName;
	private String command;

	public MenuTemplate(MenuView menuView) {
		this.menuView = menuView;
		this.popupName = menuView.view.getText();
	}

	public String getCommand() {
		return command;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		command = e.getActionCommand();
		System.out.println("popupName: " + popupName);
		System.out.println("command: " + command);
		menuView.service.timeService.convertCommand(popupName, command);
		menuView.service.clockFrameService.convertCommand(popupName, command);
		//		this.menuView.generator.clockFramePrintGenerator.execute();
		this.menuView.generator.timePanelPrintGenerator.execute();
		command = null;

	}
}
