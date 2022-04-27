package security.security;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static javax.lang.model.SourceVersion.RELEASE_8;
import static javax.tools.Diagnostic.Kind.WARNING;

@SupportedAnnotationTypes("security.security.RoleAuthorization")
@AutoService(RoleAuthorizationProcessor.class)
public class RoleAuthorizationProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(asList("security.security.RoleAuthorization"));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(WARNING,"Testing warning");

        for(Element element: roundEnv.getElementsAnnotatedWith(RoleAuthorization.class)){
            BufferedWriter bufferedWriter;
            JavaFileObject builderClass;
            PackageElement packageElement = (PackageElement) element.getEnclosingElement();
            try {
                builderClass  = processingEnv.getFiler().createSourceFile(element.getSimpleName().toString());
                bufferedWriter = new BufferedWriter(builderClass.openWriter());
                if(element.getKind().isClass()){
                    bufferedWriter.append("package ");
                    bufferedWriter.append(packageElement.getQualifiedName());
                    bufferedWriter.append(";");
                    bufferedWriter.newLine();

                    //bufferedWriter.append("@RoleAuthorization()");
                    bufferedWriter.newLine();
                    bufferedWriter.append("public class StudentManagementController{ public void nothing(){}}");
                    //for(Element enclosed: element.getEnclosedElements()){

                    //}
                }
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
