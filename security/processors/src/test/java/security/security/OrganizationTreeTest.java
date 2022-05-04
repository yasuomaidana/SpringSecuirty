package security.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationTreeTest {

    @Test
    void add() {
        OrganizationTree organization = new OrganizationTree("R_");

        String role1 = "ADMIN_TRAINEE";
        String role2 = "ADMIN_TRAINEE_MARKETING";
        String role3 = "STUDENT";

        organization.add(role1);
        organization.add(role2);
        organization.add(role3);

        organization.build();

        assertNotNull(organization.getRoot("STUDENT"),"Student should exist");
        assertNotNull(organization.getRoot("ADMIN"),"Admin should exist");
        assertNull(organization.getRoot("PROFESSOR"),"Professor shouldn't exists");

        Organization admins = organization.getRoot("ADMIN");
        assertEquals(2,admins.getChildrenPermissions().size());
    }
}