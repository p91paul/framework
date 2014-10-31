package applica.framework.data.mongodb.tests;

import applica.framework.data.mongodb.Constraints.ConstraintException;
import applica.framework.data.mongodb.tests.data.MockRepositoriesFactory;
import applica.framework.data.mongodb.tests.model.Brand;
import applica.framework.data.mongodb.tests.model.Game;
import applica.framework.data.mongodb.tests.model.Player;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/14
 * Time: 12:29
 */
public class ConstraintsTest {


    @org.junit.Test
    public void testUnique() {

        GameNameUniqueConstraint uniqueConstraint = new GameNameUniqueConstraint();
        uniqueConstraint.setRepositoriesFactory(new MockRepositoriesFactory());
        Game game = new Game(
                "game_test",
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
        );

        Exception exception = null;
        try {
            uniqueConstraint.check(game);
        } catch (ConstraintException e) {
            exception = e;
            System.out.println(e.getMessage());
        }

        Assert.notNull(exception, "Must be unique");
        Assert.isAssignable(ConstraintException.class, exception.getClass());
    }
    
}
