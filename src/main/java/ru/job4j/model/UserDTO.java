package ru.job4j.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDTO {

    @NotBlank(message = "Username must not be empty")
    @Size(min = 2, max = 255, message = "Username length should be between 2 and 255")
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 5, max = 255, message = "Password length should be greater than 5 and less than 255")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
