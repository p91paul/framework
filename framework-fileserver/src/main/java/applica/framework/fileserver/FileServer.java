package applica.framework.fileserver;

import java.io.IOException;
import java.io.InputStream;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 29/10/13
 * Time: 15:11
 */
public interface FileServer {
    String saveFile(String path, String extension, InputStream fileStream) throws IOException;

    InputStream getFile(String path) throws IOException;

    void deleteFile(String path) throws IOException;

    String saveImage(String path, String extension, InputStream imageStream) throws IOException;
}
