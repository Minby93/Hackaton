package ru.hackaton.hackaton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hackaton.hackaton.entities.Team;
import ru.hackaton.hackaton.repositories.TeamRepository;
import ru.hackaton.hackaton.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Создание команды
     */
    public ResponseEntity<String> createTeam(Team newTeam){
        try{
            if(teamRepository.existsByName(newTeam.getName())){
                return ResponseEntity.status(400).body("Команда с таким названием уже существует!");
            }

            Team team = new Team();

            team.setName(newTeam.getName());
            // Достать ID авторизованного пользователя из контекста
            //team.setAdminID();
            //team.setListOfMembers();
            team.setCountOfMembers(1);

            return ResponseEntity.status(200).body("Команда успешно создана!");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Возможность исключения из команды
     * Возможность одобрения заявки на вступление
     * Возможность отказа
     */

    /**
     * Заявка на вступление в команду
     */
    public ResponseEntity<String> enterTeam(Long userID, Long teamID){
        try{

            if (!userRepository.existsById(userID)){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }

            Optional<Team> teamOpt = teamRepository.findById(teamID);

            if (teamOpt.isPresent()){

                Team team = teamOpt.get();

                //team.setEnterRequest(team.getEnterRequest().add());

                teamRepository.save(team);

                return ResponseEntity.status(200).body("Запрос на вступление успешно отправлен!");
            }

            return ResponseEntity.status(500).body("Ошибка при получении информации о пользователе!");

        }
        catch (Exception e){
            return  ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Выход из команды
     */
    public ResponseEntity<String> exitTeam(Long userID, Long teamID){
        try{
            if (!userRepository.existsById(userID)){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }

            Optional<Team> teamOpt = teamRepository.findById(teamID);

            if (teamOpt.isPresent()){

                Team team = teamOpt.get();

                if (!team.getListOfMembers().contains(userID)){
                    return ResponseEntity.status(400).body("Пользователь не состоит в данной команде!");
                }

                //team.getListOfMembers().remove(Long.valueOf());

                team.setListOfMembers(team.getListOfMembers());

                return ResponseEntity.status(200).body("Команда успешно покинута!");
            }

            return ResponseEntity.status(500).body("Ошибка при получении информации о пользователе!");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Принятие в команду лидером
     */
    public ResponseEntity<String> acceptMember(Long userID, Long teamID, Long adminID){
        try{
            if (!userRepository.existsById(userID) || !userRepository.existsById(adminID)){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }

            Optional<Team> teamOpt = teamRepository.findById(teamID);

            if (teamOpt.isPresent()){

                Team team = teamOpt.get();

                team.getListOfMembers().add(userID);

                team.getEnterRequest().remove(Long.valueOf(userID));

                teamRepository.save(team);

                return ResponseEntity.status(200).body("Пользователь успешно был принят в команду!");

            }

            return ResponseEntity.status(500).body("Ошибка при получении информации о пользователе!");
        }
        catch (Exception e){
            return  ResponseEntity.status(500).body(e.getMessage());
        }

    }

    /**
     * Отказ на принятие в команду лидером
     */
    public ResponseEntity<String> declineMember(Long userID, Long teamID, Long adminID){
        try{
            if (!userRepository.existsById(userID) || !userRepository.existsById(adminID)){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }

            Optional<Team> teamOpt = teamRepository.findById(teamID);

            if (teamOpt.isPresent()){

                Team team = teamOpt.get();

                team.getEnterRequest().remove(Long.valueOf(userID));

                teamRepository.save(team);

                return ResponseEntity.status(200).body("Пользователь успешно был принят в команду!");

            }

            return ResponseEntity.status(500).body("Ошибка при получении информации о пользователе!");
        }
        catch (Exception e){
            return  ResponseEntity.status(500).body(e.getMessage());
        }

    }

    /**
     * Получение команды
     */
    public ResponseEntity<String> getTeam(Long teamID){
        try{

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }

            Optional<Team> teamOpt = teamRepository.findById(teamID);

            if (teamOpt.isPresent()){

                Team team = teamOpt.get();

                return ResponseEntity.status(200).body(team.toString());

            }

            return ResponseEntity.status(500).body("Ошибка при получении информации о пользователе!");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Получение всех команд
     */
    public ResponseEntity<String> getAllTeams(){
        try{

            List<Team> teamOpt = teamRepository.findAll();

            return ResponseEntity.status(200).body(teamOpt.toString());

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
