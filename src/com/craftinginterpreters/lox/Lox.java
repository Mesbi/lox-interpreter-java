package com.craftinginterpreters.lox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Lox {
    public static void main(String[] args) throws IOException {

        // ---- Bloco de teste temporário ----
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Literal(45.67)));

        System.out.println(new AstPrinter().print(expression));
        // ---- Fim do bloco de teste ----
        
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
    }
    // runPrompt() ...
    private static void run(String source) {
        // Por enquanto, apenas imprimimos o código fonte
        System.out.println("Executing source: " + source);
        // O Scanner será chamado aqui futuramente
    }


    private static void run(String source) {
        // Por enquanto, apenas imprimimos os tokens (ou o código fonte)
        // Aqui você chamará seu Scanner
        System.out.println("Executing source: " + source);
         Scanner scanner = new Scanner(source);
         List<Token> tokens = scanner.scanTokens();
        // // For now, just print the tokens.
         for (Token token : tokens) {
            System.out.println(token);
        // }
    }

static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
        // Caso especial: o erro aconteceu no final do arquivo.
        report(token.line, " at end", message);
    } else {
        // Caso comum: o erro está em um token específico.
        // Nós usamos o 'lexeme' do token para mostrar exatamente qual é.
        report(token.line, " at '" + token.lexeme + "'", message);
    }
}

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
