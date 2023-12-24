package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Request;
import com.graduationproject.serviceproviderplatform.repository.RequestRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class RequestService {
    private RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Transactional
    public void delete(Request request) {
        requestRepository.delete(request);
    }
}
