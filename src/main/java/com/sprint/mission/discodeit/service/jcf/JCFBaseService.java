package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.service.BaseService;

import java.util.*;

public abstract class JCFBaseService<T extends BaseEntity> implements BaseService<T, UUID> {
    protected final Map<UUID, T> data;

    public JCFBaseService() {
        this.data = new HashMap<>();
    }

    public void create(T entity) {
        data.put(entity.getId(), entity);
    }

    public T findById(UUID uuid) {
        return data.get(uuid);
    }

    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public void update(UUID uuid, T entity) {
        if(data.containsKey(uuid)) data.put(uuid, entity);
    }

    public void delete(UUID uuid) {
        data.remove(uuid);
    }
}
