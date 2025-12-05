package dz.khidma.express.service;

import dz.khidma.express.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    Category create(Category category);
    Optional<Category> update(Long id, Category category);
    boolean delete(Long id);
}
