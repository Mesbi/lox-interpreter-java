package com.lox;

import java.util.List;

class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final Environment closure; // vem o closure!

    LoxFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        // Cria um novo ambiente para a função, com o ambiente da closure como pai.
        Environment environment = new Environment(closure);
        for (int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        try {
            // Executa o corpo da função no novo ambiente.
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            // Captura a exceção de retorno e retorna o valor.
            return returnValue.value;
        }

        return null; // Retorno implícito de 'nil' se não houver 'return'.
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
