package ru.hackaton.hackaton.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hackaton.hackaton.entities.User;
import ru.hackaton.hackaton.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@RequestBody User user, @PathVariable Long id){
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

}
