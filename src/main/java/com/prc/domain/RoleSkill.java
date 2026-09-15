package com.prc.domain;
import jakarta.persistence.*;
@Entity @Table(uniqueConstraints=@UniqueConstraint(columnNames={"role_id","name"})) public class RoleSkill {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private String id; @ManyToOne(optional=false) @JoinColumn(name="role_id") private RoleProfile role; @Column(nullable=false) private String name; @Column(nullable=false) private int target;
 protected RoleSkill(){} public RoleSkill(RoleProfile role,String name,int target){this.role=role;this.name=name;this.target=target;} public String getName(){return name;} public int getTarget(){return target;}
}
