package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Person;
import ru.job4j.model.Role;
import ru.job4j.model.UserDTO;
import ru.job4j.repository.RoleRepository;
import ru.job4j.repository.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleRepository.findByName("ROLE_USER"));
        person.setRoles(userRoles);
        userRepository.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return userRepository.findAll();
    }

    @PatchMapping("/")
    public ResponseEntity<Void> changeUserPassword(@Valid @RequestBody UserDTO userDTO) {
        Person person = userRepository.findByUsername(userDTO.getUsername());
        if (person == null) {
            throw new NullPointerException("User " + userDTO.getUsername() + " not found");
        }
        if (userDTO.getPassword() != null || userDTO.getPassword().trim().length() > 0) {
            person.setPassword(encoder.encode(userDTO.getPassword()));
        }
        userRepository.save(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
