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


@Override
public Object visitBinaryExpr(Expr.Binary expr) {
    // Avalia ambos os operandos antes de aplicar o operador.
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {
        // Operadores de comparação
        case GREATER:
            checkNumberOperands(expr.operator, left, right);
            return (double)left > (double)right;
        case GREATER_EQUAL:
            checkNumberOperands(expr.operator, left, right);
            return (double)left >= (double)right;
        case LESS:
            checkNumberOperands(expr.operator, left, right);
            return (double)left < (double)right;
        case LESS_EQUAL:
            checkNumberOperands(expr.operator, left, right);
            return (double)left <= (double)right;

        // Operadores de igualdade
        case BANG_EQUAL: return !isEqual(left, right);
        case EQUAL_EQUAL: return isEqual(left, right);

        // Operadores aritméticos
        case MINUS:
            checkNumberOperands(expr.operator, left, right);
            return (double)left - (double)right;
        case SLASH:
            checkNumberOperands(expr.operator, left, right);
            if ((double)right == 0) {
                throw new RuntimeError(expr.operator, "Division by zero.");
            }
            return (double)left / (double)right;
        case STAR:
            checkNumberOperands(expr.operator, left, right);
            return (double)left * (double)right;
        case PLUS:
            // O '+' é especial: pode somar números ou concatenar strings.
            if (left instanceof Double && right instanceof Double) {
                return (double)left + (double)right;
            }
            if (left instanceof String && right instanceof String) {
                return (String)left + (String)right;
            }
            // Se os tipos forem misturados, lance um erro.
            throw new RuntimeError(expr.operator,
                "Operands must be two numbers or two strings.");
    }

    // Inalcançável.
    return null;
}

// Compara dois objetos para igualdade, tratando nil.
private boolean isEqual(Object a, Object b) {
    if (a == null && b == null) return true;
    if (a == null) return false;
    return a.equals(b);
}
    
}
