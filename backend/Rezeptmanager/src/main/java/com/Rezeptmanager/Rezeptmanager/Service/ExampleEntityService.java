package com.Rezeptmanager.Rezeptmanager.Service;

import com.Rezeptmanager.Rezeptmanager.Model.ExampleEntity;
import com.Rezeptmanager.Rezeptmanager.Repository.ExampleEntityRepository;
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