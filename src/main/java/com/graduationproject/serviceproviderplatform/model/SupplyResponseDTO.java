package com.graduationproject.serviceproviderplatform.model;

public class SupplyResponseDTO {
    private Supply supply;
    private String message;

    public SupplyResponseDTO(Supply supply, String message) {
        this.supply = supply;
        this.message = message;
    }

    public Supply getSupply() {
        return supply;
    }

    public void setSupply(Supply supply) {
        this.supply = supply;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}