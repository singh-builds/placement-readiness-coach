package com.prc.domain;
import com.prc.user.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
@Entity public class AuditEvent { @Id @GeneratedValue(strategy=GenerationType.UUID) private String id; @ManyToOne private AppUser actor; @Column(nullable=false) private String action; @Column(nullable=false) private String entityType; @Column(nullable=false) private String entityId; @Column(nullable=false) private Instant occurredAt=Instant.now(); protected AuditEvent(){} public AuditEvent(AppUser a,String action,String type,String entityId){actor=a;this.action=action;entityType=type;this.entityId=entityId;} }
