package applica.framework.modules;

import applica.framework.annotations.Action;
import applica.framework.utils.TypeUtils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 01/10/14
 * Time: 19:02
 */

@applica.framework.annotations.Module("hibernate")
public class HibernateModule implements Module {

    @Action(value = "scan", description = "Scan for class in target directory")
    public void scan(Properties properties) {
        try {
            String pathToJar = properties.get("jar").toString();
            JarFile jarFile = new JarFile(pathToJar);
            Enumeration e = jarFile.entries();

            URL[] urls = {
                new URL("jar:file:" + pathToJar + "!/")
            };
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = (JarEntry) e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');

                try {
                    Class c = cl.loadClass(className);
                    if (TypeUtils.isEntity(c)) {
                        System.out.println(c.getName());
                    }
                } catch(Throwable ex) {}

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
