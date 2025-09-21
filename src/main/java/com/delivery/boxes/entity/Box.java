package com.delivery.boxes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boxes")
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @Size(max = 20, message = "TxRef must not exceed 20 characters")
    @NotBlank(message = "TxRef is required")
    private String txref;

    @Column(nullable = false)
    @Max(value = 500, message = "Weight limit cannot exceed 500gr")
    @Min(value = 1, message = "Weight limit must be at least 1gr")
    private Integer weightLimit;

    @Column(nullable = false)
    @Min(value = 0, message = "Battery capacity cannot be negative")
    @Max(value = 100, message = "Battery capacity cannot exceed 100%")
    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoxState state = BoxState.IDLE;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    // Constructors
    public Box() {}

    public Box(String txref, Integer weightLimit, Integer batteryCapacity) {
        this.txref = txref;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = BoxState.IDLE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTxref() { return txref; }
    public void setTxref(String txref) { this.txref = txref; }

    public Integer getWeightLimit() { return weightLimit; }
    public void setWeightLimit(Integer weightLimit) { this.weightLimit = weightLimit; }

    public Integer getBatteryCapacity() { return batteryCapacity; }
    public void setBatteryCapacity(Integer batteryCapacity) { this.batteryCapacity = batteryCapacity; }

    public BoxState getState() { return state; }
    public void setState(BoxState state) { this.state = state; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    // Helper methods
    public int getCurrentWeight() {
        return items.stream().mapToInt(Item::getWeight).sum();
    }

    public boolean canAcceptWeight(int additionalWeight) {
        return getCurrentWeight() + additionalWeight <= weightLimit;
    }

    public boolean canStartLoading() {
        return batteryCapacity >= 25 && (state == BoxState.IDLE || state == BoxState.LOADED);
    }
}