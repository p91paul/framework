package applica.framework.data;

/**
 * Applica (www.applicadoit.com) User: bimbobruno Date: 12/09/14 Time: 10:35
 */
public class Key {

    private Object value;

    public Key(Object value) {
        this.value = value;
    }

    public Key() {
    }

    public String getStringValue() {
        if (value != null)
            return value.toString();

        return null;
    }

    public int getIntValue() {
        return IEntity.checkedId(value);
    }

    public long getLongValue() {
        return LEntity.checkedId(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
