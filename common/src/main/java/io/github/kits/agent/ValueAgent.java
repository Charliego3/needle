package io.github.kits.agent;

import io.github.kits.Lambdas;
import io.github.kits.PropertiesKit;
import io.github.kits.Strings;
import io.github.kits.annotations.Value;
import io.github.kits.log.Logger;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static javassist.bytecode.AccessFlag.FINAL;
import static javassist.bytecode.AccessFlag.PRIVATE;
import static javassist.bytecode.AccessFlag.PROTECTED;
import static javassist.bytecode.AccessFlag.PUBLIC;
import static javassist.bytecode.AccessFlag.STATIC;
import static javassist.bytecode.AccessFlag.TRANSIENT;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class ValueAgent {

	public static byte[] value(ClassPool cp, CtClass ctClass, List<CtField> ctFields) {
		try {
			ctFields.forEach(Lambdas.rethrowConsumer(ctField -> {
				Value valueAnn = (Value) ctField.getAnnotation(Value.class);
				cp.importPackage(PropertiesKit.class.getCanonicalName());
				setValueBody(ctClass, ctField, valueAnn);
				cp.importPackage(BigDecimal.class.getCanonicalName());
			}));
			ctClass.writeFile("/Users/whimthen/dev/project/Backstage/javaagent-test/AgnetTest");
			return ctClass.toBytecode();
		} catch (Exception e) {
			Logger.error(e);
		}
		return null;
	}

	private static void setValueBody(CtClass ctClass, CtField ctField, Value value) throws Exception {
		switch (ctField.getModifiers()) {
			case PRIVATE:
			case PUBLIC:
			case PROTECTED:
			case FINAL:
			case TRANSIENT:
			case PRIVATE + TRANSIENT:
			case PUBLIC + TRANSIENT:
			case PROTECTED + TRANSIENT:
				setP(ctClass, ctField, value);
				break;
			case STATIC:
			case PRIVATE + STATIC:
			case PUBLIC + STATIC:
			case PROTECTED + STATIC:
			case STATIC + FINAL:
			case PRIVATE + STATIC + TRANSIENT:
			case PUBLIC + STATIC + TRANSIENT:
			case PROTECTED + STATIC + TRANSIENT:
				setPS(ctClass, ctField, value);
				break;
			case PRIVATE + STATIC + FINAL:
			case PUBLIC + STATIC + FINAL:
			case PROTECTED + STATIC + FINAL:
				setPSF(ctClass, ctField, value);
				break;
		}
	}

	/**
	 * private | public | protected static final
	 *
	 * @param ctClass
	 * @param ctField
	 * @param value
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 */
	private static void setPSF(CtClass ctClass, CtField ctField, Value value) throws CannotCompileException, NotFoundException {
		String   name     = ctField.getName();
		CtMethod ctMethod = null;
		try {
			ctMethod = ctClass.getDeclaredMethod("get" + Strings.toUpperPrefix(name));
		} catch (NotFoundException e) {

		}
		if (Objects.nonNull(ctMethod)) {
			ctMethod.setBody("return " + name + ";");
		} else {
//			setPS(ctClass, ctField, value);
		}
		CtField field = CtField.make(ctField.getType()
											.getSimpleName() + " " + ctField.getName() + " = \"datadatadatadata\";", ctClass);
		field.setModifiers(ctField.getModifiers());
		ctClass.removeField(ctField);
		ctClass.addField(field);
	}

	/**
	 * private | public | protected static
	 *
	 * @param ctClass
	 * @param ctField
	 * @param value
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private static void setPS(CtClass ctClass, CtField ctField, Value value) throws CannotCompileException, NotFoundException {
		CtConstructor classInitializer = ctClass.getClassInitializer();
		if (Objects.isNull(classInitializer)) {
			classInitializer = ctClass.makeClassInitializer();
		}
		String psValue = getValue(ctField, value);
		classInitializer.insertAfter(ctField.getName() + psValue);
	}

	/**
	 * private | public | protected
	 *
	 * @param ctClass
	 * @param ctField
	 * @param value
	 * @throws Exception
	 */
	private static void setP(CtClass ctClass, CtField ctField, Value value) throws Exception {
		CtConstructor[] declaredConstructors = ctClass.getDeclaredConstructors();
		if (Objects.nonNull(declaredConstructors)) {
			Stream.of(declaredConstructors)
				  .forEach(Lambdas.rethrowConsumer(
						  ctConstructor -> {
							  String pValue = getValue(ctField, value);
							  ctConstructor.insertAfter("this." + ctField.getName() + pValue);
						  }));
		}
	}

	private static String getValue(CtField ctField, Value value) throws NotFoundException {
		String        fieldName  = ctField.getType()
										  .getSimpleName();
		String        propName   = value.name();
		StringBuilder fieldValue = new StringBuilder("PropertiesKit.get");
		switch (fieldName) {
			case "BigDecimal":
				fieldValue.insert(0, "new BigDecimal(");
				setValue(fieldValue, false, "String", value);
				fieldValue.append(")");
				break;
			case "Date":
				fieldValue.insert(0, "new Date(");
				fieldValue.append("String(");
				fieldValue.append(propName)
						  .append("))");
				break;
			case "int":
			case "short":
			case "long":
			case "float":
			case "double":
			case "boolean":
			case "String":
				setValue(fieldValue, false, fieldName, value);
				break;
			case "Integer":
			case "Short":
			case "Long":
			case "Float":
			case "Double":
			case "Boolean":
				setValue(fieldValue, true, fieldName, value);
				break;
			default:
				fieldValue = new StringBuilder("new ").append(fieldName)
													  .append("()");
		}
		fieldValue.insert(0, " = ");
		fieldValue.append(";");
		return fieldValue.toString();
	}

	private static void setValue(StringBuilder fieldValue, boolean isUpper, String typeName, Value value) {
		String upperTypeName = Strings.toUpperPrefix(typeName);
		if (isUpper) {
			fieldValue.insert(0, upperTypeName + ".valueOf(");
		}
		if (upperTypeName.contains("Int")) {
			upperTypeName = "Int";
		}
		fieldValue.append(upperTypeName);
		if (defaultValue(value, fieldValue)) {
			fieldValue.append("(\"")
					  .append(value.prop())
					  .append("\", \"")
					  .append(value.name())
					  .append("\")");
		}
		if (isUpper) {
			fieldValue.append(")");
		}
	}

	private static String upperPrefix(String val) {
		return val.substring(0, 1)
				  .toUpperCase() + val.substring(1);
	}

	private static boolean defaultValue(Value value, StringBuilder fieldValue) {
		if (Strings.isNotNullOrEmpty(value.defaultValue())) {
			fieldValue.append("OrDefault(\"")
					  .append(value.prop())
					  .append("\", \"")
					  .append(value.name())
					  .append("\", \"")
					  .append(value.defaultValue())
					  .append("\")");
			return false;
		}
		return true;
	}

}
