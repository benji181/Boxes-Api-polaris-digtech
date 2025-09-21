//package com.delivery.boxes.service;
//
//import com.delivery.boxes.dto.BoxCreateRequest;
//import com.delivery.boxes.dto.BoxResponse;
//import com.delivery.boxes.dto.ItemLoadRequest;
//import com.delivery.boxes.entity.Box;
//import com.delivery.boxes.entity.BoxState;
//import com.delivery.boxes.exception.BoxNotFoundException;
//import com.delivery.boxes.exception.DuplicateBoxException;
//import com.delivery.boxes.exception.InvalidOperationException;
//import com.delivery.boxes.repository.BoxRepository;
//import com.delivery.boxes.repository.ItemRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BoxServiceTest {
//
//    @Mock
//    private BoxRepository boxRepository;
//
//    @Mock
//    private ItemRepository itemRepository;
//
//    @InjectMocks
//    private BoxService boxService;
//
//    private Box testBox;
//    private BoxCreateRequest createRequest;
//    private ItemLoadRequest itemRequest;
//
//    @BeforeEach
//    void setUp() {
//        testBox = new Box("TEST001", 500, 85);
//        testBox.setId(1L);
//
//        createRequest = new BoxCreateRequest("TEST001", 500, 85);
//        itemRequest = new ItemLoadRequest("TEST_ITEM", 100, "ITEM001");
//    }
//
//    @Test
//    void createBox_Success() {
//        when(boxRepository.existsByTxref(anyString())).thenReturn(false);
//        when(boxRepository.save(any(Box.class))).thenReturn(testBox);
//
//        BoxResponse response = boxService.createBox(createRequest);
//
//        assertNotNull(response);
//        assertEquals("TEST001", response.getTxref());
//        assertEquals(500, response.getWeightLimit());
//        assertEquals(85, response.getBatteryCapacity());
//        assertEquals(BoxState.IDLE, response.getState());
//
//        verify(boxRepository).existsByTxref("TEST001");
//        verify(boxRepository).save(any(Box.class));
//    }
//
//    @Test
//    void createBox_DuplicateTxref_ThrowsException() {
//        when(boxRepository.existsByTxref(anyString())).thenReturn(true);
//
//        assertThrows(DuplicateBoxException.class, () -> {
//            boxService.createBox(createRequest);
//        });
//
//        verify(boxRepository).existsByTxref("TEST001");
//        verify(boxRepository, never()).save(any(Box.class));
//    }
//
//    @Test
//    void loadBoxWithItem_Success() {
//        when(boxRepository.findByTxref(anyString())).thenReturn(Optional.of(testBox));
//        when(boxRepository.save(any(Box.class))).thenReturn(testBox);
//
//        BoxResponse response = boxService.loadBoxWithItem("TEST001", itemRequest);
//
//        assertNotNull(response);
//        assertEquals(BoxState.LOADED, response.getState());
//
//        verify(boxRepository).findByTxref("TEST001");
//        verify(itemRepository).save(any());
//        verify(boxRepository).save(testBox);
//    }
//
//    @Test
//    void loadBoxWithItem_BoxNotFound_ThrowsException() {
//        when(boxRepository.findByTxref(anyString())).thenReturn(Optional.empty());
//
//        assertThrows(BoxNotFoundException.class, () -> {
//            boxService.loadBoxWithItem("NONEXISTENT", itemRequest);
//        });
//    }
//
//    @Test
//    void loadBoxWithItem_LowBattery_ThrowsException() {
//        testBox.setBatteryCapacity(20); // Below 25%
//        when(boxRepository.findByTxref(anyString())).thenReturn(Optional.of(testBox));
//
//        assertThrows(InvalidOperationException.class, () -> {
//            boxService.loadBoxWithItem("TEST001", itemRequest);
//        });
//    }
//
//    @Test
//    void loadBoxWithItem_ExceedsWeightLimit_ThrowsException() {
//        ItemLoadRequest heavyItem = new ItemLoadRequest("HEAVY_ITEM", 600, "HEAVY001");
//        when(boxRepository.findByTxref(anyString())).thenReturn(Optional.of(testBox));
//
//        assertThrows(InvalidOperationException.class, () -> {
//            boxService.loadBoxWithItem("TEST001", heavyItem);
//        });
//    }
//
//    @Test
//    void getBatteryLevel_Success() {
//        when(boxRepository.findByTxref(anyString())).thenReturn(Optional.of(testBox));
//
//        Integer batteryLevel = boxService.getBatteryLevel("TEST001");
//
//        assertEquals(85, batteryLevel);
//        verify(boxRepository).findByTxref("TEST001");
//    }
//
//    @Test
//    void getBatteryLevel_BoxNotFound_ThrowsException() {
//        when(boxRepository.findByTxref(anyString())).thenReturn(Optional.empty());
//
//        assertThrows(BoxNotFoundException.class, () -> {
//            boxService.getBatteryLevel("NONEXISTENT");
//        });
//    }
//}