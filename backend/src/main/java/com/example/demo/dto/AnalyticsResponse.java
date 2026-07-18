package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsResponse {
    private Long totalStudents;
    private Long totalCompanies;
    private Long totalJobs;
    private Long totalApplications;
    private Double placementRate;
    private List<CompanyAppCount> applicationsPerCompany;
    private Map<String, Long> statusDistribution;

    public AnalyticsResponse() {}

    public AnalyticsResponse(Long totalStudents, Long totalCompanies, Long totalJobs, Long totalApplications, Double placementRate, List<CompanyAppCount> applicationsPerCompany, Map<String, Long> statusDistribution) {
        this.totalStudents = totalStudents;
        this.totalCompanies = totalCompanies;
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
        this.placementRate = placementRate;
        this.applicationsPerCompany = applicationsPerCompany;
        this.statusDistribution = statusDistribution;
    }

    public Long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Long getTotalCompanies() {
        return totalCompanies;
    }

    public void setTotalCompanies(Long totalCompanies) {
        this.totalCompanies = totalCompanies;
    }

    public Long getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(Long totalJobs) {
        this.totalJobs = totalJobs;
    }

    public Long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(Long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public Double getPlacementRate() {
        return placementRate;
    }

    public void setPlacementRate(Double placementRate) {
        this.placementRate = placementRate;
    }

    public List<CompanyAppCount> getApplicationsPerCompany() {
        return applicationsPerCompany;
    }

    public void setApplicationsPerCompany(List<CompanyAppCount> applicationsPerCompany) {
        this.applicationsPerCompany = applicationsPerCompany;
    }

    public Map<String, Long> getStatusDistribution() {
        return statusDistribution;
    }

    public void setStatusDistribution(Map<String, Long> statusDistribution) {
        this.statusDistribution = statusDistribution;
    }
}
