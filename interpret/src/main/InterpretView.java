package main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.value.ReflectionService;
import main.value.member.InstancePanel;

public class InterpretView extends JFrame implements Observer, Runnable, ItemListener {
	//private static final InterpretView interpretView = new InterpretView();

	private final ReflectionService reflectionService = ReflectionService.getInstance();

	private final Thread thread = new Thread(this);
	// ログテキストエリア
	public final JTextArea logTextArea = new LogTextArea(10, 40);

	public InterpretView() {
		final JPanel pane = new JPanel(new GridBagLayout());
		this.setContentPane(pane);
		// InterpretViewの配置決め
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 10, 10, 10);

		// インスタンスパネル
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		pane.add(new InstancePanel(), gbc);
		// メンバタブ
		// final JPanel tabbedPane = memberTabCardPanel;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		pane.add(Autowired.memberPanel, gbc);
		// ログパネル
		this.logTextArea.setEditable(false);
		final JScrollPane scrollpane = new JScrollPane(logTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		pane.add(scrollpane, gbc);

		addWindowListener(new InterpretWindowAdapter());
		this.setVisible(true);
		this.setMinimumSize(new Dimension(1200, 450));
	}

	//		public static final InterpretView getInstance() {
	//			return interpretView;
	//		}

	@Override
	public void run() {
		while (true) {

		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		String cmd = (String) e.getItem();
	}

	@Override
	public void update(PrintGenerator printGenerator) {
		//		final String item = this.tabChangeComboBox.getSelectedItem().toString();
		//		memberTabCardLayout.show(memberTabCardPanel, item);

	}
}