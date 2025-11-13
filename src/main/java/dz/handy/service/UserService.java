package dz.handy.service;

import dz.handy.entity.User;

import java.util.List;
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
     * Deletes a user by username.
     * @param username username
     * @return true if deleted, false if not found
     */
    boolean delete(String username);
}
