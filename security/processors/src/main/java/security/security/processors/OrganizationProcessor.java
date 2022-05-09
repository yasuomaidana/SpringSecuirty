package security.security.processors;

import lombok.Setter;
import security.security.Department;
import security.security.Organization;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public abstract class OrganizationProcessor {
    Organization organization;
    String packageName;

    @Setter
    String single = "hasRole";
    @Setter
    String multiple = "hasAnyRole";


    OrganizationProcessor(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv,ProcessingEnvironment processingEnv){
        generateOrganization(annotations,roundEnv,processingEnv);
    }

    public abstract void generateOrganization(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv,ProcessingEnvironment processingEnv);

    public abstract void prepareOrganization(Element annotation);

    public String generateSingleAnnotation(Department department, boolean firstTime){
        String blankSpace = String.join("", Collections.nCopies(department.getLevel()-1, "\t"));
        String simpleAnnotation = "";
        simpleAnnotation += blankSpace + "@Inherited\n";
        simpleAnnotation += blankSpace + "@Retention(RetentionPolicy.RUNTIME)\n";
        if(department.getChildrenPermissions().size()>1){
            simpleAnnotation += blankSpace +
                    String.format("@PreAuthorize(\"%s(%s)\")\n",multiple, department.getSetOfPermission());
        }else{
            simpleAnnotation += blankSpace +
                    String.format("@PreAuthorize(\"%s(%s)\")\n",single, department.getSetOfPermission());
        }

        simpleAnnotation += firstTime?"public ":blankSpace;
        simpleAnnotation += "@interface "+department.getPathName()+" {\n";

        StringBuilder innerAnnotation = new StringBuilder();
        for(Department innerDepartment: department.getChildrenPermissions()){
            innerAnnotation.append("\n");
            innerAnnotation.append(generateSingleAnnotation(innerDepartment,false));
        }
        simpleAnnotation += innerAnnotation;
        simpleAnnotation += firstTime?"\n":"";
        simpleAnnotation += blankSpace+"}\n";
        return  simpleAnnotation;
    }

    public String generateAnnotations(String packageName, Department rootDepartment){
        String content ="";
        content+="package ";
        content+=packageName;
        content+=";\n\n";
        content+= "import org.springframework.security.access.prepost.PreAuthorize;\n" +
                "\n" +
                "import java.lang.annotation.Inherited;\n" +
                "import java.lang.annotation.Retention;\n" +
                "import java.lang.annotation.RetentionPolicy;\n";
        content+=generateSingleAnnotation(rootDepartment,true);
        return content;
    }

    public void writeAnnotations(ProcessingEnvironment processingEnv) throws IOException {
        for(Department department: organization.getDepartments()){
            JavaFileObject builderClass = processingEnv.getFiler().createSourceFile(department.getPathName());
            BufferedWriter writer = new BufferedWriter(builderClass.openWriter());
            writer.write(generateAnnotations(packageName,department));
            writer.close();
        }
    }
}
