package applica.framework.data.mongodb.tests.data;

import applica.framework.data.Entity;
import applica.framework.data.LoadRequest;
import applica.framework.data.LoadResponse;
import applica.framework.data.Repository;
import applica.framework.data.mongodb.tests.model.Brand;
import applica.framework.data.mongodb.tests.model.Game;
import applica.framework.data.mongodb.tests.model.Player;

import java.util.Arrays;
import java.util.Optional;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/14
 * Time: 12:43
 */
public class MockGamesRepository extends MockRepository<Game> {

    public MockGamesRepository() {
        save(
                new Game(
                        Game.GTA_ID,
                        "gta5",
                        new Brand(Brand.ROCKSTAR_ID, "rockstar"),
                        Brand.ROCKSTAR_ID,
                        Arrays.asList(
                                new Player(Player.BRUNO_ID, "bruno"),
                                new Player(Player.MASSIMO_ID, "massimo")
                        ),
                        Arrays.asList(
                                Player.BRUNO_ID,
                                Player.MASSIMO_ID
                        )
                )
        );
    }

    @Override
    public Class<Game> getEntityType() {
        return Game.class;
    }
}
