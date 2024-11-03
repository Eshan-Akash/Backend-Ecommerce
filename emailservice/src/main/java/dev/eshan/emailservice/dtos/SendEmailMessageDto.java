package dev.eshan.emailservice.dtos;

import lombok.Data;

@Data
public class SendEmailMessageDto {
    private String from;
    private String to;
    private String subject;
    private String body;
}
