package applica.framework.tests.model;

import applica.framework.data.SEntity;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/14
 * Time: 12:31
 */

public class Player extends SEntity {

    public static final String BRUNO_ID = "player1";
    public static final String MASSIMO_ID = "player2";

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

    @Override
    public String toString() {
        return name;
    }
}
