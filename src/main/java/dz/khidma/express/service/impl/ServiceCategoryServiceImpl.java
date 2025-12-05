package dz.khidma.express.service.impl;

import dz.khidma.express.entity.Category;
import dz.khidma.express.repository.ServiceCategoryRepository;
import dz.khidma.express.service.ServiceCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryServiceImpl(ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Category create(Category category) {
        if (category.getId() != null && repository.existsById(category.getId())) {
            throw new IllegalStateException("service category already exists");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("category name is required");
        }
        if (repository.existsByName(category.getName())) {
            throw new IllegalStateException("service category name already exists");
        }
        return repository.save(category);
    }

    @Override
    public Optional<Category> update(Long id, Category category) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("category name is required");
        }
        // uniqueness check for name if changed
        Optional<Category> byName = repository.findByName(category.getName());
        if (byName.isPresent() && !byName.get().getId().equals(id)) {
            throw new IllegalStateException("service category name already exists");
        }
        category.setId(id);
        Category saved = repository.save(category);
        return Optional.of(saved);
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
