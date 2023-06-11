package ru.iu3.backend.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.models.Museum;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface MuseumRepository extends JpaRepository<Museum, Long> {
    public default Optional<Museum> findByName(String name) {
        List<Museum> countries = this.findAll();
        for (Museum country : countries) {
            if (Objects.equals(country.name, name)) {
                return this.findById(country.id);
            }
        }

        return Optional.empty();
    }
}