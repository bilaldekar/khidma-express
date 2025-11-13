package dz.handy.repository;

import dz.handy.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByClientUsername(String username);
    List<ServiceRequest> findByWorkerUsername(String username);

    List<ServiceRequest> findByClientUsernameAndStatus(String username, ServiceRequest.Status status);
    List<ServiceRequest> findByWorkerUsernameAndStatus(String username, ServiceRequest.Status status);

    List<ServiceRequest> findByCategoryId(Long categoryId);
    List<ServiceRequest> findByCategoryIdAndStatus(Long categoryId, ServiceRequest.Status status);
}
