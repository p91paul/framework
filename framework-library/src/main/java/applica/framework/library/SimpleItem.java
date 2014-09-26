package applica.framework.library;

import applica.framework.library.utils.Func;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleItem {
    private String label;
    private String value;

    public SimpleItem() {
        super();
    }

    public SimpleItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static <T> List<SimpleItem> createList(List<T> objects, Func<T, String> labelFunc, Func<T, String> valueFunc) {
        List<SimpleItem> items = new ArrayList<SimpleItem>();
        if (objects != null) {
            for (T obj : objects) {
                items.add(new SimpleItem(labelFunc.eval(obj), valueFunc.eval(obj)));
            }
        }
        return items;
    }

    public static List<SimpleItem> createList(List<?> objects, String labelProperty, String valueProperty) {
        List<SimpleItem> items = new ArrayList<SimpleItem>();
        if (objects != null) {
            for (Object obj : objects) {
                try {
                    Object label = PropertyUtils.getSimpleProperty(obj, labelProperty);
                    Object value = PropertyUtils.getSimpleProperty(obj, valueProperty);
                    String slabel = label != null ? label.toString() : "";
                    String svalue = value != null ? value.toString() : "";
                    items.add(new SimpleItem(slabel, svalue));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }
}