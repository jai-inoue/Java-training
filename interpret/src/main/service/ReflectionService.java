package main.service;

import java.awt.Window;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.JDialog;

import main.InterpretView;
import main.clazz.Argument;
import main.utils.StringUtils;

public class ReflectionService implements Service {
	// objectタイプ
	private String clazzName;
	private Constructor<?>[] constructors;
	private Argument[] constructorArgments = null;
	private Argument[] methodArguments = null;
	private Argument fieldArgument;
	private Map<String, Object> instances = new HashMap<>();
	private Object instance;
	private Field[] fields;
	private Map<String, Field> fieldMap;
	private Map<String, Method> methodMap;
	public List<JDialog> instanceDialogs = new ArrayList<JDialog>();

	// arrayタイプ
	private int arraySize;
	private String referenceName;
	private Object[] arrayInstance;
	private Argument[] elementArgments;
	private String instanceType;

	public void setClazz(final String clazzName) throws ClassNotFoundException {
		this.clazzName = clazzName;
		this.constructors = convertToConstructor(clazzName);
	}

	private Constructor<?>[] convertToConstructor(final String clazzName) throws ClassNotFoundException {
		Class<?> clazz;
		Constructor<?>[] constructors;
		clazz = Class.forName(clazzName);
		constructors = clazz.getDeclaredConstructors();
		return constructors;
	}

	public Object validateArgument(final Argument arg)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Object validatedArg = null;
		if (arg == null) {
			return validatedArg;
		}

