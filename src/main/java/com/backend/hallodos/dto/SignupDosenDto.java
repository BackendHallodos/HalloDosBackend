package com.backend.hallodos.dto;

public class SignupDosenDto {

    private String username;
    private String email;
    private String password;
    private String sequrity_answer;

    public String getUsername() {
        return username;
    }

    public String getSequrity_answer() {
        return sequrity_answer;
    }

    public void setSequrity_answer(String sequrity_answer) {
        this.sequrity_answer = sequrity_answer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
