package security.security;

import com.google.auto.service.AutoService;
import lombok.SneakyThrows;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("security.security.RoleApplication")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RolesGeneratorProcessor extends AbstractProcessor {
    private Organization roleOrganization;
    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roleOrganization = new Organization("R_");
        for(Element annotation: roundEnv.getElementsAnnotatedWith(RoleApplication.class)){
            PackageElement packageElement = (PackageElement) annotation.getEnclosingElement();
            String packageName = packageElement.toString();
            prepareOrganization(annotation);
            writeAnnotations(packageName);
        }
        return true;
    }

    private void prepareOrganization(Element annotation){
        for(Element enclosed: annotation.getEnclosedElements()
                .stream()
                .filter(enclosedRaw->
                        enclosedRaw.getKind().name().equals("ENUM_CONSTANT"))
                .collect(Collectors.toList())){
            roleOrganization.add(enclosed.getSimpleName().toString());
        }
        roleOrganization.build();
    }

    private String generateSingleAnnotation(Department department,boolean firstTime){
        String blankSpace = String.join("", Collections.nCopies(department.getLevel()-1, "\t"));
        String simpleAnnotation = "";
        simpleAnnotation += blankSpace + "@Inherited\n";
        simpleAnnotation += blankSpace + "@Retention(RetentionPolicy.RUNTIME)\n";
        if(department.getChildrenPermissions().size()>1){
            simpleAnnotation += blankSpace +
                    String.format("@PreAuthorize(\"hasAnyRole(%s)\")\n", department.getSetOfPermission());
        }else{
            simpleAnnotation += blankSpace +
                    String.format("@PreAuthorize(\"hasRole(%s)\")\n", department.getSetOfPermission());
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
    private String generateAnnotations(String packageName, Department rootDepartment){
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
    private void writeAnnotations(String packageName) throws IOException {
        for(Department department: roleOrganization.getDepartments()){
            JavaFileObject builderClass = processingEnv.getFiler().createSourceFile(department.getPathName());
            BufferedWriter writer = new BufferedWriter(builderClass.openWriter());
            writer.write(generateAnnotations(packageName,department));
            writer.close();
        }
    }
}
