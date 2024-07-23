package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.Job;
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
        JobSeeker jobSeeker = jobSeekerRepository.findByJid(jid);
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
        Enterprise enterprise = enterpriseRepository.findByEid(eid);
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
        JobSeeker jobSeeker = jobSeekerRepository.findByUser_Uid(uid);
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void sendEmailForJobToJobSeeker(JobSeeker jobSeeker, Job job) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("tjobnoreplymail@gmail.com");
            mimeMessageHelper.setTo(jobSeeker.getUser().getEmail());
            mimeMessageHelper.setSubject("New Job Posting: " + job.getTitle());
            mimeMessageHelper.setText("Dear " + jobSeeker.getFirst_name() + " " + jobSeeker.getLast_name() + ",\n\n" +
                    "A new job has been posted in your occupation category:\n\n" +
                    "Job Title: " + job.getTitle() + "\n" +
                    "Description: " + job.getDescription() + "\n" +
                    "Location: " + job.getAddress() + "\n" +
                    "Salary: " + job.getMinSalary() + " - " + job.getMaxSalary() + "\n" +
                    "\n" +
                    "Please log in to your account to view more details and apply.\n\n" +
                    "Best regards,\n" +
                    "Your Company Name");

            emailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //sendApprovalEmail
    public void sendApprovalEmail(Job job, int eid) {

        Enterprise enterprise = enterpriseRepository.findByEid(eid);
        String toEmail = enterprise.getUser().getEmail();
        String enName = enterprise.getEnterprise_name();
        String name = "Top Job";
        String jobTitle = job.getTitle();
        String subject = "Job Approval Notification";
        String body = String.format(
                "Dear %s,%n%nCongratulations! Your job posting titled '%s' has been approved.%n%n" +
                        "Best regards" +
                        " %n%s",
                enName, jobTitle, enName
        );

        sendEmailToEnterprise(eid, name, toEmail, subject, body);
    }

    public void sendRejectionEmail(Job job, int eid, String rejectReason, String otherReason) {
        Enterprise enterprise = enterpriseRepository.findByEid(eid);

        if (enterprise != null) {
            String toEmail = enterprise.getUser().getEmail();
            String enName = enterprise.getEnterprise_name(); // Make sure this method name matches your entity's getter method
            String jobTitle = job.getTitle();
            String name = "Top Job";
            String subject = "Job Application Rejected";
            String body = String.format(
                    "Dear %s,%n%n" +
                            "We regret to inform you that your job posting titled '%s' has been rejected.%n" +
                            "Reason: %s%n%n" +
                            "Best regards,%n" +
                            "%s",
                    enName, jobTitle, rejectReason, name
            );
            sendEmailToEnterprise(eid, name, toEmail, subject, body);
        } else {
            // Handle case where enterprise is not found
            System.err.println("Enterprise not found for eid: " + eid);
        }
    }


}
