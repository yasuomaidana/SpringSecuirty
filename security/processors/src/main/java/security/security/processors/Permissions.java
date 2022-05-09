package security.security.processors;

import lombok.SneakyThrows;
import security.security.Organization;
import security.security.annotations.PermissionApplication;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static javax.lang.model.element.ElementKind.PACKAGE;


public class Permissions extends OrganizationProcessor{

    public Permissions(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) {
        super(annotations, roundEnv, processingEnv);
    }

    @SneakyThrows
    @Override
    public void generateOrganization(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) {
        organization = new Organization("P_");
        organization.setNameSplitter(":");
        organization.setDepartmentJoiner(":");
        boolean write = false;
        for(Element annotation: roundEnv.getElementsAnnotatedWith(PermissionApplication.class)){
            if(annotation.getEnclosingElement().getKind()==PACKAGE){
                PackageElement packageElement = (PackageElement) annotation.getEnclosingElement();
                packageName = packageElement.toString();
                write = true;
                continue;
            }
            prepareOrganization(annotation);
        }
        if(write){
            organization.buildPermissions();
            writeAnnotations(processingEnv,"hasAnyAuthority","hasAuthority");
        }
    }

    @Override
    public void prepareOrganization(Element annotation) {
        PermissionApplication rawAnnotation = annotation.getAnnotation(PermissionApplication.class);
        String permission = rawAnnotation.value();
        if(!permission.toUpperCase().replace(":","_").equals(annotation.getSimpleName().toString())){
            throw new RuntimeException(
                    String.format("Enum name and permission don't matches Enum:%s Permission:%s"
                            , annotation.getSimpleName().toString(),permission));
        }
        organization.add(permission);
    }
}
