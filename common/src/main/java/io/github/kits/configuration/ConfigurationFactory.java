package io.github.kits.configuration;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigurationFactory {

    public static ValueConfiguration createConfiguration(Class type) {
        ValueConfiguration configuration;
        if (String.class.isAssignableFrom(type)) {
            configuration = new StringValueConfiguration();
        } else if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
            configuration = new IntValueConfiguration();
        } else if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
            configuration = new BooleanValueConfiguration();
        } else if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
            configuration = new LongValueConfiguration();
        } else if (Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type)) {
            configuration = new ShortValueConfiguration();
        } else if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
            configuration = new FloatValueConfiguration();
        } else if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
            configuration = new DoubleValueConfiguration();
        } else {
            configuration = new PropValueConfiguration();
        }
        return configuration;
    }

}
