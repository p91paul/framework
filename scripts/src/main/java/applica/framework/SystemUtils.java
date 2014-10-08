package applica.framework;

import java.io.File;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 08/10/14
 * Time: 18:08
 */
public class SystemUtils {

    public static String multiplatformPath(String path) {
        return path.replace("/", File.separator);
    }

}
