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
        return ResponseEntity.ok(categories);
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
        if(categoryRepository.existsByName(category.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use");
        }
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + category.getId());
        }
        Category updatedCategory = optionalCategory.get();
        updatedCategory.setName(category.getName());
        updatedCategory.setImage(category.getImage());
        categoryRepository.save(updatedCategory);
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
        if(!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + id);
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully");
    }

    // ###################################### Services endpoints ###################################### //

    @GetMapping("/{id}/services") // Get all services belong to this category
    public ResponseEntity<List<Service>> getAllServices(@PathVariable Long id) {
        if(!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Service> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{categoryId}/services/{serviceId}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long categoryId,@PathVariable Long serviceId) {
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Optional<Service> service = serviceRepository.findById(serviceId);
        if (service.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.get());
    }

    @PostMapping("/{categoryId}/services")
    public ResponseEntity<String> addNewService(@PathVariable Long categoryId, @Valid @RequestBody Service service, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with categoryId = " + categoryId);
        }
        if(serviceRepository.existsByName(service.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Service already exists");
        }

        Category updatedCategory = category.get();
        Service newService = serviceRepository.save(service);
        service.setCategory(updatedCategory);
        updatedCategory.addService(newService);
        categoryRepository.save(updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body("Service added successfully");
    }

    @PutMapping("/{categoryId}/services/{serviceId}")
    public ResponseEntity<String> updateService(@PathVariable Long categoryId, @PathVariable Long serviceId, @Valid @RequestBody Service service, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(serviceId != service.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        // Is it important to also check the categoryId? It may not be in the RequestBody (service)
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with categoryId = " + categoryId);
        }
        Optional<Service> optionalService = serviceRepository.findById(serviceId);
        if(optionalService.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }
        if(serviceRepository.existsByName(service.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use");
        }

        Service updatedService = optionalService.get();
        updatedService.setName(service.getName());
        updatedService.setDescription(service.getDescription());
        updatedService.setImage(service.getImage());
        updatedService.setAvailable(service.isAvailable());
        updatedService.setAvgPrice(service.getAvgPrice());
        serviceRepository.save(updatedService);
        return ResponseEntity.status(HttpStatus.OK).body("Service updated successfully");
    }

    @DeleteMapping("/{categoryId}/services/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable Long categoryId, @PathVariable Long serviceId) {
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + categoryId);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }
        serviceRepository.deleteById(serviceId);
        return ResponseEntity.status(HttpStatus.OK).body("Service deleted successfully");
    }

}
