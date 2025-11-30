//package dz.handy.entity;
//
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "COMMUNE")
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//public class Commune {
//
//    @Id
//    private Long id;
//
//    private String commune;
//
//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "wilaya_id", referencedColumnName = "id")
//    private Wilaya wilaya;
//}
