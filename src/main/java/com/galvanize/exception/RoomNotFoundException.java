package com.galvanize.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by localadmin on 14/01/2016.
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Room not found")
public class RoomNotFoundException extends Exception {
//public class RoomNotFoundException extends RuntimeException { also works but Throwable does not

    public RoomNotFoundException(String str){
        super(str);
        System.out.println("....................................exception created with message.........................................");
    }

    public RoomNotFoundException(){
          System.out.println("....................................exception created without message.........................................");
    }


}
