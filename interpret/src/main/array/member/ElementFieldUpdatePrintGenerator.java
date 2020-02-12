package main.array.member;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import main.PrintGenerator;
import main.array.ArrayReflectionService;

public class ElementFieldUpdatePrintGenerator extends PrintGenerator {
	private static final ElementFieldUpdatePrintGenerator fieldUpdatePrintGenerator = new ElementFieldUpdatePrintGenerator();

	private final ArrayReflectionService reflectionService = ArrayReflectionService.getInstance();

	private String fieldName;
	private Object fieldValue;

	@Override
	public void execute()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// クリア
		this.fieldName = null;
		this.fieldValue = null;
		// text取得
		final ElementFieldPanel fieldPanel = ElementFieldPanel.getInstance();
		// fieldと値を保存
		reflectionService.setFieldArgments(fieldPanel.getInputText(), fieldPanel.getFieldComboBox().getSelectedIndex());
		// fieldを更新するための準備。
		final Field field = reflectionService.getFields()[fieldPanel.getFieldComboBox().getSelectedIndex()];
		if (!Modifier.isStatic(field.getModifiers())) {
			// staticフィールドじゃない
			field.setAccessible(true);
		} else {
			// staticフィールドだとprivateとfinalを外す必要がある
			Field modifierField = null;
			modifierField = Field.class.getDeclaredField("modifiers");
			modifierField.setAccessible(true);
			modifierField.setInt(field, field.getModifiers() & ~Modifier.PRIVATE & ~Modifier.FINAL);
			field.setAccessible(true);
		}
		// fieldを更新する。
		Object instance = reflectionService.getNewInstance();
		field.set(instance, reflectionService.parsePrimitive(reflectionService.getFieldArgument()));
		// ログ用にfield名と値を保持。
		this.fieldName = field.getName();
		this.fieldValue = field.get(instance);
		// アクセス制限を戻す
		field.setAccessible(false);
		this.notifyObservers();
	}

	@Override
	public String getLog() {
		if (this.fieldName == null || this.fieldValue == null) {
			return "";
		}
		return "Success!\n" + fieldName + " = " + fieldValue.toString() + ".\n";
	}

	public static ElementFieldUpdatePrintGenerator getInstance() {
		return fieldUpdatePrintGenerator;
	}
}
