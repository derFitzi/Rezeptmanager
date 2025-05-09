package com.Rezeptmanager.Rezeptmanager.Controller;

import com.Rezeptmanager.Rezeptmanager.Model.ExampleEntity;
import com.Rezeptmanager.Rezeptmanager.Service.ExampleEntityService;
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
