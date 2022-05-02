package security.security;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

public class OrganizationTree {
    ArrayList<Organization> root = new ArrayList<>();
    String prefix;
    OrganizationTree(String prefix){
        this.prefix=prefix;
    }
    public void add(String permissions) {
        Queue<String> permissionStack = (Queue<String>) Arrays.stream(permissions.split("_")).collect(Collectors.toList());
        String permission = permissionStack.poll();
        if(root.stream().anyMatch(rootElement->rootElement.getName().equals(permission))){

        }else {
            root.add(new Organization(permission));
        }
    }
}

class Organization {
    @Getter
    private String name;
    @Getter
    private String pathName;
    @Getter
    private String setOfPermission;
    @Setter
    static String joiner;
    private ArrayList<Organization> childrenPermissions =new ArrayList<>();
    Organization(String name){
        this.name = name;
    }

    public void addChildren(String childrenName){
        this.childrenPermissions.add(new Organization(childrenName));
    }

    private void addChildren(Organization children){
        this.childrenPermissions.add(0,children);
    }

    private void buildPermissions(){
        if(childrenPermissions.size()>0){
            setOfPermission ="";
            childrenPermissions.forEach(children->children.buildPermissions());
            childrenPermissions.stream()
                    .forEach(children-> setOfPermission+=children.setOfPermission+",");
        } else {
            setOfPermission = String.format("'%s'",name);
        }
    }

    private void prepareOrganization(String fatherPath){
        if(childrenPermissions.size()>0){
            String oldName = name;
            name = oldName+"S";
            pathName = fatherPath+name;
            String childrenFatherPath = pathName+"_";
            childrenPermissions.stream()
                    .forEach(childrenPermission->childrenPermission.prepareOrganization(childrenFatherPath));
            Organization children = new Organization(oldName);
            children.pathName = childrenFatherPath+oldName;
            addChildren(children);
        } else {
            pathName = fatherPath+name;
        }
    }

    public void buildOrganization(){
        prepareOrganization("");
        buildPermissions();
    }

    public Organization getChildren(String childrenName){
        return  this.childrenPermissions.stream()
                .filter(childrenPermission->
                        Objects.equals(childrenPermission.name, childrenName))
                .findFirst().orElse(null);
    }
}