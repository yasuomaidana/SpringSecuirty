package security.security;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {

    @Test
    void getChildren(){
        String fatherRole = "ADMIN";
        Organization role = new Organization(fatherRole);
        role.addChildren("TRAINEE");
        role.addChildren("SUPERVISOR");
        role.buildOrganization();
        Organization traineeRole = role.getChildren("TRAINEE");
        Organization nonExisting = role.getChildren("FINDER");
        assertEquals("TRAINEE",traineeRole.getName(),"Names doesn't matches");
        assertNull(nonExisting,"This shouldn't exist");
    }

    @Test
    void buildOrganization() {
        String fatherRole = "ADMIN";
        Organization role = new Organization(fatherRole);
        role.addChildren("TRAINEE");
        role.addChildren("SUPERVISOR");
        role.buildOrganization();
        assertEquals("ADMINS",role.getName(),"NAMES doesn't matches");
    }
}