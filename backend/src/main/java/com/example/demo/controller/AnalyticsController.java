package com.example.demo.controller;

import com.example.demo.dto.AnalyticsResponse;
import com.example.demo.dto.CompanyAppCount;
import com.example.demo.model.Application;
import com.example.demo.model.ApplicationStatus;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.JobPostingRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsResponse> getDashboardAnalytics() {
        long totalStudents = studentRepository.count();
        long totalCompanies = companyRepository.count();
        long totalJobs = jobPostingRepository.count();
        
        List<Application> applications = applicationRepository.findAll();
        long totalApplications = applications.size();

        // Calculate unique placed students (status = SELECTED)
        long placedStudentsCount = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.SELECTED)
                .map(a -> a.getStudent().getId())
                .distinct()
                .count();

        double placementRate = totalStudents > 0 
                ? Math.round(((double) placedStudentsCount / totalStudents) * 100.0 * 100.0) / 100.0 
                : 0.0;

        // Group applications by company name
        Map<String, Long> appGrouped = applications.stream()
                .collect(Collectors.groupingBy(a -> a.getJobPosting().getCompany().getName(), Collectors.counting()));
        
        // Also ensure any company with no applications is visible with count 0
        companyRepository.findAll().forEach(company -> {
            appGrouped.putIfAbsent(company.getName(), 0L);
        });

        List<CompanyAppCount> applicationsPerCompany = appGrouped.entrySet().stream()
                .map(entry -> new CompanyAppCount(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // Group applications by status name
        Map<String, Long> statusGrouped = applications.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus().name(), Collectors.counting()));
        
        Map<String, Long> statusDistribution = new HashMap<>(statusGrouped);
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusDistribution.putIfAbsent(status.name(), 0L);
        }

        AnalyticsResponse response = new AnalyticsResponse(
                totalStudents,
                totalCompanies,
                totalJobs,
                totalApplications,
                placementRate,
                applicationsPerCompany,
                statusDistribution
        );

        return ResponseEntity.ok(response);
    }
}
