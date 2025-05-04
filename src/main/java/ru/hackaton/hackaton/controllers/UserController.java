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

    @PostMapping("/registration")
    public ResponseEntity<String> createUser(@RequestBody User user){
        try {
            return userService.createUser(user);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@RequestBody User user, @PathVariable Long id){
        try {
            return userService.updateUser(user, id);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        try {
            return userService.deleteUser(id);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id){
        try {
            return userService.getUser(id);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllUsers(){
        try{
            return userService.getAllUser();
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

}
