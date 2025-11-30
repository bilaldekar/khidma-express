package dz.khidma.express.service.impl;

import dz.khidma.express.entity.User;
import dz.khidma.express.repository.UserRepository;
import dz.khidma.express.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public Optional<User> patch(String username, Map<String, Object> updates) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String str && str.isBlank()) {
                // Allow setting blank string values if explicitly provided
            }
            switch (key) {
                case "email" -> user.setEmail((String) value);
                case "phoneNumber1" -> user.setPhoneNumber1((String) value);
                case "firstName" -> user.setFirstName((String) value);
                case "lastName" -> user.setLastName((String) value);
                case "enabled" -> user.setEnabled(castToInteger(value));
                case "latitude" -> user.setLatitude(castToDouble(value));
                case "longitude" -> user.setLongitude(castToDouble(value));
                case "wilaya" -> user.setWilaya((String) value);
                case "commune" -> user.setCommune((String) value);
                case "accountNonLocked" -> user.setAccountNonLocked(castToInt(value));
                case "creationDate" -> user.setCreationDate(castToDate(value));
                case "expiryDate" -> user.setExpiryDate(castToDate(value));
                case "lastLogin" -> user.setLastLogin(castToDate(value));
                // Intentionally ignore fields that should not be patched via this endpoint
                // such as username, password, roles, attempt/lock fields managed internally
                default -> {
                    // ignore unknown fields
                }
            }
        }

        User saved = userRepository.save(user);
        return Optional.of(saved);
    }

    private Integer castToInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Integer i) return i;
        if (value instanceof Number n) return n.intValue();
        if (value instanceof String s) return Integer.valueOf(s);
        throw new IllegalArgumentException("Cannot cast to Integer: " + value);
    }

    private int castToInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Integer i) return i;
        if (value instanceof Number n) return n.intValue();
        if (value instanceof String s) return Integer.parseInt(s);
        throw new IllegalArgumentException("Cannot cast to int: " + value);
    }

    private Double castToDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Double d) return d;
        if (value instanceof Number n) return n.doubleValue();
        if (value instanceof String s) return Double.valueOf(s);
        throw new IllegalArgumentException("Cannot cast to Double: " + value);
    }

    private Date castToDate(Object value) {
        if (value == null) return null;
        if (value instanceof Date d) return d;
        if (value instanceof Number n) return new Date(n.longValue());
        if (value instanceof String s) {
            try {
                long epoch = Long.parseLong(s);
                return new Date(epoch);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Date must be epoch millis (number/string)");
            }
        }
        throw new IllegalArgumentException("Cannot cast to Date: " + value);
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
