package ru.hackaton.hackaton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hackaton.hackaton.entities.Team;
import ru.hackaton.hackaton.entities.User;
import ru.hackaton.hackaton.entities.VM;
import ru.hackaton.hackaton.repositories.TeamRepository;
import ru.hackaton.hackaton.repositories.VmRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;

@Service
public class VMService {

    private static final String BASE_VM_NAME = "Base_Ubuntu"; // Имя базового шаблона ВМ
    private static final String SCRIPT_PATH = "/path/to/setup-vm.sh"; // Путь к скрипту настройки ВМ
    private static final String SSH_USER = "user"; // Стартовый пользователь в базовой ВМ
    private static final String SSH_KEY_PATH = "/path/to/private_key"; // Путь к приватному SSH-ключу (если нужен)
    @Autowired
    private UserService userService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private VmRepository vmRepository;

    // Метод создания новой виртуальной машины
    public ResponseEntity<String > createVM(Long teamID) throws Exception {
        Long adminID = userService.getCurrentUser().getId();

        if (!teamRepository.existsById(teamID)){
            return ResponseEntity.status(400).body("Команды с таким ID не существует!");
        }

        Optional<Team> teamOptional = teamRepository.findById(teamID);

        if (teamOptional.isPresent()) {

            Team team = teamOptional.get();

            if (adminID != team.getLeader().getId()) {
                return ResponseEntity.status(400).body("У вас нет прав на создание ВМ для данной команды!");
            }


            VM vm = new VM();

            vm.setVmName("team-vm-" + team.getName());

            // 1. Клонируем базовый образ
            executeCommand("vboxmanage", "clonevm", BASE_VM_NAME, "--name", vm.getVmName(), "--register");


            // 2. Запускаем новую ВМ
            executeCommand("vboxmanage", "startvm", vm.getVmName(), "--type", "headless");

            Thread.sleep(180000);

            executeCommand("VBoxManage", "guestcontrol", vm.getVmName(), "run", "--username", "user", "--password", "test", "--exe", "/usr/bin/sudo", "--", "sudo", "/usr/sbin/dhclient"
            );

            vm.setTeam(team);

            vmRepository.save(vm);

            return ResponseEntity.status(200).body("Сервер успешно создан! Пароль: " + changePassword(vm));

        }
        else return ResponseEntity.status(500).body("Ошибка при загрузке информации о команде!");
    }


    private String changePassword(VM vm) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "VBoxManage", "guestcontrol", vm.getVmName(),
                "run", "--username", "user", "--password", "test",
                "--exe", "/usr/bin/sudo", "--", "sudo", "/home/user/change_pass.sh"
        );

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String password = reader.readLine();
        process.waitFor();

        return password;
    }


    // Метод перезагрузки виртуальной машины
    public ResponseEntity<String> resetVM(Long teamID) throws Exception {
        try {

            User user = userService.getCurrentUser();

            if (!teamRepository.existsById(teamID)) {
                return ResponseEntity.status(400).body("Не удалось найти команду с таким ID!");
            }

            Optional<Team> teamOptional = teamRepository.findById(teamID);

            if (teamOptional.isPresent()) {

                Team team = teamOptional.get();

                if (team.getLeader() != user){
                    return ResponseEntity.status(400).body("У вас нет прав для управления сервером данной команды!");
                }

                Optional<VM> vmOptional = vmRepository.findByTeam(team);

                if (vmOptional.isPresent()) {
                    VM vm = vmOptional.get();

                    executeCommand("vboxmanage", "controlvm", vm.getVmName(), "reset");

                    return ResponseEntity.status(200).body("Сервер будет перезагружен в течении 2 минут!");
                }
                else return ResponseEntity.status(500).body("Ошибка при загрузке информации о сервере!");
            }
            else return ResponseEntity.status(500).body("Ошибка при загрузке информации о команде!");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // Метод удаления виртуальной машины
    public ResponseEntity<String> deleteVM(Long teamID) throws Exception {
        try {

            User user = userService.getCurrentUser();

            if (!teamRepository.existsById(teamID)) {
                return ResponseEntity.status(400).body("Не удалось найти команду с таким ID!");
            }

            Optional<Team> teamOptional = teamRepository.findById(teamID);

            if (teamOptional.isPresent()) {

                Team team = teamOptional.get();

                if (team.getLeader() != user){
                    return ResponseEntity.status(400).body("У вас нет прав для управления сервером данной команды!");
                }

                Optional<VM> vmOptional = vmRepository.findByTeam(team);

                if (vmOptional.isPresent()) {
                    VM vm = vmOptional.get();

                    executeCommand("vboxmanage", "controlvm", vm.getVmName(), "poweroff");
                    executeCommand("vboxmanage", "unregistervm", "--delete", vm.getVmName());

                    vmRepository.delete(vm);

                    return ResponseEntity.status(200).body("Сервер успешно удален!");
                }
                else return ResponseEntity.status(500).body("Ошибка при загрузке информации о сервере!");
            }
            else return ResponseEntity.status(500).body("Ошибка при загрузке информации о команде!");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // Метод выполнения команд
    private void executeCommand(String... command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
   /*     if (exitCode != 0) {
            throw new RuntimeException("Ошибка выполнения команды: " + Arrays.toString(command));
        }*/
    }

}
