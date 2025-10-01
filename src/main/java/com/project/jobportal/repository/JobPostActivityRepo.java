package com.project.jobportal.repository;

import com.project.jobportal.entity.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostActivityRepo extends JpaRepository<JobPostActivity, Integer> {
}
