package com.prc.domain;
import com.prc.user.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
@Entity public class LearningPlanItem {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private String id; @ManyToOne(optional=false) private AppUser student; @ManyToOne(optional=false) private RoleProfile role; @Column(nullable=false) private String topic; @Column(nullable=false,length=1000) private String task; @Column(nullable=false) private boolean done; private Instant completedAt;
 protected LearningPlanItem(){} public LearningPlanItem(AppUser s,RoleProfile r,String topic,String task){student=s;role=r;this.topic=topic;this.task=task;} public String getId(){return id;} public String getTopic(){return topic;} public String getTask(){return task;} public boolean isDone(){return done;} public void setDone(boolean done){this.done=done;this.completedAt=done?Instant.now():null;}
}
