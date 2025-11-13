package dz.handy.service;

import dz.handy.entity.ServiceCategory;
import dz.handy.entity.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerService {


    Optional<Worker> findByUsername(String username);

    Worker create(Worker worker);


    List<Worker> findNearbyWorkers(ServiceCategory category, double clientLat, double clientLon, double radiusKm);

}
