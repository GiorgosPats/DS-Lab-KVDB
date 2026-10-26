package gr.hua.dit.kvdb;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-Memory Key-Value Database controller.
 */
@RestController
@RequestMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
public class KvdbController {

    private final ConcurrentHashMap<String, String> db;

    public KvdbController() {
        this.db = new ConcurrentHashMap<>();
    }

    /**
     * Creates a new key with the given value; fails if the key already exists.
     */
    @PostMapping(value = "/kv/{key}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String insert(@PathVariable String key, @RequestBody String value) {
        if (this.db.containsKey(key)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "key already exists");
        }
        this.db.put(key, value);
        return "<null>";
    }

    /**
     * Creates or replaces the value associated with the given key.
     */
    @PutMapping(value = "/kv/{key}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String upsert(@PathVariable String key, @RequestBody String value) {
        final String previousValue = this.db.put(key, value);
        return previousValue == null ? "<null>" : previousValue;
    }

    /**
     * Replaces the value of an existing key; fails if the key is absent.
     */
    @PatchMapping(value = "/kv/{key}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String update(@PathVariable String key, @RequestBody String value) {
        if (!this.db.containsKey(key)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "key is not present");
        }
        final String previousValue = this.db.put(key, value);
        return previousValue == null ? "<null>" : previousValue;
    }

    /**
     * Removes the key and returns its previous value; fails if the key is absent.
     */
    @DeleteMapping("/kv/{key}")
    public String delete(@PathVariable String key) {
        if (!this.db.containsKey(key)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "key is not present");
        }
        final String previousValue = this.db.remove(key);
        return previousValue == null ? "<null>" : previousValue;
    }

    /**
     * Returns the current value for the given key; fails if the key is absent.
     */
    @GetMapping("/kv/{key}")
    public String get(@PathVariable String key) {
        if (!this.db.containsKey(key)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "key is not present");
        }
        final String value = this.db.get(key);
        return value == null ? "<null>" : value;
    }

    /**
     * Lists all keys, one per line.
     */
    @GetMapping("/kv")
    public String list() {
        final List<String> keys = this.db.keySet().stream().toList();
        final String list = String.join("\n", keys);
        return list;
    }

    @PostConstruct
    private void populateInitialData() {
        this.db.put("name", "Key-Value Database");
        this.db.put("programmer", "John Doe");
        this.db.put("version", "0.1.0");
    }
}