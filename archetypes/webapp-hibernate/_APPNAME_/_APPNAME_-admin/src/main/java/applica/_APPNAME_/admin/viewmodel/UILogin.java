package applica._APPNAME_.admin.viewmodel;

import applica.framework.annotations.*;
import applica.framework.data.SEntity;
import applica.framework.data.hibernate.annotations.IgnoreMapping;
import applica.framework.library.fields.renderers.PasswordFieldRenderer;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 26/10/13
 * Time: 10:34
 */
@IgnoreMapping
@Form(UILogin.EID)
@FormButtons({@FormButton(label="login", type="submit")})
public class UILogin extends SEntity {

    public static final String EID = "login";

    @FormField(description = "login")
    private String username;

    @FormField(description = "password")
    @FormFieldRenderer(PasswordFieldRenderer.class)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
