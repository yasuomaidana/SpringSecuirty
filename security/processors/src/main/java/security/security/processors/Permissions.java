package security.security.processors;

import lombok.SneakyThrows;
import security.security.Organization;
import security.security.annotations.PermissionApplication;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;


public class Permissions {

    Organization organization;

    @SneakyThrows
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        organization = new Organization("P_");
        for(Element annotation: roundEnv.getElementsAnnotatedWith(PermissionApplication.class)){
            PackageElement packageElement = (PackageElement) annotation.getEnclosingElement();
            String packageName = packageElement.toString();
            prepareOrganization(annotation);
        }
        return false;
    }

    public void prepareOrganization(Element annotation) {

        for(Element enclosed: annotation.getEnclosedElements()){
            ElementKind type = enclosed.getKind();
        }
    }
}
