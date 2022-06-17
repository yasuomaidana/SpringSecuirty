package security.controller.studentcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.config.security.R_STUDENT;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/student")
@R_STUDENT
public class StudentController {

    public static final List<Student> STUDENTS = Arrays.asList(new Student[]{
            new Student("James Bond", 1),
            new Student("Maria Jones",2),
            new Student("Ana Smith",3)
    });

    @GetMapping("{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId){
        return STUDENTS.stream().filter(student -> studentId.equals(student.getStudentId())).findFirst()
                .orElseThrow(()->new IllegalStateException("Student "+studentId+" does not exists"));
    }
}
