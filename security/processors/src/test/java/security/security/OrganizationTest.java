package security.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {

    @Test
    void add() {
        Organization organization = new Organization("R_");

        String role1 = "ADMIN_TRAINEE";
        String role2 = "ADMIN_TRAINEE_MARKETING";
        String role3 = "STUDENT";

        organization.add(role1);
        organization.add(role2);
        organization.add(role3);

        organization.build();

        assertNotNull(organization.getDepartment("STUDENT"),"Student should exist");
        assertNotNull(organization.getDepartment("ADMIN"),"Admin should exist");
        assertNull(organization.getDepartment("PROFESSOR"),"Professor shouldn't exists");

        Department admins = organization.getDepartment("ADMIN");
        assertEquals(2,admins.getChildrenPermissions().size());
    }

    @Test
    void addSinglePermission(){
        Organization organization = new Organization("P_");
        organization.setNameSplitter(":");
        organization.setDepartmentJoiner(":");

        String permission1 = "student:read";
        organization.add(permission1);
        organization.build();
        organization.buildPermissions();
        organization.getDepartments();
        Department permission = organization.getDepartments().get(0);
        assertEquals("P_STUDENTS_READ",permission.getName());
        assertEquals("'student:read'",permission.getSetOfPermission());
        assertEquals(1,permission.getLevel());
    }

    @Test
    void addTwoPermissionSameDepartment(){
        Organization organization = new Organization("P_");
        organization.setNameSplitter(":");
        organization.setDepartmentJoiner(":");

        String permission1 = "student:read";
        String permission2 = "student:write";
        organization.add(permission1);
        organization.add(permission2);
        organization.buildPermissions();

        Department permission = organization.getDepartments().get(0);
        permission.getLevel();
    }

    @Test
    void addThreePermissionDifferentDepartment(){
        Organization organization = new Organization("P_");
        organization.setNameSplitter(":");
        organization.setDepartmentJoiner(":");

        String permission1 = "student:read";
        String permission2 = "student:write";
        String permission3 = "admin:write";
        organization.add(permission1);
        organization.add(permission2);
        organization.add(permission3);
        organization.buildPermissions();

        Department permission = organization.getDepartments().get(0);
        permission.getLevel();
    }
}