package ru.iu3.backend.controllers;

public class UserController {
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @RequestBody User userDetails) {

        User user = null;
        Optional
                uu = userRepository.findById(userId);
        if (uu.isPresent()) {
            user = uu.get();
            user.login = userDetails.login;
            user.email = userDetails.email;
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
    }
}
