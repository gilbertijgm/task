package com.proyecto.task.advice;

import java.util.List;

public class ServiceUtils {

    public static <T> void validateNotEmpty(List<T> list, String message){
        if (list.isEmpty()){
            throw new NoContentException(message);
        }
    }
}
