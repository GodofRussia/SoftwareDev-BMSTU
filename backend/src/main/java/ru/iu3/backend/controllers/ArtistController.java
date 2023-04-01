package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iu3.backend.repositories.ArtistRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/artists")
    public List
    getAllCountries() {
        return artistRepository.findAll();
    }
}
