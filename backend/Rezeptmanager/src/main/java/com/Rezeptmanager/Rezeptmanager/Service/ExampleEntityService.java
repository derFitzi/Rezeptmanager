package com.example.service;

import com.example.model.ExampleEntity;
import com.example.repository.ExampleEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleEntityService {

    private final ExampleEntityRepository repository;

    public ExampleEntityService(ExampleEntityRepository repository) {
        this.repository = repository;
    }

    public List<ExampleEntity> getAllEntities() {
        return repository.findAll();
    }

    public ExampleEntity saveEntity(ExampleEntity entity) {
        return repository.save(entity);
    }
}