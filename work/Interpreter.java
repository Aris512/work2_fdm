package work;

import work.analysis.DepthFirstAdapter;
import work.node.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Interpreter extends DepthFirstAdapter {

    private final Map<String, Object> variables = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    // declara una variable int e inicia en 0.
    @Override
    public void caseAIntDeclarationDeclaration(AIntDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), 0);
    }

    // declara una variable double e inicia en 0.0.
    @Override
    public void caseADoubleDeclarationDeclaration(ADoubleDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), 0.0);
    }

    // declara una variable string e inicia en "".
    @Override
    public void caseAStringDeclarationDeclaration(AStringDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), "");
    }

    // ======= Declaraciones y asignaciones =======
    // declara una variable int y la asigna a un valor.
    @Override
    public void caseAIntDeclarationAssignmentDeclaration(AIntDeclarationAssignmentDeclaration node) {
        String varName = node.getVar().getText();
        Object value = evalExpr(node.getExpr());
        variables.put(varName, value);
    }

    // declara una variable double y la asigna a un valor.
    @Override
    public void caseADoubleDeclarationAssignmentDeclaration(ADoubleDeclarationAssignmentDeclaration node) {
        String varName = node.getVar().getText();
        Object value = evalExpr(node.getExpr());
        variables.put(varName, value);
    }

    // declara una variable string y la asigna a un valor.
    @Override
    public void caseAStringDeclarationAssignmentDeclaration(AStringDeclarationAssignmentDeclaration node) {
        String varName = node.getVar().getText();
        String value = node.getStringLiteral().getText().replace("\"", "");
        variables.put(varName, value);
    }

    // asignar un valor de cadena a una variable string.
    @Override
    public void caseAStrAssignmentAssignment(AStrAssignmentAssignment node) {
        String varName = node.getVar().getText();
        String value = node.getStringLiteral().getText().replace("\"", "");
        variables.put(varName, value);
    }

    // asignar el resultado a una variable int o double.
    @Override
    public void caseAExprAssignmentAssignment(AExprAssignmentAssignment node) {
        String varName = node.getVar().getText();
        Object value = evalExpr(node.getExpr());
        variables.put(varName, value);
    }

    // ======= Imprimir =======
    // imprimir el valor de una variable y un salto de línea.
    @Override
    public void caseAPrintlnVarLine(APrintlnVarLine node) {
        System.out.println(variables.getOrDefault(node.getVar().getText(), "undefined"));
    }

    // imprimir un numero y salto de línea.
    @Override
    public void caseAPrintlnNumberLine(APrintlnNumberLine node) {
        System.out.println(node.getNumber().getText());
    }

    // imprimir una cadena y salto de línea.
    @Override
    public void caseAPrintlnStringLine(APrintlnStringLine node) {
        System.out.println(node.getStringLiteral().getText().replace("\"", ""));
    }

    // imprimir el valor de una variable sin salto de línea.
    @Override
    public void caseAPrintVarLine(APrintVarLine node) {
        System.out.print(variables.getOrDefault(node.getVar().getText(), "undefined"));
    }

    // imprimir un numero sin salto de línea.
    @Override
    public void caseAPrintNumberLine(APrintNumberLine node) {
        System.out.print(node.getNumber().getText());
    }

    // imprimir una cadena sin salto de línea.
    @Override
    public void caseAPrintStringLine(APrintStringLine node) {
        System.out.print(node.getStringLiteral().getText().replace("\"", ""));
    }

    // ======= Líneas de entrada y control de flujo =======
    // lee una entrada del usuario y la asigna a una variable.
    @Override
    public void caseAInputLine(AInputLine node) {
        String varName = node.getVar().getText();
        String input = scanner.nextLine();
        Object oldValue = variables.get(varName);
        if (oldValue instanceof Integer) {
            try {
                variables.put(varName, Integer.parseInt(input));
            } catch (Exception e) {
                variables.put(varName, 0);
            }
        } else if (oldValue instanceof Double) {
            try {
                variables.put(varName, Double.parseDouble(input));
            } catch (Exception e) {
                variables.put(varName, 0.0);
            }
        } else {
            variables.put(varName, input);
        }
    }

    // control de flujo: líneas de control como if, if/else, while.
    @Override
    public void caseAFlowControlLine(AFlowControlLine node) {
        node.getFlowControl().apply(this);
    }

    // ejecuta una línea de asignación.
    @Override
    public void caseAAssignmentLine(AAssignmentLine node) {
        node.getAssignment().apply(this);
    }

    // ejecuta el bloque if
    @Override
    public void caseAIfFlowControl(AIfFlowControl node) {
        if (evalCondicion(node.getCondition())) {
            for (PLine line : node.getLine()) {
                line.apply(this);
            }
        }
    }

    // ejecuta el bloque if-else
    @Override
    public void caseAIfElseFlowControl(AIfElseFlowControl node) {
        if (evalCondicion(node.getCondition())) {
            for (PLine line : node.getLine()) {
                line.apply(this);
            }
        } else {
            AElseStatement elseNode = (AElseStatement) node.getElseStatement();
            for (PLine line : elseNode.getLine()) {
                line.apply(this);
            }
        }
    }

    // ejecuta el bloque while
    @Override
    public void caseAWhileFlowControl(AWhileFlowControl node) {
        while (evalCondicion(node.getCondition())) {
            for (PLine line : node.getLine()) {
                line.apply(this);
            }
        }
    }

    // ======= Evaluación de condiciones y expresiones =======

    // evaluar una condición logica.
    private boolean evalCondicion(PCondition cond) {
        if (cond instanceof ASecondConditionCondition) {
            return evalSecondCondicion(((ASecondConditionCondition) cond).getSecondCondition());
        } else if (cond instanceof AAndCondition) {
            AAndCondition and = (AAndCondition) cond;
            return evalCondicion(and.getCondition()) && evalSecondCondicion(and.getSecondCondition());
        } else if (cond instanceof AOrCondition) {
            AOrCondition or = (AOrCondition) cond;
            return evalCondicion(or.getCondition()) || evalSecondCondicion(or.getSecondCondition());
        }
        return false;
    }

    // evaluar una segunda condición.
    private boolean evalSecondCondicion(PSecondCondition cond) {
        if (cond instanceof ADoubleEqualsSecondCondition) {
            ADoubleEqualsSecondCondition eq = (ADoubleEqualsSecondCondition) cond;
            Object left = eval(eq.getItem1());
            Object right = eval(eq.getItem2());
            return compareEquals(left, right);
        } else if (cond instanceof ANotEqualsSecondCondition) {
            ANotEqualsSecondCondition neq = (ANotEqualsSecondCondition) cond;
            Object left = eval(neq.getItem1());
            Object right = eval(neq.getItem2());
            return !compareEquals(left, right);
        } else if (cond instanceof AGreaterSecondCondition) {
            AGreaterSecondCondition gt = (AGreaterSecondCondition) cond;
            Object left = eval(gt.getItem1());
            Object right = eval(gt.getItem2());
            return toDouble(left) > toDouble(right);
        } else if (cond instanceof AGreaterEqSecondCondition) {
            AGreaterEqSecondCondition ge = (AGreaterEqSecondCondition) cond;
            Object left = eval(ge.getItem1());
            Object right = eval(ge.getItem2());
            return toDouble(left) >= toDouble(right);
        } else if (cond instanceof ALessSecondCondition) {
            ALessSecondCondition lt = (ALessSecondCondition) cond;
            Object left = eval(lt.getItem1());
            Object right = eval(lt.getItem2());
            return toDouble(left) < toDouble(right);
        } else if (cond instanceof ALessEqSecondCondition) {
            ALessEqSecondCondition le = (ALessEqSecondCondition) cond;
            Object left = eval(le.getItem1());
            Object right = eval(le.getItem2());
            return toDouble(left) <= toDouble(right);
        }
        throw new RuntimeException("Unknown condition type: " + cond.getClass().getSimpleName());
    }

    // Comparar igualdad considerando números y cadenas
    private boolean compareEquals(Object left, Object right) {
        if (left instanceof Number && right instanceof Number) {
            return toDouble(left) == toDouble(right);
        }
        return String.valueOf(left).equals(String.valueOf(right));
    }

    // Evaluar una expresión aritmética o de cadena.
    private Object evalExpr(PExpr expr) {
        if (expr instanceof APlusExpr) {
            Object left = evalExpr(((APlusExpr) expr).getExpr());
            Object right = evalTerm(((APlusExpr) expr).getTerm());
            if (left instanceof String || right instanceof String) {
                return String.valueOf(left) + String.valueOf(right);
            }
            return toDouble(left) + toDouble(right);
        } else if (expr instanceof AMinusExpr) {
            Object left = evalExpr(((AMinusExpr) expr).getExpr());
            Object right = evalTerm(((AMinusExpr) expr).getTerm());
            if (left instanceof String || right instanceof String) {
                return "";
            }
            return toDouble(left) - toDouble(right);
        } else if (expr instanceof ATermExpr) {
            return evalTerm(((ATermExpr) expr).getTerm());
        }
        return 0;
    }

    // Evaluar un término (multiplicación, división, módulo, o unario)
    private Object evalTerm(PTerm term) {
        if (term instanceof AMultTerm) {
            Object left = evalTerm(((AMultTerm) term).getTerm());
            Object right = evalUnary(((AMultTerm) term).getUnary());
            return toDouble(left) * toDouble(right);
        } else if (term instanceof ADivTerm) {
            Object left = evalTerm(((ADivTerm) term).getTerm());
            Object right = evalUnary(((ADivTerm) term).getUnary());
            return toDouble(left) / toDouble(right);
        } else if (term instanceof AModTerm) {
            Object left = evalTerm(((AModTerm) term).getTerm());
            Object right = evalUnary(((AModTerm) term).getUnary());
            return toDouble(left) % toDouble(right);
        } else if (term instanceof AUnaryTerm) {
            return evalUnary(((AUnaryTerm) term).getUnary());
        }
        return 0;
    }

    // Evaluar unario (negativo o factor)
    private Object evalUnary(PUnary unary) {
        if (unary instanceof ANegUnary) {
            Object value = evalUnary(((ANegUnary) unary).getUnary());
            return -toDouble(value);
        } else if (unary instanceof AFactorUnary) {
            return evalFactor(((AFactorUnary) unary).getFactor());
        }
        return 0;
    }

    // Evaluar un factor (número, variable, agrupación)
    private Object evalFactor(PFactor factor) {
        if (factor instanceof ANumberFactor) {
            String text = ((ANumberFactor) factor).getNumber().getText();
            if (text.contains(".")) {
                return Double.parseDouble(text);
            } else {
                return Integer.parseInt(text);
            }
        } else if (factor instanceof AVarFactor) {
            return variables.getOrDefault(((AVarFactor) factor).getVar().getText(), 0);
        } else if (factor instanceof AGroupedFactor) {
            return evalExpr(((AGroupedFactor) factor).getExpr());
        }
        return 0;
    }

    // Método general para evaluar cualquier nodo de expresión
    public Object eval(Node node) {
        if (node instanceof PExpr) {
            return evalExpr((PExpr) node);
        } else if (node instanceof PFactor) {
            return evalFactor((PFactor) node);
        } else if (node instanceof PTerm) {
            return evalTerm((PTerm) node);
        } else if (node instanceof AExprItem1) {
            // Soporte para condiciones relacionales como i<=f
            AExprItem1 expr = (AExprItem1) node;
            return eval(expr.getExpr());
        } else if (node instanceof AExprItem2) {
            AExprItem2 expr = (AExprItem2) node;
            return eval(expr.getExpr());
        }
        throw new RuntimeException("Tipo de nodo no soportado en eval: " + node.getClass().getSimpleName());
    }

    // Convierte un objeto a double, si es posible
    private double toDouble(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        String s = value.toString().trim();
        if (s.isEmpty())
            return 0;
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            throw new RuntimeException("No se puede convertir a double: " + value);
        }
    }

    // ======= Incremento y decremento =======
    // incrementa el valor de una variable en 1.
    @Override
    public void caseAIncrementLine(AIncrementLine node) {
        String varName = node.getVar().getText();
        Object value = variables.getOrDefault(varName, 0);
        if (value instanceof Number) {
            variables.put(varName, ((Number) value).doubleValue() + 1);
        } else {
            try {
                double v = Double.parseDouble(value.toString());
                variables.put(varName, v + 1);
            } catch (Exception e) {
                variables.put(varName, 1);
            }
        }
    }

    // decrementa el valor de una variable en 1.
    @Override
    public void caseADecrementLine(ADecrementLine node) {
        String varName = node.getVar().getText();
        Object value = variables.getOrDefault(varName, 0);
        if (value instanceof Number) {
            variables.put(varName, ((Number) value).doubleValue() - 1);
        } else {
            try {
                double v = Double.parseDouble(value.toString());
                variables.put(varName, v - 1);
            } catch (Exception e) {
                variables.put(varName, -1);
            }
        }
    }

}