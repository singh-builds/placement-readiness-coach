package com.prc.auth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,String>{Optional<PasswordResetToken> findByTokenHash(String tokenHash);}
