package shop.holy.v3.ecommerce.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi publicApi(
            @Value("${openapi.service.group}") String apiDocs,
            OpenApiCustomizer openApiCustomizer) {
        return GroupedOpenApi.builder()
                .group(apiDocs) // /v3/api-docs/api-service
//                .packagesToScan(
//                        "shop.holy.v3.ecommerce.api.controller"
//                )
                .addOpenApiCustomizer(openApiCustomizer)
                .build();
    }

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    public OpenAPI openAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.server}") String serverUrl
    ) {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSchemas("Byte", new Schema<>().
                                        type("integer").format("int32").description("This is a global schema for Byte type").example(1))
                                .addSchemas("Date", new Schema<>().
                                        type("string").format("date-time").description("This is a global schema for Date type").example("2021-09-01 00:00:00"))
                )
                .info(new Info().title(title)
                        .description("API documents")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }

    @Bean
    @Primary
    public OpenApiCustomizer customOpenApiCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                RequestBody requestBody = operation.getRequestBody();
                if (requestBody == null) {
                    return;
                }
                requestBody.getContent().forEach((mediaType, content) -> {
                    if (mediaType.equals("multipart/form-data")) {
                        Schema<?> schema = content.getSchema();
                        if (schema.get$ref() != null) {
                            // Get the referenced model schema
                            String ref = schema.get$ref();
                            Schema<?> modelSchema = openApi.getComponents().getSchemas().get(ref.replace("#/components/schemas/", ""));
                            if (modelSchema != null) {
                                // Recursively introspect and add individual fields as parts
                                Map<String, Schema<?>> parts = extractFieldsFromModel(modelSchema, openApi.getComponents());
                                content.setSchema(createMultipartSchema(parts));
                            }
                        }
                    }
                });
            }));
            for (Map.Entry<String, Schema> schema : openApi.getComponents().getSchemas().entrySet()) {
                LinkedHashMap<String, Object> propertiesMap = (LinkedHashMap<String, Object>) schema.getValue().getProperties();
                for (var entry : propertiesMap.entrySet()) {
                    if (entry.getValue() instanceof StringSchema) {
                        if (Objects.equals(((StringSchema) entry.getValue()).getFormat(), "byte")) {
                            ((StringSchema) entry.getValue()).format("int32").type("integer");
                        }
                    }
                }
            }
        };
    }

    private Map<String, Schema<?>> extractFieldsFromModel(Schema<?> schema, Components components) {
        Map<String, Schema<?>> parts = new LinkedHashMap<>();
        // Loop through the properties of the schema and handle each field
        if (schema.getProperties() != null) {
            schema.getProperties().forEach((fieldName, fieldSchema) -> {
                if (fieldSchema != null) {
                    // Handle nested objects recursively
                    if (fieldSchema.get$ref() != null) {
                        String ref = fieldSchema.get$ref().replace("#/components/schemas/", "");
                        Schema<?> nestedSchema = components.getSchemas().get(ref);
                        parts.putAll(extractFieldsFromModel(nestedSchema, components).entrySet().stream()
                                .collect(Collectors.toMap(e -> fieldName + "." + e.getKey(), Map.Entry::getValue)));
                    } else {
                        if (Objects.equals(fieldSchema.getFormat(), "binary")) {
                            fieldSchema.nullable(true);
                        }
                        parts.put(fieldName, (Schema<?>) fieldSchema);
                    }
                }
            });
        }
        return parts;
    }

    private Schema<?> createMultipartSchema(Map<String, Schema<?>> parts) {
        Schema<?> multipartSchema = new Schema<>();
        multipartSchema.setType("object");
        parts.forEach(multipartSchema::addProperty);

        return multipartSchema;
    }

}
