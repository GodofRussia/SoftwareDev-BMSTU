package ru.iu3.backend.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.repositories.MuseumRepository;
import ru.iu3.backend.services.MuseumService;
import ru.iu3.backend.tools.DataValidationException;


import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class MuseumController {
    @Autowired
    MuseumRepository museumRepository;
    @Autowired
    MuseumService museumService;

    @GetMapping("/museums")
    public Page<Museum> getAllMuseums(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return museumRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/museums/{id}")
    public ResponseEntity<Museum> getMuseum(@PathVariable(value = "id") Long museumId) throws DataValidationException {
        Museum museum = museumRepository.findById(museumId)
                .orElseThrow(()-> new DataValidationException("Музей с таким индексом не найден"));
        return ResponseEntity.ok(museum);
    }

    @PostMapping("/deletemuseums")
    public ResponseEntity deleteMuseums(@Valid @RequestBody List<Museum> museums) {
        museumService.deleteAllWithConstraints(museums);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/museums/{id}")
    public ResponseEntity<Museum> updateMuseum(@PathVariable(value = "id") Long museumId,
                                               @RequestBody Museum museumDetails) {
        Museum museum = null;
        Optional<Museum>
                cc = museumRepository.findById(museumId);
        if (cc.isPresent()) {
            museum = cc.get();
            museum.name = museumDetails.name;
            museum.location = museumDetails.location;
            museumRepository.save(museum);
            return ResponseEntity.ok(museum);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Museum not found");
        }
    }

    @PostMapping("/museums")
    public ResponseEntity<Object> createMuseum(@RequestBody Museum museum) throws Exception {
        try {
            Museum nc = museumRepository.save(museum);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("constraint [museums.name]"))
                error = "Museum already exists";
            else
                error = "Undefined error";
            Map<String, String>
                    map =  new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object> (map, HttpStatus.OK);
        }
    }

    @DeleteMapping("/museums/{id}")
    public ResponseEntity<Object> deleteMuseum(@PathVariable(value = "id") Long museumId) {
        Optional<Museum> museum = museumRepository.findById(museumId);
        Map<String, Boolean> resp = new HashMap<>();
        if (museum.isPresent()) {
            museumRepository.delete(museum.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/museums/{id}/paintings")
    public ResponseEntity<Object> getMuseumPaintings(@PathVariable(value = "id") Long museumId) throws Exception {
        Optional<Museum> cc = museumRepository.findById(museumId);
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().paintings);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Museum not found");
    }
}
