package com.funscope;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Map;

public class TestResourceRule implements MethodRule {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String configPath = System.getProperty("config");
                Yaml yaml = new Yaml();
                Map<String, Map<String, ?>> map = yaml.load(new FileInputStream(configPath));
                injectResources(map.get("resources"), target, target.getClass());
                base.evaluate();
            }
        };
    }

    private void injectResources(Map<String, ?> resources, Object target, Class<?> clazz) throws IllegalAccessException {
        if (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                TestResource testResource = field.getAnnotation(TestResource.class);
                if (testResource != null) {
                    String resourcePath = testResource.value();
                    Object resource = resources.get(resourcePath);
                    if (resource != null) {
                        field.setAccessible(true);
                        field.set(target, OBJECT_MAPPER.convertValue(resource, field.getType()));
                    }
                }
            }
            injectResources(resources, target, clazz.getSuperclass());
        }
    }
}
