package dz.khidma.express.controller;

import dz.khidma.express.entity.Worker;
import dz.khidma.express.repository.WorkerRepository;
import dz.khidma.express.service.ServiceCategoryService;
import dz.khidma.express.service.WorkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
@CrossOrigin(origins = "*")
public class WorkerController {

    private final WorkerService workerService;
    private final WorkerRepository workerRepository;
    private final ServiceCategoryService serviceCategoryService;

    public WorkerController(WorkerService workerService, WorkerRepository workerRepository, ServiceCategoryService serviceCategoryService) {
        this.workerService = workerService;
        this.workerRepository = workerRepository;
        this.serviceCategoryService = serviceCategoryService;
    }

    @GetMapping
    public List<Worker> getAll() {
        return workerRepository.findAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<Worker> getById(@PathVariable String username) {
        return workerService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/top")
    public ResponseEntity<List<Worker>> getTopWorkers(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        List<Worker> topWorkers = workerService.findTopWorkersByRating(limit);
        return ResponseEntity.ok(topWorkers);
    }

//    @PostMapping
//    public ResponseEntity<Worker> create(@RequestBody Worker worker) {
//        try {
//            Worker saved = workerService.create(worker);
//            return ResponseEntity.created(URI.create("/api/workers/" + saved.getUsername())).body(saved);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//    }

//    @PutMapping("/{username}")
//    public ResponseEntity<Worker> update(@PathVariable String username, @RequestBody Worker worker) {
//        return workerService.update(username, worker)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

//    @DeleteMapping("/{username}")
//    public ResponseEntity<Void> delete(@PathVariable String username) {
//        boolean deleted = workerService.delete(username);
//        if (!deleted) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Worker>> findNearby(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("radiusKm") double radiusKm) {
        if (categoryId == null) {
            List<Worker> nearby = workerService.findNearbyWorkers(null, lat, lon, radiusKm);
            return ResponseEntity.ok(nearby);
        } else {
            return serviceCategoryService.findById(categoryId)
                    .map(cat -> ResponseEntity.ok(workerService.findNearbyWorkers(cat, lat, lon, radiusKm)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
    }
}
