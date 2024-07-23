package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.entity.Notification;
import com.SWP.WebServer.repository.JobSeekerRepository;
import com.SWP.WebServer.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private EmailService emailService;

    public void notifyJobSeekers(Job job) {
        List<JobSeeker> jobSeekers = jobSeekerRepository.findJobSeekersByOccupation(job.getJobCategoryEntity().getJobCategoryId());
        for (JobSeeker jobSeeker : jobSeekers) {
            Notification notification = new Notification();
            notification.setMessage("New job posted in your occupation category: " + job.getTitle());
            notification.setJobSeeker(jobSeeker);
            notification.setJob(job);
            notification.setRead(false);
            markForDeletion(notification, 5);
            //cmt email service de test
//            emailService.sendEmailForJobToJobSeeker(jobSeeker, job);
//             Save notification
            notificationRepository.save(notification);
            // Send real-time notification
            messagingTemplate.convertAndSend("/topic/notifications/" + jobSeeker.getUser().getUid(), notification.getMessage());
//            System.out.println(notification);
        }
    }

    public void markForDeletion(Notification notification, int daysUntilDeletion) {
//        Use for Scheduling days
//        cvApply.setDeletionTime(LocalDateTime.now().plusDays(daysUntilDeletion));

//        Use for testing in minutes
        notification.setDeletionTime(LocalDateTime.now().plusMinutes(daysUntilDeletion));
    }

    // Nho kiem tra xem co cai @EnableScheduling trong WebServerSwpApplication thi moi chay duoc
    @Scheduled(cron = "*/60 * * * * *") // Run every 30 seconds for quicker testing
//    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void deleteNotification() {
//        System.out.println("I am scanning for deletion");
//        It Works!
        List<Notification> notifications = notificationRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Notification motification : notifications) {
            if (motification.getDeletionTime() != null && now.isAfter(motification.getDeletionTime())) {
                notificationRepository.delete(motification);
//                System.out.println("Done!");
            }
        }
    }


    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }
}
