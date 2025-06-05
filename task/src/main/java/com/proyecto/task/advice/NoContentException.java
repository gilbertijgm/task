package com.proyecto.task.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends RuntimeException{

    public NoContentException(String message) {
        super(message);
    }
}
//	Cuando querés comunicar que la lista existe pero está vacía