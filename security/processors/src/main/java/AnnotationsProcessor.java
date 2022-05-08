import com.google.auto.service.AutoService;
import security.security.processors.RoleOrganization;

import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_8;


@SupportedAnnotationTypes({"security.security.annotations.RoleApplication","security.security.annotations.PermissionApplication"})
@SupportedSourceVersion(RELEASE_8)
@AutoService(Processor.class)
public class AnnotationsProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
        RoleOrganization roles = new RoleOrganization(annotations,roundEnv,processingEnv);
        return true;
    }
}
