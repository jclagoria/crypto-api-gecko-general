package ar.com.api.general.model;

import lombok.Builder;

import java.util.List;

@Builder
public class ResponseApi {

    private String code;
    List<MessageApi> messages;

}
