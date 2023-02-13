package ru.otr.sf.user.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;
    private HttpStatus httpStatus;
    private int code;

    

}
