package com.project.wsms.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {
    private String type;
    private String message;
    private Object data;
    private String sender;
    private String receiver;
}
