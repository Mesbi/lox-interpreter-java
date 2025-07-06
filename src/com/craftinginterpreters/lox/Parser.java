package com.lox;

import java.util.ArrayList;
import java.util.List;
import static com.lox.TokenType.*;

class Parser {
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Método principal agora retorna uma lista de declarações
    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
    while (!isAtEnd()) {
        statements.add(declaration());
         }
            return statements; 
       
    }
    //  Novo ponto de entrada que distingue declarações de variáveis de outras.
    private Stmt declaration() {
    try {
        if (match(VAR)) return varDeclaration();
        return statement();
    } catch (ParseError error) {
        synchronize();
        return null;
    }
}

// Método para analisar uma declaração de variável.
private Stmt varDeclaration() {
    Token name = consume(IDENTIFIER, "Expect variable name.");
    Expr initializer = null;
    if (match(EQUAL)) {
        initializer = expression();
    }
    consume(SEMICOLON, "Expect ';' after variable declaration.");
    return new Stmt.Var(name, initializer);
}

// Método para analisar outras declarações.
private Stmt statement() {
    if (match(IF)) return ifStatement();
    if (match(PRINT)) return printStatement();
    if (match(LEFT_BRACE)) return new Stmt.Block(block());
    return expressionStatement();
}

// Métodos para cada tipo de declaração.
private Stmt ifStatement() {
    consume(LEFT_PAREN, "Expect '(' after 'if'.");
    Expr condition = expression();
    consume(RIGHT_PAREN, "Expect ')' after if condition.");

    Stmt thenBranch = statement();
    Stmt elseBranch = null;
    if (match(ELSE)) {
        elseBranch = statement();
    }

    return new Stmt.If(condition, thenBranch, elseBranch);
}

private Stmt printStatement() {
    Expr value = expression();
    consume(SEMICOLON, "Expect ';' after value.");
    return new Stmt.Print(value);
}

private Stmt expressionStatement() {
    Expr expr = expression();
    consume(SEMICOLON, "Expect ';' after expression.");
    return new Stmt.Expression(expr);
}
    private List<Stmt> block() {
    List<Stmt> statements = new ArrayList<>();
    while (!check(RIGHT_BRACE) && !isAtEnd()) {
        statements.add(declaration());
    }
    consume(RIGHT_BRACE, "Expect '}' after block.");
    return statements;
}

// Atualize o método `expression()` para incluir atribuição.
private Expr expression() {
    return assignment(); // O novo ponto de entrada para expressões.
}

// --- REGRAS DE EXPRESSÃO ---
    private Expr expression() {
    return equality();
}
    
private Expr assignment() {
    Expr expr = equality(); // ou a regra que você tinha antes (equality, comparison, etc.)
    if (match(EQUAL)) {
        Token equals = previous();
        Expr value = assignment(); // Permite atribuições encadeadas (a = b = c)
        if (expr instanceof Expr.Variable) {
            Token name = ((Expr.Variable)expr).name;
            return new Expr.Assign(name, value);
        }
        error(equals, "Invalid assignment target.");
    }
    return expr;
}
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

// Atualize o `primary()` para reconhecer identificadores como expressões.
private Expr primary() {
    if (match(FALSE)) return new Expr.Literal(false);
    if (match(TRUE)) return new Expr.Literal(true);
    if (match(NIL)) return new Expr.Literal(null);

    if (match(NUMBER, STRING)) {
        return new Expr.Literal(previous().literal);
    }

      if (match(IDENTIFIER)) {
        return new Expr.Variable(previous());
    }

    if (match(LEFT_PAREN)) {
        Expr expr = expression();
        consume(RIGHT_PAREN, "Expect ')' after expression.");
        return new Expr.Grouping(expr);
    }

    throw error(peek(), "Expect expression.");


    // --- MÉTODOS AUXILIARES ---

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
    
    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;
            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }
            advance();
        }
    }
}
  





    
  
    


