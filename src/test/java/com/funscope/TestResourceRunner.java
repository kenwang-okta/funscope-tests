package com.funscope;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestResourceRunner extends BlockJUnit4ClassRunner {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Map<String, ?> resources;

    public TestResourceRunner(Class<?> clazz) throws InitializationError, FileNotFoundException {
        super(clazz);
        String configPath = System.getProperty("config");
        Yaml yaml = new Yaml();
        Map<String, Map<String, ?>> map = yaml.load(new FileInputStream(configPath));
        resources = map.get("resources");
    }

    @Override
    protected void validateTestMethods(List<Throwable> errors) {
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(Test.class);

        for (FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoid(false, errors);
            for (Parameter parameter : eachTestMethod.getMethod().getParameters()) {
                if (parameter.getAnnotation(TestResource.class) == null) {
                    errors.add(new Exception("Method " + eachTestMethod.getName() + " should only have parameters annotated by @TestResource"));
                }
            }
        }
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                Object[] args = Arrays.stream(method.getMethod().getParameters())
                        .map(parameter -> {
                            TestResource testResource = parameter.getAnnotation(TestResource.class);
                            String resourcePath = testResource.value();
                            Object resource = resources.get(resourcePath);
                            if (resource != null) {
                                return OBJECT_MAPPER.convertValue(resource, parameter.getType());
                            }
                            return null;
                        }).toArray();
                method.invokeExplosively(test, args);
            }
        };
    }

    @Override
    protected List<TestRule> getTestRules(Object target) {
        return super.getTestRules(target);
    }
}
