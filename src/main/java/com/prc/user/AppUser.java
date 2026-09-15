package com.prc.user;
import jakarta.persistence.*;
@Entity @Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class AppUser {
 @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
 @Column(nullable=false) private String name;
 @Column(nullable=false) private String email;
 @Column(nullable=false) private String passwordHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private UserRole role;
 protected AppUser(){} public AppUser(String name,String email,String passwordHash,UserRole role){this.name=name;this.email=email.toLowerCase();this.passwordHash=passwordHash;this.role=role;}
 public String getId(){return id;} public String getName(){return name;} public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;} public UserRole getRole(){return role;}
 public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;}
}
