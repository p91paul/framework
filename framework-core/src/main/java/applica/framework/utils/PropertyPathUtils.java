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
            return navigatePath(type, getIterator(property), navigateDataType, (f, s) -> f.getType(),
                    (f, s) -> f.getGenericType());
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getPropertyFromPath(Object obj, String property) {
        return (T) navigatePath(obj, getIterator(property), nothing, getProperty, getProperty);
    }

    public static void setPropertyFromPath(Object obj, String property, Object value) {
        BiFunction<Object, String, Void> setProperty = (o, p) -> {
            try {
                PropertyUtils.setProperty(o, p, value);
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        navigatePath(obj, getIterator(property), createProperties, getProperty, setProperty);
    }

    public static <T> T getParentPropertyFromPath(Object obj, String property) {
        return (T) navigatePath(obj, getIterator(property), createProperties, getProperty, nothing);
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

    private static BiFunction<Class, String, Field> navigateDataType = (cls, property) -> {
        try {
            return TypeUtils.getField(cls, property);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    private static final BiFunction<Object, String, Object> nothing = (obj, property) -> obj;

    private static final BiFunction<Object, String, Object> createProperties = (o, p) -> {
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

    private static final BiFunction<Object, String, Object> getProperty = (obj, property) -> {
        try {
            return PropertyUtils.getProperty(obj, property);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

}
