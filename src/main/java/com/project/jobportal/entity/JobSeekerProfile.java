package com.project.jobportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "")
public class JobSeekerProfile {

    @Id
    private int userAccountId;

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

    public JobSeekerProfile(Users userId) {
        this.userId = userId;
    }
}
