package com.example.demo.repository;

import com.example.demo.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);
    List<Application> findByJobPostingCompanyId(Long companyId);
    List<Application> findByJobPostingId(Long jobPostingId);
    boolean existsByJobPostingIdAndStudentId(Long jobPostingId, Long studentId);
    long countByStatus(com.example.demo.model.ApplicationStatus status);
}
