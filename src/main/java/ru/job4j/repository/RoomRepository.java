package ru.job4j.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.model.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
