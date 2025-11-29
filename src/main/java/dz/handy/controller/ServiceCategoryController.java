package dz.handy.controller;

import dz.handy.entity.ServiceCategory;
import dz.handy.service.ServiceCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class ServiceCategoryController {

    private final ServiceCategoryService service;

    public ServiceCategoryController(ServiceCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceCategory> getAll(@RequestParam(value = "lang", required = false) String lang) {
        return service.findAll().stream()
                .map(c -> toLocalized(c, lang))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategory> getById(@PathVariable Long id,
                                                   @RequestParam(value = "lang", required = false) String lang) {
        return service.findById(id)
                .map(c -> ResponseEntity.ok(toLocalized(c, lang)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<ServiceCategory> getByName(@PathVariable String name,
                                                     @RequestParam(value = "lang", required = false) String lang) {
        return service.findByName(name)
                .map(c -> ResponseEntity.ok(toLocalized(c, lang)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceCategory> create(@RequestBody ServiceCategory category) {
        try {
            ServiceCategory saved = service.create(category);
            return ResponseEntity.created(URI.create("/api/categories/" + saved.getId())).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategory> update(@PathVariable Long id, @RequestBody ServiceCategory category) {
        try {
            return service.update(id, category)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private ServiceCategory toLocalized(ServiceCategory src, String lang) {
        String code = lang == null ? "en" : lang.toLowerCase(Locale.ROOT);
        String name = src.getName();
        String desc = src.getDescription();
        switch (code) {
            case "fr":
                name = src.getNameFr() != null ? src.getNameFr() : (name != null ? name : src.getNameAr());
                desc = src.getDescriptionFr() != null ? src.getDescriptionFr() : (desc != null ? desc : src.getDescriptionAr());
                break;
            case "ar":
                name = src.getNameAr() != null ? src.getNameAr() : (name != null ? name : src.getNameFr());
                desc = src.getDescriptionAr() != null ? src.getDescriptionAr() : (desc != null ? desc : src.getDescriptionFr());
                break;
            default:
                // en or unknown -> keep default name/description
                if (name == null) name = src.getNameFr() != null ? src.getNameFr() : src.getNameAr();
                if (desc == null) desc = src.getDescriptionFr() != null ? src.getDescriptionFr() : src.getDescriptionAr();
        }
        return ServiceCategory.builder()
                .id(src.getId())
                .name(name)
                .description(desc)
                .nameAr(src.getNameAr())
                .nameFr(src.getNameFr())
                .descriptionAr(src.getDescriptionAr())
                .descriptionFr(src.getDescriptionFr())
                .build();
    }
}
