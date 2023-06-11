package ru.iu3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface ArtistRepository  extends JpaRepository<Artist, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Artist a WHERE a.country.id = :countryId")
    void deleteByCountryId(@Param("countryId") Long countryId);

    @Query("SELECT a FROM Artist a WHERE a.country.id = :countryId")
    List<Artist> getByCountryId(@Param("countryId") Long countryId);

    public default Optional<Artist> findByName(String name) {
        List<Artist> artists = this.findAll();
        for (Artist artist : artists) {
            if (Objects.equals(artist.name, name)) {
                return this.findById(artist.id);
            }
        }

        return Optional.empty();
    }
}