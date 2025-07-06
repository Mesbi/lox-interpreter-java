package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
//import java.util.Scanner;


public class Lox {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

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
        if (hadRuntimeError) System.exit(70); //verificação para erros de execução 
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
            hadError = false;
            hadRuntimeError = false; // Também é bom resetar esta flag em modo interativo.
        }
    }

private static void run(String source) {
        // 1. SCANNER: Transforma o texto em tokens.
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // 2. PARSER: Transforma os tokens em uma Árvore Sintática (AST).
        Parser parser = new Parser(tokens);
        //Expr expression = parser.parse();
        List<Stmt> statements = parser.parse(); // Agora retorna List<Stmt>
    
        // 3.Pare se houver um erro de sintaxe
        if (hadError) return;
        // A chamada ao interpretador agora passa a lista de declarações
        interpreter.interpret(statements);
    }
       // --- MÉTODOS DE RELATÓRIO DE ERRO ---
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

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
        "\n[line " + error.token.line + "]");
        hadRuntimeError = true; 
    }
}
