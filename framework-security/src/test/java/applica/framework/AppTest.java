package applica.framework;

import applica.framework.security.authorization.HasPermissionAuthorization;
import applica.framework.security.authorization.Permissions;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );

        Permissions.instance().register("groups:create", new HasPermissionAuthorization());
        Permissions.instance().register("groups:remove", (u, p, o) -> {  });
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
