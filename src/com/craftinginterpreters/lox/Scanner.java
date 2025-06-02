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

    // Verifica se chegamos ao fim do código fonte
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Método principal que será expandido nas próximas seções (4.5 em diante)
    private void scanToken() {
        // char c = advance(); // Função advance() será definida em 4.5.1
        // switch (c) {
        //     // Casos para cada tipo de token...
        // }
        // Por enquanto, para passar desta etapa e testar a estrutura:
        System.out.println("Scanning token starting at: " + source.substring(start, current+1 > source.length() ? source.length() : current+1));
        // Você precisará implementar a lógica de reconhecimento de tokens aqui,
        // começando com `advance()` e os `switch` cases nas próximas seções do livro.
        // Por ora, você pode apenas avançar para testar o loop.
        if (current < source.length()) current++; // Simplesmente avança para evitar loop infinito nesta fase inicial
    }

    // Outros métodos auxiliares como advance(), addToken(), match(), peek(), etc.,
    // serão introduzidos a partir da seção 4.5.1.
}
