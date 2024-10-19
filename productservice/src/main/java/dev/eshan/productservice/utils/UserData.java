package dev.eshan.productservice.utils;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserData {
    String userId;
    String email;
    List<String> userRole;
    List<String> userPermission;
}
