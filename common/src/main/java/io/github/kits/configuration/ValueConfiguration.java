package io.github.kits.configuration;

import io.github.kits.PropertiesKit;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ValueConfiguration {

    public abstract Object getProp(String[] fileNames, String key, String defaultValue);

}

class IntValueConfiguration extends ValueConfiguration {

    @Override
    public Object getProp(String[] fileNames, String key, String defaultValue) {
        return ConfigurationKit.getValue(fileNames, fileName -> PropertiesKit.getInt(fileName, key), defaultValue);
    }

}

class StringValueConfiguration extends ValueConfiguration {

    @Override
    public Object getProp(String[] fileNames, String key, String defaultValue) {
        return ConfigurationKit.getValue(fileNames, fileName -> PropertiesKit.getString(fileName, key), defaultValue);
    }

}

class BooleanValueConfiguration extends ValueConfiguration {

    @Override
    public Object getProp(String[] fileNames, String key, String defaultValue) {
        return ConfigurationKit.getValue(fileNames, fileName -> PropertiesKit.getBoolean(fileName, key), defaultValue);
    }

}

class LongValueConfiguration extends ValueConfiguration {

    @Override
    public Object getProp(String[] fileNames, String key, String defaultValue) {
        return ConfigurationKit.getValue(fileNames, fileName -> PropertiesKit.getLong(fileName, key), defaultValue);
    }

}

class ShortValueConfiguration extends ValueConfiguration {

    @Override
    public Object getProp(String[] fileNames, String key, String defaultValue) {
        return ConfigurationKit.getValue(fileNames, fileName -> PropertiesKit.getShort(fileName, key), defaultValue);
    }

}

class FloatValueConfiguration extends ValueConfiguration {

    @Override
    public Object getProp(String[] fileNames, String key, String defaultValue) {
        return ConfigurationKit.getValue(fileNames, fileName -> PropertiesKit.getFloat(fileName, key), defaultValue);
    }

}

class DoubleValueConfiguration extends ValueConfiguration {

    @Override
    public Object getProp(String[] fileNames, String key, String defaultValue) {
        return ConfigurationKit.getValue(fileNames, fileName -> PropertiesKit.getDouble(fileName, key), defaultValue);
    }

}

class PropValueConfiguration extends ValueConfiguration {

    @Override
    public Object getProp(String[] fileNames, String key, String defaultValue) {
        return ConfigurationKit.getValue(fileNames, fileName -> PropertiesKit.getString(fileName, key), defaultValue);
    }

}

class ConfigurationKit {

    static Object getValue(String[] fileNames, Function<String, Object> function, Object defaultValue) {
        Object value = null;
        for (String fileName : fileNames) {
            Object propVal = function.apply(fileName);
            if (Objects.nonNull(propVal)) {
                if (propVal instanceof Integer || propVal instanceof Short) {
                    if (!propVal.equals(0)) {
                        value = propVal;
                        break;
                    }
                } else if (propVal instanceof Float) {
                    if (!propVal.equals(0F)) {
                        value = propVal;
                        break;
                    }
                } else if (propVal instanceof Double) {
                    if (!propVal.equals(0D)) {
                        value = propVal;
                        break;
                    }
                } else if (propVal instanceof Long) {
                    if (!propVal.equals(0L)) {
                        value = propVal;
                        break;
                    }
                } else {
                    value = propVal;
                    break;
                }
            }
        }
        if (Objects.isNull(value) && Objects.nonNull(defaultValue)) {
            value = defaultValue;
        }
        return value;
    }

}
