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

            if (event.getStatus().equals("STARTED") || event.getStatus().equals("ENDED")){
                return ResponseEntity.status(400).body("В данном мероприятии уже нельзя принять участие, так как оно уже началось/закончилось!");
            }

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
     * Удаление команды
     */

    public ResponseEntity<String> deleteTeam(Long teamID){
        try{

            Long userID = userService.getCurrentUser().getId();

            if (!userRepository.existsById(userID)){
                return ResponseEntity.status(400).body("Не найден пользователь с таким ID!");
            }

            if (!teamRepository.existsById(teamID)){
                return ResponseEntity.status(400).body("Не найдена команда с таким ID!");
            }

            Optional<Team> teamOpt = teamRepository.findById(teamID);

            if (teamOpt.isPresent()) {

                Team team = teamOpt.get();

                if (!(team.getLeader().getId() == userID)){
                    return ResponseEntity.status(400).body("У вас нет прав для удаления этой команды!");
                }

                teamRepository.delete(team);

                return ResponseEntity.status(200).body("Команда была успешно удалена!");
            }
            else return ResponseEntity.status(500).body("Ошибка при загрузке информации о команде!");

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
                Event event = team.getEvent();

                if (event.getStatus().equals("STARTED") || event.getStatus().equals("ENDED")){
                    return ResponseEntity.status(400).body("В данном мероприятии уже нельзя принять участие, так как оно уже началось/закончилось!");
                }

                List<Long> enterRequests = new ArrayList<>();

                if (team.getCountOfMembers() >= event.getMaxMembers()){
                    return ResponseEntity.status(400).body("В данной команде не осталось свободных мест!");
                }

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

                if ((team.getLeader().getId() == userID) && (team.getCountOfMembers() != 1)){
                    return ResponseEntity.status(400).body("Так как вы являетесь лидером команды, вы не можете покинуть её пока в ней есть кто-то помимо вас!");
                }

                team.getMembers().remove(user);
                team.setCountOfMembers(team.getCountOfMembers() - 1);

                boolean deleted = false;

                if (team.getCountOfMembers() == 0){
                    teamRepository.delete(team);
                    deleted = true;
                }
                else teamRepository.save(team);

                return deleted
                        ? ResponseEntity.status(200).body("Команда успешно покинута и она была удалена, потому что вы были последним участником!")
                        : ResponseEntity.status(200).body("Команда успешно покинута!");

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

                Event event = team.getEvent();

                if (event.getStatus().equals("STARTED") || event.getStatus().equals("ENDED")){
                    return ResponseEntity.status(400).body("Вы не можете менять состав команды, так как мероприятие уже началось/закончилось!");
                }

                if (team.getCountOfMembers() >= event.getMaxMembers()){
                    return ResponseEntity.status(400).body("В данной команде не осталось свободных мест!");
                }

                if (leader == team.getLeader()) {


                    team.getMembers().add(user);

                    team.getEnterRequest().remove(Long.valueOf(userID));

                    team.setCountOfMembers(team.getCountOfMembers() + 1);

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

            List<Team> team = teamRepository.findAll();

            return ResponseEntity.status(200).body(team.toString());

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
