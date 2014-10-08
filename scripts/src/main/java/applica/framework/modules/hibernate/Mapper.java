package applica.framework.modules.hibernate;

import applica.framework.data.Entity;
import applica.framework.library.SimpleItem;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 08/10/14
 * Time: 13:19
 */
public class Mapper {

    private Class<? extends Entity> type;
    private int level = 0;
    private StringBuilder xml = new StringBuilder();

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
            open("class", attr("name", type.getName()), attr("table", pluralize(type.getSimpleName().toLowerCase()))); endl();
                openClose("property", attr("name", "id")); endl();
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

    private String pluralize(String singolar) {
        if (singolar.endsWith("y")) {
            return String.format("%sies", singolar.substring(0, singolar.length() - 1));
        } else {
            return String.format("%ss", singolar);
        }
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
