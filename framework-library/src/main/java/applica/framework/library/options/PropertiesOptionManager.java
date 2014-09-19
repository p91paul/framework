package applica.framework.library.options;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesOptionManager implements OptionsManager {

    private Properties properties;

    private String path = "/WEB-INF/options.properties";

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    private void init() throws IOException {
        properties = new Properties();

        Assert.notNull(path);

        if(path.startsWith("env:")) {
            path = "file:///".concat(System.getenv(path.substring(4)));
        }

        Resource res = resourceLoader.getResource(path);
        InputStream in = res.getInputStream();
        properties.load(in);
        in.close();
    }

    @Override
    public String get(String key) {
        return properties.getProperty(key);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
