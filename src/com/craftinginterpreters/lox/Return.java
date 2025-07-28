package com.lox;

class Return extends RuntimeException {
    final Object value;

    Return(Object value) {
        super(null, null, false, false); // Desabilita funcionalidades de exceção do JVM
        this.value = value;
    }
}
