package dz.handy.service;

import dz.handy.entity.ServiceRequest;

import java.util.List;
import java.util.Optional;

public interface ServiceRequestService {

    List<ServiceRequest> findAll();

    Optional<ServiceRequest> findById(Long id);

    /**
     * Find all service requests created by a given client username.
     */
    List<ServiceRequest> findByClientUsername(String username);

    /**
     * Find all service requests created by a given client username filtered by status.
     */
    List<ServiceRequest> findByClientUsername(String username, ServiceRequest.Status status);

    /**
     * Find all service requests assigned to a given worker username.
     */
    List<ServiceRequest> findByWorkerUsername(String username);

    /**
     * Find all service requests assigned to a given worker username filtered by status.
     */
    List<ServiceRequest> findByWorkerUsername(String username, ServiceRequest.Status status);

    /**
     * Find all service requests by category id.
     */
    List<ServiceRequest> findByCategoryId(Long categoryId);

    /**
     * Find all service requests by category id filtered by status.
     */
    List<ServiceRequest> findByCategoryId(Long categoryId, ServiceRequest.Status status);

    /**
     * Creates a new service request.
     * @param request the request to create
     * @return saved entity
     * @throws IllegalStateException if an entity with the same id already exists (when id provided)
     */
    ServiceRequest create(ServiceRequest request);

    /**
     * accept an existing service request. Returns empty if not found.
     * @param id path id to enforce
     * @param request payload (its id will be overridden by {@code id})
     * @return saved entity or empty if not found
     */
    Optional<ServiceRequest> accept(Long id, ServiceRequest request);
    Optional<ServiceRequest> complete(Long id, ServiceRequest request);
    Optional<ServiceRequest> validate(Long id, ServiceRequest request);
    Optional<ServiceRequest> cancel(Long id, ServiceRequest request);

    /**
     * Deletes a service request by id.
     * @param id identifier
     * @return true if deleted, false if not found
     */
    boolean delete(Long id);
}
