package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.ContactInfoDto;
import com.SWP.WebServer.dto.UpdateInfoEnDTO;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ResourceNotFoundException;
import com.SWP.WebServer.repository.EnterpriseRepository;
import com.SWP.WebServer.service.Impl.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {
    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Override
    public Enterprise getUserProfile(String userId) {
        return enterpriseRepository.findByUser_Uid(Integer.parseInt(userId));
    }

    @Override
    public Enterprise getUserProfileByEid(int userId) {
        return enterpriseRepository.findByEid(userId);
    }

    @Override
    public List<Enterprise> getAllEnterprises() {
        return enterpriseRepository.findAll();
    }

    @Override
    public Optional<Enterprise> getEnterpriseById(int id) {
        return enterpriseRepository.findById(id);
    }

    @Override
    public void updateContactInfo(ContactInfoDto body, String userId) {
        Enterprise user = enterpriseRepository.findByUser_Uid(Integer.parseInt(userId));
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

        enterpriseRepository.save(user);
    }


    @Override
    public Enterprise updateInfoEn(UpdateInfoEnDTO body, String userId) {
        int id = Integer.parseInt(userId);
        Enterprise user = enterpriseRepository.findByUser_Uid(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        // Update user fields
        if (body.getCity() != null) user.setCity(body.getCity());
        if (body.getState() != null) user.setState(body.getState());
        if (body.getEnterpriseName() != null) user.setEnterprise_name(body.getEnterpriseName());
        if (body.getPhone() != null) user.setPhone(body.getPhone());
        if (body.getCompanyStory() != null) user.setCompanyStory(body.getCompanyStory());
        if (body.getFounded() != null) user.setFounded(body.getFounded());
        if (body.getEmployees() != null) user.setEmployees(body.getEmployees());
        if (body.getFounder() != null) user.setFounder(body.getFounder());


        // Handle intro content and embedded videos
        if (body.getCompanyStory() != null) {
            String transformedIntro = transformIntroContent(body.getCompanyStory());
            user.setCompanyStory(transformedIntro);
        }


        return enterpriseRepository.save(user);
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

    @Override
    public void updateAvatar(String url, String userId) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(userId));
        enterprise.setAvatar_url(url);
        enterpriseRepository.save(enterprise);
    }
    public void toggleActiveStatus(int id) {
        Optional<Enterprise> optionalEnterprise = enterpriseRepository.findById(id);
        if (optionalEnterprise.isPresent()) {
            Enterprise enterprise = optionalEnterprise.get();
            User user = enterprise.getUser();
            user.setActive(!user.isActive());
            enterpriseRepository.save(enterprise);
        } else {
            throw new IllegalArgumentException("Enterprise not found with ID: " + id);
        }
    }
}
