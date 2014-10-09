package applica.framework.modules.hibernate;

import applica.framework.data.Entity;
import applica.framework.data.Key;
import applica.framework.library.SimpleItem;
import applica.framework.utils.Strings;
import applica.framework.utils.TypeUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 08/10/14
 * Time: 13:19
 */
public class Mapper extends XmlBuilder {

    private Class<? extends Entity> type;

    private static final Class<?>[] ALLOWED_TYPES = new Class<?>[] {
            String.class,
            Integer.class,
            Float.class,
            Double.class,
            Boolean.class,
            Byte.class,
            Short.class,
            Long.class,
            Date.class
    };

    public Mapper(Class<? extends Entity> type) {
        this.type = type;
    }

    private static boolean isAllowed(Class<?> type) {
        if (type.isPrimitive()) return true;

        for(Class<?> allowedType : ALLOWED_TYPES) {
            if (type.equals(allowedType)) return true;
        }

        return false;
    }

    public String map() {
        raw("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); endl();
        raw("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD//EN\" \"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">"); endl();
        open("hibernate-mapping"); endl();
        open("class",
                attr("name", type.getName()),
                attr("table", Strings.pluralize(StringUtils.uncapitalize(type.getSimpleName())))
        ); endl();
        open("id", attr("name", "id"), attr("type", "long")); endl();
        openClose("generator", attr("class", "native")); endl();
        close("id"); endl();

        TypeUtils.getAllFields(type)
                .stream()
                .filter(t -> !Modifier.isTransient(t.getModifiers()))
                .filter(t -> !Modifier.isStatic(t.getModifiers()))
                .filter(t -> !t.getName().equals("id"))
                .filter(t ->
                                Mapper.isAllowed(t.getType()) ||
                                        TypeUtils.isList(t.getType()) ||
                                        TypeUtils.isEntity(t.getType()) ||
                                        Key.class.equals(t.getType())
                )
                .forEach(t -> {
                    if (TypeUtils.isEntity(t.getType())) {
                        String foreignKeyName = String.format("%sId", StringUtils.uncapitalize(t.getType().getSimpleName()));
                        openClose("many-to-one",
                                attr("name", t.getName()),
                                attr("class", t.getType().getName()),
                                attr("column", foreignKeyName),
                                attr("lazy", "false")
                        ); endl();
                    } else if (TypeUtils.isList(t.getType())) {
                        ParameterizedType listType = (ParameterizedType) t.getGenericType();
                        Type[] arguments = listType.getActualTypeArguments();
                        Class<?> typeArgument = (Class<?>) arguments[0];
                        if (TypeUtils.isEntity(typeArgument)) {
                            open("list",
                                    attr("name", t.getName()),
                                    attr("table", Strings.pluralize(StringUtils.uncapitalize(typeArgument.getSimpleName()))),
                                    attr("lazy", "false")
                            ); endl();
                                String foreignKeyName = String.format("%sId", StringUtils.uncapitalize(type.getSimpleName()));
                                openClose("key", attr("column", foreignKeyName)); endl();
                                openClose("list-index", attr("column", "id")); endl();
                                openClose("one-to-many", attr("class", typeArgument.getName())); endl();
                            close("list"); endl();
                        } else if (Mapper.isAllowed(typeArgument)) {
                            open("list",
                                    attr("name", t.getName()),
                                    attr("table", StringUtils.uncapitalize(t.getName())),
                                    attr("lazy", "false")
                            ); endl();
                            String foreignKeyName = String.format("%sId", StringUtils.uncapitalize(type.getSimpleName()));
                                openClose("key", attr("column", foreignKeyName)); endl();
                                openClose("list-index", attr("column", "id")); endl();
                                openClose("element", attr("column", "value"), attr("type", typeArgument.getName())); endl();
                            close("list"); endl();
                        }
                    } else if (Key.class.equals(t.getType())) {
                        open("component", attr("name", t.getName()), attr("class", t.getType().getName())); endl();
                        openClose("property", attr("name", "value"), attr("type", "long")); endl();
                        close("component"); endl();
                    } else {
                        openClose("property", attr("name", t.getName())); endl();
                    }
                });


        close("class"); endl();
        close("hibernate-mapping"); endl();
        return xml.toString();
    }

}
