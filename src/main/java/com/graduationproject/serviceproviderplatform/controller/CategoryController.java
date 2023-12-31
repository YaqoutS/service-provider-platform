package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.*;
import com.graduationproject.serviceproviderplatform.service.CategoryService;
import com.graduationproject.serviceproviderplatform.service.InputService;
import com.graduationproject.serviceproviderplatform.service.OptionService;
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
    private OptionService optionService;
    private ServiceInputRepository serviceInputRepository;
    private InputService inputService;
    private EmployeeRepository employeeRepository;

    public CategoryController(CategoryRepository categoryRepository,
                              CategoryService categoryService,
                              ServiceRepository serviceRepository,
                              CompanyRepository companyRepository,
                              ServiceService serviceService,
                              ServiceOptionRepository serviceOptionRepository,
                              OptionService optionService,
                              ServiceInputRepository serviceInputRepository,
                              InputService inputService,
                              EmployeeRepository employeeRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.companyRepository = companyRepository;
        this.serviceRepository = serviceRepository;
        this.serviceService = serviceService;
        this.serviceOptionRepository = serviceOptionRepository;
        this.optionService = optionService;
        this.serviceInputRepository = serviceInputRepository;
        this.inputService = inputService;
        this.employeeRepository = employeeRepository;
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

    // ###################################### Service endpoints ###################################### //

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
        System.out.println(service);
        return ResponseEntity.status(HttpStatus.OK).body(service.get());
    }

    @PostMapping("/{categoryId}/services")
    public ResponseEntity<String> addNewService(@PathVariable Long categoryId, @Valid @RequestBody Service service, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with categoryId = " + categoryId);
        }
        List<Service> services = serviceRepository.findAllByName(service.getName());
        for (Service s : services) {
            if(s.getCategory().getId() == categoryId)
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Service already exists in this category");
        }
        Category category = categoryRepository.findById(categoryId).get();
        service.setCategory(category);
        service = serviceRepository.save(service);
        for (ServiceOption option: service.getServiceOptions()) {
            option.setService(service);
            serviceOptionRepository.save(option);
        }
        for (ServiceInput input: service.getServiceInputs()) {
            input.setService(service);
            serviceInputRepository.save(input);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Service added successfully with id = " + service.getId());
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
        List<Service> services = serviceRepository.findAllByName(service.getName());
        for (Service s : services) {
            if(s.getCategory().getId() == categoryId)
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Service already exists in this category");
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

    // We need an API to add and remove employees to/from the service
    // I think it's better to put this API here rather than in the employee controller

    // ###################################### ServiceOption endpoints ###################################### //

    @PostMapping("/{categoryId}/services/{serviceId}/options")
    public ResponseEntity<String> addNewServiceOption(@PathVariable Long categoryId, @PathVariable Long serviceId, @Valid @RequestBody ServiceOption option, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with categoryId = " + categoryId);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }

        Service service = serviceRepository.findById(serviceId).get();
        option.setService(service);
        option = serviceOptionRepository.save(option);

        return ResponseEntity.status(HttpStatus.OK).body("Option added successfully with id = " + option.getId());
    }

    @PutMapping("/{categoryId}/services/{serviceId}/options/{optionId}")
    public ResponseEntity<String> updateServiceOption(@PathVariable Long categoryId, @PathVariable Long serviceId, @PathVariable Long optionId, @Valid @RequestBody ServiceOption option, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(optionId != option.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with categoryId = " + categoryId);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }
        if(!serviceOptionRepository.existsById(optionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no option with id = " + optionId);
        }

        ServiceOption updatedOption = serviceOptionRepository.findById(optionId).get();
        updatedOption.setName(option.getName());
        updatedOption.setDescription(option.getDescription());
        updatedOption.setPrice(option.getPrice());

        serviceOptionRepository.save(updatedOption);
        return ResponseEntity.status(HttpStatus.OK).body("Option updated successfully");
    }

    @DeleteMapping("/{categoryId}/services/{serviceId}/options/{optionId}")
    public ResponseEntity<String> deleteServiceOption(@PathVariable Long categoryId, @PathVariable Long serviceId, @PathVariable Long optionId) {
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + categoryId);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }
        if(!serviceOptionRepository.existsById(optionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no option with id = " + optionId);
        }
        ServiceOption option = serviceOptionRepository.findById(optionId).get();
        optionService.delete(option);
        return ResponseEntity.status(HttpStatus.OK).body("Option deleted successfully");
    }

    // ###################################### ServicesInput endpoints ###################################### //

    @PostMapping("/{categoryId}/services/{serviceId}/inputs")
    public ResponseEntity<String> addNewServiceInput(@PathVariable Long categoryId, @PathVariable Long serviceId, @Valid @RequestBody ServiceInput input, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with categoryId = " + categoryId);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }

        Service service = serviceRepository.findById(serviceId).get();
        input.setService(service);
        input = serviceInputRepository.save(input);

        return ResponseEntity.status(HttpStatus.OK).body("Input added successfully with id = " + input.getId());
    }

    @PutMapping("/{categoryId}/services/{serviceId}/inputs/{inputId}")
    public ResponseEntity<String> updateServiceInput(@PathVariable Long categoryId, @PathVariable Long serviceId, @PathVariable Long inputId, @Valid @RequestBody ServiceInput input, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(inputId != input.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with categoryId = " + categoryId);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }
        if(!serviceInputRepository.existsById(inputId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no input with id = " + inputId);
        }

        ServiceInput updatedInput = serviceInputRepository.findById(inputId).get();
        updatedInput.setName(input.getName());
        updatedInput.setDescription(input.getDescription());
        updatedInput.setRequired(input.isRequired());

        serviceInputRepository.save(updatedInput);
        return ResponseEntity.status(HttpStatus.OK).body("Input updated successfully");
    }

    @DeleteMapping("/{categoryId}/services/{serviceId}/inputs/{inputId}")
    public ResponseEntity<String> deleteServiceInput(@PathVariable Long categoryId, @PathVariable Long serviceId, @PathVariable Long inputId) {
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no category with id = " + categoryId);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }
        if(!serviceInputRepository.existsById(inputId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no input with id = " + inputId);
        }
        ServiceInput input = serviceInputRepository.findById(inputId).get();
        inputService.delete(input);
        return ResponseEntity.status(HttpStatus.OK).body("Service input deleted successfully");
    }

    // ###################################### Service availability endpoints ###################################### //

    @GetMapping("/{categoryId}/services/{serviceId}/unavailable-dates")
    public ResponseEntity<List<LocalDate>> getUnavailableDates(@PathVariable Long categoryId, @PathVariable Long serviceId) {
        if (!categoryRepository.existsById(categoryId) || !serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Service service = serviceRepository.findById(serviceId).get();
        List<Appointment> appointments = service.getAppointments();

        System.out.println("Appointments" + appointments);

        // Group appointments by date
        Map<LocalDate, Long> appointmentsByDate = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getStartDate, Collectors.counting()));

        System.out.println("Appointments by date" + appointmentsByDate);

        // Filter dates where all slots are reserved (assuming 10 slots per day)
        List<LocalDate> unavailableDays = appointmentsByDate.entrySet().stream()
                    .filter(entry -> getAllAvailableTimes(serviceId, entry.getKey()).isEmpty())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(unavailableDays);
    }

    @GetMapping("/{categoryId}/services/{serviceId}/available-times/{date}")
    public ResponseEntity<List<LocalTime>> getAvailableTimes(@PathVariable Long categoryId, @PathVariable Long serviceId, @PathVariable LocalDate date) {
        if (!categoryRepository.existsById(categoryId) || !serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<LocalTime> availableTimes = getAllAvailableTimes(serviceId, date);
        return ResponseEntity.status(HttpStatus.OK).body(availableTimes);
    }

    private List<LocalTime> getEmployeeAvailableTimes(Employee employee, LocalDate date) {

        System.out.println("Inside getEmployeeAvailableTimes. Id = " + employee.getId());

        if (!employee.getWorkDays().contains(date.getDayOfWeek())) {
            return Collections.emptyList(); // Not a working day
        }

        List<Appointment> appointments = employee.getAppointments()
                .stream().filter(a -> a.getStartDate().equals(date)).toList();

        System.out.println("Employee appointments" + appointments);

        // Create a set of all possible time slots for the day (assuming 1-hour slots from 8am to 6pm)
        Set<LocalTime> allTimeSlots = new HashSet<>();
        LocalTime currentTime = employee.getWorkStartTime();
        while (currentTime.isBefore(employee.getWorkEndTime())) {
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
        System.out.println("Available times: ");
        System.out.println("");
        return availableTimes;
    }

    private List<LocalTime> getAllAvailableTimes(Long serviceId, LocalDate date) {
        Service service = serviceRepository.findById(serviceId).get();
        List<LocalTime> availableTimes = new ArrayList<>();

        for (Employee employee : service.getEmployees()) {
            List<LocalTime> employeeTimes = getEmployeeAvailableTimes(employee, date);
            employeeTimes = employeeTimes.stream()
                    .filter(time -> !availableTimes.contains(time))
                    .collect(Collectors.toList());
            availableTimes.addAll(employeeTimes);
        }
        Collections.sort(availableTimes);
        return availableTimes;
    }
}
