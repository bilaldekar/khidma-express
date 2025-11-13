package dz.handy.service.impl;

import dz.handy.entity.User;
import dz.handy.repository.UserRepository;
import dz.handy.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findById(username);
    }

    @Override
    public User create(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("username must be provided");
        }
        if (userRepository.existsById(user.getUsername())) {
            throw new IllegalStateException("user already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> update(String username, User user) {
        if (!userRepository.existsById(username)) {
            return Optional.empty();
        }
        user.setUsername(username);
        User saved = userRepository.save(user);
        return Optional.of(saved);
    }

    @Override
    public boolean delete(String username) {
        if (!userRepository.existsById(username)) {
            return false;
        }
        userRepository.deleteById(username);
        return true;
    }
}
