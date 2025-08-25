package com.ican.cortex.platform.api.primary.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudService<T, ID> {

    @Autowired
    private JpaRepository<T, ID> repository;

    public T create(T entity) {
        return repository.save(entity);
    }

    public Optional<T> read(ID id) {
        return repository.findById(id);
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }
}
