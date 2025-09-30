package com.project.jobportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {

    @Id
    private int userAccountId;

    private String firstName;
    private String lastName;
    private String city;
    private String company;
    private String country;
    private String state;
    @Column(nullable = true, length = 64)
    private String profilePhoto;


    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto == null) return null;
        return "/photos/recruiter/"+userAccountId+"/"+profilePhoto;
    }

    public RecruiterProfile(Users user) {
        this.userId = user;
    }
}
