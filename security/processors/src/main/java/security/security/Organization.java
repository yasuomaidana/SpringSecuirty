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
    Organization(String prefix){
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

class Department {
    @Getter
    private String name;
    @Getter
    private String pathName;
    @Getter
    private String setOfPermission;
    @Setter
    static String joiner ="_";
    @Getter
    private final ArrayList<Department> childrenPermissions =new ArrayList<>();
    Department(String name){
        this.name = name;
    }
    @Setter
    private static String prefix="";
    @Getter
    private int level;

    public void addChildren(String childrenName){
        this.childrenPermissions.add(new Department(childrenName));
    }

    private void addChildren(Department children){
        this.childrenPermissions.add(0,children);
    }

    private void buildPermissions(String fatherPath,int level){
        this.level=level;
        if(childrenPermissions.size()>0){
            setOfPermission ="";
            childrenPermissions.stream().findFirst().ifPresent(masterChild->masterChild.buildPermissions(fatherPath,this.level+1));
            String childFatherPath = fatherPath+name.substring(0,name.length()-1)+joiner;
            childrenPermissions.stream().skip(1).forEach(children->children.buildPermissions(childFatherPath,this.level+1));
            childrenPermissions
                    .forEach(children-> setOfPermission+=children.setOfPermission+",");
            setOfPermission = setOfPermission.substring(0,setOfPermission.length()-1);
        } else {
            setOfPermission = String.format("'%s'",fatherPath+name);
        }
    }

    private void prepareOrganization(String fatherPath){
        if(childrenPermissions.size()>0){
            String oldName = name;
            name = oldName+"S";
            pathName = fatherPath+name;
            String childrenFatherPath = pathName+"_";
            childrenPermissions
                    .forEach(childrenPermission->childrenPermission.prepareOrganization(childrenFatherPath));
            Department children = new Department(oldName);
            children.pathName = childrenFatherPath+oldName;
            addChildren(children);
        } else {
            pathName = fatherPath+name;
        }
    }

    public void buildOrganization(){
        prepareOrganization(prefix);
        buildPermissions("",1);
    }

    public Department getChildren(String childrenName){
        return  this.childrenPermissions.stream()
                .filter(childrenPermission->
                        Objects.equals(childrenPermission.name, childrenName))
                .findFirst().orElse(null);
    }
}