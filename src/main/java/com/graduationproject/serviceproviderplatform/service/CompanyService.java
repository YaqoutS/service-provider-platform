package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.*;
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
    private SupplyService supplyService;

    public CompanyService(CompanyRepository companyRepository, AdminRepository adminRepository, CategoryService categoryService, EmployeeService employeeService, RequestService requestService, SupplyService supplyService) {
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
        this.categoryService = categoryService;
        this.employeeService = employeeService;
        this.requestService = requestService;
        this.supplyService = supplyService;
    }

    @Transactional
    public void delete(Company company) {
        for (Category category : company.getCategories()) {
            categoryService.delete(category);
        }
        for (Employee employee : company.getEmployees()) {
            employeeService.delete(employee);
        }
        Admin admin = adminRepository.findByCompany(company).get();
        for (Supply supply : admin.getSupplies()) {
            supplyService.delete(supply);
        }
        adminRepository.delete(admin);
        companyRepository.delete(company);
    }
}
