package com.norbertkoziana.Session.Authentication.email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements ConfirmationEmailService, ChangePasswordEmailService {

    private static final String from = "springbootemailsender19882@gmail.com";

    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void sendConfirmationMail(String emailAddress, String token) {
        String confirmationUrl = "http://localhost:8080/auth/confirm?token=" + token;

        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildConfirmationEmailContent(confirmationUrl), true);
            helper.setTo(emailAddress);
            helper.setFrom(from);
            helper.setSubject("Confirm your email");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e){
            throw new IllegalStateException("failed to send email");
        }
    }

    private String buildConfirmationEmailContent(String confirmationUrl) {
        return
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Confirm Your Email</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            background-color: #ffffff;\n" +
                        "            margin: 50px auto;\n" +
                        "            padding: 20px;\n" +
                        "            max-width: 600px;\n" +
                        "            border-radius: 8px;\n" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "        }\n" +
                        "        .header {\n" +
                        "            text-align: center;\n" +
                        "            padding: 20px 0;\n" +
                        "            border-bottom: 1px solid #dddddd;\n" +
                        "        }\n" +
                        "        .content {\n" +
                        "            margin: 20px 0;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        .button {\n" +
                        "            display: inline-block;\n" +
                        "            padding: 15px 25px;\n" +
                        "            font-size: 16px;\n" +
                        "            color: #ffffff !important;\n" +
                        "            background-color: #007bff;\n" +
                        "            text-decoration: none;\n" +
                        "            font-weight: bold;\n" +
                        "            border-radius: 5px;\n" +
                        "        }\n" +
                        "        .footer {\n" +
                        "            text-align: center;\n" +
                        "            padding: 20px 0;\n" +
                        "            color: #aaaaaa;\n" +
                        "            font-size: 14px;\n" +
                        "            border-top: 1px solid #dddddd;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <h1>Confirm Your Email Address</h1>\n" +
                        "        </div>\n" +
                        "        <div class=\"content\">\n" +
                        "            <p>Thank you for registering. Please click the button below to confirm your email address.</p>\n" +
                        "            <a href=\"" + confirmationUrl + "\" class=\"button\">Confirm Email</a>\n" +
                        "        </div>\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>If you did not create an account, no further action is required.</p>\n" +
                        "            <p>&copy; 2024 Your Company Name. All rights reserved.</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";
    }

    @Override
    @Async
    public void sendPasswordChangeMail(String emailAddress, String token) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(buildChangePasswordEmailContent(token), true);
            helper.setTo(emailAddress);
            helper.setFrom(from);
            helper.setSubject("Change your password");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e){
            throw new IllegalStateException("failed to send email");
        }
    }

    private String buildChangePasswordEmailContent(String token) {
        return
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Reset Your Password</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            background-color: #ffffff;\n" +
                        "            margin: 50px auto;\n" +
                        "            padding: 20px;\n" +
                        "            max-width: 600px;\n" +
                        "            border-radius: 8px;\n" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "        }\n" +
                        "        .header {\n" +
                        "            text-align: center;\n" +
                        "            padding: 20px 0;\n" +
                        "            border-bottom: 1px solid #dddddd;\n" +
                        "        }\n" +
                        "        .content {\n" +
                        "            margin: 20px 0;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        .footer {\n" +
                        "            text-align: center;\n" +
                        "            padding: 20px 0;\n" +
                        "            color: #aaaaaa;\n" +
                        "            font-size: 14px;\n" +
                        "            border-top: 1px solid #dddddd;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <h1>Reset Your Password</h1>\n" +
                        "        </div>\n" +
                        "        <div class=\"content\">\n" +
                        "            <p>To reset your password, use the following token:</p>\n" +
                        "            <p><strong>" + token + "</strong></p>\n" +
                        "            <p>If you did not request a password reset, please ignore this email.</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>&copy; 2024 Your Company Name. All rights reserved.</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";
    }

}
