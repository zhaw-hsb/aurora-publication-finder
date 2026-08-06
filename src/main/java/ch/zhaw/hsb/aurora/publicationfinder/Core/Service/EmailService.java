/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Service;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyCredentialsConfiguration;

/**
 * This class provides the service to send emails
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class EmailService {
    
    
    private static final String SMTP_HOST = "smtps.zhaw.ch";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = PropertyCredentialsConfiguration.getMail();
    private static final String SMTP_PASSWORD = PropertyCredentialsConfiguration.getMailPassword();


    /**
     * Method to send an email
     * @param to email address of the receiver
     * @param subject subject of the email
     * @param body body of the email
     */
    public void sendEmail(String to, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        File attachmentFile = null;

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Check if body contains more than one line
            boolean needsAttachment = body != null && body.lines().count() > 10;

            if (needsAttachment) {
                attachmentFile = File.createTempFile("email_content_", ".txt");

                try (FileWriter writer = new FileWriter(attachmentFile)) {
                    writer.write(body);
                }

                Multipart multipart = new MimeMultipart();

                // Email text body
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText("See attachment file.");
                multipart.addBodyPart(textPart);

                // Attachment part
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentFile);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName("report.txt");
                multipart.addBodyPart(attachmentPart);

                message.setContent(multipart);

            } else {
                // Send normal email without attachment
                message.setText(body);
            }

            // Send email
            Transport.send(message);

            System.out.println("Email sent successfully to: " + to);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();

        } finally {
            // Cleanup temporary file
            if (attachmentFile != null && attachmentFile.exists()) {
                attachmentFile.delete();
            }
        }
    }
}
