package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.Employee;
import com.graduationproject.serviceproviderplatform.model.Service;
import com.graduationproject.serviceproviderplatform.model.Supply;
import com.graduationproject.serviceproviderplatform.repository.ServiceRepository;
import com.graduationproject.serviceproviderplatform.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.List;
import java.util.Set;
import java.util.Set;

@RestController
@RequestMapping("/services")
@CrossOrigin
public class ServiceController {
    private ServiceRepository serviceRepository;
    private SupplyRepository supplyRepository;
    private final ResourceLoader resourceLoader;
    public ServiceController(ServiceRepository serviceRepository, SupplyRepository supplyRepository, ResourceLoader resourceLoader) {
        this.serviceRepository = serviceRepository;
        this.supplyRepository = supplyRepository;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<Set<Employee>> getServiceEmployees(@PathVariable Long id) {
        if (!serviceRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Service service = serviceRepository.findById(id).get();
        return ResponseEntity.ok(service.getEmployees());
    }

    @PutMapping("/{serviceId}/add-supply/{supplyId}")
    public ResponseEntity<String> addSupplyToService(@PathVariable Long serviceId, @PathVariable Long supplyId) {
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }
        if(!supplyRepository.existsById(supplyId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no supply with id = " + supplyId);
        }

        Service service = serviceRepository.findById(serviceId).get();
        Supply supply = supplyRepository.findById(supplyId).get();
        service.addSupply(supply);
        supply.addService(service);
        serviceRepository.save(service);
        return ResponseEntity.status(HttpStatus.OK).body("Supply added successfully");
    }

    //`http://localhost:8085/services/${request.service.id}/available-employees?${request.appointment.startDate}`
    @GetMapping("/{id}/available-employees")
    public ResponseEntity<Set<Employee>> getServiceAvailableEmployees(@PathVariable Long id,
                                                                      @RequestParam(required = false) LocalDate startDate) {
        if (!serviceRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Service service = serviceRepository.findById(id).get();
        return ResponseEntity.ok(service.getEmployees());
    }

    @PostMapping("/{serviceId}/uploadImage")
    public ResponseEntity<String> handleFileUpload(@RequestParam("image") MultipartFile image, @PathVariable String serviceId) {
        System.out.println("image received");
        try {
            URI resourceUri = resourceLoader.getResource("classpath:").getURI();
            String decodedResourcePath = resourceUri.getPath();
            String ServiceImagesRoot = decodedResourcePath.substring(1);
            String relativePath = "assets/servicesImages/" + serviceId + ".jpg";
            System.out.println(ServiceImagesRoot + relativePath);
            Path absolutePath = Paths.get(ServiceImagesRoot + relativePath);
            Files.createDirectories(absolutePath.getParent());
            Files.write(absolutePath, image.getBytes());
            return ResponseEntity.ok("Image uploaded successfully. Path: " + relativePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }
}
