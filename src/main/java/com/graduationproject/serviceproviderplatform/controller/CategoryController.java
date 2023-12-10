package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.Category;
import com.graduationproject.serviceproviderplatform.model.Service;
import com.graduationproject.serviceproviderplatform.repository.CategoryRepository;
import com.graduationproject.serviceproviderplatform.repository.ServiceRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private CategoryRepository categoryRepository;
    private ServiceRepository serviceRepository;

    public CategoryController(CategoryRepository categoryRepository, ServiceRepository serviceRepository) {
        this.categoryRepository = categoryRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(category.get());
    }

    //@Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(id != category.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        if(categoryRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + category.getId());
        }
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
    }

    //@Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category, BindingResult bindingResult) {
        System.out.println(category);
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        System.out.println("Category: " + category);
        if(categoryRepository.findByName(category.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
        }
        Category newCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully with id = " + newCategory.getId());
    }

    //@Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        if(categoryRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + id);
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully");
    }

    @PutMapping("/{id}/add-service")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody Service service, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }

        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + id);
        }
        if(serviceRepository.findByName(service.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Service already exists");
        }

        Category updatedCategory = category.get();
        Service newService = serviceRepository.save(service);
        service.addCategory(updatedCategory);
        updatedCategory.addService(newService);
        categoryRepository.save(updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body("Service added successfully");
    }
}
