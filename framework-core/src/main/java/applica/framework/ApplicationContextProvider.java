package applica.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider {

    private static ApplicationContext applicationContext;

    @Autowired
    protected void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    public static ApplicationContext provide() {
        if(applicationContext == null) {
            throw new RuntimeException("ApplicationContext not provided. Please add applicationContextProvider bean in configuration");
        }

        return applicationContext;
    }

}
