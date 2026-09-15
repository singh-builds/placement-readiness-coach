package com.prc.domain;
import jakarta.persistence.*;
@Entity public class RoleProfile {
 @Id private String id; @Column(nullable=false) private String name; @Column(nullable=false,length=1000) private String summary; @Column(nullable=false) private String icon;
 protected RoleProfile(){} public RoleProfile(String id,String name,String summary,String icon){this.id=id;this.name=name;this.summary=summary;this.icon=icon;}
 public String getId(){return id;} public String getName(){return name;} public String getSummary(){return summary;} public String getIcon(){return icon;}
}
