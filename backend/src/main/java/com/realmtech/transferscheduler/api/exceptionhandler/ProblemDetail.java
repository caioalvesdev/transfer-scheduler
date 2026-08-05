package com.realmtech.transferscheduler.api.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class ProblemDetail {

    private final int status;
    private URI type;
    private String title;
    private final Map<String, Object> properties = new LinkedHashMap<>();

    private ProblemDetail(int status) {
        this.status = status;
    }

    public static ProblemDetail forStatus(HttpStatus status) {
        return new ProblemDetail(status.value());
    }

    public void setType(URI type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }
}
