package com.project.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_type")
public class UsersType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userTypeId;

    private String userTypeName;

    @OneToMany(targetEntity = Users.class, mappedBy = "usersType", cascade = CascadeType.ALL)
    private List<Users> users;

    UsersType(int userTypeId, String userTypeName){
        this.userTypeId = userTypeId;
        this.userTypeName = userTypeName;
    }
}
