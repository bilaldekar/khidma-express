package dz.khidma.express.repository;

import dz.khidma.express.entity.ServiceCategory;
import dz.khidma.express.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, String> {
    List<Worker> findByCategory(ServiceCategory serviceCategory);
}
