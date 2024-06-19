package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.ContactInfoDto;
import com.SWP.WebServer.dto.UpdateInfoDTO;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.exception.ResourceNotFoundException;
import com.SWP.WebServer.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JobSeekerServiceImpl implements JobSeekerService{

    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    public JobSeeker updateInfo(UpdateInfoDTO body, String userId)  {
        int id = Integer.parseInt(userId);
        JobSeeker user = jobSeekerRepository.findByUser_Uid(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        // Update user fields
        if (body.getCity() != null) user.setCity(body.getCity());
        if (body.getState() != null) user.setState(body.getState());
        if (body.getFirst_name() != null) user.setFirst_name(body.getFirst_name());
        if (body.getLast_name() != null) user.setLast_name(body.getLast_name());
        if (body.getUser_name() != null) user.getUser().setUser_name(body.getUser_name());
        if (body.getOccupation() != null) user.setOccupation(body.getOccupation());
        if (body.getEmail() != null) user.getUser().setEmail(body.getEmail());

        // Handle intro content and embedded videos
        if (body.getIntro() != null) {
            String transformedIntro = transformIntroContent(body.getIntro());
            user.setIntro(transformedIntro);
        }

        return jobSeekerRepository.save(user);
    }

    private String transformIntroContent(String intro) {
        // Regular expression to match and replace oembed tags with iframe tags for different YouTube URL formats
        String oembedRegex = "<oembed\\s+url=\"(https?://(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtube\\.com/embed/|youtu\\.be/)([\\w-]+)(?:[?&].*)?)\"></oembed>";
        Pattern oembedPattern = Pattern.compile(oembedRegex);
        Matcher oembedMatcher = oembedPattern.matcher(intro);

        StringBuffer result = new StringBuffer();
        while (oembedMatcher.find()) {
            String videoId = oembedMatcher.group(2);
            String startTime = "";
            String url = oembedMatcher.group(1);

            // Extract start time if present in the URL
            Pattern timePattern = Pattern.compile("[?&]t=(\\d+)");
            Matcher timeMatcher = timePattern.matcher(url);
            if (timeMatcher.find()) {
                startTime = "?start=" + timeMatcher.group(1);
            }

            String embedUrl = "https://www.youtube.com/embed/" + videoId + startTime;
            String iframe = String.format("<iframe width=\"560\" height=\"315\" src=\"%s\" frameborder=\"0\" allowfullscreen></iframe>", embedUrl);
            oembedMatcher.appendReplacement(result, iframe);
        }
        oembedMatcher.appendTail(result);

        return result.toString();
    }

    public void updateAvatar(
            String url,
            String userId) {
        JobSeeker jobSeeker = jobSeekerRepository.findByUser_Uid(Integer.parseInt(userId));
        jobSeeker.setAvatar_url(url);
        jobSeekerRepository.save(jobSeeker);
    }

    public void updateResume(
            String url,
            String userId) {
        JobSeeker jobSeeker = jobSeekerRepository.findByUser_Uid(Integer.parseInt(userId));
        jobSeeker.setResume_url(url);
        jobSeekerRepository.save(jobSeeker);
    }

    public void updateContactInfo(
            ContactInfoDto body,
            String userId) {
        JobSeeker user = jobSeekerRepository.findByUser_Uid(Integer.parseInt(userId));
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // Update contact information
        if (body.getWeb_url() != null) {
            user.setWeb_url(body.getWeb_url());
        }
        if (body.getPhone() != null) {
            user.setPhone(body.getPhone());
        }

        jobSeekerRepository.save(user);
    }

}
