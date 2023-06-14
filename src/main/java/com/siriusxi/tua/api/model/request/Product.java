package com.siriusxi.tua.api.model.request;

import java.util.Objects;

public record Product(String id,
                      String name,
                      Double price) {
    public Product(String id) {
        this(Objects.requireNonNull(id), null, null);
    }
}
