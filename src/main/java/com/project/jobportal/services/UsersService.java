package com.project.jobportal.services;

import com.project.jobportal.dto.DtoUsers;
import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.JobSeekerProfileRepository;
import com.project.jobportal.repository.RecruiterProfileRepository;
import com.project.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    @Autowired
    UsersService(UsersRepository usersRepository, JobSeekerProfileRepository jobSeekerProfileRepository,
                 RecruiterProfileRepository recruiterProfileRepository){
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public Users addNew(DtoUsers dtoUsers){
        // Convert the user
        Users user = new Users();
        user.setEmail(dtoUsers.getEmail());
        user.setPassword(dtoUsers.getPassword());
        user.setUsersType(dtoUsers.getUsersType());

        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));

        Users savedUser = usersRepository.save(user);
        int userTypeId = user.getUsersType().getUserTypeId();
        if(userTypeId == 1){
            recruiterProfileRepository.save(new RecruiterProfile(user));
        }else{
            jobSeekerProfileRepository.save(new JobSeekerProfile(user));
        }
        return savedUser;
    }

    public Optional<Users> checkEmail(String email){
        return usersRepository.findByEmail(email);
    }

}
