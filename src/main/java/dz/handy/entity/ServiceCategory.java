package dz.handy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "service_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCategory {

    @Id
    @SequenceGenerator(name = "ServiceCategoryIdSeq", sequenceName = "ServiceCategory_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "ServiceCategoryIdSeq")
    private Long id;

    private String name;

    private String nameAr;

    private String nameFr;

    private String description;
    private String descriptionAr;
    private String descriptionFr;

//    @OneToMany(mappedBy = "category")
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private List<Worker> workers;
}
