package ru.iu3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.models.Painting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface PaintingRepository extends JpaRepository<Painting, Long>
{
    @Modifying
    @Transactional
    @Query("DELETE FROM Painting p WHERE p.artist.id = :artistId")
    void deleteByArtistId(@Param("artistId") Long artistId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Painting p WHERE p.museum.id = :museumId")
    void deleteByMuseumId(@Param("museumId") Long museumId);

    public default Optional<Painting> findByName(String name) {
        List<Painting> countries = this.findAll();
        for (Painting country : countries) {
            if (Objects.equals(country.name, name)) {
                return this.findById(country.id);
            }
        }

        return Optional.empty();
    }
}