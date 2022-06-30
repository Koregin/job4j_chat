package ru.job4j.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.model.Person;

public interface UserRepository extends JpaRepository<Person, Integer> {

    @Query("select p from Person p join fetch p.roles where p.username = :fUsername")
    Person findByUsername(@Param("fUsername") String username);
}
