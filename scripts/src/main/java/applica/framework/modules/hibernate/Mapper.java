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
public class Mapper {

    private Class<? extends Entity> type;
    private int level = 0;
    private StringBuilder xml = new StringBuilder();

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

    private Attr attr(String name, String value) {
        return new Attr(name, value);
    }

    public String getXml() {
        raw("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); endl();
        raw("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD//EN\" \"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">"); endl();
        open("hibernate-mapping"); endl();
            open("class",
                    attr("name", type.getName()),
                    attr("table", Strings.pluralize(StringUtils.uncapitalize(type.getSimpleName())))
            ); endl();
                open("id", attr("name", "id")); endl();
                    openClose("generator", attr("class", "native")); endl();
                close("id"); endl();

                TypeUtils.getAllFields(type)
                        .stream()
                        .filter(t -> !Modifier.isTransient(t.getModifiers()))
                        .filter(t -> !Modifier.isStatic(t.getModifiers()))
                        .filter(t -> !t.getName().equals("id"))
                        .filter(t ->
                                isAllowed(t.getType()) ||
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
                                        attr("column", foreignKeyName)
                                ); endl();
                            } else if (TypeUtils.isList(t.getType())) {
                                ParameterizedType listType = (ParameterizedType) t.getGenericType();
                                Type[] arguments = listType.getActualTypeArguments();
                                Class<?> typeArgument = (Class<?>) arguments[0];
                                if (TypeUtils.isEntity(typeArgument)) {
                                    //many to one
                                } else if (isAllowed(typeArgument)) {
                                    //list of primitive types
                                }
                            } else if (Key.class.equals(t.getType())) {
                                //key type
                            } else {
                                //simple property
                            }
                        });


            close("class"); endl();
        close("hibernate-mapping"); endl();
        return xml.toString();
    }

    private void openClose(String tag, Attr... attributes) {
        levelize();
        xml.append(String.format("<%s", tag));
        writeAttributes(attributes);
        xml.append(" />");
    }

    private void open(String tag, Attr... attributes) {
        levelize();
        xml.append(String.format("<%s", tag));
        writeAttributes(attributes);
        xml.append(">");
        level++;
    }

    private void close(String tag) {
        level--;
        levelize();
        xml.append(String.format("</%s>", tag));
    }

    private void raw(String s) {
        xml.append(s);
    }

    private void levelize() {
        for (int i = 0; i < level; i++) {
            xml.append("\t");
        }
    }

    private void writeAttributes(Attr... attributes) {
        StringBuilder attrs = new StringBuilder();
        int index = 0;
        if (attributes.length > 0) {
            xml.append(" ");

        }
        for (Attr attribute : attributes) {
            index++;
            attrs.append(attribute.toString());
            if (index < attributes.length) {
                attrs.append(" ");
            }
        }

        xml.append(attrs.toString());
    }

    private void endl() {
        xml.append("\n");
    }

    private static boolean isAllowed(Class<?> type) {
        if (type.isPrimitive()) return true;

        for(Class<?> allowedType : ALLOWED_TYPES) {
            if (type.equals(allowedType)) return true;
        }

        return false;
    }

    class Attr {
        private String name;
        private String value;

        Attr(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s=\"%s\"", name, value.replace("\"", "\\\""));
        }
    }


}
