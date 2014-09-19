package applica.framework.library.mail;

import applica.framework.library.options.OptionsManager;
import applica.framework.library.velocity.VelocityBuilderProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.StringWriter;

public class TemplatedMail {

    private Log logger = LogFactory.getLog(getClass());

    public static final int HTML = 1;
    public static final int TEXT = 2;

    private VelocityContext context;
    private String templatePath;
    private String from;
    private String to;
    private String subject;
    private OptionsManager options;
    private int mailFormat;

    public TemplatedMail() {
        context = new VelocityContext();
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public OptionsManager getOptions() {
        return options;
    }

    public void setOptions(OptionsManager options) {
        this.options = options;
    }

    public void put(String key, Object value) {
        context.put(key, value);
    }


    public int getMailFormat() {
        return mailFormat;
    }

    public void setMailFormat(int mailFormat) {
        this.mailFormat = mailFormat;
    }

    public void send() throws MailException, AddressException, MessagingException {
        if (options == null) throw new MailException("options not setted");
        if (!StringUtils.hasLength(from)) throw new MailException("from not setted");
        if (!StringUtils.hasLength(to)) throw new MailException("to not setted");
        if(mailFormat == 0){
            mailFormat = TEXT;
        }

        Session session = MailUtils.getMailSession(options);
        MimeMessage message = new MimeMessage(session);
        message.addFrom(new InternetAddress[]{new InternetAddress(from)});
        message.addRecipient(RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        logger.info(String.format("Sending email '%s' with template '%s' to '%s'", subject, templatePath, to));

        Template template = VelocityBuilderProvider.provide().engine().getTemplate(templatePath, "UTF-8");
        StringWriter bodyWriter = new StringWriter();
        template.merge(context, bodyWriter);

        if(mailFormat == TEXT){
            message.setContent(bodyWriter.toString(),"text/plain" );
            message.setText(bodyWriter.toString(), "UTF-8");
        } else if (mailFormat == HTML){
            message.setContent(bodyWriter.toString(),"text/html" );
        }

        Transport.send(message);
    }
}
