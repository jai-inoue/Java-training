package main.generator;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextField;

import main.di.AutowiredService;
import main.service.ConstructorService;
import main.service.ReflectionService;
import main.view.panel.ConstructorPanel;

/**
 * You can create a instance by the constuctor. The instance is printed
 * in the InstanceListPanel and saved in ReflectionService.
 *
 * @author inoue-keiichi
 *
 */
public class ConstructorCreatePrintGenerator extends PrintGenerator {
	private final ReflectionService reflectionService;
	private final ConstructorService constructorService;

	public ConstructorCreatePrintGenerator(AutowiredService service) {
		super(service);
		reflectionService = service.reflectionService;
		constructorService = service.constructorService;
	}

	private String constructorName;

	@Override
	public void execute()
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchFieldException, SecurityException {
		// 入力された文字列を保存
		final ConstructorPanel constructorPanel = constructorService.getconstructorPanel();
		int i = 0;
		for (Component comp : constructorPanel.getArgsPanel().getComponents()) {
			JTextField field = (JTextField) comp;
			reflectionService.getConstructorArgments()[i].value = field.getText();
			i++;
		}
		// 入力された文字列を引数に変換
		final Object[] args = reflectionService.validateArguments(reflectionService.getConstructorArgments());
		// プルダウンで指定されたコンストラクタを取得してインスタンス生成
		final int index = constructorPanel.getConstructorComboBox().getSelectedIndex();
		final Object instance = reflectionService.getConstructors()[index].newInstance(args);
		// 作ったインスタンスは再利用できるようにする
		String key = instance.getClass().getSimpleName();
		// 同じキーがあれば番号を付加する
		key = arrangeKey(key);
		reflectionService.getInstances().put(key, instance);
		// ログ用
		this.constructorName = reflectionService.getConstructors()[index].toGenericString();
		this.notifyObservers(key);
	}

	/**
	 * インスタンスに紐づく適切なキーを返します。
	 *
	 *
	 * @param key
	 * @return
	 */
	private String arrangeKey(final String key) {
		if (!reflectionService.getInstances().containsKey(key)) {
			return key;
		}
		int i = 1;
		String changedKey = key + "_" + i;
		// keyが被らないようにする
		while (reflectionService.getInstances().containsKey(changedKey)) {
			i++;
			changedKey = key + "_" + i;
		}
		return changedKey;
	}

	@Override
	public String getLog() {
		if (this.constructorName == null) {
			return "";
		}
		return "[Success] " + this.constructorName + " => The instance was created.\n";
	}

}
