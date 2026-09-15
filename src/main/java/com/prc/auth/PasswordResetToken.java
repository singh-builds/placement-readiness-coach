package com.prc.auth;
import com.prc.user.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
@Entity public class PasswordResetToken {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private String id; @ManyToOne(optional=false) private AppUser user; @Column(nullable=false,unique=true) private String tokenHash; @Column(nullable=false) private Instant expiresAt; private Instant usedAt;
 protected PasswordResetToken(){} PasswordResetToken(AppUser user,String tokenHash,Instant expiresAt){this.user=user;this.tokenHash=tokenHash;this.expiresAt=expiresAt;} public AppUser getUser(){return user;} public Instant getExpiresAt(){return expiresAt;} public boolean usable(){return usedAt==null&&expiresAt.isAfter(Instant.now());} public void consume(){usedAt=Instant.now();}
}
