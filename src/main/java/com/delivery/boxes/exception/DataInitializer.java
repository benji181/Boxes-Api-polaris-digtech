package com.delivery.boxes.config;

import com.delivery.boxes.entity.Box;
import com.delivery.boxes.entity.BoxState;
import com.delivery.boxes.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BoxRepository boxRepository;

    @Override
    public void run(String... args) throws Exception {
        // Preload some sample boxes
        if (boxRepository.count() == 0) {
            Box box1 = new Box("BOX001", 500, 85);
            Box box2 = new Box("BOX002", 300, 45);
            Box box3 = new Box("BOX003", 400, 20); // Low battery - cannot load
            Box box4 = new Box("BOX004", 250, 90);

            box2.setState(BoxState.LOADED);
            box3.setState(BoxState.IDLE);

            boxRepository.save(box1);
            boxRepository.save(box2);
            boxRepository.save(box3);
            boxRepository.save(box4);

            System.out.println("Sample boxes loaded into database");
        }
    }
}