package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Service;
import com.graduationproject.serviceproviderplatform.model.Supply;
import com.graduationproject.serviceproviderplatform.repository.SupplyRepository;

@org.springframework.stereotype.Service
public class SupplyService {
    private SupplyRepository supplyRepository;

    public SupplyService(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    public void delete (Supply supply) {
        for (Service service : supply.getServices()) {
            service.removeSupply(supply);
        }

        supplyRepository.delete(supply);
    }
}
