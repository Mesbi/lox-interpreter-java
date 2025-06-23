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
private Expr primary() {
    if (match(FALSE)) return new Expr.Literal(false);
    if (match(TRUE)) return new Expr.Literal(true);
    if (match(NIL)) return new Expr.Literal(null);

    if (match(NUMBER, STRING)) {
        return new Expr.Literal(previous().literal);
    }

    if (match(LEFT_PAREN)) {
        Expr expr = expression();
        consume(RIGHT_PAREN, "Expect ')' after expression.");
        return new Expr.Grouping(expr);
    }

    throw error(peek(), "Expect expression.");
}
    // 
private Expr unary() {
    if (match(BANG, MINUS)) {
        Token operator = previous();
        Expr right = unary(); // Chamada recursiva para unários aninhados (ex: --!!true)
        return new Expr.Unary(operator, right);
    }
    return primary();
}
    //
private Expr factor() {
    Expr expr = unary(); // Pega a expressão à esquerda (de maior precedência)

    while (match(SLASH, STAR)) { // Enquanto encontrar operadores '*' ou '/'
        Token operator = previous();
        Expr right = unary();
        expr = new Expr.Binary(expr, operator, right); // Agrupa com o 'expr' anterior
    }

    return expr;
}

private Expr term() {
    Expr expr = factor();
    while (match(MINUS, PLUS)) {
        Token operator = previous();
        Expr right = factor();
        expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
}

private Expr comparison() {
    Expr expr = term();
    while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
        Token operator = previous();
        Expr right = term();
        expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
}

private Expr equality() {
    Expr expr = comparison();
    while (match(BANG_EQUAL, EQUAL_EQUAL)) {
        Token operator = previous();
        Expr right = comparison();
        expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
}
    //
private Expr expression() {
    return equality();
}
    
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
