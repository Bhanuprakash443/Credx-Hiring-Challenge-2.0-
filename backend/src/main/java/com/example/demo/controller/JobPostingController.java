package com.example.demo.controller;

import com.example.demo.config.UserContext;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobPosting job) {
        Long userId = UserContext.getUserId();
        UserRole role = UserContext.getUserRole();

        if (role != UserRole.COMPANY || userId == null) {
            return ResponseEntity.status(403).body("Only companies can post jobs");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent() || userOpt.get().getAssociatedId() == null) {
            return ResponseEntity.badRequest().body("Company profile not found");
        }

        Long companyId = userOpt.get().getAssociatedId();
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Company not found");
        }

        job.setCompany(companyOpt.get());
        job.setStatus(JobStatus.PENDING);
        JobPosting savedJob = jobPostingRepository.save(job);

        // Notify Admins
        List<User> admins = userRepository.findAll().stream()
                .filter(u -> u.getRole() == UserRole.ADMIN)
                .toList();
        for (User admin : admins) {
            notificationRepository.save(new Notification(admin.getId(), 
                "New job posting '" + savedJob.getTitle() + "' from " + companyOpt.get().getName() + " is pending approval."));
        }

        return ResponseEntity.ok(savedJob);
    }

    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllJobs() {
        UserRole role = UserContext.getUserRole();
        Long userId = UserContext.getUserId();

        if (role == UserRole.ADMIN) {
            return ResponseEntity.ok(jobPostingRepository.findAll());
        } else if (role == UserRole.COMPANY && userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent() && userOpt.get().getAssociatedId() != null) {
                return ResponseEntity.ok(jobPostingRepository.findByCompanyId(userOpt.get().getAssociatedId()));
            }
        }
        
        // Student or default public view (only approved and active)
        LocalDate today = LocalDate.now();
        List<JobPosting> activeJobs = jobPostingRepository.findByStatusAndDeadlineGreaterThanEqual(JobStatus.APPROVED, today);
        return ResponseEntity.ok(activeJobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (!jobOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        JobPosting job = jobOpt.get();
        UserRole role = UserContext.getUserRole();
        
        // Security check for students (must be approved)
        if (role == UserRole.STUDENT && job.getStatus() != JobStatus.APPROVED) {
            return ResponseEntity.status(403).body("Unauthorized to view this posting");
        }
        
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateJobStatus(@PathVariable Long id, @RequestParam JobStatus status) {
        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (!jobOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        JobPosting job = jobOpt.get();
        UserRole role = UserContext.getUserRole();
        Long userId = UserContext.getUserId();

        if (role == UserRole.ADMIN) {
            if (status != JobStatus.APPROVED && status != JobStatus.REJECTED) {
                return ResponseEntity.badRequest().body("Admin can only approve or reject jobs");
            }
            job.setStatus(status);
            job = jobPostingRepository.save(job);

            // Notify company users
            final Long targetCompanyId = job.getCompany().getId();
            List<User> companyUsers = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == UserRole.COMPANY && targetCompanyId.equals(u.getAssociatedId()))
                    .toList();
            for (User companyUser : companyUsers) {
                notificationRepository.save(new Notification(companyUser.getId(),
                        "Your job posting '" + job.getTitle() + "' has been " + status.name() + " by admin."));
            }

            return ResponseEntity.ok(job);
        } else if (role == UserRole.COMPANY && userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent() && userOpt.get().getAssociatedId().equals(job.getCompany().getId())) {
                if (status != JobStatus.CLOSED) {
                    return ResponseEntity.badRequest().body("Companies can only close their jobs");
                }
                job.setStatus(status);
                job = jobPostingRepository.save(job);
                return ResponseEntity.ok(job);
            }
        }

        return ResponseEntity.status(403).body("Unauthorized to update job status");
    }
}
