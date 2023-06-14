package com.siriusxi.tua.domain;

import com.siriusxi.tua.api.model.request.Product;

import java.io.Serializable;

public record ProductMessage(Product product,
                             String action) implements Serializable {

}
