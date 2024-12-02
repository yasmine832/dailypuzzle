package com.example.dailypuzzle.model;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class User implements UserDetails { //interface required by spring security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // db is responsible for generating id
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; //eg "ROLE_USER" "ROLE_ADMIN"


    @Column(name="chosen time", nullable = false)
    private String chosenTime;

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
        return UserDetails.super.isEnabled();
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
        return chosenTime;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }
}
