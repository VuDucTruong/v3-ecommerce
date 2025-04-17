package shop.holy.v3.ecommerce.shared.config.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;
import shop.holy.v3.ecommerce.shared.exception.BaseBizException;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi publicApi(
            @Value("${openapi.service.group}") String apiDocs,
            OpenApiCustomizer openApiCustomizer) {
        return GroupedOpenApi.builder()
                .group(apiDocs) // /v3/api-docs/api-service.
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
        StringBuilder markdown = getStringBuilder();
        return new OpenAPI()

                .components(
                        new Components()
                                .addSchemas("Byte", new Schema<>().
                                        type("integer").format("int32").description("This is a global schema for Byte type").example(1))
                                .addSchemas("Date", new Schema<>().
                                        type("string").format("date-time").description("This is a global schema for Date type").example("2021-09-01 00:00:00"))
                )
                .info(new Info().title(title)
                        .description(markdown.toString())
                        .license(new License().name("Phong license").url("https://springdoc.org")));
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
                            Schema<?> modelSchema = openApi.getComponents()
                                    .getSchemas().get(ref.replace("#/components/schemas/", ""));

                            if (modelSchema != null) {
                                // Recursively introspect and add individual fields as parts
                                Map<String, Schema<?>> parts = extractFieldsFromModel(modelSchema, openApi.getComponents());
                                content.setSchema(createMultipartSchema(parts));
                            }
                        }
                    }
                });
            }));
        };
    }

    private static StringBuilder getStringBuilder() {
        StringBuilder markdown = new StringBuilder();
        markdown.append("### Exception Catalog\n\n");
        markdown.append("| Error Code              | HTTP Status | Message                         |\n");
        markdown.append("|-------------------------|-------------|--------------------------------|\n");

        for (BizErrors error : BizErrors.values()) {
            BaseBizException exception = error.exception();
            String httpStatus = getHttpStatus(exception);

            markdown.append(String.format("| %-23s | %-11s | %-32s |\n",
                    error.name(),
                    httpStatus,
                    exception.getMessage()));
        }
        return markdown;
    }


    private static String getHttpStatus(BaseBizException exception) {
        if (exception instanceof UnAuthorisedException) {
            return "401";
        } else if (exception instanceof ResourceNotFoundException) {
            return "404";
        } else if (exception instanceof BadRequestException) {
            return "400";
        }
        return "500";
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
                        log.info("Nested schema: {}", ref);
                        parts.putAll(
                                extractFieldsFromModel(nestedSchema, components).entrySet().stream()
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
