package com.prc.auth;
import com.prc.security.JwtService;
import com.prc.user.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AppUserRepository users; private final PasswordEncoder passwords; private final JwtService jwt; private final PasswordResetTokenRepository resetTokens;
 public AuthController(AppUserRepository users,PasswordEncoder passwords,JwtService jwt,PasswordResetTokenRepository resetTokens){this.users=users;this.passwords=passwords;this.jwt=jwt;this.resetTokens=resetTokens;}
 @PostMapping("/register") public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest r){ if(users.findByEmail(r.email()).isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).build(); AppUser u=users.save(new AppUser(r.name(),r.email(),passwords.encode(r.password()),UserRole.STUDENT)); return ResponseEntity.status(HttpStatus.CREATED).body(response(u)); }
 @PostMapping("/login") public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest r){ return users.findByEmail(r.email().toLowerCase()).filter(u->passwords.matches(r.password(),u.getPasswordHash())).map(this::response).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); }
 @PostMapping("/forgot-password") public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest r){ users.findByEmail(r.email().toLowerCase()).ifPresent(user->{String raw=UUID.randomUUID().toString()+UUID.randomUUID();resetTokens.save(new PasswordResetToken(user,sha256(raw),Instant.now().plusSeconds(900))); /* Replace this controlled log with a mail-provider delivery adapter. */ System.out.println("DEVELOPMENT ONLY password reset token for "+user.getEmail()+": "+raw);}); return ResponseEntity.accepted().build(); }
 @PostMapping("/reset-password") public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest r){return resetTokens.findByTokenHash(sha256(r.token())).filter(PasswordResetToken::usable).map(t->{t.getUser().setPasswordHash(passwords.encode(r.newPassword()));t.consume();resetTokens.save(t);users.save(t.getUser());return ResponseEntity.noContent().<Void>build();}).orElseGet(()->ResponseEntity.status(HttpStatus.BAD_REQUEST).build());}
 private String sha256(String input){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
 private AuthResponse response(AppUser u){return new AuthResponse(jwt.issue(u),u.getId(),u.getName(),u.getEmail(),u.getRole());}
 public record RegisterRequest(@NotBlank @Size(max=100) String name,@NotBlank @Email String email,@NotBlank @Size(min=12,max=72) String password){}
 public record LoginRequest(@NotBlank @Email String email,@NotBlank String password){}
 public record ForgotPasswordRequest(@NotBlank @Email String email){}
 public record ResetPasswordRequest(@NotBlank String token,@NotBlank @Size(min=12,max=72) String newPassword){}
 public record AuthResponse(String token,String userId,String name,String email,UserRole role){}
}
