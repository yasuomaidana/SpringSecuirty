package security.security;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Department {
    @Getter
    private String name;
    @Getter
    private String pathName;
    @Getter
    private String setOfPermission;
    @Setter
    static String joiner = "_";
    @Getter
    private ArrayList<Department> childrenPermissions = new ArrayList<>();

    Department(String name) {
        this.name = name;
    }

    @Setter
    private static String prefix = "";
    @Getter
    private int level;

    public void addChildren(String childrenName) {
        this.childrenPermissions.add(new Department(childrenName));
    }

    private void addChildren(Department children) {
        this.childrenPermissions.add(0, children);
    }

    private void buildPermissions(String fatherPath, int level) {
        this.level = level;
        if (childrenPermissions.size() > 0) {
            setOfPermission = "";
            childrenPermissions.stream().findFirst().ifPresent(masterChild -> masterChild.buildPermissions(fatherPath, this.level + 1));
            String childFatherPath = fatherPath + name.substring(0, name.length() - 1) + joiner;
            childrenPermissions.stream().skip(1).forEach(children -> children.buildPermissions(childFatherPath, this.level + 1));
            childrenPermissions
                    .forEach(children -> setOfPermission += children.setOfPermission + ",");
            setOfPermission = setOfPermission.substring(0, setOfPermission.length() - 1);
        } else {
            setOfPermission = String.format("'%s'", fatherPath + name);
        }
    }

    private void prepareOrganization(String fatherPath) {
        if (childrenPermissions.size() > 0) {
            String oldName = name;
            name = oldName + "S";
            pathName = fatherPath + name;
            String childrenFatherPath = pathName + "_";
            childrenPermissions
                    .forEach(childrenPermission -> childrenPermission.prepareOrganization(childrenFatherPath));
            Department children = new Department(oldName);
            children.pathName = childrenFatherPath + oldName;
            addChildren(children);
        } else {
            pathName = fatherPath + name;
        }
    }

    public void buildOrganization() {
        prepareOrganization(prefix);
        buildPermissions("", 1);
    }

    public Department getChildren(String childrenName) {
        return this.childrenPermissions.stream()
                .filter(childrenPermission ->
                        Objects.equals(childrenPermission.name, childrenName))
                .findFirst().orElse(null);
    }

    public void cleanDepartments(){
        childrenPermissions=childrenPermissions.stream().filter(department -> department.getSetOfPermission().contains(joiner))
                .collect(Collectors.toCollection(ArrayList::new));
        childrenPermissions.forEach(Department::cleanDepartments);
        int size = childrenPermissions.size();
        if(size==1){
            Department replacement = childrenPermissions.stream().findFirst().get();
            name = replacement.pathName;
            pathName = replacement.pathName;
            setOfPermission = replacement.setOfPermission;
            childrenPermissions = new ArrayList<>();
        } else if (size>1) {
            setOfPermission ="";
            childrenPermissions.forEach(children->setOfPermission+=children.setOfPermission+",");
            setOfPermission = setOfPermission.substring(0,setOfPermission.length()-1);
        }
        name = name.toUpperCase();
        pathName = pathName.toUpperCase();
    }
}
