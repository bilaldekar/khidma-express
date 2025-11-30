package dz.khidma.express.repository;

import dz.khidma.express.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("SELECT r.roleId FROM Role r JOIN r.users u WHERE Lower(u.username) = :username")
    List<String> findRoleIdByUser(@Param("username") String username);

}
