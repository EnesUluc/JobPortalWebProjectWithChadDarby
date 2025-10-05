package com.project.jobportal.services;

import com.project.jobportal.dto.DtoUsers;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.JobSeekerProfileRepository;
import com.project.jobportal.repository.RecruiterProfileRepository;
import com.project.jobportal.repository.UsersRepository;
import org.antlr.v4.runtime.RecognitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UsersService(UsersRepository usersRepository, JobSeekerProfileRepository jobSeekerProfileRepository,
                 RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder){
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users addNew(DtoUsers dtoUsers){
        // Convert the user
        Users user = new Users();
        user.setEmail(dtoUsers.getEmail());
        user.setPassword(passwordEncoder.encode(dtoUsers.getPassword()));
        user.setUsersType(dtoUsers.getUsersType());

        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));

        Users savedUser = usersRepository.save(user);
        int userTypeId = user.getUsersType().getUserTypeId();

        // If this is a Recruiter, save the recruiter_profile table
        if(userTypeId == 1){
            recruiterProfileRepository.save(new RecruiterProfile(user));
        }else{
            // save the job_seeker_profile
            jobSeekerProfileRepository.save(new JobSeekerProfile(user));
        }
        return savedUser;
    }

    public Optional<Users> checkEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {
        Authentication authenticaton = SecurityContextHolder.getContext().getAuthentication();
        if(!(authenticaton instanceof AnonymousAuthenticationToken)){
            String username = authenticaton.getName();
            Users user = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
            int userId = user.getUserId();

            if(authenticaton.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                return recruiterProfileRepository.findById(userId)
                        .orElse(new RecruiterProfile());
            }else{
                return jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
            }

        }

        System.out.println("User is null");
        return null;
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();

            return usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
        }

        return null;
    }

    public Users findByEmail(String username) {
        return usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
