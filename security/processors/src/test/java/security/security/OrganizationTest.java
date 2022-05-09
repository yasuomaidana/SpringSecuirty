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
        organization.buildPermissions();
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

        Department permissions = organization.getDepartments().get(0);
        assertEquals(1,permissions.getLevel());
        assertEquals("'student:read','student:write'",permissions.getSetOfPermission());
        assertEquals(2,permissions.getChildrenPermissions().get(0).getLevel());
        assertEquals("'student:read'",permissions.getChildrenPermissions().get(0).getSetOfPermission());
        assertEquals("'student:write'",permissions.getChildrenPermissions().get(1).getSetOfPermission());
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

        Department permissions = organization.getDepartments().get(0);
        assertEquals(1,permissions.getLevel());
        assertEquals("'student:read','student:write'",permissions.getSetOfPermission());
        assertEquals(2,permissions.getChildrenPermissions().get(0).getLevel());
        assertEquals("'student:read'",permissions.getChildrenPermissions().get(0).getSetOfPermission());
        assertEquals("'student:write'",permissions.getChildrenPermissions().get(1).getSetOfPermission());

        permissions = organization.getDepartments().get(1);
        assertEquals(1,permissions.getLevel());
        assertEquals("'admin:write'",permissions.getSetOfPermission());
    }

    @Test
    void addFourPermissionThreeLevelsTwoDifferentDepartments(){
        Organization organization = new Organization("P_");
        organization.setNameSplitter(":");
        organization.setDepartmentJoiner(":");

        String permission1 = "student:read";
        String permission2 = "student:write";
        String permission4 = "student:homework:read";
        String permission3 = "admin:write";

        organization.add(permission1);
        organization.add(permission2);
        organization.add(permission3);
        organization.add(permission4);
        organization.buildPermissions();

        Department permissions = organization.getDepartments().get(0);
        assertEquals(1,permissions.getLevel());
        assertEquals("'student:read','student:write','student:homework:read'",permissions.getSetOfPermission());
        assertEquals(2,permissions.getChildrenPermissions().get(0).getLevel());
        assertEquals("'student:read'",permissions.getChildrenPermissions().get(0).getSetOfPermission());
        assertEquals("'student:write'",permissions.getChildrenPermissions().get(1).getSetOfPermission());

        permissions = organization.getDepartments().get(1);
        assertEquals(1,permissions.getLevel());
        assertEquals("'admin:write'",permissions.getSetOfPermission());
    }

    @Test
    void addFivePermissionThreeLevelsTwoDifferentDepartments(){
        Organization organization = new Organization("P_");
        organization.setNameSplitter(":");
        organization.setDepartmentJoiner(":");

        String permission1 = "student:read";
        String permission2 = "student:write";
        String permission4 = "student:homework:read";
        String permission5 = "student:homework:write";
        String permission3 = "admin:write";

        organization.add(permission1);
        organization.add(permission2);
        organization.add(permission3);
        organization.add(permission4);
        organization.add(permission5);
        organization.buildPermissions();

        Department permissions = organization.getDepartments().get(0);
        assertEquals(1,permissions.getLevel());
        assertEquals("'student:read','student:write','student:homework:read','student:homework:write'",permissions.getSetOfPermission());
        assertEquals(2,permissions.getChildrenPermissions().get(0).getLevel());
        assertEquals("'student:read'",permissions.getChildrenPermissions().get(0).getSetOfPermission());
        assertEquals("'student:write'",permissions.getChildrenPermissions().get(1).getSetOfPermission());

        permissions = organization.getDepartments().get(1);
        assertEquals(1,permissions.getLevel());
        assertEquals("'admin:write'",permissions.getSetOfPermission());
    }

    @Test
    void addSixPermissionThreeLevelsTwoDifferentDepartments(){
        Organization organization = new Organization("P_");
        organization.setNameSplitter(":");
        organization.setDepartmentJoiner(":");

        String permission1 = "student:read";
        String permission2 = "student:write";
        String permission4 = "student:homework:read";
        String permission5 = "student:homework:write";
        String permission3 = "admin:write";
        String permission6 = "admin:read";

        organization.add(permission1);
        organization.add(permission2);
        organization.add(permission3);
        organization.add(permission4);
        organization.add(permission5);
        organization.add(permission6);
        organization.buildPermissions();

        Department permissions = organization.getDepartments().get(0);
        assertEquals(1,permissions.getLevel());
        assertEquals("'student:read','student:write','student:homework:read','student:homework:write'",permissions.getSetOfPermission());
        assertEquals(2,permissions.getChildrenPermissions().get(0).getLevel());
        assertEquals("'student:read'",permissions.getChildrenPermissions().get(0).getSetOfPermission());
        assertEquals("'student:write'",permissions.getChildrenPermissions().get(1).getSetOfPermission());

        permissions = organization.getDepartments().get(1);
        assertEquals(1,permissions.getLevel());
        assertEquals("'admin:write','admin:read'",permissions.getSetOfPermission());
    }

    @Test()
    public void expectErrorWhenDuplicatedPermissions(){
        Organization organization = new Organization("Fail");
        String duplicated = "DUPLICATED";
        organization.add(duplicated);
        String errorMessage = null;
        try{
            organization.add(duplicated);
        }catch (RuntimeException re){
            errorMessage = re.getMessage();
        }
        assertEquals("Permission duplicated",errorMessage);
    }
}