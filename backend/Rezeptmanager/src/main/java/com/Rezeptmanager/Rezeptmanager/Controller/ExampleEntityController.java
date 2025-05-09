package com.example.controller;

import com.example.model.ExampleEntity;
import com.example.service.ExampleEntityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/example-entities")
public class ExampleEntityController {

    private final ExampleEntityService service;

    public ExampleEntityController(ExampleEntityService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExampleEntity> getAllEntities() {
        return service.getAllEntities();
    }

    @PostMapping
    public ExampleEntity saveEntity(@RequestBody ExampleEntity entity) {
        return service.saveEntity(entity);
    }
}