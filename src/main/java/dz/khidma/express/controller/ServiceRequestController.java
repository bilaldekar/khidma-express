package dz.khidma.express.controller;

import dz.khidma.express.entity.ServiceRequest;
import dz.khidma.express.service.ServiceRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class ServiceRequestController {

    private final ServiceRequestService service;

    public ServiceRequestController(ServiceRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceRequest> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-client/{username}")
    public List<ServiceRequest> getByClientUsername(@PathVariable String username,
                                                    @RequestParam(value = "status", required = false) ServiceRequest.Status status) {
        if (status != null) {
            return service.findByClientUsername(username, status);
        }
        return service.findByClientUsername(username);
    }

    @GetMapping("/by-worker/{username}")
    public List<ServiceRequest> getByWorkerUsername(@PathVariable String username,
                                                    @RequestParam(value = "status", required = false) ServiceRequest.Status status) {
        if (status != null) {
            return service.findByWorkerUsername(username, status);
        }
        return service.findByWorkerUsername(username);
    }

    @GetMapping("/by-category/{categoryId}")
    public List<ServiceRequest> getByCategory(@PathVariable Long categoryId,
                                              @RequestParam(value = "status", required = false) ServiceRequest.Status status) {
        if (status != null) {
            return service.findByCategoryId(categoryId, status);
        }
        return service.findByCategoryId(categoryId);
    }

    @PostMapping
    public ResponseEntity<ServiceRequest> create(@RequestBody ServiceRequest request) {
        try {
            ServiceRequest saved = service.create(request);
            return ResponseEntity.created(URI.create("/api/requests/" + saved.getId())).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<ServiceRequest> accept(@PathVariable Long id, @RequestBody ServiceRequest request) {
        return service.accept(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/complete/{id}")
    public ResponseEntity<ServiceRequest> complete(@PathVariable Long id, @RequestBody ServiceRequest request) {
        return service.complete(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/validate/{id}")
    public ResponseEntity<ServiceRequest> validate(@PathVariable Long id, @RequestBody ServiceRequest request) {
        return service.validate(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ServiceRequest> cancel(@PathVariable Long id, @RequestBody ServiceRequest request) {
        return service.cancel(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        boolean deleted = service.delete(id);
//        if (!deleted) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.noContent().build();
//    }
}
