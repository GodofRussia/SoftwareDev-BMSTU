package ru.iu3.backend.models;

//import javax.persistence.*;
import jakarta.persistence.*;

@Table(name = "countries")
@Access(AccessType.FIELD)
@Entity
public class Country {

    public Country() { }
    public Country(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;
}
