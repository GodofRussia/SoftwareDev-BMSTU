package ru.iu3.backend.repositories;


import jdk.javadoc.doclet.Doclet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.backend.models.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public default Optional<User> findByLogin(String login) {
        List<User> users = this.findAll();
        for (User user : users) {
            if (Objects.equals(user.login, login)) {
                return this.findById(user.id);
            }
        }

        return Optional.empty();
    }

    public default Optional<User> findByToken(String token) {
        List<User> users = this.findAll();
        for (User user : users) {
            if (Objects.equals(user.token, token)) {
                return this.findById(user.id);
            }
        }

        return Optional.empty();
    }
}