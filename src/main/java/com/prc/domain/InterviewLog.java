package com.prc.domain;
import com.prc.user.AppUser;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity public class InterviewLog {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private String id; @ManyToOne(optional=false) private AppUser student; @Column(nullable=false) private String roleName; @Column(nullable=false) private LocalDate interviewDate; @Column(nullable=false) private int rating; @Column(nullable=false,length=2000) private String notes;
 protected InterviewLog(){} public InterviewLog(AppUser s,String roleName,LocalDate date,int rating,String notes){student=s;this.roleName=roleName;interviewDate=date;this.rating=rating;this.notes=notes;} public String getId(){return id;} public String getRoleName(){return roleName;} public LocalDate getInterviewDate(){return interviewDate;} public int getRating(){return rating;} public String getNotes(){return notes;}
}
