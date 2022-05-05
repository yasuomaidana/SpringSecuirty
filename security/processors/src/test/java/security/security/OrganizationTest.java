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
}