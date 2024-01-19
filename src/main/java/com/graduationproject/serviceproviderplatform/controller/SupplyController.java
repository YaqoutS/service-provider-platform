package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.Supply;
import com.graduationproject.serviceproviderplatform.model.User;
import com.graduationproject.serviceproviderplatform.repository.SupplyRepository;
import com.graduationproject.serviceproviderplatform.repository.UserRepository;
import com.graduationproject.serviceproviderplatform.service.SupplyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/supplies")
@CrossOrigin
public class SupplyController {
    private SupplyRepository supplyRepository;
    private SupplyService supplyService;
    private UserRepository userRepository;

    public SupplyController(SupplyRepository supplyRepository, SupplyService supplyService, UserRepository userRepository) {
        this.supplyRepository = supplyRepository;
        this.supplyService = supplyService;
        this.userRepository = userRepository;
    }

    // I think we will never use this API
    @GetMapping
    public ResponseEntity<List<Supply>> getAllSupplies() {
        List<Supply> supplies = supplyRepository.findAll();
        return ResponseEntity.ok(supplies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supply> getSupplyById(@PathVariable Long id) {
        Optional<Supply> supply = supplyRepository.findById(id);
        if (supply.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(supply.get());
    }

    @PostMapping
    public ResponseEntity<String> createSupply(@Valid @RequestBody Supply supply, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        System.out.println("Supply: " + supply);
        if (supply.getUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request - user can't be null");
        }
        if (!userRepository.existsById(supply.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with id = " + supply.getUser().getId());
        }
        User user = userRepository.findById(supply.getUser().getId()).get();
        supply.setUser(user);
        supply = supplyRepository.save(supply);
        return ResponseEntity.status(HttpStatus.CREATED).body("Supply created successfully with id = " + supply.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSupply(@PathVariable Long id, @Valid @RequestBody Supply supply, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(id != supply.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        Optional<Supply> optionalSupply = supplyRepository.findById(id);
        if(optionalSupply.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no supply with id = " + supply.getId());
        }

        Supply updatedSupply = optionalSupply.get();
        updatedSupply.setName(supply.getName());
        updatedSupply.setDescription(supply.getDescription());
        updatedSupply.setPrice(supply.getPrice());
        updatedSupply.setImage(supply.getImage());
        supplyRepository.save(updatedSupply);
        return ResponseEntity.status(HttpStatus.OK).body("Supply updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupply(@PathVariable Long id) {
        if(!supplyRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no supply with id = " + id);
        }
        Supply supply = supplyRepository.findById(id).get();
        supplyService.delete(supply);
        return ResponseEntity.status(HttpStatus.OK).body("Supply deleted successfully");
    }
}
