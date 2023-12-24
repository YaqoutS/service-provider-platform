package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Category;
import com.graduationproject.serviceproviderplatform.model.Service;
import com.graduationproject.serviceproviderplatform.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    private ServiceService serviceService;

    public CategoryService(CategoryRepository categoryRepository, ServiceService serviceService) {
        this.categoryRepository = categoryRepository;
        this.serviceService = serviceService;
    }

    @Transactional
    public void delete(Category category) {
        for (Service service: category.getServices()) {
            serviceService.delete(service);
        }
        categoryRepository.delete(category);
    }
}
