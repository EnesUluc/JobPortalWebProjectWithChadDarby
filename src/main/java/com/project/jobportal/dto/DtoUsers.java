package com.project.jobportal.dto;

import com.project.jobportal.entity.UsersType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUsers {

    private String email;
    @NotEmpty
    private String password;
    private UsersType usersType;
}

