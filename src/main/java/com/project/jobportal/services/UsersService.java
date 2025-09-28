package com.project.jobportal.services;

import com.project.jobportal.dto.DtoUsers;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public Users addNew(DtoUsers dtoUsers){
        // Convert the user
        Users user = new Users();
        user.setEmail(dtoUsers.getEmail());
        user.setPassword(dtoUsers.getPassword());
        user.setUsersType(dtoUsers.getUsersType());

        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));
        return usersRepository.save(user);
    }

    public Optional<Users> checkEmail(String email){
        return usersRepository.findByEmail(email);
    }

}