		// 入力された引数は、文字列またはインスタンスへのアクセスキーとして利用する
		if (!StringUtils.macthRegex(arg.value)) {
			validatedArg = parsePrimitive(arg);
			return validatedArg;
		}
		// ${}の中身を取り出す
		String key = arg.value.substring(2, arg.value.length() - 1);
		Object obj;
		String[] keys;
		//インスタンスフィールドの場合
		if (key.matches(".*\\..*")) {
			keys = key.split("\\.");
			obj = this.instances.get(keys[0]);
			Field field = obj.getClass().getDeclaredField(keys[1]);
			validatedArg = field.get(obj);
		} else if (key.matches(".*\\[[0-9]*\\](|_)[0-9]*")) {// 配列要素の場合
			keys = new String[2];
			keys[0] = key.substring(0, key.indexOf("[")) + "[]";
			keys[1] = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
			obj = this.instances.get(keys[0]);
			validatedArg = Array.get(obj, Integer.parseInt(keys[1]));
		} else {
			validatedArg = this.instances.get(key);
		}
		return validatedArg;
	}

	public Object[] validateArguments(final Argument[] args)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Object[] validatedArgs = null;
		if (args == null) {
			return validatedArgs;
		}
		validatedArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			validatedArgs[i] = validateArgument(args[i]);
		}
		return validatedArgs;
	}

	public Object validateArray(final String arrayKey) {
		Object instance = this.instances.get(arrayKey.substring(2, arrayKey.length() - 1));
		if (!instance.getClass().isArray()) {
			throw new IllegalArgumentException("This is not array.");
		}
		return instance;
	}

	public Object parsePrimitive(final Argument arg) {
		// 引数が配列の場合に使う
		String[] argStrs = null;
		if (arg.type.getName().indexOf("[") != -1) {
			argStrs = arg.value.split(",");
		}
		switch (arg.type.getName()) {
		case "byte":
			return Byte.parseByte(arg.value);
		case "char":
			return arg.value.toCharArray()[0];
		case "double":
			return Double.parseDouble(arg.value);
		case "float":
			return Float.parseFloat(arg.value);
		case "int":
			return (int) Integer.parseInt(arg.value);
		case "java.lang.Integer":
			return (int) Integer.parseInt(arg.value);
		case "long":
			return Long.parseLong(arg.value);
		case "short":
			return Short.parseShort(arg.value);
		case "boolean":
			return Boolean.parseBoolean(arg.value);
		case "[B":
			byte[] bytes = new byte[argStrs.length];
			for (int i = 0; i < argStrs.length; i++) {
				bytes[i] = Byte.parseByte(argStrs[i]);
			}
			return bytes;
		case "[C":
			return arg.value.toCharArray();
		case "[D":
			double[] doubles = new double[argStrs.length];
			for (int i = 0; i < argStrs.length; i++) {
				doubles[i] = Double.parseDouble(argStrs[i]);
			}
			return doubles;
		case "[F":
			float[] floats = new float[argStrs.length];
			for (int i = 0; i < argStrs.length; i++) {
				floats[i] = Float.parseFloat(argStrs[i]);
			}
			return floats;
		case "[I":
			int[] ints = new int[argStrs.length];
			for (int i = 0; i < argStrs.length; i++) {
				ints[i] = Integer.parseInt(argStrs[i]);
			}
			return ints;
		case "[J":
			long[] longs = new long[argStrs.length];
			for (int i = 0; i < argStrs.length; i++) {
				longs[i] = Long.parseLong(argStrs[i]);
			}
			return longs;
		case "[S":
			short[] shorts = new short[argStrs.length];
			for (int i = 0; i < argStrs.length; i++) {
				shorts[i] = Short.parseShort(argStrs[i]);
			}
			return shorts;
		case "[Z":
			boolean[] booleans = new boolean[argStrs.length];
			for (int i = 0; i < argStrs.length; i++) {
				booleans[i] = Boolean.parseBoolean(argStrs[i]);
			}
			return booleans;
		default:
			try {
				return arg.type.cast(arg.value);
			} catch (ClassCastException e) {
				throw new ClassCastException();
			}
		}
	}

	public Constructor<?>[] getConstructor() {
		return constructors;
	}

	public void setArgTypes(final int constructorsIndex) {
		if (constructorsIndex == -1) {
			return;
		}
		Class<?>[] types = constructors[constructorsIndex].getParameterTypes();
		// 引き数なし
		if (types.length < 1) {
			this.constructorArgments = null;
			return;
		}
		Argument[] args = new Argument[types.length];
		for (int i = 0; i < types.length; i++) {
			args[i] = new Argument();
			args[i].type = types[i];
		}
		this.constructorArgments = args;
	}

	public void setMethodArgTypes(final String methodName) {
		if (Objects.isNull(methodName)) {
			return;
		}
		Class<?>[] types = methodMap.get(methodName).getParameterTypes();
		// 引き数なし
		if (types.length < 1) {
			this.methodArguments = null;
			return;
		}
		Argument[] args = new Argument[types.length];
		for (int i = 0; i < types.length; i++) {
			args[i] = new Argument();
			args[i].type = types[i];
		}
		this.methodArguments = args;
	}

	public void setFieldArgTypes(final int fieldsIndex) {
		if (fieldsIndex == -1) {
			return;
		}
		Argument arg = new Argument();
		Class<?> type = this.fields[fieldsIndex].getType();
		arg.type = type;

		this.fieldArgument = arg;
	}

	public Map<String, Field> getFieldMap() {
		return fieldMap;
	}

	public Map<String, Method> getMethodMap() {
		return methodMap;
	}

	public Constructor<?>[] getConstructors() {
		return constructors;
	}

	public Object getNewInstance() {
		return this.instance;
	}

	//	public void setNewInstance(final Object instance) {
	//		this.instance = instance;
	//	}

	public void setFieldMap(final Map<String, Field> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public void setMethodMap(final Map<String, Method> methodMap) {
		this.methodMap = methodMap;
	}

	public Argument getFieldArgument() {
		return this.fieldArgument;
	}

	public Argument[] getConstructorArgments() {
		return constructorArgments;
	}

	public Argument[] getMethodArgments() {
		return methodArguments;
	}

	public void setFieldArgments(final String value, final String fieldName) {
		final Argument arg = new Argument();
		final Field field = this.fieldMap.get(fieldName);
		arg.type = field.getType();
		arg.value = value;
		this.fieldArgument = arg;
	}

	public String getClazzName() {
		return this.clazzName;
	}

	public Map<String, Object> getInstances() {
		return this.instances;
	}

	// array
	public void setArraySize(final String arraySize) {
		this.arraySize = Integer.parseInt(arraySize);
	}

	public int getArraySize() {
		return this.arraySize;
	}

	public void setArray(final String referenceName, final String size) throws NumberFormatException {
		this.referenceName = referenceName;
		this.arraySize = Integer.parseInt(size);
	}

	public String getReferenceName() {
		return this.referenceName;
	}

	public void setElement(final Object element, final String index)
			throws ArrayIndexOutOfBoundsException, NumberFormatException {
		try {
			int i = Integer.parseInt(index);

			Object[] arrayInstance = (Object[]) this.instance;
			arrayInstance[i] = element;

		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException();
		} catch (NumberFormatException e) {
			throw new NumberFormatException();
		}
	}

	public Object getNewArrayInstance() {
		return this.arrayInstance;
	}

	//	public void setNewArrayInstance(final Object[] arrayInstance) {
	//		this.arrayInstance = arrayInstance;
	//	}

	public Argument[] getElementArgments() {
		return elementArgments;
	}

	public void setElementArgTypes(Object[] instance) {
		final Class<?> type = instance.getClass().getComponentType();
		final int length = Array.getLength(instance);
		final Argument[] args = new Argument[length];
		for (Argument arg : args) {
			arg.type = type;
		}
		this.elementArgments = args;
	}

	public void setInstanceType(final String instanceType) {
		this.instanceType = instanceType;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void disposeAllWindows() {
		// インスタンスの中身を表すダイアログを開放する
		for (JDialog dialog : this.instanceDialogs) {
			dialog.dispose();
		}
		// interpretViewが持つWindowクラスのオブジェクトを開放する
		for (String key : this.instances.keySet()) {
			if (this.instances.get(key) instanceof InterpretView) {
				continue;
			} else if (this.instances.get(key) instanceof Window) {
				((Window) this.instances.get(key)).dispose();
			}
		}
	}
}
