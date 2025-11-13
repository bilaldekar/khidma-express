package dz.handy.service;

import dz.handy.entity.ServiceCategory;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryService {
    List<ServiceCategory> findAll();
    Optional<ServiceCategory> findById(Long id);
    Optional<ServiceCategory> findByName(String name);
    ServiceCategory create(ServiceCategory category);
    Optional<ServiceCategory> update(Long id, ServiceCategory category);
    boolean delete(Long id);
}
