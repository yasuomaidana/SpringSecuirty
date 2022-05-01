package security.security;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoleOrPermissionTest {


    @Test
    void containedSingleName() {
        String singleRole = "STUDENT";
        RoleOrPermission role = new RoleOrPermission(singleRole);
        ArrayList<String> roleName = role.containedNames();
        assertEquals(1,roleName.size(),"Same length failed");
        assertEquals(singleRole,roleName.get(0),"Must have the same name");
    }

    @Test
    void containedDoubleLevelName(){
        String fatherRole = "ADMIN";
        RoleOrPermission role = new RoleOrPermission(fatherRole);
        role.addChildren("TRAINEE");
        ArrayList<String> roles = role.containedNames();
        assertEquals(3,roles.size(),"Same length failed");
    }

    @Test
    void containedDoubleLevelNames(){
        String fatherRole = "ADMIN";
        RoleOrPermission role = new RoleOrPermission(fatherRole);
        role.addChildren("TRAINEE");
        role.addChildren("SUPERVISOR");
        ArrayList<String> roles = role.containedNames();
        assertEquals(4,roles.size(),"Same length failed");
    }

    @Test
    void containedThreeLevelNames(){
        String fatherRole = "ADMIN";
        RoleOrPermission role = new RoleOrPermission(fatherRole);
        role.addChildren("TRAINEE");
        role.addChildren("SUPERVISOR");
        ArrayList<String> roles = role.containedNames();
        assertEquals(4,roles.size(),"Same length failed");
    }

    @Test
    void getChildren(){
        String fatherRole = "ADMIN";
        RoleOrPermission role = new RoleOrPermission(fatherRole);
        role.addChildren("TRAINEE");
        role.addChildren("SUPERVISOR");
        RoleOrPermission traineeRole = role.getChildren("TRAINEE");
        RoleOrPermission nonExisting = role.getChildren("FINDER");
        assertEquals(traineeRole.name,"TRAINEE","Names doesn't matches");
        assertNull(nonExisting,"This shouldn't exist");
    }
}