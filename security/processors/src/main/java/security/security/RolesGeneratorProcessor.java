package security.security;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("security.security.RoleApplication")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RolesGeneratorProcessor extends AbstractProcessor {
    private Organization roleOrganization;
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roleOrganization = new Organization("R_");
        for(Element annotation: roundEnv.getElementsAnnotatedWith(RoleApplication.class)){
            for(Element enclosed: annotation.getEnclosedElements()
                    .stream()
                    .filter(enclosedRaw->
                            enclosedRaw.getKind().name().equals("ENUM_CONSTANT"))
                    .collect(Collectors.toList())){
                roleOrganization.add(enclosed.getSimpleName().toString());
            }
        }
        roleOrganization.build();
        return false;
    }
}
