package com.delivery.boxes.dto;

import jakarta.validation.constraints.*;

public class BoxCreateRequest {

    @NotBlank(message = "TxRef is required")
    @Size(max = 20, message = "TxRef must not exceed 20 characters")
    private String txref;

    @NotNull(message = "Weight limit is required")
    @Min(value = 1, message = "Weight limit must be at least 1gr")
    @Max(value = 500, message = "Weight limit cannot exceed 500gr")
    private Integer weightLimit;

    @NotNull(message = "Battery capacity is required")
    @Min(value = 0, message = "Battery capacity cannot be negative")
    @Max(value = 100, message = "Battery capacity cannot exceed 100%")
    private Integer batteryCapacity;

    // Constructors
    public BoxCreateRequest() {}

    public BoxCreateRequest(String txref, Integer weightLimit, Integer batteryCapacity) {
        this.txref = txref;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
    }

    // Getters and Setters
    public String getTxref() { return txref; }
    public void setTxref(String txref) { this.txref = txref; }

    public Integer getWeightLimit() { return weightLimit; }
    public void setWeightLimit(Integer weightLimit) { this.weightLimit = weightLimit; }

    public Integer getBatteryCapacity() { return batteryCapacity; }
    public void setBatteryCapacity(Integer batteryCapacity) { this.batteryCapacity = batteryCapacity; }
}