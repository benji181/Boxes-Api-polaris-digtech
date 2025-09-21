package com.delivery.boxes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Name can only contain letters, numbers, hyphen and underscore")
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @Min(value = 1, message = "Weight must be at least 1gr")
    private Integer weight;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code can only contain uppercase letters, numbers and underscore")
    @NotBlank(message = "Code is required")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    private Box box;

    // Constructors
    public Item() {}

    public Item(String name, Integer weight, String code) {
        this.name = name;
        this.weight = weight;
        this.code = code;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Box getBox() { return box; }
    public void setBox(Box box) { this.box = box; }
}