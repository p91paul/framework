package applica.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.function.BiFunction;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.IteratorUtils;

/**
 *
 * @author Paolo Inaudi
 */
public class PropertyPathUtils {

    public static Type getDataTypeFromPath(Class type, String property) {
        try {
            return navigatePath(type, getIterator(property), NAVIGATE_TYPE, GET_TYPE, GET_GENERIC_TYPE);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getPropertyFromPath(Object obj, String property) {
        return (T) navigatePath(obj, getIterator(property), DO_NOTHING, GET_PROPERTY, GET_PROPERTY);
    }

    public static void setPropertyFromPath(Object obj, String property, Object value) {
        navigatePath(obj, getIterator(property), CREATE_PROPERTY, GET_PROPERTY, SET_PROPERTY(value));
    }

    public static <T> T getParentPropertyFromPath(Object obj, String property) {
        return (T) navigatePath(obj, getIterator(property), CREATE_PROPERTY, GET_PROPERTY, DO_NOTHING);
    }

    public static <T, R, I> R navigatePath(T obj, String property, BiFunction<T, String, I> navigate,
            BiFunction<I, String, T> forNext, BiFunction<I, String, R> forResult) {
        return navigatePath(obj, getIterator(property), navigate, forNext, forResult);
    }

    public static final BiFunction<Object, String, Object> DO_NOTHING = (obj, property) -> obj;

    public static final BiFunction<Class, String, Field> NAVIGATE_TYPE = (cls, property) -> {
        try {
            return TypeUtils.getField(cls, property);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    public static final BiFunction<Field, String, Type> GET_GENERIC_TYPE = (f, s) -> f.getGenericType();

    public static final BiFunction<Field, String, Class> GET_TYPE = (f, s) -> f.getType();

    public static final BiFunction<Object, String, Object> CREATE_PROPERTY = (o, p) -> {
        try {
            if (PropertyUtils.getProperty(o, p) == null) {
                Object child = PropertyUtils.getPropertyType(o, p).newInstance();
                PropertyUtils.setProperty(o, p, child);
            }
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    public static final BiFunction<Object, String, Object> GET_PROPERTY = (obj, property) -> {
        try {
            return PropertyUtils.getProperty(obj, property);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    public static BiFunction<Object, String, Void> SET_PROPERTY(final Object value) {
        BiFunction<Object, String, Void> setProperty = (o, p) -> {
            try {
                PropertyUtils.setProperty(o, p, value);
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return setProperty;
    }

    private static Iterator<String> getIterator(String property) {
        return IteratorUtils.arrayIterator(property.split("."));
    }

    private static <T, R, I> R navigatePath(T obj, Iterator<String> propertyPath, BiFunction<T, String, I> navigate,
            BiFunction<I, String, T> forNext, BiFunction<I, String, R> forResult) {
        if (obj == null)
            return null;
        final String property = propertyPath.next();
        I result = navigate.apply(obj, property);
        if (propertyPath.hasNext())
            return navigatePath(forNext.apply(result, property), propertyPath, navigate, forNext, forResult);
        else
            return forResult.apply(result, property);
    }

}
