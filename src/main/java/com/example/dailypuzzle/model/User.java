package com.example.dailypuzzle.model;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class User implements UserDetails { //interface required by spring security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // db is responsible for generating id
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid format")
    @Column(unique = true, nullable = false)
    private String email; //make unque todo

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;


    @Transient //not saved in db
    @NotBlank(message = "Password confirmation is required")
    private String matchingPassword;

    @Column(nullable = false)
    private String role = "ROLE_USER"; //by default


    @Column(name = "chosen time", nullable = false)
    private String chosenTime = "00:00"; //format "HH:MM"

  //foreign key
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //removed from list, removed from db
    private List<SolvedPuzzle> solvedPuzzles;

    //user cans olve multiple puzzles and this entity tracks progress


    public User() {
    }


    public @NotBlank(message = "Password confirmation is required") String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(@NotBlank(message = "Password confirmation is required") String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public List<SolvedPuzzle> getSolvedPuzzles() {
        return solvedPuzzles;
    }

    public void setSolvedPuzzles(List<SolvedPuzzle> solvedPuzzles) {
        this.solvedPuzzles = solvedPuzzles;
    }

    public void getSolvedPuzzles(SolvedPuzzle solvedPuzzle) {
        this.solvedPuzzles.add(solvedPuzzle);
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

    public String getChosenTime() {
        return chosenTime == null ? "00:00" : chosenTime;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }


    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
