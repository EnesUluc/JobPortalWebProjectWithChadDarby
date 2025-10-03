package com.project.jobportal.controller;

import ch.qos.logback.core.util.StringUtil;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.Skills;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.UsersRepository;
import com.project.jobportal.services.JobSeekerProfileService;
import com.project.jobportal.util.FileUploadUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {
    private JobSeekerProfileService jobSeekerProfileService;
    private UsersRepository usersRepository;

    @Autowired
    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UsersRepository usersRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public String jobSeekerProfile(Model model){
        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();
            Users users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User could not find"));
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(users.getUserId());

            if(seekerProfile.isPresent()){
                jobSeekerProfile = seekerProfile.get();

                if(jobSeekerProfile.getSkills().isEmpty()){
                    skills.add(new Skills());
                    jobSeekerProfile.setSkills(skills);
                }
            }
            model.addAttribute("profile",jobSeekerProfile);
            model.addAttribute("skills", 2);
        }
        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNew(JobSeekerProfile jobSeekerProfile, Model model,
                         @RequestParam("image")MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();
            Users users = usersRepository.findByEmail(username).orElseThrow(()
                    -> new UsernameNotFoundException("User could not find"));
            jobSeekerProfile.setUserId(users);
            jobSeekerProfile.setUserAccountId(users.getUserId());
        }

        List<Skills> skillsList = new ArrayList<>();
        model.addAttribute("profile", jobSeekerProfile);
        model.addAttribute("skills", skillsList);

        for(Skills skills : jobSeekerProfile.getSkills()){
            skills.setJobSeekerProfile(jobSeekerProfile);
        }

        String imageName = "";
        String resumeName = "";

        if(!Objects.equals(image.getOriginalFilename(),"")){
            System.out.println("Buraya girdiii.");
            imageName = StringUtils.cleanPath(
                    Objects.requireNonNull(image.getOriginalFilename())
            );
            System.out.println("ImageName: "+imageName);
            jobSeekerProfile.setProfilePhoto(imageName);
        }
        if(!Objects.equals(pdf.getOriginalFilename(),"")){
            resumeName = StringUtils.cleanPath(
                    Objects.requireNonNull(pdf.getOriginalFilename())
            );
            System.out.println("ResumeName: "+resumeName);
            jobSeekerProfile.setResume(resumeName);
        }

        JobSeekerProfile savedJobSeeker = jobSeekerProfileService.addNew(jobSeekerProfile);

        try{
            String uploadDir = "photos/candidate/"+ jobSeekerProfile.getUserAccountId();
            if(!Objects.equals(image.getOriginalFilename(),"")){
                FileUploadUtil.saveFile(uploadDir, imageName, image);
            }
            if(!Objects.equals(pdf.getOriginalFilename(),"")){
                FileUploadUtil.saveFile(uploadDir, resumeName, pdf);
            }
        }catch(IOException e){
            throw  new RuntimeException(e);

        }

        return "redirect:/dashboard/";
    }
}
