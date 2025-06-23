package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
    }
    // Executa o interpretador em modo interativo (REPL)
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            // Em modo interativo, não saímos se houver erro. Apenas resetamos a flag.
            hadError = false;
        }
    }

private static void run(String source) {
        // 1. SCANNER: Transforma o texto em tokens.
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // 2. PARSER: Transforma os tokens em uma Árvore Sintática (AST).
        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        // 3. VERIFICAÇÃO DE ERRO: Se o parser encontrou um erro, pare aqui.
        if (hadError) return;

        // 4. "EXECUÇÃO" TEMPORÁRIA: Imprime a árvore para verificarmos se está correta.
        System.out.println(new AstPrinter().print(expression));
    }
       // Versão do erro para o Parser
static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
        report(token.line, " at end", message);
    } else {
        report(token.line, " at '" + token.lexeme + "'", message);
    }
}
        // Versão do erro para o Scanner
  static void error(int line, String message) {
        report(line, "", message);
    }
        // imprime a mensagem de erro
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
