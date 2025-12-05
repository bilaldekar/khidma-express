package dz.khidma.express.service.impl;

import dz.khidma.express.entity.Category;
import dz.khidma.express.entity.User;
import dz.khidma.express.entity.Worker;
import dz.khidma.express.repository.ServiceCategoryRepository;
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
    private final ServiceCategoryRepository serviceCategoryRepository;

    public UserServiceImpl(UserRepository userRepository, ServiceCategoryRepository serviceCategoryRepository) {
        this.userRepository = userRepository;
        this.serviceCategoryRepository = serviceCategoryRepository;
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
            boolean handled = false;
            // Base User fields
            switch (key) {
                case "email" -> { user.setEmail((String) value); handled = true; }
                case "phoneNumber1" -> { user.setPhoneNumber1((String) value); handled = true; }
                case "firstName" -> { user.setFirstName((String) value); handled = true; }
                case "lastName" -> { user.setLastName((String) value); handled = true; }
                case "enabled" -> { user.setEnabled(castToInteger(value)); handled = true; }
                case "latitude" -> { user.setLatitude(castToDouble(value)); handled = true; }
                case "longitude" -> { user.setLongitude(castToDouble(value)); handled = true; }
                case "wilaya" -> { user.setWilaya((String) value); handled = true; }
                case "commune" -> { user.setCommune((String) value); handled = true; }
                case "accountNonLocked" -> { user.setAccountNonLocked(castToInt(value)); handled = true; }
                case "creationDate" -> { user.setCreationDate(castToDate(value)); handled = true; }
                case "expiryDate" -> { user.setExpiryDate(castToDate(value)); handled = true; }
                case "lastLogin" -> { user.setLastLogin(castToDate(value)); handled = true; }
                default -> {}
            }

            // Worker-specific fields if applicable
            if (!handled && user instanceof Worker worker) {
                switch (key) {
                    case "specialization" -> worker.setSpecialization((String) value);
                    case "rating" -> {
                        Double d = castToDouble(value);
                        if (d != null) worker.setRating(d);
                    }
                    case "available" -> worker.setAvailable(castToBoolean(value));
                    case "categoryId" -> {
                        Long id = castToLong(value);
                        Category sc = serviceCategoryRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("ServiceCategory not found: id=" + id));
                        worker.setCategory(sc);
                    }
                    case "categoryName" -> {
                        String name = (String) value;
                        Category sc = serviceCategoryRepository.findByName(name)
                                .orElseThrow(() -> new IllegalArgumentException("ServiceCategory not found: name=" + name));
                        worker.setCategory(sc);
                    }
                    case "category" -> {
                        // Allow either id (number) or name (string)
                        Category sc = resolveCategory(value);
                        worker.setCategory(sc);
                    }
                    default -> { /* ignore unknown fields */ }
                }
            }
        }

        User saved = userRepository.save(user);
        return Optional.of(saved);
    }

    private boolean castToBoolean(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean b) return b;
        if (value instanceof Number n) return n.intValue() != 0;
        if (value instanceof String s) {
            String t = s.trim().toLowerCase();
            if (t.equals("true") || t.equals("yes") || t.equals("1")) return true;
            if (t.equals("false") || t.equals("no") || t.equals("0")) return false;
            throw new IllegalArgumentException("Cannot cast to boolean: " + value);
        }
        throw new IllegalArgumentException("Cannot cast to boolean: " + value);
    }

    private Long castToLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long l) return l;
        if (value instanceof Integer i) return i.longValue();
        if (value instanceof Number n) return n.longValue();
        if (value instanceof String s) return Long.valueOf(s);
        throw new IllegalArgumentException("Cannot cast to Long: " + value);
    }

    private Category resolveCategory(Object value) {
        if (value == null) return null;

        // Accept a numeric id directly
        if (value instanceof Number n) {
            Long id = n.longValue();
            return serviceCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("ServiceCategory not found: id=" + id));
        }

        // Accept an id/name provided as a String
        if (value instanceof String s) {
            String trimmed = s.trim();
            // Try numeric string as id first
            try {
                Long id = Long.valueOf(trimmed);
                return serviceCategoryRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("ServiceCategory not found: id=" + id));
            } catch (NumberFormatException ignore) {
                // then treat as name
                return serviceCategoryRepository.findByName(trimmed)
                        .orElseThrow(() -> new IllegalArgumentException("ServiceCategory not found: name=" + trimmed));
            }
        }

        // Accept an object like { "id": 5 } or { "name": "Plumbing" }
        if (value instanceof Map<?, ?> map) {
            Object idVal = map.get("id");
            if (idVal != null) {
                Long id = castToLong(idVal);
                return serviceCategoryRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("ServiceCategory not found: id=" + id));
            }
            Object nameVal = map.get("name");
            if (nameVal != null) {
                String name = String.valueOf(nameVal).trim();
                return serviceCategoryRepository.findByName(name)
                        .orElseThrow(() -> new IllegalArgumentException("ServiceCategory not found: name=" + name));
            }
            throw new IllegalArgumentException("Category map must contain 'id' or 'name'");
        }

        throw new IllegalArgumentException("Unsupported category value type: " + value.getClass().getName());
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
