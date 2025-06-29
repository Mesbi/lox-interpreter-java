package com.lox;

class Interpreter implements Expr.Visitor<Object> {

    // Método principal que inicia a interpretação.
    void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    // Método que efetivamente dispara o mecanismo do Visitor.
    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }
    
    // Converte o objeto resultado em uma string para exibição.
    private String stringify(Object object) {
        if (object == null) return "nil";

        // Lida com números Double para remover o ".0" de inteiros.
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    // OS MÉTODOS 'visit...' VÊM AQUI...
}
