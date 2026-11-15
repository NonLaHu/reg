package edu.utycc.reg.service;

import edu.utycc.reg.dto.StudentDashboardDto;
import edu.utycc.reg.entity.Student;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentDashboardService {

    private final StudentRepository studentRepository;

    public StudentDashboardService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentDashboardDto getStudentDashboard(User user) {
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentDashboardDto dto = new StudentDashboardDto();
        dto.setName(student.getName());
        dto.setRollNo(student.getRollNo());
        dto.setMajor(student.getMajor() != null ? student.getMajor().getName() : "");
        dto.setSemester(student.getSemester() != null ? student.getSemester().getYearLabel() : "");
        dto.setEnrolled(student.isEnrolled());
        dto.setBirthDate(student.getBirthDate());

        return dto;
    }
}
