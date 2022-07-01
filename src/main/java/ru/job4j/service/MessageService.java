package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.Message;
import ru.job4j.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public List<Message> getAll() {
        return repository.findAll();
    }

    public Message save(Message message) {
        return repository.save(message);
    }

    public void deleteById(int messageId) {
        repository.deleteById(messageId);
    }

    public Optional<Message> findById(int messageId) {
        return repository.findById(messageId);
    }
}
