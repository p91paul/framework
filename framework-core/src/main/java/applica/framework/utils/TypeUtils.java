package applica.framework.utils;

import applica.framework.data.Entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeUtils {
    public static boolean isEntity(Class<?> type) {
        return implementsInterface(type, Entity.class, true);
    }

    public static boolean isList(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();

        doGetAllField(type, fields);

        return fields;
    }

    public static boolean implementsInterface(Class<?> type, Class<?> interfaceType, boolean searchInSuperclasses) {
        boolean found = false;
        for (Class<?> item : type.getInterfaces()) {
            if (item.equals(interfaceType)) {
                found = true;
                break;
            }
        }

        if (found) {
            return true;
        } else {
            if (searchInSuperclasses && type.getSuperclass() != null) {
                return implementsInterface(type.getSuperclass(), interfaceType, searchInSuperclasses);
            }
        }

        return false;
    }

    public static void doGetAllField(Class<?> type, List<Field> fields) {
        for (Field newField : type.getDeclaredFields()) {
            boolean found = false;
            for (Field oldField : fields) {
                if (oldField.getName().equals(newField.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fields.add(newField);
            }
        }

        if (type.getSuperclass() != null) {
            doGetAllField(type.getSuperclass(), fields);
        }
    }

    public static Field getField(Class<?> type, String name) throws NoSuchFieldException {
        Field field = null;
        try {
            field = type.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            if (type.getSuperclass() != null) {
                field = getField(type.getSuperclass(), name);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (field == null) throw new NoSuchFieldException();
        return field;
    }

    public static <T extends Annotation> T getFieldAnnotation(Class<T> annotationClass, Class<?> type, String fieldName) {
        Field field = null;
        try {
            field = getField(type, fieldName);
        } catch (NoSuchFieldException e) {
        }

        T annotation = null;
        if (field != null) {
            annotation = field.getAnnotation(annotationClass);
        }

        return annotation;
    }

    public static Class<?> getListGeneric(Class<?> type, String property) {
        Class<?> genericType = null;

        try {
            Field field = TypeUtils.getField(type, property);
            ParameterizedType ptype = (ParameterizedType) field.getGenericType();
            Type[] types = ptype.getActualTypeArguments();
            if (types.length > 0) {
                Type t = types[0];
                genericType = (Class<?>) t;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return genericType;
    }

    public static Class<?> getListGeneric(Class<?> type) {
        Class<?> genericType = null;

        Type st = type.getGenericSuperclass();
        ParameterizedType ptype = (ParameterizedType) st;
        Type[] types = ptype.getActualTypeArguments();
        if (types.length > 0) {
            Type t = types[0];
            genericType = (Class<?>) t;
        }

        return genericType;
    }

    public static Class<?> genericCheckedType(Type type) {
        if (type instanceof ParameterizedType) {
            return genericCheckedType(((ParameterizedType) type).getRawType());
        } else {
            return (Class<?>) type;
        }
    }
}

