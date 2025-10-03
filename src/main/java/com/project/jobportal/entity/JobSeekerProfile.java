package com.project.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_seeker_profile")
public class JobSeekerProfile {

    public JobSeekerProfile(Users userId) {
        this.userId = userId;
    }

    @Id
    private Integer userAccountId;

    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String state;
    private String workAuthorization;
    private String employmentType;
    private String resume;
    @Column(nullable = true, length = 64)
    private String profilePhoto;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    @OneToMany(targetEntity = Skills.class, cascade = CascadeType.ALL,
            mappedBy = "jobSeekerProfile")
    List<Skills> skills;

    @Transient // Means don't send to db
    public String getPhotosImagePath(){
        if(profilePhoto == null || userAccountId == null) return null;
        return "/photos/candidate/" + userAccountId + "/" + profilePhoto;
    }

}
