package applica.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.IteratorUtils;

/**
 *
 * @author Paolo Inaudi
 */
public class PropertyPathUtils {

    /**
     * Returns data type of the specified field.
     *
     * Field can be nested using dot notation: Imagine class A and B. Class A has a field foo of class B; class B has a
     * field bar. Providing as property "foo.bar" and type A.class returns the type of field bar in class B.
     *
     * @param type The class from where the path specified in property begins
     * @param property The field path
     * @return The type of the requested field
     */
    public static Type getDataTypeFromPath(Class type, String property) {
        try {
            return navigatePath(type, property, NAVIGATE_TYPE, GET_TYPE, GET_GENERIC_TYPE);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the value of the specified field in obj.
     *
     * Field can be nested using dot notation: Imagine class A and B. Class A has a field foo of class B; class B has a
     * field bar. Providing as property "foo.bar" and an object of class A gets the field foo from obj, and then the
     * field bar from foo.
     *
     * @param <T> The type of the field to be get
     * @param obj The object from where the path spefied in property begins
     * @param property The field path
     * @return The value of the requested field
     */
    public static <T> T getPropertyFromPath(Object obj, String property) {
        return (T) navigatePath(obj, property, GET_PROPERTY);
    }

    /**
     * Returns true if the specified field in obj exists.
     *
     * Field can be nested using dot notation: Providing as property "foo.bar" and an object of class A returns true if
     * A has a field foo of class B, and class B has a field bar
     *
     * @param obj The object from where the path spefied in property begins
     * @param property The field path
     * @return true if the specified field exists, false otherwise
     */
    public static boolean hasProperty(Object obj, String property) {
        Optional<Boolean> result = Optional.ofNullable(
                navigatePath(obj, property, GET_PROPERTY, (o, p) -> PropertyUtils.isReadable(o, p)));
        return result.orElse(false);
    }

    /**
     * Sets the value of the specified field in obj.
     *
     * Field can be nested using dot notation: Imagine class A and B. Class A has a field foo of class B; class B has a
     * field bar. Providing as property "foo.bar" and an object obj of class A, this method sets obj.foo.bar to value.
     *
     * If some property in the path is null, this method creates a new object then continues iterating the path. In the
     * previous example, if obj.foo, first obj.foo is assigned a new B(), then obj.foo.bar is set to value
     *
     * @param obj The object from where the path spefied in property begins
     * @param property The field path
     * @param value the value to which field property will be set
     */
    public static void setPropertyFromPath(Object obj, String property, Object value) {
        navigatePath(obj, property, CREATE_GET_PROPERTY, SET_PROPERTY(value));
    }

    /**
     * Gets the object containing the specified field.
     *
     * Field can be nested using dot notation: Imagine class A and B. Class A has a field foo of class B; class B has a
     * field bar. If provided as property "foo.bar" and an object obj of class A, this method returns obj.foo, which
     * contains bar.
     *
     * @param <T> The type of the field to be get
     * @param obj The object from where the path spefied in property begins
     * @param property The field path
     * @return The value of the parent of the requested field
     */
    public static <T> T getParentPropertyFromPath(Object obj, String property) {
        return (T) navigatePath(obj, property, CREATE_GET_PROPERTY, DO_NOTHING);
    }

    /**
     * Navigates through a field path built using dot notation.
     *
     * The field path "foo.bar" will be split into "foo" and "bar", and forResult will be applied first with obj and
     * "foo", then again with the result of the first application and "bar"
     *
     * @param <T> The type of obj and the result
     * @param obj the object on which the navigation will happen
     * @param property the field path
     * @param forResult this function must take an object of type T and a string representing an item of the field path
     * in property, and return another object of type T. The function will be applied again on the result with the next
     * item of the path, until all path items have been applied
     * @return the return value of the last application of forResult
     */
    public static <T> T navigatePath(T obj, String property, BiFunction<T, String, T> forResult) {
        return navigatePath(obj, property, forResult, forResult);
    }

    /**
     * Navigates through a field path built using dot notation.
     *
     * Same as {@link #navigatePath(java.lang.Object, java.lang.String, java.util.function.BiFunction) }, except that
     * the function forNext is applied to the internal items of the path (e.g with property "foo.bar.baz", forNext will
     * be applied with "foo" and "bar" and forResult with "baz")
     *
     * @param <T> The type of obj
     * @param <R> The type of the result
     * @param obj the object on which the navigation will happen
     * @param property the field path
     * @param forNext this function must take an object of type T and a string representing an item of the field path in
     * property, and return another object of type T. The function will be applied again on the result with the next
     * item of the path, until all path items but the last one have been applied
     * @param forResult this function must take an object of type T and a string representing an item of the field path
     * in property, and return another object of type T. The function will be applied again on the result with the next
     * item of the path, until all path items have been applied
     * @return the return value of the last application of forResult
     */
    public static <T, R> R navigatePath(T obj, String property, BiFunction<T, String, T> forNext,
            BiFunction<T, String, R> forResult) {
        return (R) navigatePath(obj, property, (BiFunction) DO_NOTHING, forNext, forResult);
    }

    /**
     * Navigates through a field path built using dot notation.
     *
     * Same as {@link #navigatePath(java.lang.Object, java.lang.String, java.util.function.BiFunction, java.util.function.BiFunction)
     * }, with the addition of the navigate function, that will be applied to every item before forNext and forResult
     *
     * @param <T> The type of obj
     * @param <R> The type of the result
     * @param <I> The internal type used during iterations
     * @param obj the object on which the navigation will happen
     * @param property the field path
     * @param navigate this function must take an object of type T and a string representing an item of the field path
     * in property, and return another object of type I, to which forNext or forResult will be then applied
     * @param forNext this function must take an object of type I and a string representing an item of the field path in
     * property, and return another object of type T. The function will be applied again on the result with the next
     * item of the path, until all path items but the last one have been applied
     * @param forResult this function must take an object of type I and a string representing an item of the field path
     * in property, and return another object of type T. The function will be applied again on the result with the next
     * item of the path, until all path items have been applied
     * @return the return value of the last application of forResult
     */
    public static <T, R, I> R navigatePath(T obj, String property, BiFunction<T, String, I> navigate,
            BiFunction<I, String, T> forNext, BiFunction<I, String, R> forResult) {
        return navigatePath(obj, getIterator(property), navigate, forNext, forResult);
    }

    /**
     * This function does nothing, it simply returns obj
     */
    public static final BiFunction<Object, String, Object> DO_NOTHING = (obj, property) -> obj;

    /**
     * This function returns the {@link Field} corresponding to property in class cls
     */
    public static final BiFunction<Class, String, Field> NAVIGATE_TYPE = (cls, property) -> {
        try {
            return TypeUtils.getField(cls, property);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * This function returns the generic type of a {@link Field}. See {@link #NAVIGATE_TYPE}
     */
    public static final BiFunction<Field, String, Type> GET_GENERIC_TYPE = (f, s) -> f.getGenericType();

    /**
     * This function returns the type of a {@link Field}. See {@link #NAVIGATE_TYPE}
     */
    public static final BiFunction<Field, String, Class> GET_TYPE = (f, s) -> f.getType();

    /**
     * This function gets a property p from object o.
     */
    public static final BiFunction<Object, String, Object> GET_PROPERTY = (obj, property) -> {
        try {
            return PropertyUtils.getProperty(obj, property);
        } catch (IllegalArgumentException e) { //obj is null
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * This function gets a property p from object o.
     *
     * If p in o exists but is null, a new empty object will be created, assigned to o.p then returned
     */
    public static final BiFunction<Object, String, Object> CREATE_GET_PROPERTY = (o, p) -> {
        try {
            if (PropertyUtils.getProperty(o, p) == null) {
                Object child = PropertyUtils.getPropertyType(o, p).newInstance();
                PropertyUtils.setProperty(o, p, child);
            }
            return PropertyUtils.getProperty(o, p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * This function sets the property p of object o to value
     *
     * @param value the value to be set
     * @return A function to be applied to o and p
     */
    public static BiFunction<Object, String, Void> SET_PROPERTY(final Object value) {
        BiFunction<Object, String, Void> setProperty = (o, p) -> {
            try {
                BeanUtils.setProperty(o, p, value);
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return setProperty;
    }

    private static Iterator<String> getIterator(String property) {
        return IteratorUtils.arrayIterator(property.split("\\."));
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
