package security.security;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class OrganizationTree {
    private ArrayList<Organization> root = new ArrayList<>();
    private String prefix;
    @Setter
    private String nameSplitter = "_";
    OrganizationTree(String prefix){
        this.prefix=prefix;
    }
    public void add(String permissions) {
        Queue<String> permissionStack =  Arrays.stream(permissions.split(nameSplitter)).collect(Collectors.toCollection(LinkedList::new));
        String permission = permissionStack.poll();
        Organization organizationRoot = root.stream().filter(organization -> organization.getName().equals(permission)).findFirst().orElse(null);
        if(organizationRoot!=null){
            add(permissionStack,organizationRoot);
        }else {
            root.add(new Organization(permission));
            organizationRoot = root.stream().filter(organization -> organization.getName().equals(permission)).findFirst().orElse(null);
            add(permissionStack,organizationRoot);
        }
    }

    private void add(Queue<String> permissionStack,Organization organizationNode){
        String newPermission = permissionStack.poll();
        if(newPermission==null) return;
        Organization newOrganizationNode = organizationNode.getChildren(newPermission);
        if(newOrganizationNode == null){
            organizationNode.addChildren(newPermission);
            add(permissionStack,organizationNode.getChildren(newPermission));
        } else {
            add(permissionStack,newOrganizationNode);
        }

    }

    public void build() {
        Organization.setPrefix(prefix);
        root.stream().forEach(organizationNode->organizationNode.buildOrganization());
    }
    public void setOrganizationJoiner(String organizationJoiner) {
        Organization.joiner = organizationJoiner;
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
    static String joiner ="_";
    @Getter
    private ArrayList<Organization> childrenPermissions =new ArrayList<>();
    Organization(String name){
        this.name = name;
    }
    @Setter
    private static String prefix="";

    public void addChildren(String childrenName){
        this.childrenPermissions.add(new Organization(childrenName));
    }

    private void addChildren(Organization children){
        this.childrenPermissions.add(0,children);
    }

    private void buildPermissions(String fatherPath){
        if(childrenPermissions.size()>0){
            setOfPermission ="";
            childrenPermissions.stream().findFirst().ifPresent(masterChild->masterChild.buildPermissions(fatherPath));
            String childFatherPath = fatherPath+name.substring(0,name.length()-1)+joiner;
            childrenPermissions.stream().skip(1).forEach(children->children.buildPermissions(childFatherPath));
            childrenPermissions.stream()
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
        prepareOrganization(prefix);
        buildPermissions("");
    }

    public Organization getChildren(String childrenName){
        return  this.childrenPermissions.stream()
                .filter(childrenPermission->
                        Objects.equals(childrenPermission.name, childrenName))
                .findFirst().orElse(null);
    }
}