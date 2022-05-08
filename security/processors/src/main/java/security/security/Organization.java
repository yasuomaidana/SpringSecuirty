package security.security;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class Organization {
    @Getter
    private final ArrayList<Department> departments = new ArrayList<>();
    private final String prefix;
    @Setter
    private String nameSplitter = "_";
    public Organization(String prefix){
        this.prefix=prefix;
    }
    public void add(String permissions) {
        Queue<String> permissionStack =  Arrays.stream(permissions.split(nameSplitter)).collect(Collectors.toCollection(LinkedList::new));
        String permission = permissionStack.poll();
        Department departmentRoot = departments.stream().filter(organization -> organization.getName().equals(permission)).findFirst().orElse(null);
        if(departmentRoot!=null){
            add(permissionStack,departmentRoot);
        }else {
            departments.add(new Department(permission));
            departmentRoot = departments.stream().filter(organization -> organization.getName().equals(permission)).findFirst().orElse(null);
            add(permissionStack,departmentRoot);
        }
    }

    public Department getDepartment(String rootName){
        return departments.stream()
                .filter(rootNode->
                        rootNode.getName().equals(rootName)||rootNode.getName().equals(rootName+"S"))
                .findFirst().orElse(null);
    }

    private void add(Queue<String> permissionStack, Department organizationNode){
        String newPermission = permissionStack.poll();
        if(newPermission==null) return;
        Department newOrganizationNode = organizationNode.getChildren(newPermission);
        if(newOrganizationNode == null){
            organizationNode.addChildren(newPermission);
            newOrganizationNode = organizationNode.getChildren(newPermission);
        }
        add(permissionStack,newOrganizationNode);
    }

    public void build() {
        Department.setPrefix(prefix);
        departments.forEach(Department::buildOrganization);
    }
    public void setDepartmentJoiner(String organizationJoiner) {
        Department.joiner = organizationJoiner;
    }
}

