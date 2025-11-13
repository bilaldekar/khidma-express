package dz.handy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @SequenceGenerator(name = "ReviewIdSeq", sequenceName = "Review_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "ReviewIdSeq")
    private Long id;

    private int rating;

    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
