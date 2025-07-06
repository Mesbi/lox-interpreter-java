package com.lox;

class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    private Environment environment = new Environment(); // Começa com o ambiente global

    // Método principal que inicia a interpretação.
    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    // Método que efetivamente dispara o mecanismo do Visitor.
    private void execute(Stmt stmt) {
        stmt.accept(this);
    }
    
      // Implemente os métodos 'visit' para DECLARAÇÕES.
    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    // Método auxiliar para executar um bloco em um novo escopo.
    void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous; // Restaura o ambiente anterior
        }
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression); // Avalia a expressão e descarta o resultado.
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    // Implemente os métodos 'visit' para as NOVAS EXPRESSÕES.
    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return environment.get(expr.name);
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

   private void checkNumberOperand(Token operator, Object operand) {
    if (operand instanceof Double) return;
    throw new RuntimeError(operator, "Operand must be a number.");
} 
    private void checkNumberOperands(Token operator, Object left, Object right) {
    if (left instanceof Double && right instanceof Double) return;
    throw new RuntimeError(operator, "Operands must be numbers.");
}
    
}
