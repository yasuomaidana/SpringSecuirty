package security.student;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import security.security.P_STUDENTS;
import security.security.R_ADMINS;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/student") // for only one role use "hasRole"
@R_ADMINS
public class StudentManagementController {

    public static final List<Student> STUDENTS = Arrays.asList(new Student("James Bond", 1),
            new Student("Maria Jones",2),
            new Student("Ana Smith",3));

    @GetMapping
    public List<Student> getAllStudents(){
        return STUDENTS;
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student){
        System.out.println(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){
        System.out.println(studentId);
    }

    @PutMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student){
        System.out.println(String.format("%s %s",student, studentId));
    }
}
