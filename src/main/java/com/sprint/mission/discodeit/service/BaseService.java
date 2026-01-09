package com.sprint.mission.discodeit.service;

import java.util.List;

public interface BaseService<T, ID> {
    void create(T entity);
    T findById(ID id);
    List<T> findAll();
    void update(ID id, T entity);
    void delete(ID id);
}
