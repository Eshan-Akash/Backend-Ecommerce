package dev.eshan.productservice.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
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
}
