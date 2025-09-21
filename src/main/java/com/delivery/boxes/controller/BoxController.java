package com.delivery.boxes.controller;

import com.delivery.boxes.dto.*;
import com.delivery.boxes.service.BoxService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
public class BoxController {

    @Autowired
    private BoxService boxService;

    @PostMapping
    public ResponseEntity<BoxResponse> createBox(@Valid @RequestBody BoxCreateRequest request) {
        BoxResponse response = boxService.createBox(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{txref}/load")
    public ResponseEntity<BoxResponse> loadBox(@PathVariable String txref,
                                               @Valid @RequestBody ItemLoadRequest itemRequest) {
        BoxResponse response = boxService.loadBoxWithItem(txref, itemRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{txref}/items")
    public ResponseEntity<List<ItemResponse>> getLoadedItems(@PathVariable String txref) {
        List<ItemResponse> items = boxService.getLoadedItems(txref);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BoxResponse>> getAvailableBoxes() {
        List<BoxResponse> boxes = boxService.getAvailableBoxesForLoading();
        return ResponseEntity.ok(boxes);
    }

    @GetMapping("/{txref}/battery")
    public ResponseEntity<Integer> getBatteryLevel(@PathVariable String txref) {
        Integer batteryLevel = boxService.getBatteryLevel(txref);
        return ResponseEntity.ok(batteryLevel);
    }

    @GetMapping("/{txref}")
    public ResponseEntity<BoxResponse> getBox(@PathVariable String txref) {
        BoxResponse response = boxService.getBoxByTxref(txref);
        return ResponseEntity.ok(response);
    }
}