package com.vulpux.security.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Student {
    private final String studentName;
    private final Integer studentId;
}
