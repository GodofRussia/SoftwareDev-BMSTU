package ru.iu3.backend.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.models.Museum;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    public default Optional<Country> findByName(String name) {
        List<Country> countries = this.findAll();
        for (Country country : countries) {
            if (Objects.equals(country.name, name)) {
                return this.findById(country.id);
            }
        }

        return Optional.empty();
    }
}