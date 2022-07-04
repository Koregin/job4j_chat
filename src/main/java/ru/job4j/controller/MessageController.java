package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Message;
import ru.job4j.model.Person;
import ru.job4j.repository.UserRepository;
import ru.job4j.service.MessageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/message")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getSimpleName());
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public MessageController(MessageService messageService, UserRepository userRepository, ObjectMapper objectMapper) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages =  messageService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable("id") int messageId) {
        Message message = messageService.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Message with message id = " + messageId + " not found"
                ));
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        if (message.getId() != 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "redundant param: id MUST be 0");
        }
        if (message.getMessage() == null || message.getMessage().trim().length() == 0) {
            throw new NullPointerException("missed param: message");
        }
        if (message.getRoom() == null || message.getRoom().getId() == 0) {
            throw new NullPointerException("missed param: room or roomId = 0");
        }
        Person requestUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        message.setPersonId(requestUser.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(messageService.save(message));
    }

    @PutMapping("/")
    public ResponseEntity<Message> update(@RequestBody Message message) {
        if (message.getId() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "missed param: id");
        }
        if (message.getMessage() == null || message.getMessage().trim().length() == 0) {
            throw new NullPointerException("missed param: message");
        }
        if (message.getRoom() == null) {
            throw new NullPointerException("missed param: room");
        }
        Person requestUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (requestUser.getId() != message.getPersonId()) {
            throw new IllegalArgumentException("This user is not author this message. Update forbidden!");
        }
        messageService.save(message);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int messageId) {
        Message message = messageService.findById(messageId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "message with message id = " + messageId + " not found"
        ));
        Person requestUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (!Objects.equals(requestUser.getId(), message.getPersonId())) {
            throw new IllegalArgumentException("This user is not author this message. Delete forbidden!");
        }
        messageService.deleteById(messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }

}
