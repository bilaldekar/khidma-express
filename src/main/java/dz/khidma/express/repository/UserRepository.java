package dz.khidma.express.repository;


import dz.khidma.express.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User getUserByUsernameIgnoreCase(String username);

    @Modifying
    @Query("UPDATE User SET attemptDate  = :attemptDate WHERE username =:username")
    void resetAttemptLoginDate(@Param("username") String username, @Param("attemptDate") Date attemptDate);

    @Modifying
    @Query("UPDATE User SET attempt = 0 WHERE username =:username")
    void resetAttemptLogin(@Param("username") String username);

    @Modifying
    @Query("UPDATE User SET attempt = attempt + 1 WHERE username =:username")
    void incrementAttemptLogin(@Param("username") String username);


    @Modifying
    @Query("UPDATE User u SET u.lastLogin = CURRENT_TIMESTAMP WHERE u.username = :username")
    void updateLastLogin(@Param("username") String username);

    @Modifying
    @Query("UPDATE User SET accountNonLocked = 0, attemptDate  = :attemptDate WHERE username =:username")
    void lockUser(@Param("username") String username, @Param("attemptDate") Date attemptDate);

    @Modifying
    @Query("UPDATE User SET accountNonLocked = 1 WHERE username =:username")
    void unlockUser(@Param("username") String username);

    @Modifying
    @Query("UPDATE User SET accountNonLocked = 1, attemptDate  = :attemptDate, attempt = 0 WHERE username =:username")
    void unlockUserAndResetAttempt(@Param("username") String username, @Param("attemptDate") Date attemptDate);


    @Query("UPDATE User u SET u.attempt = :failAttempts WHERE u.username = :username")
    @Modifying
    void updateFailedAttempts(@Param("username") String username, @Param("failAttempts") int failAttempts);
}