package ru.hackaton.hackaton.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hackaton.hackaton.services.TeamService;

@RestController
@RequestMapping("/team/user")
public class UserTeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/enter/{teamID}")
    public ResponseEntity<String> enterTeam(@PathVariable Long teamID){
        try{
            return teamService.enterTeam(teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/exit/{teamID}")
    public ResponseEntity<String> exitTeam(@PathVariable Long teamID){
        try{
            return teamService.exitTeam(teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
