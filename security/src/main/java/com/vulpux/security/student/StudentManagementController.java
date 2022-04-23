package com.vulpux.security.student;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/student")
public class StudentManagementController {

    public static final List<Student> STUDENTS = Arrays.asList(new Student("James Bond", 1),
            new Student("Maria Jones",2),
            new Student("Ana Smith",3));

    public List<Student> getAllStudents(){
        return STUDENTS;
    }
    
    public void registerNewStudent(Student student){
        System.out.println(student);
    }

    public void deleteStudent(Integer studentId){
        System.out.println(studentId);
    }

    public void updateStudent(Integer studentId, Student student){
        System.out.println(student);
    }
}
