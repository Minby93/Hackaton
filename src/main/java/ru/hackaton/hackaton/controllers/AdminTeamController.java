package ru.hackaton.hackaton.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hackaton.hackaton.entities.Team;
import ru.hackaton.hackaton.services.TeamService;

@RestController
@RequestMapping("/team/admin")
public class AdminTeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<String> createTeam(@RequestBody Team team){
        try{
            return teamService.createTeam(team);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptMember(Long userID, Long teamID, Long adminID){
        try{
            return teamService.acceptMember(userID,teamID,adminID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/decline")
    public ResponseEntity<String> declineMember(Long userID, Long teamID, Long adminID){
        try{
            return teamService.declineMember(userID, teamID, adminID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
