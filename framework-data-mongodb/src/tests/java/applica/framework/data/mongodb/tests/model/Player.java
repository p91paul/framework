package applica.framework.data.mongodb.tests.model;

import applica.framework.data.SEntity;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/14
 * Time: 12:31
 */
public class Player extends SEntity {

    private String name;

    public Player(String id, String name) {
        setId(id);
        this.name = name;
    }

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
