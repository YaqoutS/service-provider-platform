package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
