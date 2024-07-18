package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.repository.EnterpriseRepository;
import com.SWP.WebServer.repository.JobSeekerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

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

    public String sendMailToJobSeeker(int jid,
                                      String name,
                                      String email,
                                      String subject,
                                      String body) {
        JobSeeker  jobSeeker = jobSeekerRepository.findByJid(jid);
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("tjobnoreplymail@gmail.com");
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

    public String sendEmailToEnterprise(int eid, String name, String email, String subject, String body) {
        Enterprise  enterprise = enterpriseRepository.findByEid(eid);
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("tjobnoreplymail@gmail.com");
            mimeMessageHelper.setTo(enterprise.getUser().getEmail());
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


    public void sendRejectionEmail(String enId, int uid, String[] rejectReason) {
        JobSeeker  jobSeeker = jobSeekerRepository.findByUser_Uid(uid);
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(enId));


        if (jobSeeker == null || enterprise == null) {
            throw new IllegalArgumentException("JobSeeker or Enterprise not found");
        }

        String toEmail = jobSeeker.getUser().getEmail();
        String fullName = jobSeeker.getFirst_name() + " " + jobSeeker.getLast_name();
        String jobTitle = jobSeeker.getOccupation(); // Or another job-related field if you have one
        String enName = enterprise.getEnterprise_name();

        String subject = "Application Rejected";
        StringBuilder reasonText = new StringBuilder();
        reasonText.append(String.format(
                "Dear %s,%n%nWe regret to inform you that your application for the position of %s at %s has been rejected.%n%nReasons for Rejection:%n",
                fullName, jobTitle, enName
        ));
        for (String reason : rejectReason) {
            reasonText.append(String.format("- %s%n", reason));
        }
        reasonText.append(String.format("%nBest regards, %s", enName));

        sendEmail(toEmail, subject, reasonText.toString());

    }

    public void sendAcceptanceEmail(String enId, int uid) {
        JobSeeker jobSeeker = jobSeekerRepository.findByUser_Uid(uid);
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(enId));

        if (jobSeeker == null || enterprise == null) {
            throw new IllegalArgumentException("JobSeeker or Enterprise not found");
        }

        String toEmail = jobSeeker.getUser().getEmail();
        String fullName = jobSeeker.getFirst_name() + " " + jobSeeker.getLast_name();
        String jobTitle = jobSeeker.getOccupation(); // Or another job-related field if you have one
        String enName = enterprise.getEnterprise_name();
        String subject = "Application Accepted";
        String text = String.format(
                "Dear %s,%n%nCongratulations! Your application for the position of %s at %s has been accepted.%n%nBest regards,%n%s",
                fullName, jobTitle, enName, enName
        );

        sendEmail(toEmail, subject, text);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("tjobnoreplymail@gmail.com");

            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            emailSender.send(mimeMessage);
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }


}
