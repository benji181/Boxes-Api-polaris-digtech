package com.delivery.boxes.service;

import com.delivery.boxes.dto.*;
import com.delivery.boxes.entity.Box;
import com.delivery.boxes.entity.BoxState;
import com.delivery.boxes.entity.Item;
import com.delivery.boxes.exception.BoxNotFoundException;
import com.delivery.boxes.exception.InvalidOperationException;
import com.delivery.boxes.exception.DuplicateBoxException;
import com.delivery.boxes.repository.BoxRepository;
import com.delivery.boxes.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private ItemRepository itemRepository;

    public BoxResponse createBox(BoxCreateRequest request) {
        if (boxRepository.existsByTxref(request.getTxref())) {
            throw new DuplicateBoxException("Box with txref '" + request.getTxref() + "' already exists");
        }

        Box box = new Box(request.getTxref(), request.getWeightLimit(), request.getBatteryCapacity());
        Box savedBox = boxRepository.save(box);

        return mapToBoxResponse(savedBox);
    }

    public BoxResponse loadBoxWithItem(String txref, ItemLoadRequest itemRequest) {
        Box box = boxRepository.findByTxref(txref)
                .orElseThrow(() -> new BoxNotFoundException("Box with txref '" + txref + "' not found"));

        // Check if box can start loading
        if (!box.canStartLoading()) {
            throw new InvalidOperationException("Box cannot be loaded. Battery level must be at least 25% and box must be in IDLE or LOADED state");
        }

        // Check weight limit
        if (!box.canAcceptWeight(itemRequest.getWeight())) {
            throw new InvalidOperationException("Adding this item would exceed the box weight limit");
        }

        // Set box to LOADING state if it's IDLE
        if (box.getState() == BoxState.IDLE) {
            box.setState(BoxState.LOADING);
        }

        // Create and save item
        Item item = new Item(itemRequest.getName(), itemRequest.getWeight(), itemRequest.getCode());
        item.setBox(box);
        itemRepository.save(item);

        // Update box state to LOADED after loading
        box.setState(BoxState.LOADED);
        Box savedBox = boxRepository.save(box);

        return mapToBoxResponse(savedBox);
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getLoadedItems(String txref) {
        Box box = boxRepository.findByTxref(txref)
                .orElseThrow(() -> new BoxNotFoundException("Box with txref '" + txref + "' not found"));

        return itemRepository.findByBoxTxref(txref).stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BoxResponse> getAvailableBoxesForLoading() {
        return boxRepository.findAvailableBoxesForLoading().stream()
                .map(this::mapToBoxResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer getBatteryLevel(String txref) {
        Box box = boxRepository.findByTxref(txref)
                .orElseThrow(() -> new BoxNotFoundException("Box with txref '" + txref + "' not found"));

        return box.getBatteryCapacity();
    }

    @Transactional(readOnly = true)
    public BoxResponse getBoxByTxref(String txref) {
        Box box = boxRepository.findByTxref(txref)
                .orElseThrow(() -> new BoxNotFoundException("Box with txref '" + txref + "' not found"));

        return mapToBoxResponse(box);
    }

    private BoxResponse mapToBoxResponse(Box box) {
        return new BoxResponse(
                box.getId(),
                box.getTxref(),
                box.getWeightLimit(),
                box.getBatteryCapacity(),
                box.getState(),
                box.getCurrentWeight()
        );
    }

    private ItemResponse mapToItemResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getWeight(),
                item.getCode()
        );
    }
}