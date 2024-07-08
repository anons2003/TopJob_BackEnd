package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.repository.JobRepository;
import com.SWP.WebServer.repository.JobSeekerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private JobSeekerRepository repository;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    //--ham set mail verify qua gmail mac dinh--//
    public void sendMail(
            String email, String subject, String html) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("tjobnoreplymail@gmail.com");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(html, true);

        emailSender.send(message);
    }

    public String sendMailFromJobSeeker(int jid,
                                      String name,
                                      String email,
                                      String subject,
                                      String body) {
        JobSeeker  jobSeeker = repository.findByJid(jid);
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("dvtson2004@gmail.com");
            mimeMessageHelper.setTo(jobSeeker.getUser().getEmail());
            mimeMessageHelper.setSubject(subject);

            // Create the email body with HTML
            String htmlBody = "<html><body>"
                    + "<p>Hello,</p>"
                    + "<p>You got a new message from " + name + ":</p>"
                    + "<p>Email: " + email + "</p>"
                    + "<p>Subject: " + subject + "</p>"
                    + "<p>Message: " + body + "</p>"
                    + "</body></html>";

            mimeMessageHelper.setText(htmlBody, true); // Set to true to indicate HTML

            emailSender.send(mimeMessage);
            return "Mail sent!";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
