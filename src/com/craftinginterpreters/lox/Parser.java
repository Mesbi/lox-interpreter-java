package com.lox;

import java.util.List;
import static com.lox.TokenType.*;

class Parser {
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Método principal que inicia a análise
    Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null; // Retorna null se encontrar um erro de sintaxe.
        }
    }

    // MÉTODOS PARA AS REGRAS DA GRAMÁTICA VIRÃO AQUI...
    // we will start with the lowest level rule


    // --- MÉTODOS AUXILIARES ---

    // Verifica se o token atual corresponde a algum dos tipos dados. Se sim, consome o token.
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    // Verifica se o token atual é do tipo esperado, mas não o consome.
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    // Consome o token atual e o retorna.
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    // Verifica se chegamos ao final da lista de tokens.
    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    // Retorna o token atual sem consumi-lo.
    private Token peek() {
        return tokens.get(current);
    }

    // Retorna o token anterior.
    private Token previous() {
        return tokens.get(current - 1);
    }
}
