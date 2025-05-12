package ru.hackaton.hackaton.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hackaton.hackaton.services.VMService;

@RestController
@RequestMapping("/vm")
public class VMController {

    @Autowired
    private VMService vmService;

    @PostMapping("/create/{teamID}")
    public ResponseEntity<String> createVM(@PathVariable Long teamID){
        try{
            return vmService.createVM(teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/reset/{teamID}")
    public ResponseEntity<String> resetVM(@PathVariable Long teamID){
        try{
            return vmService.resetVM(teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{teamID}")
    public ResponseEntity<String> deleteVM(@PathVariable Long teamID){
        try{
            return vmService.deleteVM(teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
