package ru.iu3.backend.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserWithCredentials {
    public long id;

    public String login;
    public String email;
    public String token;
    public LocalDateTime activity;
    public Set<Museum> museums = new HashSet<>();


    public UserWithCredentials(User user) {
        this.id = user.id;
        this.login = user.login;
        this.email = user.email;
        this.token = user.token;
        this.activity = user.activity;
        this.museums = user.museums;
    }
}
