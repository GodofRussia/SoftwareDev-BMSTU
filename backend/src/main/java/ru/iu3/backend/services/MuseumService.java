package ru.iu3.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.repositories.MuseumRepository;
import ru.iu3.backend.repositories.PaintingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MuseumService {
    @Autowired
    MuseumRepository museumRepository;
    @Autowired
    PaintingRepository paintingRepository;

    public void deleteAllWithConstraints(List<Museum> museumList) {
        for (Museum museum : museumList) {
            paintingRepository.deleteByMuseumId(museum.id);
        }

        museumRepository.deleteAll(museumList);
    }
}
