package com.UDP.exceptions;

import java.io.IOException;

public class ServerException extends IOException {
    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public static ServerException receivingResponseException(){
        return new ServerException("An error has occurred during response processing");
    }
}
