package applica._APPNAME_.admin.controllers;

import applica.framework.CrudConfigurationException;
import applica.framework.Form;
import applica.framework.FormCreationException;
import applica.framework.FormDescriptor;
import applica.framework.library.fields.renderers.*;
import applica.framework.library.forms.renderers.NoFrameFormRenderer;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.FormResponse;
import applica.framework.library.responses.SimpleResponse;
import applica._APPNAME_.admin.fields.renderers.LibraryDemoFileRenderer;
import applica._APPNAME_.admin.fields.renderers.LibraryDemoMultiSearchableInputRenderer;
import applica._APPNAME_.admin.fields.renderers.LibraryDemoSelectRenderer;
import applica._APPNAME_.admin.fields.renderers.LibraryDemoSingleSearchableInputRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 3/1/13
 * Time: 10:08 AM
 */
@Controller
@RequestMapping("/librarydemo")
public class LibraryDemoController {

    @Autowired ApplicationContext context;

    @RequestMapping("/form")
    public @ResponseBody SimpleResponse form() {
        try {
            FormResponse formResponse = new FormResponse();

            Form form = new Form();
            form.setRenderer(context.getBean(NoFrameFormRenderer.class));

            FormDescriptor descriptor = new FormDescriptor(form);
            descriptor.addField("check", Boolean.class, "check", "", "", context.getBean(DefaultFieldRenderer.class));
            descriptor.addField("color", String.class, "color", "", "", context.getBean(ColorFieldRenderer.class));
            descriptor.addField("mail", String.class, "mail", "", "", context.getBean(MailFieldRenderer.class));
            descriptor.addField("password", String.class, "password", "", "", context.getBean(PasswordFieldRenderer.class));
            descriptor.addField("date", Date.class, "date", "", "", context.getBean(DefaultFieldRenderer.class));
            descriptor.addField("file", String.class, "file", "", "", context.getBean(LibraryDemoFileRenderer.class));
            descriptor.addField("percentage", Integer.class, "percentage", "", "", context.getBean(PercentageFieldRenderer.class));
            descriptor.addField("readonly", Integer.class, "readonly", "", "", context.getBean(ReadOnlyFieldRenderer.class));
            descriptor.addField("select", String.class, "readonly", "", "", context.getBean(LibraryDemoSelectRenderer.class));
            descriptor.addField("text", String.class, "text", "", "", context.getBean(DefaultFieldRenderer.class));
            descriptor.addField("textarea", String.class, "readonly", "", "", context.getBean(TextAreaFieldRenderer.class));
            descriptor.addField("time", String.class, "time", "", "", context.getBean(TimePickerRenderer.class));
            descriptor.addField("html", String.class, "html", "", "", context.getBean(HtmlFieldRenderer.class));

            descriptor.addField("singleSearchableInput", String.class, "singleSearchableInput", "", "", context.getBean(LibraryDemoSingleSearchableInputRenderer.class));
            descriptor.addField("multiSearchableInput", List.class, "multiSearchableInput", "", "", context.getBean(LibraryDemoMultiSearchableInputRenderer.class));
            formResponse.setTitle("Library demo form");
            formResponse.setContent(form.writeToString());

            return formResponse;
        } catch (FormCreationException e) {
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            return new ErrorResponse(e.getMessage());
        } catch(Exception e) {
            return new ErrorResponse(e.getMessage());
        }
    }

}
