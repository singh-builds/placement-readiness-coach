package com.prc.domain;
import com.prc.user.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class DemoData {
 @Bean CommandLineRunner seed(RoleProfileRepository roles,RoleSkillRepository skills,AppUserRepository users,PasswordEncoder encoder){return args->{
  if(roles.count()==0){
   RoleProfile java=new RoleProfile("backend-java","Backend Developer (Java)","Server-side APIs, data modelling and system design with Java/Spring.","bi-hdd-stack"); roles.save(java);
   for(Object[] x:new Object[][]{{"Core Java & OOP",85},{"Spring Boot",80},{"SQL & Data Modelling",80},{"REST API Design",75},{"DSA",80},{"System Design Basics",65}})skills.save(new RoleSkill(java,(String)x[0],(Integer)x[1]));
   RoleProfile react=new RoleProfile("frontend-react","Frontend Developer (React)","Component-driven UI, state management and web performance.","bi-layout-text-window"); roles.save(react);
   for(Object[] x:new Object[][]{{"JavaScript (ES6+)",85},{"React",80},{"HTML/CSS",80},{"REST/API Integration",70},{"DSA",65},{"Testing (Jest/RTL)",55}})skills.save(new RoleSkill(react,(String)x[0],(Integer)x[1]));
  }
  createUser(users,encoder,"Aditi Rao","student@placementcoach.local",UserRole.STUDENT); createUser(users,encoder,"Placement Officer","officer@placementcoach.local",UserRole.PLACEMENT_OFFICER); createUser(users,encoder,"Career Trainer","trainer@placementcoach.local",UserRole.TRAINER);
 };}
 private void createUser(AppUserRepository users,PasswordEncoder encoder,String name,String email,UserRole role){if(users.findByEmail(email).isEmpty())users.save(new AppUser(name,email,encoder.encode("ChangeMe123!"),role));}
}
