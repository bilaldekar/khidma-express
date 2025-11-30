package dz.khidma.express.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column(name = "ROLE_ID", nullable = false, length = 64)
    private String roleId;


    @Column(name = "ROLE_DESCRIPTION")
    private String roleDescription;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
