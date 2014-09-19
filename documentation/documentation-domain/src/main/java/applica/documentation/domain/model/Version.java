package applica.documentation.domain.model;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 18:55
 */
public class Version extends OEntity {

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
