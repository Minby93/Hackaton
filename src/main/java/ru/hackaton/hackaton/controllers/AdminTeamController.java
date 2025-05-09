package ru.hackaton.hackaton.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hackaton.hackaton.entities.Team;
import ru.hackaton.hackaton.services.TeamService;

@RestController
@RequestMapping("/team/admin")
public class AdminTeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/create/{eventID}")
    public ResponseEntity<String> createTeam(@RequestBody Team team, @PathVariable Long eventID){
        try{
            return teamService.createTeam(team, eventID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{teamID}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamID){
        try{
            return teamService.deleteTeam(teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/accept/{teamID}/{userID}")
    public ResponseEntity<String> acceptMember(@PathVariable Long userID,@PathVariable Long teamID){
        try{
            return teamService.acceptMember(userID,teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/decline/{teamID}/{userID}")
    public ResponseEntity<String> declineMember(@PathVariable Long userID, @PathVariable Long teamID){
        try{
            return teamService.declineMember(userID, teamID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
