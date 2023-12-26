package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.*;
import com.graduationproject.serviceproviderplatform.service.CategoryService;
import com.graduationproject.serviceproviderplatform.service.ServiceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;
    private CompanyRepository companyRepository;
    private ServiceRepository serviceRepository;
    private ServiceService serviceService;
    private ServiceOptionRepository serviceOptionRepository;
    private RequestRepository requestRepository;
    private AppointmentRepository appointmentRepository;

    public CategoryController(CategoryRepository categoryRepository, CategoryService categoryService, ServiceRepository serviceRepository, CompanyRepository companyRepository, ServiceService serviceService, ServiceOptionRepository serviceOptionRepository, RequestRepository requestRepository, AppointmentRepository appointmentRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.companyRepository = companyRepository;
        this.serviceRepository = serviceRepository;
        this.serviceService = serviceService;
        this.serviceOptionRepository = serviceOptionRepository;
        this.requestRepository = requestRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryDTO(category))
                .collect(Collectors.toList());;
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CategoryDTO(category.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO category, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(id != category.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + category.getId());
        }
        Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
        if(existingCategory.isPresent() && existingCategory.get().getId() != id) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use");
        }
        Category updatedCategory = optionalCategory.get();
        updatedCategory.setName(category.getName());
        updatedCategory.setImage(category.getImage());
        updatedCategory.setCompany(companyRepository.findById(category.getCompanyId()).get());
        categoryRepository.save(updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        System.out.println(categoryDTO);
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        System.out.println("Category: " + categoryDTO);
//        if(categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
//        }
        Category category = new Category(categoryDTO);
        if (categoryDTO.getCompanyId() != null && companyRepository.existsById(categoryDTO.getCompanyId())) {
            category.setCompany(companyRepository.findById(categoryDTO.getCompanyId()).get());
        }
        category = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully with id = " + category.getId());
    }

    //@Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        if(!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + id);
        }
        Category category = categoryRepository.findById(id).get();
        categoryService.delete(category);
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
        List<Service> services = serviceRepository.findAllByName(service.getName());
        for (Service s : services) {
            if(s.getCategory().getId() == categoryId)
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Service already exists in this category");
        }
        Category updatedCategory = category.get();
        Service newService = serviceRepository.save(service);
        service.setCategory(updatedCategory);
        updatedCategory.addService(newService);
        categoryRepository.save(updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body("Service added successfully with id = " + newService.getId());
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
        Service service = serviceRepository.findById(serviceId).get();
        serviceService.delete(service);
        return ResponseEntity.status(HttpStatus.OK).body("Service deleted successfully");
    }

    @GetMapping("/{categoryId}/services/{serviceId}/unavailable-dates")
    public ResponseEntity<List<LocalDate>> getUnavailableDates(@PathVariable Long categoryId, @PathVariable Long serviceId) {
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Appointment> appointments = serviceRepository.findById(serviceId).get().getAppointments();

        // Group appointments by date
        Map<LocalDate, Long> appointmentsByDate = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getStartDate, Collectors.counting()));

        // Filter dates where all slots are reserved (assuming 10 slots per day)
        List<LocalDate> unavailableDays = appointmentsByDate.entrySet().stream()
                .filter(entry -> getAvailableTimes(serviceId, entry.getKey()).size() == 0  )
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(unavailableDays);
    }

    @GetMapping("/{categoryId}/services/{serviceId}/available-times/{date}")
    public ResponseEntity<List<LocalTime>> getAvailableTimes(@PathVariable Long categoryId, @PathVariable Long serviceId, @PathVariable LocalDate date) {
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<LocalTime> availableTimes = getAvailableTimes(serviceId, date);

        return ResponseEntity.status(HttpStatus.OK).body(availableTimes);
    }

    private List<LocalTime> getAvailableTimes(Long serviceId, LocalDate date) {
        // I think this will be called in the previous two APIs ...
        List<Appointment> appointments = serviceRepository.findById(serviceId).get().getAppointments()
                .stream().filter(a -> a.getStartDate().equals(date)).toList();

        // Create a set of all possible time slots for the day (assuming 1-hour slots from 8am to 6pm)
        Set<LocalTime> allTimeSlots = new HashSet<>();
        LocalTime currentTime = LocalTime.of(8, 0);
        while (currentTime.isBefore(LocalTime.of(18, 0))) {
            allTimeSlots.add(currentTime);
            currentTime = currentTime.plusHours(1);
        }

        // Remove reserved time slots
        appointments.forEach(appointment -> {
            LocalTime startTime = appointment.getStartTime();
            LocalTime endTime = appointment.getEndTime();

            // Remove all time slots between start and end time
            while (startTime.isBefore(endTime)) {
                allTimeSlots.remove(startTime);
                startTime = startTime.plusHours(1);
            }
        });

        // Convert the set of available time slots to a sorted list
        List<LocalTime> availableTimes = new ArrayList<>(allTimeSlots);
        Collections.sort(availableTimes);
        return availableTimes;
    }
}
