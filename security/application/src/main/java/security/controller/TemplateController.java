package security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Controller
@RequestMapping("/")
@CrossOrigin(origins = {"*"})
public class TemplateController {
    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/login2")
    @ResponseBody
    public String getLogin2(){
        return "Hi";
    }

    @GetMapping("/login3")
    public ResponseEntity<String[]> getLogin3(){
        return status(OK).body(new String[]{"Other way", "Bye"});
    }

    @GetMapping("/courses")
    public String getCourses(){
        return "courses";
    }

    @GetMapping("/failed")
    public ResponseEntity<String> failedLogin(){
        return status(FORBIDDEN).body("FORBIDDEN");
    }
}
