package com.project.jobportal.controller;

import com.project.jobportal.dto.DtoUsers;
import com.project.jobportal.entity.Users;
import com.project.jobportal.entity.UsersType;
import com.project.jobportal.services.UsersService;
import com.project.jobportal.services.UsersTypeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {

    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

    UsersController(UsersTypeService usersTypeService, UsersService usersService){
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> usersTypes = usersTypeService.findAll();
        model.addAttribute("getAllTypes", usersTypes);
        model.addAttribute("user", new DtoUsers());

        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid DtoUsers dtoUsers, Model model){

        Optional<Users> ifExist = usersService.checkEmail(dtoUsers.getEmail());
        if(ifExist.isPresent()){
            List<UsersType> usersTypes = usersTypeService.findAll();
            model.addAttribute("getAllTypes", usersTypes);
            model.addAttribute("user", new DtoUsers());
            model.addAttribute("error","Email already registered, try to login or register with other email.");
            return "register";
        }
        usersService.addNew(dtoUsers);
        return "dashboard";
    }
}
