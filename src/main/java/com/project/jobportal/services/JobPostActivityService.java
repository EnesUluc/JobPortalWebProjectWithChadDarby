package com.project.jobportal.services;

import com.project.jobportal.dto.RecruiterJobsDto;
import com.project.jobportal.entity.IRecruiterJobs;
import com.project.jobportal.entity.JobCompany;
import com.project.jobportal.entity.JobLocation;
import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.repository.JobPostActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepo jobPostActivityRepo;

    @Autowired
    JobPostActivityService(JobPostActivityRepo jobPostActivityRepo){
        this.jobPostActivityRepo = jobPostActivityRepo;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return jobPostActivityRepo.save(jobPostActivity);
    }
    public List<RecruiterJobsDto> getRecruiterJobs(int recruiter){
        List<IRecruiterJobs> recruiterJobs = jobPostActivityRepo.getRecruiterJobs(recruiter);

        List<RecruiterJobsDto> recruiterJobsDtos = new ArrayList<>();

        for(IRecruiterJobs rec: recruiterJobs){
            JobCompany jobCompany = new JobCompany(rec.getCompanyId(), rec.getName(), "");
            JobLocation location = new JobLocation(rec.getLocationId(), rec.getCity(), rec.getState(), rec.getCountry());
            recruiterJobsDtos.add(new RecruiterJobsDto(rec.getTotalCandidates(), rec.getJob_post_id(),
                    rec.getJob_title(), location, jobCompany));
        }

        return recruiterJobsDtos;
    }

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepo.findById(id).orElseThrow(() -> new RuntimeException("Could not found user"));
    }
}
