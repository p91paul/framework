package applica.framework;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.nio.file.Paths;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 07/10/14
 * Time: 17:02
 */
public class AppContext {

    private static AppContext s_current;

    public static AppContext current() {
        if (s_current == null) {
            s_current = new AppContext();
            s_current.init();
        }

        return s_current;
    }

    private String appDir;
    private String appName;

    private void init() {
        appDir = Paths.get("").toAbsolutePath().toString();
        File manifest = new File(appPath("app.manifest"));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(manifest);
            Element root = document.getDocumentElement();
            appName = root.getElementsByTagName("appname").item(0).getTextContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String appPath(String path) {
        String normalized = path;
        if (path.startsWith("\\") || path.startsWith("/")) {
            normalized = path.substring(0, path.length() - 1);
        }
        return String.format("%s%s%s", appDir, File.separator, normalized);
    }

    public String getAppName() {
        return appName;
    }

}
