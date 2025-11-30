package dz.khidma.express.service.impl;

import dz.khidma.express.entity.ServiceRequest;
import dz.khidma.express.repository.ServiceRequestRepository;
import dz.khidma.express.service.ServiceRequestService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository repository;

    public ServiceRequestServiceImpl(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ServiceRequest> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ServiceRequest> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<ServiceRequest> findByClientUsername(String username) {
        return repository.findByClientUsername(username);
    }

    @Override
    public List<ServiceRequest> findByClientUsername(String username, ServiceRequest.Status status) {
        return repository.findByClientUsernameAndStatus(username, status);
    }

    @Override
    public List<ServiceRequest> findByWorkerUsername(String username) {
        return repository.findByWorkerUsername(username);
    }

    @Override
    public List<ServiceRequest> findByWorkerUsername(String username, ServiceRequest.Status status) {
        return repository.findByWorkerUsernameAndStatus(username, status);
    }

    @Override
    public List<ServiceRequest> findByCategoryId(Long categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public List<ServiceRequest> findByCategoryId(Long categoryId, ServiceRequest.Status status) {
        return repository.findByCategoryIdAndStatus(categoryId, status);
    }

    @Override
    public ServiceRequest create(ServiceRequest request) {
        if (request.getId() != null && repository.existsById(request.getId())) {
            throw new IllegalStateException("service request already exists");
        }
        request.setStatus(ServiceRequest.Status.PENDING);
        request.setRequestDate(new Date());
        return repository.save(request);
    }

    @Override
    public Optional<ServiceRequest> accept(Long id, ServiceRequest request) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        request.setId(id);
        request.setStatus(ServiceRequest.Status.ACCEPTED);
        request.setAcceptDate(new Date());
        ServiceRequest saved = repository.save(request);
        return Optional.of(saved);
    }

    @Override
    public Optional<ServiceRequest> complete(Long id, ServiceRequest request) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        request.setId(id);
        request.setStatus(ServiceRequest.Status.COMPLETED);
        request.setCompleteDate(new Date());
        ServiceRequest saved = repository.save(request);
        return Optional.of(saved);
    }

    @Override
    public Optional<ServiceRequest> validate(Long id, ServiceRequest request) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        request.setId(id);
        request.setStatus(ServiceRequest.Status.CLOSED);
        request.setValidationDate(new Date());
        ServiceRequest saved = repository.save(request);
        return Optional.of(saved);
    }

    @Override
    public Optional<ServiceRequest> cancel(Long id, ServiceRequest request) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        request.setId(id);
        request.setStatus(ServiceRequest.Status.CANCELLED);
        ServiceRequest saved = repository.save(request);
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
