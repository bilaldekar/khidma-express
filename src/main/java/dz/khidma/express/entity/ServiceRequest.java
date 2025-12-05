package dz.khidma.express.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@Entity
@Table(name = "service_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {

    @Id
    @SequenceGenerator(name = "ServiceRequestIdSeq", sequenceName = "ServiceRequest_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "ServiceRequestIdSeq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties({"serviceRequests"})
    private Client client;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    @JsonIgnoreProperties({"assignedRequests"})
    private Worker worker;

    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptDate ;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completeDate ;

    @Temporal(TemporalType.TIMESTAMP)
    private Date validationDate ;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer rating;

    private String reviewComment;


    public enum Status {
        PENDING, ACCEPTED, IN_PROGRESS, COMPLETED, CLOSED, CANCELLED
    }
}

