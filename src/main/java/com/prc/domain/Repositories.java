package com.prc.domain;
import com.prc.user.AppUser;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
interface RoleProfileRepository extends JpaRepository<RoleProfile,String>{}
interface RoleSkillRepository extends JpaRepository<RoleSkill,String>{List<RoleSkill> findByRoleId(String roleId);}
interface DiagnosticAttemptRepository extends JpaRepository<DiagnosticAttempt,String>{List<DiagnosticAttempt> findByStudentOrderByCompletedAtDesc(AppUser student);}
interface LearningPlanItemRepository extends JpaRepository<LearningPlanItem,String>{List<LearningPlanItem> findByStudentOrderByTopic(AppUser student); Optional<LearningPlanItem> findByIdAndStudent(String id,AppUser student);}
interface InterviewLogRepository extends JpaRepository<InterviewLog,String>{List<InterviewLog> findByStudentOrderByInterviewDateDesc(AppUser student);}
interface AuditEventRepository extends JpaRepository<AuditEvent,String>{}
