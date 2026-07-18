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
@RequestMapping("/api/applications")
public class ApplicationController {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping
    public ResponseEntity<?> applyForJob(@RequestParam Long jobId) {
        Long userId = UserContext.getUserId();
        UserRole role = UserContext.getUserRole();

        if (role != UserRole.STUDENT || userId == null) {
            return ResponseEntity.status(403).body("Only students can apply for jobs");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent() || userOpt.get().getAssociatedId() == null) {
            return ResponseEntity.badRequest().body("Student profile not found");
        }

        Long studentId = userOpt.get().getAssociatedId();
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Student profile does not exist");
        }

        Optional<JobPosting> jobOpt = jobPostingRepository.findById(jobId);
        if (!jobOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Job posting not found");
        }

        JobPosting job = jobOpt.get();
        Student student = studentOpt.get();

        // 1. Verify Job Status is APPROVED
        if (job.getStatus() != JobStatus.APPROVED) {
            return ResponseEntity.badRequest().body("Applications are not open for this job as it is not approved");
        }

        // 2. Verify Deadline is not passed
        if (job.getDeadline().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Application deadline has passed");
        }

        // 3. Verify CGPA eligibility
        if (student.getCgpa() < job.getMinCgpa()) {
            return ResponseEntity.badRequest().body("You do not meet the minimum CGPA eligibility of " + job.getMinCgpa() + " (your CGPA: " + student.getCgpa() + ")");
        }

        // 4. Verify no duplicate applications
        if (applicationRepository.existsByJobPostingIdAndStudentId(jobId, studentId)) {
            return ResponseEntity.badRequest().body("You have already applied for this job");
        }

        Application application = new Application(job, student);
        Application savedApp = applicationRepository.save(application);

        // Notify Company Users
        final Long targetCompanyId = job.getCompany().getId();
        List<User> companyUsers = userRepository.findAll().stream()
                .filter(u -> u.getRole() == UserRole.COMPANY && targetCompanyId.equals(u.getAssociatedId()))
                .toList();
        for (User companyUser : companyUsers) {
            notificationRepository.save(new Notification(companyUser.getId(),
                    "New application received from " + student.getName() + " for '" + job.getTitle() + "'."));
        }

        return ResponseEntity.ok(savedApp);
    }

    @GetMapping
    public ResponseEntity<List<Application>> getApplications() {
        UserRole role = UserContext.getUserRole();
        Long userId = UserContext.getUserId();

        if (role == UserRole.ADMIN) {
            return ResponseEntity.ok(applicationRepository.findAll());
        } else if (role == UserRole.COMPANY && userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent() && userOpt.get().getAssociatedId() != null) {
                return ResponseEntity.ok(applicationRepository.findByJobPostingCompanyId(userOpt.get().getAssociatedId()));
            }
        } else if (role == UserRole.STUDENT && userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent() && userOpt.get().getAssociatedId() != null) {
                return ResponseEntity.ok(applicationRepository.findByStudentId(userOpt.get().getAssociatedId()));
            }
        }

        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long id, @RequestParam ApplicationStatus status, @RequestParam(required = false) String notes) {
        Optional<Application> appOpt = applicationRepository.findById(id);
        if (!appOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Application application = appOpt.get();
        UserRole role = UserContext.getUserRole();
        Long userId = UserContext.getUserId();

        if (role != UserRole.COMPANY || userId == null) {
            return ResponseEntity.status(403).body("Only companies can update application statuses");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent() || !userOpt.get().getAssociatedId().equals(application.getJobPosting().getCompany().getId())) {
            return ResponseEntity.status(403).body("Unauthorized to manage applications for this job");
        }

        application.setStatus(status);
        if (notes != null) {
            application.setNotes(notes);
        }
        application = applicationRepository.save(application);

        // Notify Student
        final Long targetStudentId = application.getStudent().getId();
        Optional<User> studentUserOpt = userRepository.findAll().stream()
                .filter(u -> u.getRole() == UserRole.STUDENT && targetStudentId.equals(u.getAssociatedId()))
                .findFirst();
        if (studentUserOpt.isPresent()) {
            notificationRepository.save(new Notification(studentUserOpt.get().getId(),
                    "Your application status for '" + application.getJobPosting().getTitle() + "' has been updated to: " + status.name() + "."));
        }

        return ResponseEntity.ok(application);
    }
}
