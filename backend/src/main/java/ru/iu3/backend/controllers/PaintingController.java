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
import ru.iu3.backend.models.Country;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.repositories.ArtistRepository;
import ru.iu3.backend.repositories.CountryRepository;
import ru.iu3.backend.repositories.MuseumRepository;
import ru.iu3.backend.repositories.PaintingRepository;
import ru.iu3.backend.tools.DataValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class PaintingController {
    @Autowired
    PaintingRepository paintingRepository;
    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    MuseumRepository museumRepository;

    @GetMapping("/paintings")
    public Page<Painting> getAllPaintings(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return paintingRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/paintings/{id}")
    public ResponseEntity<Painting> getPainting(@PathVariable(value = "id") Long paintingId) throws DataValidationException {
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(()-> new DataValidationException("Картина с таким индексом не найдена"));
        return ResponseEntity.ok(painting);
    }

    @PostMapping("/deletepaintings")
    public ResponseEntity deletePaintings(@Valid @RequestBody List<Painting> paintings) {
        paintingRepository.deleteAll(paintings);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting painting) throws Exception {
        try {
            Optional<Museum> museumOptional = museumRepository.findById(painting.museum.id);
            Optional<Artist> artistOptional = artistRepository.findById(painting.artist.id);
            if (museumOptional.isPresent() && artistOptional.isPresent()) {
                painting.museum = museumOptional.get();
                painting.artist = artistOptional.get();
            }
            Painting nc = paintingRepository.save(painting);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        } catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("constraint [paintings.name]"))
                error = "Painting already exists";
            else
                error = "Undefined error";
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object> (map, HttpStatus.OK);
        }
    }

    @PutMapping("/paintings/{id}")
    public ResponseEntity<Painting> updatePainting(@PathVariable(value = "id") Long paintingId, @RequestBody Painting paintingDetails) {
        Painting painting = null;
        Optional<Painting> cc = paintingRepository.findById(paintingId);
        if (cc.isPresent()) {
            painting = cc.get();
            Optional<Museum> museumOptional = museumRepository.findById(paintingDetails.museum.id);
            Optional<Artist> artistOptional = artistRepository.findById(paintingDetails.artist.id);
            if (museumOptional.isPresent()) {
                painting.museum = museumOptional.get();
            }
            if (artistOptional.isPresent()) {
                painting.artist = artistOptional.get();
            }
            painting.name = paintingDetails.name;
            painting.year = paintingDetails.year;
            paintingRepository.save(painting);
            return ResponseEntity.ok(painting);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "painting not found");
        }
    }

    @DeleteMapping("/paintings/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable(value = "id") Long paintingId) {
        Optional<Painting> painting = paintingRepository.findById(paintingId);
        Map<String, Boolean> resp = new HashMap<>();
        if (painting.isPresent()) {
            paintingRepository.delete(painting.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
