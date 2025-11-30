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
//import java.util.List;
//
//@Entity
//@Table(name = "WILAYA")
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//public class Wilaya {
//    @Id
//    private Long id;
//
//    private String wilaya;
//
//    @JsonIgnore
//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "wilaya")
//    private List<Commune> communeList;
//}
