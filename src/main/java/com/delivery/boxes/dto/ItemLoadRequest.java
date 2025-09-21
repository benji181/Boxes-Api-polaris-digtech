package com.delivery.boxes.dto;

import jakarta.validation.constraints.*;

public class ItemLoadRequest {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Name can only contain letters, numbers, hyphen and underscore")
    private String name;

    @NotNull(message = "Weight is required")
    @Min(value = 1, message = "Weight must be at least 1gr")
    private Integer weight;

    @NotBlank(message = "Code is required")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code can only contain uppercase letters, numbers and underscore")
    private String code;

    // Constructors
    public ItemLoadRequest() {}

    public ItemLoadRequest(String name, Integer weight, String code) {
        this.name = name;
        this.weight = weight;
        this.code = code;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}