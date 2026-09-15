package com.prc.security;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class JwtServiceTest {
 @Test void rejectsAnUnsafeSigningSecret(){ assertThrows(IllegalStateException.class, () -> new JwtService("too-short",30)); }
}
