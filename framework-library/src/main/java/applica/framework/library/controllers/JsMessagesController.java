package applica.framework.library.controllers;

import applica.framework.library.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 2/26/13
 * Time: 5:14 PM
 */

/**
 * Use this controller to inject translations in javascript
 * Translations are in a msg global variables.
 * All texts in resources files are injected changing the sintax
 * Es: label.user_name -> msg.LABEL_USER_NAME
 * Es: msg.password_changed -> msg.MSG_PASSWORD_CHANGED
 */
@RequestMapping("/framework-dynamic-resources")
public class JsMessagesController {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private Cache cache;

    @RequestMapping("/messages.js")
    public @ResponseBody String messages(Locale locale) {
        String messages = (String)cache.get("localization.messages");

        if(messages == null) {
            Properties properties = new Properties();
            InputStream in = getClass().getResourceAsStream(String.format("/messages/messages_%s.properties", locale));
            if(in == null) return "";
            StringBuffer values = new StringBuffer();
            values.append("var msg = {};\r\n");
            try {
                properties.load(in);

                for (Map.Entry<Object, Object> message : properties.entrySet()) {
                    values.append(String.format("msg.%s = '%s';\r\n",
                            message.getKey().toString().replace(".", "_").toUpperCase(),
                            message.getValue().toString().replace("'", "\\'")
                    ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {}
            }

            messages = values.toString();
            cache.put("localization.messages", messages);
        }

        return messages;
    }

}
