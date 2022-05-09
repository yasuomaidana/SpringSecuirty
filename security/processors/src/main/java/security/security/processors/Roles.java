package security.security.processors;

import lombok.SneakyThrows;
import security.security.Organization;
import security.security.annotations.RoleApplication;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

public class Roles extends OrganizationProcessor {

    public Roles(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv){
        super(annotations,roundEnv,processingEnv);
    }

    @SneakyThrows
    public void generateOrganization(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) {
        organization = new Organization("R_");
        for(Element annotation: roundEnv.getElementsAnnotatedWith(RoleApplication.class)){
            PackageElement packageElement = (PackageElement) annotation.getEnclosingElement();
            packageName = packageElement.toString();
            prepareOrganization(annotation);
            writeAnnotations(processingEnv,"hasAnyRole","hasRole");
        }
    }

    public void prepareOrganization(Element annotation) {
        for(Element enclosed: annotation.getEnclosedElements()
                .stream()
                .filter(enclosedRaw->
                        enclosedRaw.getKind().name().equals("ENUM_CONSTANT"))
                .collect(Collectors.toList())){
            organization.add(enclosed.getSimpleName().toString());
        }
        organization.build();
    }

}
