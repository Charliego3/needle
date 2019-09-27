package io.github.kits.agent;

import io.github.kits.Lists;
import io.github.kits.Strings;
import io.github.kits.annotations.Value;
import io.github.kits.log.Logger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom JSON tool class:
 * 	  Annotations provide:
 * 		Customize whether the hump is named
 * 		Custom Json string name matching
 * 		Customize whether serialization is required
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class AgentHandler implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		if (Strings.isNotNullOrEmpty(className)) {
			ClassPool cp      = ClassPool.getDefault();
			CtClass   ctClass = null;
			try {
				ctClass = cp.get(className.replace('/', '.'));
				// 撤销上次修改
//				ctClass.detach();
				// ValueAgent
				List<CtField> valueFields = getValueField(ctClass);
				if (Lists.isNotNullOrEmpty(valueFields)) {
					return ValueAgent.value(cp, ctClass, valueFields);
				}
			} catch (NotFoundException e) {
				Logger.error(e);
			}
		}
		return classfileBuffer;
	}

	private List<CtField> getValueField(CtClass ctClass) {
		if (Objects.nonNull(ctClass)) {
			CtField[] declaredFields = ctClass.getDeclaredFields();
			return Stream.of(declaredFields)
						 .filter(ctField -> {
							 try {
								 return Objects.nonNull(ctField.getAnnotation(Value.class));
							 } catch (ClassNotFoundException e) {
								 return false;
							 }
						 })
						 .collect(Collectors.toList());
		}
		return null;
	}

}
