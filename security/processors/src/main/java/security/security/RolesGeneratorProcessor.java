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
            String annotationName = annotation.getSimpleName().toString();
            prepareOrganization(annotation);
            generateAnnotations(packageElement.toString());
        }
        return false;
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

    private void generateAnnotations(String packageName) throws IOException {
        Department example = roleOrganization.getDepartments().get(0);
        JavaFileObject builderClass = processingEnv.getFiler().createSourceFile(example.getName());
        BufferedWriter writer = new BufferedWriter(builderClass.openWriter());
        writer.append("package ");
        writer.append(packageName);
        writer.append(";");
        writer.newLine();
        writer.newLine();
        writer.append("public @interface ");
        writer.append(example.getName());
        writer.append(" {");
        writer.newLine();
        writer.append("}");
        writer.close();
    }
}
