package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByCustomer_IdAndStatus(Long customerId, String status);
    List<Request> findByEmployee_IdAndStatus(Long employeeId, String status);
    List<Request> findByCustomer_Id(Long customerId);
    List<Request> findByStatus(String status);
    List<Request> findByEmployee_Id(Long  employeeId);
    List<Request> findByService_Id(Long serviceId);

    List<Request> findByEmployee_IdAndService_Id(Long employeeId, Long serviceId);

    List<Request> findByEmployee_IdAndStatusAndService_Id(Long employeeId, String status, Long serviceId);
}
