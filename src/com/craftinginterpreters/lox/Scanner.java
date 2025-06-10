package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap; // Para keywords, mais adiante
import java.util.List;
import java.util.Map; // Para keywords

import static com.craftinginterpreters.lox.TokenType.*; // Importa todos os tipos de token

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;    // Primeiro caractere do lexema sendo escaneado
    private int current = 0;  // Caractere atual sendo considerado
    private int line = 1;     // Linha atual no código fonte

    // Mapa de palavras-chave (será preenchido mais adiante)
    // private static final Map<String, TokenType> keywords;
    // static {
    //     keywords = new HashMap<>();
    //     // Preencher com as palavras-chave de Lox
    // }

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken(); // Esta função será o coração do scanner
        }

        tokens.add(new Token(EOF, "", null, line)); // Adiciona token de fim de arquivo
        return tokens;
    }

    

    // Método principal que será expandido nas próximas seções (4.5 em diante)
    private void scanToken() {
         char c = advance(); // Função advance() será definida em 4.5.1
         switch (c) {
                 case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
                 case '"': string(); break;
            // ... outros casos de um caractere
            case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
            // ... outros casos de um ou dois caracteres

            case '/':
                if (match('/')) {
                    // Um comentário vai até o fim da linha.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;

            case ' ':
            case '\r':
            case '\t':
                // Ignora espaços em branco.
                break;

            case '\n':
                line++;
                break;

            default:
   if (isDigit(c)) {
    number();
} else if (isAlpha(c)) {
    identifier();
} else {
    Lox.error(line, "Unexpected character.");
}
        }
    }

    // Métodos auxiliares: isAtEnd, advance, addToken, match, peek...
    // Novo método auxiliar:
private void string() {
    while (peek() != '"' && !isAtEnd()) {
        if (peek() == '\n') line++;
        advance();
    }

    if (isAtEnd()) {
        Lox.error(line, "Unterminated string.");
        return;
    }

    advance(); // O '"' de fechamento.

    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
}
    private void number() {
    while (isDigit(peek())) advance();

    // Procura pela parte fracionária.
    if (peek() == '.' && isDigit(peekNext())) {
        advance(); // Consome o "."
        while (isDigit(peek())) advance();
    }

    addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
}

private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
}

private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
}
    private void identifier() {
    while (isAlphaNumeric(peek())) advance();

    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null) type = IDENTIFIER;
    addToken(type);
}

private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
           (c >= 'A' && c <= 'Z') ||
            c == '_';
}

private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
}

    // Adiciona o mapa de palavras-chave no início da classe
private static final Map<String, TokenType> keywords;

static {
    keywords = new HashMap<>();
    keywords.put("and",    AND);
    keywords.put("class",  CLASS);
    // ... todas as outras palavras-chave
    keywords.put("var",    VAR);
    keywords.put("while",  WHILE);
}
}
