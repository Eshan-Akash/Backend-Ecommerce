package dev.eshan.productservice.utils;

import com.nimbusds.jose.shaded.gson.FieldNamingPolicy;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class Utils {
    public static final Gson gson = new GsonBuilder().create();
    public static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    public static final Gson gsonSnakeCase = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static final Gson gsonSnakeCaseWithPretty = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();

    public static final Gson gsonCamelUpperCase = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .create();

    public static String getMediaTypeFromFile(String fileName) {
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        return "application/octet-stream";
    }

    public static UserData createUserDataFromToken(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        List<String> roles = (List<String>) claims.get("roles");
        List<String> permissions = (List<String>) claims.get("scope");
        return UserData.builder()
                .userId(claims.get("userId").toString())
                .email(claims.get("sub").toString())
                .userRole(roles)
                .userPermission(permissions)
                .build();
    }
}
