package dz.handy.repository;

import dz.handy.entity.ServiceCategory;
import dz.handy.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, String> {
    List<Worker> findByCategory(ServiceCategory serviceCategory);
}
