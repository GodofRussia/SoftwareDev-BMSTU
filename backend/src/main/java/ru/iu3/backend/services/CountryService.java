package ru.iu3.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.repositories.ArtistRepository;
import ru.iu3.backend.repositories.CountryRepository;
import ru.iu3.backend.repositories.PaintingRepository;

import java.util.List;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PaintingRepository paintingRepository;
    @Autowired
    private ArtistRepository artistRepository;

    public void deleteAllWithConstraints(List<Country> countriesList) {
        for (Country country : countriesList) {
            List<Artist> artists = artistRepository.getByCountryId(country.id);
            for (Artist artist : artists) {
                paintingRepository.deleteByArtistId(artist.id);
            }

            artistRepository.deleteByCountryId(country.id);
        }

        countryRepository.deleteAll(countriesList);
    }
}