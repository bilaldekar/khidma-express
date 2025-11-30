package dz.khidma.express.controller;

import dz.khidma.express.entity.User;
import dz.khidma.express.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getById(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PostMapping
//    public ResponseEntity<User> create(@RequestBody User user) {
//        try {
//            User saved = userService.create(user);
//            return ResponseEntity.created(URI.create("/api/users/" + saved.getUsername())).body(saved);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//    }

    @PutMapping("/{username}")
    public ResponseEntity<User> update(@PathVariable String username, @RequestBody User user) {
        return userService.update(username, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> patch(@PathVariable String username, @RequestBody Map<String, Object> updates) {
        try {
            return userService.patch(username, updates)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        boolean deleted = userService.delete(username);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
