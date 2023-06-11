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
import ru.iu3.backend.repositories.ArtistRepository;
import ru.iu3.backend.repositories.CountryRepository;
import ru.iu3.backend.tools.DataValidationException;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/artists")
    public Page<Artist> getAllArtists(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return artistRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable(value = "id") Long artistId) throws DataValidationException {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(()-> new DataValidationException("Художник с таким индексом не найден"));
        return ResponseEntity.ok(artist);
    }

    @PostMapping("/deleteartists")
    public ResponseEntity deleteArtists(@Valid @RequestBody List<Artist> artists) {
        artistRepository.deleteAll(artists);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist artist)
            throws Exception {
        try {
            Optional<Country> cc = countryRepository.findById(artist.country.id);
            if (cc.isPresent()) {
                artist.country = cc.get();
            }
            Artist nc = artistRepository.save(artist);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("constraint [artists.name]"))
                error = "Artist already exists";
            else
                error = "Undefined error";
            Map<String, String>
                    map =  new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object> (map, HttpStatus.OK);
        }
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long artistId, @RequestBody Artist artistDetails) {
        Artist artist = null;
        Optional<Artist> cc = artistRepository.findById(artistId);
        if (cc.isPresent()) {
            artist = cc.get();
            Optional<Country> countryOptional = countryRepository.findById(artist.country.id);
            if (countryOptional.isPresent()) {
                artist.country = countryOptional.get();
            }
            artist.name = artistDetails.name;
            artist.century = artistDetails.century;
            artistRepository.save(artist);
            return ResponseEntity.ok(artist);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "country not found");
        }
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable(value = "id") Long artistId) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        Map<String, Boolean> resp = new HashMap<>();
        if (artist.isPresent()) {
            artistRepository.delete(artist.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/artists/{id}/paintings")
    public ResponseEntity<Object> getArtistPaintings(@PathVariable(value = "id") Long artistId) throws Exception {
        Optional<Artist> cc = artistRepository.findById(artistId);
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().paintings);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
    }
}
