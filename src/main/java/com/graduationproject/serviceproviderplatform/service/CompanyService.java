package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Category;
import com.graduationproject.serviceproviderplatform.model.Company;
import com.graduationproject.serviceproviderplatform.model.Employee;
import com.graduationproject.serviceproviderplatform.repository.AdminRepository;
import com.graduationproject.serviceproviderplatform.repository.CompanyRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class CompanyService {
    private CompanyRepository companyRepository;
    private AdminRepository adminRepository;
    private CategoryService categoryService;
    private EmployeeService employeeService;
    private RequestService requestService;

    public CompanyService(CompanyRepository companyRepository, AdminRepository adminRepository, CategoryService categoryService, EmployeeService employeeService, RequestService requestService) {
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
        this.categoryService = categoryService;
        this.employeeService = employeeService;
        this.requestService = requestService;
    }

    @Transactional
    public void delete(Company company) {
        for (Category category : company.getCategories()) {
            categoryService.delete(category);
        }
        for (Employee employee : company.getEmployees()) {
            employeeService.delete(employee);
        }
        adminRepository.deleteByFullName(company.getName()); // The admin for a company has name similar to its name
        companyRepository.delete(company);
    }
}
