package com.movieauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank(message = "Please provide your name")
    private String name;

    @NotNull
    private Integer age;

    @NotBlank
    private String gender; // e.g., "Male", "Female", "Other"

    @NotBlank
    private String profession;

    @NotBlank(message = "Please provide your email")
    @Email(message = "Please provide a valid email address")

    @Column(unique = true)
    private String email;

    @NotBlank(message = "Please provide your password")
    private String password;

}