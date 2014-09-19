package applica.framework.library.ui;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

public class DynamicCssFile {
    private String path;
    private String content;

    public DynamicCssFile(String path) {
        this.path = path;

        this.load();
    }

    private void load() {
        File file = new File(path);
        if (file.exists()) {
            StringBuffer cssBuffer = new StringBuffer();

            InputStream input = null;
            try {
                input = FileUtils.openInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    cssBuffer.append(line).append("\r\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(input);
            }

            content = cssBuffer.toString();
        }
    }

    public String getFilePath() {
        return path;
    }

    public void setFilePath(String filePath) {
        this.path = filePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void applyProperties(final Properties properties) {
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String pattern = String.format("$(%s)", key);
            setContent(content.replace(pattern, (String) properties.get(key)));
        }

    }
}
