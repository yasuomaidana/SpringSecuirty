package security.security;

import com.google.auto.service.AutoService;
import lombok.SneakyThrows;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("security.security.RoleApplication")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RoleAnnotationsProcessor extends OrganizationAbstractProcessor {

    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        organization = new Organization("R_");
        for(Element annotation: roundEnv.getElementsAnnotatedWith(RoleApplication.class)){
            PackageElement packageElement = (PackageElement) annotation.getEnclosingElement();
            String packageName = packageElement.toString();
            prepareOrganization(annotation);
            writeAnnotations(packageName);
        }
        return true;
    }

    @Override
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
