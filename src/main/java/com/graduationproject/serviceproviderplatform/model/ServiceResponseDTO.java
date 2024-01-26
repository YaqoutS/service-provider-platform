package com.graduationproject.serviceproviderplatform.model;

public class ServiceResponseDTO {
    private Service service;
    private String message;

    public ServiceResponseDTO(Service service, String message) {
        this.service = service;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
