package com.craftinginterpreters.lox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Lox {
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
        run(new String(bytes)); // Converte bytes para String

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
    }

    private static void runPrompt() throws IOException {
        // Implementação do modo interativo (REPL)
        // Pode ser deixado para depois, se o foco é o scanner de arquivo
        Scanner input = new Scanner(System.in);
        for (;;) {
            System.out.print("> ");
            String line = input.nextLine();
            if (line == null || line.equals("exit")) break; // Adicionado "exit" para sair
            run(line);
            hadError = false; // Reset error para o modo interativo
        }
        input.close();
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

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
