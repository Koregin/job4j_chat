package ru.job4j.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Message;
import ru.job4j.model.Person;
import ru.job4j.repository.UserRepository;
import ru.job4j.service.MessageService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public List<Message> getAllMessages() {
        return messageService.getAll();
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        if (message.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be 0", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getMessage() == null || message.getMessage().trim().length() == 0) {
            return new ResponseEntity("missed param: message", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getRoom().getId() == 0) {
            return new ResponseEntity("missed param: roomId", HttpStatus.NOT_ACCEPTABLE);
        }
        Person requestUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        message.setPersonId(requestUser.getId());
        return ResponseEntity.ok(messageService.save(message));
    }

    @PutMapping("/")
    public ResponseEntity<Message> update(@RequestBody Message message) {
        if (message.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getMessage() == null || message.getMessage().trim().length() == 0) {
            return new ResponseEntity("missed param: message", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getRoom() == null) {
            return new ResponseEntity("missed param: room", HttpStatus.NOT_ACCEPTABLE);
        }
        Person requestUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (requestUser.getId() != message.getPersonId()) {
            return new ResponseEntity("This user is not author this message. Update forbidden!", HttpStatus.NOT_ACCEPTABLE);
        }
        messageService.save(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int messageId) {
        try {
            Message message = messageService.findById(messageId);
            Person requestUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            if (!Objects.equals(requestUser.getId(), message.getPersonId())) {
                return new ResponseEntity("This user is not author this message. Delete forbidden!", HttpStatus.NOT_ACCEPTABLE);
            }
            messageService.deleteById(messageId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("messageId = " + messageId + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
