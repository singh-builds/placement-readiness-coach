package com.prc.domain;
import com.prc.user.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
@Entity public class DiagnosticAttempt {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private String id; @ManyToOne(optional=false) private AppUser student; @ManyToOne(optional=false) private RoleProfile role; @Column(nullable=false) private int score; @Column(nullable=false,length=10000) private String topicScoresJson; @Column(nullable=false) private Instant completedAt=Instant.now();
 protected DiagnosticAttempt(){} public DiagnosticAttempt(AppUser s,RoleProfile r,int score,String json){student=s;role=r;this.score=score;topicScoresJson=json;} public String getId(){return id;} public RoleProfile getRole(){return role;} public int getScore(){return score;} public String getTopicScoresJson(){return topicScoresJson;} public Instant getCompletedAt(){return completedAt;}
}
