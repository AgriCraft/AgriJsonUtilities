package com.agricraft.agrijsonutilities.util;

import com.google.gson.JsonParseException;

public class InvalidAgriJsonTypeException extends JsonParseException {
    public InvalidAgriJsonTypeException(String msg) {
        super(msg);
    }
}
