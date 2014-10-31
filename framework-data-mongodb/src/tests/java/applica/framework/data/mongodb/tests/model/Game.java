package applica.framework.data.mongodb.tests.model;

import applica.framework.data.SEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/14
 * Time: 12:29
 */
public class Game extends SEntity {

    private String name;
    private Brand brand;
    private String brandId;
    private List<Player> players = new ArrayList<>();
    private List<String> playerIds = new ArrayList<>();

    public Game(String id, String name, Brand brand, String brandId, List<Player> players, List<String> playerIds) {
        setId(id);
        this.name = name;
        this.brand = brand;
        this.brandId = brandId;
        this.players = players;
        this.playerIds = playerIds;
    }

    public Game() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<String> playerIds) {
        this.playerIds = playerIds;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}
