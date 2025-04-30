package ru.hackaton.hackaton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hackaton.hackaton.entities.Event;
import ru.hackaton.hackaton.entities.Team;
import ru.hackaton.hackaton.entities.User;
import ru.hackaton.hackaton.repositories.EventRepository;
import ru.hackaton.hackaton.repositories.TeamRepository;
import ru.hackaton.hackaton.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventRepository eventRepository;


    /**
     * Нужно добавить метод для удаления команды,
     * а так же запретить лидеру команды выходить из нее, если кто-то помимо него состоит в ней
     *
     * Рассмотреть вариант с ограничением участников в сущности команды, а не мероприятия
     */


    /**
     * Создание команды
     */
    public ResponseEntity<String> createTeam(Team newTeam, Long eventID){
        try{
            if(teamRepository.existsByName(newTeam.getName())){
                return ResponseEntity.status(400).body("Команда с таким названием уже существует!");
            }

            if (!eventRepository.existsById(eventID)){
                return ResponseEntity.status(400).body("Мероприятия с таким ID не найдено!");
            }

            User leader = userService.getCurrentUser();

            Event event = eventRepository.findById(eventID).get();


            Team team = Team.builder()
                    .name(newTeam.getName())
                    .leader(leader)
                    .members(new ArrayList<>(List.of(leader)))
                    .countOfMembers(1)
                    .event(event)
                    .build();

            teamRepository.save(team);

            return ResponseEntity.status(200).body("Команда успешно создана!");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    /**
     * Заявка на вступление в команду
     */
    public ResponseEntity<String> enterTeam(Long teamID){
        try{

            Long userID = userService.getCurrentUser().getId();

            if (!userRepository.existsById(userID)){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }

            Optional<Team> teamOpt = teamRepository.findById(teamID);

            if (teamOpt.isPresent()){

                Team team = teamOpt.get();

                List<Long> enterRequests = new ArrayList<>();
                if (team.getEnterRequest() != null){
                    enterRequests = team.getEnterRequest();
                    enterRequests.add(userID);
                }
                else {
                    enterRequests = List.of(userID);
                }

                team.setEnterRequest(enterRequests);

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
    public ResponseEntity<String> exitTeam(Long teamID){
        try{

            Long userID = userService.getCurrentUser().getId();
            if (!userRepository.existsById(userID)){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }

            Optional<Team> teamOpt = teamRepository.findById(teamID);
            Optional<User> userOpt = userRepository.findById(userID);

            if (teamOpt.isPresent() && userOpt.isPresent()){

                Team team = teamOpt.get();
                User user = userOpt.get();

                if (!team.getMembers().contains(user)){
                    return ResponseEntity.status(400).body("Пользователь не состоит в данной команде!");
                }

                team.getMembers().remove(user);

                teamRepository.save(team);

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
    public ResponseEntity<String> acceptMember(Long userID, Long teamID){
        try{

            User leader = userService.getCurrentUser();

            if (!userRepository.existsById(userID) || !userRepository.existsById(leader.getId())){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }



            Optional<Team> teamOpt = teamRepository.findById(teamID);
            Optional<User> userOpt = userRepository.findById(userID);
            if (teamOpt.isPresent() && userOpt.isPresent()){

                Team team = teamOpt.get();
                User user = userOpt.get();


                if (leader == team.getLeader()) {


                    team.getMembers().add(user);

                    team.getEnterRequest().remove(Long.valueOf(userID));

                    teamRepository.save(team);

                    return ResponseEntity.status(200).body("Пользователь успешно был принят в команду!");
                }
                else return ResponseEntity.status(400).body("У вас нет прав для одобрения заявки на вступление!");


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
    public ResponseEntity<String> declineMember(Long userID, Long teamID){
        try{
            Long adminID = userService.getCurrentUser().getId();

            if (!userRepository.existsById(userID) || !userRepository.existsById(adminID)){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }



            Optional<Team> teamOpt = teamRepository.findById(teamID);

            if (teamOpt.isPresent()){

                Team team = teamOpt.get();

                if (!team.getEnterRequest().contains(Long.valueOf(userID))){
                    return ResponseEntity.status(400).body("Нет заявок на вступление от пользователя с таким ID!");
                }

                if (adminID == team.getLeader().getId()) {

                    team.getEnterRequest().remove(Long.valueOf(userID));

                    teamRepository.save(team);

                    return ResponseEntity.status(200).body("Запрос на вступление был отклонен!");
                }
                else return ResponseEntity.status(400).body("У вас нет прав для отказа заявки на вступление!");


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
