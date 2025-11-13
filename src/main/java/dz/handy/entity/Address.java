package dz.handy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @SequenceGenerator(name = "addressIdSeq", sequenceName = "ADDRESS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "addressIdSeq")
    private Long id;

    private String city;
    private String street;
    private String houseNumber;
    private String latitude;
    private String longitude;
}

