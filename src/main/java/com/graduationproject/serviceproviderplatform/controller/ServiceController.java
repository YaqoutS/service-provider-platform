package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.Employee;
import com.graduationproject.serviceproviderplatform.model.Service;
import com.graduationproject.serviceproviderplatform.model.Supply;
import com.graduationproject.serviceproviderplatform.repository.ServiceRepository;
import com.graduationproject.serviceproviderplatform.repository.SupplyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/services")
@CrossOrigin
public class ServiceController {
    private ServiceRepository serviceRepository;
    private SupplyRepository supplyRepository;

    public ServiceController(ServiceRepository serviceRepository, SupplyRepository supplyRepository) {
        this.serviceRepository = serviceRepository;
        this.supplyRepository = supplyRepository;
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
        if(!serviceRepository.existsById(supplyId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no supply with id = " + supplyId);
        }

        Service service = serviceRepository.findById(serviceId).get();
        Supply supply = supplyRepository.findById(supplyId).get();
        service.addSupply(supply);
        supply.addService(service);

        return ResponseEntity.status(HttpStatus.OK).body("Supply added successfully");
    }
}
