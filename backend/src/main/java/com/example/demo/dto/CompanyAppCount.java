package com.example.demo.dto;

public class CompanyAppCount {
    private String companyName;
    private Long count;

    public CompanyAppCount() {}

    public CompanyAppCount(String companyName, Long count) {
        this.companyName = companyName;
        this.count = count;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
