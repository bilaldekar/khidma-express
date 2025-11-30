package dz.khidma.express.model.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RoleDTO {

    private String roleId;

    private String roleDescription;

    @JsonIgnore
    private Set<UserDTO> users;
}
