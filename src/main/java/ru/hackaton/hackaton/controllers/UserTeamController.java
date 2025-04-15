package ru.hackaton.hackaton.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hackaton.hackaton.services.TeamService;

@RestController
@RequestMapping("/team/user")
public class UserTeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/enter")
    public ResponseEntity<String> enterTeam(Long userID, Long teamID){
        try{
            return teamService.enterTeam(userID, teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/exit")
    public ResponseEntity<String> exitTeam(Long userID, Long teamID){
        try{
            return teamService.exitTeam(userID, teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
