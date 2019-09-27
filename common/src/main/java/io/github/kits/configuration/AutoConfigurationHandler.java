package io.github.kits.configuration;

import io.github.kits.Colors;
import io.github.kits.Envs;
import io.github.kits.Strings;
import io.github.kits.annotations.Value;
import io.github.kits.enums.Prop;
import io.github.kits.log.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class AutoConfigurationHandler {

    /**
     * 配置@Value注解的值, 从默认的配置文件
     */
//    public static void configValue() {
//        configValue(null);
//    }

    /**
     * 配置@Value注解字段的值
     *
     * @param propertiesName    配置文件
     */
    public static void configValue(String... propertiesName) {
        if (Strings.isNullOrEmpty(propertiesName)) {
            propertiesName = Arrays.stream(Prop.values()).filter(propEnum -> "prop".equals(propEnum.getType()))
								   .map(Prop::getProp).toArray(String[]::new);
        }
        final String[] propName = propertiesName;
        config(classes -> classes.stream()
                .filter(clazz -> !clazz.equals(AutoConfigurationHandler.class) && !clazz.isInterface())
                .forEach(clazz -> {
                    Field[] declaredFields = clazz.getDeclaredFields();
                    Arrays.stream(declaredFields)
                            .filter(field -> Objects.nonNull(field.getAnnotation(Value.class)))
                            .forEach(field -> {
                                setAnnotFieldValue(field, propName, clazz);
                            });
            }), Value.class);
    }

    /**
     * 设置@Value注解字段值, 从配置文件读取
     *      如果多个配置文件中存在同名配置, 以第一个为主
     *
     * @param field             当前需要设置值的字段
     * @param propertiesName    配置文件
     * @param clazz             字段所在的类
     */
    public static void setAnnotFieldValue(Field field, String[] propertiesName, Class clazz) {
        Class type = field.getType();
        Value value = field.getAnnotation(Value.class);
        ValueConfiguration configuration = ConfigurationFactory.createConfiguration(type);
        Object property = configuration.getProp(propertiesName, value.name(), value.defaultValue());
//        System.out.println(clazz.getCanonicalName() + "." + field.getName() + " = " + property);
        try {
            String modifier = Modifier.toString(field.getModifiers());
            boolean accessible = field.isAccessible();

            field.setAccessible(true);
            Field modifiersField = field.getClass().getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            if (modifier.contains("static final")) {
                modifiersField.set(field, field.getModifiers() & ~Modifier.FINAL);
            } else if (!modifier.contains("static")) {
                Logger.warnf(Colors.toRedBold("Field {}.{}({}), {} can not be set value"),
                        clazz.getCanonicalName(), field.getName(), field.getGenericType().getTypeName(), modifier);
                return;
            }
            Object o = clazz.newInstance();
            field.set(o, property);
            field.setAccessible(accessible);
        } catch (Exception e) {
            Logger.errorf("Set value error, Field {}.{}({})", e,
                    clazz.getCanonicalName(), field.getName(), field.getGenericType().getTypeName());
        }
    }

    /**
     * 用来配置
     *      filters: 传入父类或注解
     * @param consumer      消费class
     * @param filters       需要获取的class类型
     */
    public static void config(Consumer<List<Class>> consumer, Class... filters) {
        try {
            List<Class> aClass = Envs.findClass(filters);
            Logger.info(aClass.toString());
            consumer.accept(aClass);
        } catch (IOException io) {
            Logger.error(io);
        }
    }

}
