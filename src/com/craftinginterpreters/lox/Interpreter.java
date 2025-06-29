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

@Override
public Object visitLiteralExpr(Expr.Literal expr) {
    // O valor de um literal é o próprio literal.
    return expr.value;
}

@Override
public Object visitGroupingExpr(Expr.Grouping expr) {
    // O valor de um agrupamento é o valor da expressão dentro dele.
    return evaluate(expr.expression);
}

    // Adicione à classe Interpreter

@Override
public Object visitUnaryExpr(Expr.Unary expr) {
    // 1. Avalie o operando primeiro.
    Object right = evaluate(expr.right);

    // 2. Aplique o operador.
    switch (expr.operator.type) {
        case BANG:
            return !isTruthy(right);
        case MINUS:
            checkNumberOperand(expr.operator, right);
            return -(double)right;
    }

    // Inalcançável.
    return null;
}

// Lógica de "verdadeiro" ou "falso" do Lox: nil e false são falsos, todo o resto é verdadeiro.
private boolean isTruthy(Object object) {
    if (object == null) return false;
    if (object instanceof Boolean) return (boolean)object;
    return true;
}
    
}
