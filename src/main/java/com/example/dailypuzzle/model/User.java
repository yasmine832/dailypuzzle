package com.example.dailypuzzle.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")// AZEERT
public class User implements UserDetails { //interface required by spring security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // db is responsible for generating id
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "ROLE_USER"; //by default


    @Column(name="chosen time", nullable = false)
    private LocalTime chosenTime; //format "HH:MM" TODO

  //foreign key
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //removed from list, removed from db
    private List<SolvedPuzzle> solvedPuzzles;

    //user cans olve multiple puzzles and this entity tracks progress


    public User() {
    }


    public List<SolvedPuzzle> getSolvedPuzzles() {
        return solvedPuzzles;
    }

    public void setSolvedPuzzles(List<SolvedPuzzle> solvedPuzzles) {
        this.solvedPuzzles = solvedPuzzles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        //return UserDetails.super.isEnabled();
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

    }
    private boolean enabled = true;


    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert the role string into a GrantedAuthority
        return Collections.singletonList(new SimpleGrantedAuthority(role)); // "ROLE_USER", "ROLE_ADMIN"
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;

    }

    public LocalTime getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(LocalTime chosenTime) {
        this.chosenTime = chosenTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
