package com.appsmith.server.notifications;

import com.appsmith.server.configurations.EmailConfig;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Component
@Slf4j
public class EmailSender {

    @Autowired
    JavaMailSender emailSender;

    @Autowired
    EmailConfig emailConfig;

    private final String MAIL_FROM = "hello@appsmith.com";

    /**
     * This function sends an HTML email to the user from the default email address
     *
     * @param to
     * @param subject
     * @param text
     * @throws MailException
     */
    public void sendMail(String to, String subject, String text) throws MailException {
        log.debug("Got request to send email to: {} with subject: {} and text: {}", to, subject, text);
        // Don't send an email for local, dev or test environments
        if (!emailConfig.isEmailEnabled()) {
            return;
        }

        log.debug("Going to send email to {} with subject {}", to, subject);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setTo(to);
            helper.setFrom(MAIL_FROM);
            helper.setSubject(subject);
            helper.setText(text, true);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Unable to create the mime message while sending an email to {} with subject: {}. Cause: ", to, subject, e);
        }
    }

    /**
     * This function replaces the variables in an email template to actual values. It uses the Mustache SDK.
     *
     * @param template The name of the template where the HTML text can be found
     * @param params A Map of key-value pairs with the key being the variable in the template & value being the actual
     *              value with which it must be replaced.
     * @return
     * @throws IOException
     */
    public String replaceEmailTemplate(String template, Map<String, String> params) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        StringWriter stringWriter = new StringWriter();
        Mustache mustache = mf.compile(template);
        mustache.execute(stringWriter, params).flush();
        String emailTemplate = stringWriter.toString();
        return emailTemplate;
    }
}
