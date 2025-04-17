package ru.hackaton.hackaton.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hackaton.hackaton.entities.Event;
import ru.hackaton.hackaton.services.EventService;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/{eventID}")
    public ResponseEntity<String> getEvent(@RequestParam Long eventID){
        try{
            return eventService.getEvent(eventID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody Event event, Long adminID){
        try{
            return eventService.createEvent(event, adminID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @PostMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestBody Event event, Long adminID){
        try{
            return eventService.updateEvent(event, adminID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{eventID}")
    public ResponseEntity<String> deleteEvent(@RequestParam Long eventID, Long adminID){
        try{
            return eventService.deleteEvent(eventID, adminID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


}
