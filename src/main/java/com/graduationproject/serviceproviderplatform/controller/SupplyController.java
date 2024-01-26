package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.Image;
import com.graduationproject.serviceproviderplatform.model.Supply;
import com.graduationproject.serviceproviderplatform.model.SupplyResponseDTO;
import com.graduationproject.serviceproviderplatform.model.User;
import com.graduationproject.serviceproviderplatform.repository.SupplyRepository;
import com.graduationproject.serviceproviderplatform.repository.UserRepository;
import com.graduationproject.serviceproviderplatform.service.SupplyService;
import jakarta.validation.Valid;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/supplies")
@CrossOrigin
public class SupplyController {
    private SupplyRepository supplyRepository;
    private SupplyService supplyService;
    private UserRepository userRepository;
    private final ResourceLoader resourceLoader;

    public SupplyController(SupplyRepository supplyRepository, SupplyService supplyService, UserRepository userRepository,ResourceLoader resourceLoader) {
        this.supplyRepository = supplyRepository;
        this.supplyService = supplyService;
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
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
    public ResponseEntity<SupplyResponseDTO> createSupply(@Valid @RequestBody Supply supply, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SupplyResponseDTO(null, "Bad request"));
        }
        System.out.println("Supply: " + supply);
        if (supply.getUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SupplyResponseDTO(null, "Bad request - user can't be null"));
        }
        if (!userRepository.existsById(supply.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SupplyResponseDTO(null, "There is no user with id = " + supply.getUser().getId()));
        }
        User user = userRepository.findById(supply.getUser().getId()).get();
        supply.setUser(user);
        supply = supplyRepository.save(supply);
        Image image=new Image("suppliesImages/"+supply.getId()+".jpg");
        supply.setImage(image);
        supplyRepository.save(supply);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SupplyResponseDTO(supply, "Supply created successfully with id = " + supply.getId()));
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

    @PostMapping("/{supplyId}/uploadImage")
    public ResponseEntity<String> handleFileUpload(@RequestParam("image") MultipartFile image, @PathVariable String supplyId) {
        System.out.println("image received");
        try {
            URI resourceUri = resourceLoader.getResource("classpath:").getURI();
            String decodedResourcePath = resourceUri.getPath();
            String ServiceImagesRoot = decodedResourcePath.substring(1);
            String relativePath = "assets/suppliesImages/" + supplyId + ".jpg";
            System.out.println(ServiceImagesRoot+relativePath);
            Path absolutePath = Paths.get(ServiceImagesRoot+relativePath);
            Files.createDirectories(absolutePath.getParent());
            Files.write(absolutePath, image.getBytes());
            return ResponseEntity.ok("Image uploaded successfully. Path: " + relativePath);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }
}
