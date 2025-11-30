package dz.khidma.express.service;

import dz.khidma.express.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findByUsername(String username);

    /**
     * Creates a new user.
     * @param user the user to create
     * @return saved user
     * @throws IllegalArgumentException if username is null/blank
     * @throws IllegalStateException if a user with the same username already exists
     */
    User create(User user);

    /**
     * Updates an existing user. If the user does not exist, returns empty.
     * @param username path username to enforce
     * @param user payload user (its username will be overridden by {@code username})
     * @return saved user or empty if not found
     */
    Optional<User> update(String username, User user);

    /**
     * Partially updates an existing user. Only provided fields will be updated.
     * @param username username of the user to update
     * @param updates map of field names to new values
     * @return saved user or empty if not found
     */
    Optional<User> patch(String username, Map<String, Object> updates);

    /**
     * Deletes a user by username.
     * @param username username
     * @return true if deleted, false if not found
     */
    boolean delete(String username);
}
