package com.lox;

import java.util.HashMap;
import java.util.Map;

class Environment {
    final Environment enclosing; // Para escopos aninhados
    private final Map<String, Object> values = new HashMap<>();

    // Construtor para o escopo global
    Environment() {
        enclosing = null;
    }

    // Construtor para escopos locais
    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    // Define uma nova variável no escopo ATUAL.
    void define(String name, Object value) {
        values.put(name, value);
    }

    // Obtém o valor de uma variável. Se não encontrar no escopo atual,
    // procura no escopo pai (enclosing), recursivamente.
    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        if (enclosing != null) return enclosing.get(name);
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    // Atribui um novo valor a uma variável EXISTENTE.
    // Se não encontrar no escopo atual, procura no escopo pai.
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }
}
