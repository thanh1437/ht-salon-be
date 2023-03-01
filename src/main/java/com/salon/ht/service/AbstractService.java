package com.salon.ht.service;


import com.salon.ht.entity.AbstractModel;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T extends AbstractModel<Long>, Long extends Serializable> {
    public static final int PAGE_SIZE = 20;

    protected abstract JpaRepository<T, Long> getRepository();

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public List<T> saveAll(List<T> entities) { return getRepository().saveAll(entities); }

    public T get(Long id) {
        Optional<T> object = getRepository().findById(id);
        return object.orElse(null);
    }

    public void delete(Long id) {
        try {
            getRepository().deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        getRepository().save(entity);
    }
}
