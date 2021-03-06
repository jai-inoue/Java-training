package main.generator;

import java.lang.reflect.Array;

import main.di.AutowiredService;
import main.service.ReflectionService;

public class ArrayCreatePrintGenerator extends PrintGenerator {
	private final ReflectionService reflectionService;

	public ArrayCreatePrintGenerator(AutowiredService service) {
		super(service);
		reflectionService = service.reflectionService;
	}

	@Override
	public void execute() throws ClassNotFoundException {
		final String referenceName = reflectionService.getReferenceName();
		final int arraySize = reflectionService.getArraySize();
		final Class<?> clazz = Class.forName(referenceName);
		final Object[] arrayInstance = (Object[]) Array.newInstance(clazz, arraySize);

		// 作ったインスタンスは再利用できるようにする
		String key = arrayInstance.getClass().getSimpleName();
		// 同じキーがあれば番号を付加する
		key = arrangeKey(key);
		reflectionService.getInstances().put(key, arrayInstance);
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
		String changedKey = key.substring(0, key.indexOf("[")) + "_" + i + "[]";
		// keyが被らないようにする
		while (reflectionService.getInstances().containsKey(changedKey)) {
			i++;
			changedKey = key.substring(0, key.indexOf("[")) + "_" + i + "[]";
		}
		return changedKey;
	}

	@Override
	public String getLog() {
		return "[Success] An array was created whose type was " + reflectionService.getReferenceName() + ".\n";
	}
}
