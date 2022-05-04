package security.security;

import org.junit.jupiter.api.Test;

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


    }
}