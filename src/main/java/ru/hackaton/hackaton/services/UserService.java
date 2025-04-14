package ru.hackaton.hackaton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hackaton.hackaton.entities.User;
import ru.hackaton.hackaton.enums.Role;
import ru.hackaton.hackaton.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Создание пользователя
     */
    public ResponseEntity<String> createUser(User newUser){
        try{

            if (userRepository.existsByEmail(newUser.getEmail())){
                return ResponseEntity.status(400).body("Email уже занят!");
            }

            if (userRepository.existsByUsername(newUser.getUsername())){
                return ResponseEntity.status(400).body("Логин уже занят!");
            }

            User user = new User();

            user.setEmail(newUser.getEmail());
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            user.setFullName(newUser.getFullName());
            user.setRole(Role.USER);

            userRepository.save(user);

            return ResponseEntity.status(200).body("Пользователь успешно создан!");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Изменение пользователя
     */
    public ResponseEntity<String> updateUser(User newUser, Long id){
        try{
            // Тоже самое, что и в методе удаления пользователя

            User user = new User();

            user.setEmail(newUser.getEmail());
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            user.setFullName(newUser.getFullName());
            user.setRole(newUser.getRole() != null ? newUser.getRole() : Role.USER );

            userRepository.save(user);

            return  ResponseEntity.status(200).body("Пользователь успешно изменён!");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Получение пользователя по ID
     */
    public ResponseEntity<String> getUser(Long id){
        try {

            if (userRepository.existsById(id)) {
                return ResponseEntity.status(400).body("Пользователя с таким ID не существует!");
            }

            Optional<User> userOpt = userRepository.findById(id);

            if (userOpt.isPresent()){

                User user = userOpt.get();

                return ResponseEntity.status(200).body(user.toString());

            }
            else {
                return ResponseEntity.status(500).body("Не удалось найти пользователя!");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Удаление пользователя
     */
    public ResponseEntity<String> deleteUser(Long id){
        try{
            /* Продумать ограничение на удаление не своих аккаунтов (есть возможность кинуть запрос на удаление подставив id другого пользователя)
              Можно добавить в передаваемые параметры из контроллера ID пользователя, достав его из активной сессии
             */
            if (userRepository.existsById(id)) {
                return ResponseEntity.status(400).body("Пользователя с таким ID не существует!");
            }

            Optional<User> userOpt = userRepository.findById(id);

            if (userOpt.isPresent()){

                User user = userOpt.get();

                userRepository.delete(user);

                return ResponseEntity.status(200).body("Аккаунт успешно удален!");

            }
            else {
                return ResponseEntity.status(500).body("Не удалось найти пользователя!");
            }



        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
