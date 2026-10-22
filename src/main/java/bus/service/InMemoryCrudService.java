package bus.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bus.exception.EntityNotFoundException;
import bus.model.BaseEntity;

public class InMemoryCrudService<T extends BaseEntity> implements CRUDService<T> {
    private final Map<Long, T> storage = new LinkedHashMap<>();
    private final String entityName;

    public InMemoryCrudService(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public T findById(long id) {
        T entity = storage.get(id);
        if (entity == null) {
            throw new EntityNotFoundException(entityName, id);
        }
        return entity;
    }

    @Override
    public void save(T entity) {
        storage.put(entity.getId(), entity);
    }

    @Override
    public boolean delete(long id) {
        return storage.remove(id) != null;
    }

    @Override
    public List<T> findAllSorted(Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>(storage.values());
        result.sort(comparator);
        return result;
    }
}
