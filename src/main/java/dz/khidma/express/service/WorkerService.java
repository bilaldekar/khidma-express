package dz.khidma.express.service;

import dz.khidma.express.entity.Category;
import dz.khidma.express.entity.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerService {


    Optional<Worker> findByUsername(String username);

    Worker create(Worker worker);


    List<Worker> findNearbyWorkers(Category category, double clientLat, double clientLon, double radiusKm);

    List<Worker> findTopWorkersByRating(int limit);

}
