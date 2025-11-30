package dz.khidma.express.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "workers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker extends User {

    private String specialization;

    private double rating;

    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ServiceCategory category;

    @OneToMany(mappedBy = "worker")
    private List<ServiceRequest> assignedRequests;


    // Getters & Setters
}

