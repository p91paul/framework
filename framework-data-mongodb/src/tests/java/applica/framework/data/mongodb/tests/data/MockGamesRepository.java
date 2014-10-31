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
                        "game1",
                        "gta5",
                        new Brand("brand1", "rockstar"),
                        "brand1",
                        Arrays.asList(
                                new Player("player1", "bruno"),
                                new Player("player2", "massimo")
                        ),
                        Arrays.asList(
                                "player1",
                                "player2"
                        )
                )
        );

        save(
                new Game(
                        "game2",
                        "lastofus",
                        new Brand("brand2", "dogs"),
                        "brand2",
                        Arrays.asList(
                                new Player("player1", "bruno"),
                                new Player("player2", "massimo")
                        ),
                        Arrays.asList(
                                "player1",
                                "player2"
                        )
                )
        );
    }

    @Override
    public Class<Game> getEntityType() {
        return Game.class;
    }
}
