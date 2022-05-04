package security.security;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    @Test
    void getChildren(){
        String fatherRole = "ADMIN";
        Department role = new Department(fatherRole);
        role.addChildren("TRAINEE");
        role.addChildren("SUPERVISOR");
        role.buildOrganization();
        Department traineeRole = role.getChildren("TRAINEE");
        Department nonExisting = role.getChildren("FINDER");
        assertEquals("TRAINEE",traineeRole.getName(),"Names doesn't matches");
        assertNull(nonExisting,"This shouldn't exist");
    }

    @Test
    void buildOrganization() {
        String fatherRole = "ADMIN";
        Department role = new Department(fatherRole);
        role.addChildren("TRAINEE");
        role.addChildren("SUPERVISOR");
        role.buildOrganization();
        assertEquals("ADMINS",role.getName(),"NAMES doesn't matches");
        assertEquals("'ADMIN','ADMIN_TRAINEE','ADMIN_SUPERVISOR'",role.getSetOfPermission(),"Permissions don't matches");
    }

    @Test
    void buildTwoLevelsOrganization() {
        String fatherRole = "ADMIN";
        Department role = new Department(fatherRole);
        role.addChildren("TRAINEE");
        role.addChildren("SUPERVISOR");
        role.getChildren("TRAINEE").addChildren("MARKETING");
        role.buildOrganization();
        assertEquals("ADMINS",role.getName(),"NAMES doesn't matches");
        assertEquals("'ADMIN','ADMIN_TRAINEE','ADMIN_TRAINEE_MARKETING','ADMIN_SUPERVISOR'",role.getSetOfPermission(),"Permissions don't matches");
        Department trainees = role.getChildren("TRAINEES");
        assertNotNull(trainees,"Trainees should exist");
    }
}