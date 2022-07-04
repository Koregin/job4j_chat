package ru.job4j.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Room;
import ru.job4j.repository.RoomRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping("/")
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/{id}")
    public Room findById(@PathVariable("id") int roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Room with room id = " + roomId + " not found"
                ));
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@Valid @RequestBody Room room) {
        if (room.getId() != 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "redundant param: id MUST be 0");
        }
        if (room.getName() == null || room.getName().trim().length() == 0) {
            throw new NullPointerException("missed param: name");
        }
        return ResponseEntity.ok(roomRepository.save(room));
    }

    @PutMapping("/")
    public ResponseEntity<Room> update(@Valid @RequestBody Room room) {
        if (room.getId() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "missed param: id");
        }
        if (room.getName() == null || room.getName().trim().length() == 0) {
            throw new NullPointerException("missed param: name");
        }
        roomRepository.save(room);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int roomId) {
        try {
            roomRepository.deleteById(roomId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "room with roomId = " + roomId + " not found");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/")
    public ResponseEntity<Room> patch(@Valid @RequestBody Room room) {
        if (room.getId() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "missed param: id");
        }
        Room persistRoom = roomRepository.findById(room.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Room with room id = " + room.getId() + " not found"
                ));
        if (room.getName() != null && room.getName().trim().length() > 0) {
            persistRoom.setName(room.getName());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(roomRepository.save(persistRoom));
    }
}
