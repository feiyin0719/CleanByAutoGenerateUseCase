package com.iffly.clean.usecase.apt;


import com.google.auto.service.AutoService;
import com.iffly.clean.usecase.annotations.RequestParams;
import com.iffly.clean.usecase.annotations.ResponseParams;
import com.iffly.clean.usecase.annotations.UseCase;
import com.iffly.clean.usecase.annotations.UseCaseRepository;
import com.iffly.clean.usecase.annotations.UseCases;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class CleanUseCaseAnnotationProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(UseCaseRepository.class.getCanonicalName());
        supportTypes.add(UseCase.class.getCanonicalName());
        supportTypes.add(UseCases.class.getCanonicalName());
        supportTypes.add(RequestParams.class.getCanonicalName());
        supportTypes.add(ResponseParams.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //用哪个版本的jdk编译
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(UseCases.class);

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;


            UseCases useCases = typeElement.getAnnotation(UseCases.class);
            String beanName = useCases.name().equals("") ? typeElement.getSimpleName().toString() : useCases.name();
            String packageName = typeElement.getQualifiedName().toString() + "Impl";
            beanName = beanName.replace("UseCases", "UseCase");
            if (!beanName.endsWith("UseCase"))
                beanName = beanName + "UseCase";
            List<? extends Element> methods = processingEnv.getElementUtils().getAllMembers(typeElement);
            for (Element element1 : methods) {

                UseCase useCase = element1.getAnnotation(UseCase.class);

                if (useCase != null && element1.getKind() == ElementKind.METHOD) {
                    ExecutableElement executeableElement = (ExecutableElement) element1;
                    String repositoryFunctionName = ContastUtil.DEFAULT_REPOSITORY_METHOD_NAME;
                    TypeName requestType = null, responseType = null, repositoryType = null;
                    for (VariableElement variableElement : executeableElement.getParameters()) {
                        if (variableElement.getAnnotation(RequestParams.class) != null)
                            requestType = TypeName.get(variableElement.asType());
                        else if (variableElement.getAnnotation(ResponseParams.class) != null)
                            responseType = TypeName.get(variableElement.asType());
                        else if (variableElement.getAnnotation(UseCaseRepository.class) != null) {
                            repositoryType = TypeName.get(variableElement.asType());
                            UseCaseRepository useCaseRepository = variableElement.getAnnotation(UseCaseRepository.class);
                            repositoryFunctionName = useCaseRepository.method().equals("") ? element1.getSimpleName().toString() : useCaseRepository.method();

                        }
                    }

                    if (requestType == null)
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@UseCase Method must have @RequestParams");
                    if (responseType == null)
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@UseCase Method must have @ResponseParams");
                    if (repositoryType == null)
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@UseCase Method must have @UseCaseRepository");

                    String actionName = useCase.name().equals("") ? element1.getSimpleName().toString() : useCase.name();
                    actionName = actionName.substring(0, 1).toUpperCase() + actionName.substring(1);
                    MethodSpec methodSpec = MethodSpec.methodBuilder("execute")
                            .addParameter(requestType.box(), "request")
                            .returns(responseType).addModifiers(Modifier.PROTECTED)
                            .addStatement("return repository.$N(request)", repositoryFunctionName)
                            .build();

                    MethodSpec con = MethodSpec.constructorBuilder().addStatement("super(appExc)")
                            .addStatement("this.repository = repository")
                            .addParameter(ClassName.bestGuess(ContastUtil.APP_EXCUTORS), "appExc")
                            .addParameter(repositoryType, "repository")
                            .addModifiers(Modifier.PUBLIC).build();
                    TypeSpec autoClass = TypeSpec.classBuilder(actionName + beanName)
                            .superclass(ParameterizedTypeName.get(ClassName.bestGuess(ContastUtil.ROOT_USECASE), responseType.box(), requestType.box()))
                            .addMethod(methodSpec)
                            .addMethod(con)
                            .addField(repositoryType, "repository")
                            .addModifiers(Modifier.PUBLIC)
                            .build();

                    JavaFile javaFile = JavaFile.builder(packageName, autoClass).build();

                    // 将文档写入
                    try {
                        javaFile.writeTo(processingEnv.getFiler());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }


        return true;
    }


}
