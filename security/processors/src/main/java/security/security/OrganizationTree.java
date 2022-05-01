package security.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.stream.Collectors;

public class OrganizationTree {
    ArrayList<RoleOrPermission> root = new ArrayList<>();
    String prefix;
    OrganizationTree(String prefix){
        this.prefix=prefix;
    }
    public void add(String permissions) {
        Queue<String> permissionStack = (Queue<String>) Arrays.stream(permissions.split("_")).collect(Collectors.toList());
        String permission = permissionStack.poll();
        if(root.stream().anyMatch(rootElement->rootElement.name.equals(permission))){

        }else {
            root.add(new RoleOrPermission(permission));
        }
    }
}

class RoleOrPermission{
    String name;
    String joiner;
    ArrayList<RoleOrPermission> childrenPermissions =new ArrayList<>();
    RoleOrPermission(String name){
        this.name = name;
        this.joiner ="_";
    }
    RoleOrPermission(String name, String joiner){
        this.name= name;
        this.joiner = joiner;
    }
    public void addChildren(RoleOrPermission children){
        this.childrenPermissions.add(children);
    }
    public ArrayList<String> containedNames(){
        ArrayList<String> names = new ArrayList<>();
        if(childrenPermissions.size()>0){
            names.add(name+"S");
        } else if (childrenPermissions.size()==0) {
            names.add(name);
        }else{
            childrenPermissions.stream()
                    .forEach(childrenPermission->childrenPermission
                            .containedNames().stream()
                            .forEach(containedName->names.add(name+joiner+containedName)));
        }
        return names;
    }
}