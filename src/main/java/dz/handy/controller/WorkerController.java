package dz.handy.controller;

import dz.handy.entity.Worker;
import dz.handy.service.ServiceCategoryService;
import dz.handy.service.WorkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    private final WorkerService workerService;
    private final ServiceCategoryService serviceCategoryService;

    public WorkerController(WorkerService workerService,  ServiceCategoryService serviceCategoryService) {
        this.workerService = workerService;
        this.serviceCategoryService = serviceCategoryService;
    }

//    @GetMapping
//    public List<Worker> getAll() {
//        return workerService.findAll();
//    }

    @GetMapping("/{username}")
    public ResponseEntity<Worker> getById(@PathVariable String username) {
        return workerService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("radiusKm") double radiusKm) {
        List<Worker> nearby = workerService.findNearbyWorkers(serviceCategoryService.findById(categoryId).get(), lat, lon, radiusKm);
        return ResponseEntity.ok(nearby);
    }
}
