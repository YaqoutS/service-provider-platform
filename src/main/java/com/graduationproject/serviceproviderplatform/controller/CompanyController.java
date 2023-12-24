package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.Category;
import com.graduationproject.serviceproviderplatform.model.Company;
import com.graduationproject.serviceproviderplatform.model.Employee;
import com.graduationproject.serviceproviderplatform.model.Request;
import com.graduationproject.serviceproviderplatform.repository.CompanyRepository;
import com.graduationproject.serviceproviderplatform.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/companies")
@CrossOrigin
public class CompanyController {
    private CompanyRepository companyRepository;
    private CompanyService companyService;

    public CompanyController(CompanyRepository companyRepository, CompanyService companyService) {
        this.companyRepository = companyRepository;
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(company.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @Valid @RequestBody Company company, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(id != company.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        if(companyRepository.existsByName(company.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use");
        }
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if(optionalCompany.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no company with id = " + id);
        }
        Company updatedCompany = optionalCompany.get();

        //name, image, field, description, phone, location
        updatedCompany.setName(company.getName());
        updatedCompany.setImage(company.getImage());
        updatedCompany.setField(company.getField());
        updatedCompany.setDescription(company.getDescription());
        updatedCompany.setPhone(company.getPhone());
        updatedCompany.setAddress(company.getAddress());
        companyRepository.save(updatedCompany);
        return ResponseEntity.status(HttpStatus.OK).body("Company updated successfully");
    }

//    @PostMapping
//    public ResponseEntity<String> createCompany(@Valid @RequestBody Company company, BindingResult bindingResult) {
//        System.out.println(company);
//        if(bindingResult.hasErrors()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
//        }
//        System.out.println("Company: " + company);
//        if(companyRepository.existsByName(company.getName())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Company already exists");
//        }
//        Company newCompany = companyRepository.save(company);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Company created successfully with id = " + newCompany.getId());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if(company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no company with id = " + id);
        }
        companyService.delete(company.get());
        return ResponseEntity.status(HttpStatus.OK).body("Company deleted successfully");
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<Set<Employee>> getCompanyEmployees(@PathVariable Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(company.get().getEmployees());
    }

//    @GetMapping("/{id}/requests")
//    public ResponseEntity<List<Request>> getCompanyRequests(@PathVariable Long id) {
//        if(!companyRepository.existsById(id)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        Company company = companyRepository.findById(id).get();
//        return ResponseEntity.status(HttpStatus.OK).body(company.getRequests());
//    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<Set<Category>> getCompanyCategories(@PathVariable Long id) {
        if(!companyRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Company company = companyRepository.findById(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(company.getCategories());
    }

}
