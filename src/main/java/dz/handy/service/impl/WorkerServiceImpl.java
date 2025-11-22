package dz.handy.service.impl;

import dz.handy.entity.ServiceCategory;
import dz.handy.entity.Worker;
import dz.handy.repository.WorkerRepository;
import dz.handy.service.WorkerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;

    public WorkerServiceImpl(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }


    @Override
    public Optional<Worker> findByUsername(String username) {
        return workerRepository.findById(username);
    }

    @Override
    public Worker create(Worker worker) {
        if (worker.getUsername() == null || worker.getUsername().isBlank()) {
            throw new IllegalArgumentException("username must be provided");
        }
        if (workerRepository.existsById(worker.getUsername())) {
            throw new IllegalStateException("worker already exists");
        }
        return workerRepository.save(worker);
    }

    @Override
    public List<Worker> findNearbyWorkers(ServiceCategory category, double clientLat, double clientLon, double radiusKm) {
        List<Worker> allWorkers = (category == null)
                ? workerRepository.findAll()
                : workerRepository.findByCategory(category);
        return allWorkers.stream()
                .filter(worker -> worker.getLatitude() != null && worker.getLongitude() != null)
                .filter(worker -> distanceKm(clientLat, clientLon, worker.getLatitude(), worker.getLongitude()) <= radiusKm)
                .collect(Collectors.toList());
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

//    @Override
//    public Optional<Worker> update(String username, Worker worker) {
//        if (!workerRepository.existsById(username)) {
//            return Optional.empty();
//        }
//        worker.setUsername(username);
//        Worker saved = workerRepository.save(worker);
//        return Optional.of(saved);
//    }
//
//    @Override
//    public boolean delete(String username) {
//        if (!workerRepository.existsById(username)) {
//            return false;
//        }
//        workerRepository.deleteById(username);
//        return true;
//    }
}
