package work;

import work.analysis.DepthFirstAdapter;
import work.node.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Interpreter extends DepthFirstAdapter {

    private final Map<String, Object> variables = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    //declara una variable int e inicia en 0.
    @Override
    public void casoAIntDeclaracion(AIntDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), 0);
    }
    
    //declara una variable double e inicia en 0.0.
    @Override
    public void casoADoubleDeclaracion(ADoubleDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), 0.0);
    }

    //declara una variable string e inicia en "".
    @Override
    public void casoAStringDeclaracion(AStringDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), "");
    }

    // ======= Declaraciones y asignaciones =======
    //declara una variable int y la asigna a un valor.
    @Override
    public void casoAIntAsignar(AIntDeclarationAssignmentDeclaration node) {
        node.getAssignment().apply(this);
    }

    //declara una variable double y la asigna a un valor.
    @Override
    public void casoADoubleAsignar(ADoubleDeclarationAssignmentDeclaration node) {
        node.getAssignment().apply(this);
    }

    //declara una variable string y la asigna a un valor.
    @Override
    public void casoAStringAsignar(AStringDeclarationAssignmentDeclaration node) {
        node.getAssignment().apply(this);
    }

    //asignar un valor de cadena a una variable string.
    @Override
    public void casoAStringCadenaAsignar(AStrAssignmentAssignment node) {
        String varName = node.getVar().getText();
        String value = node.getStringLiteral().getText().replace("\"", "");
        variables.put(varName, value);
    }

    //asignar el resultado a una variable int o double.
    @Override
    public void casoAExpresionAsignar(AExprAssignmentAssignment node) {
        String varName = node.getVar().getText();
        Object value = evalExpr(node.getExpr());
        variables.put(varName, value);
    }

    // ======= Imprimir =======
    //imprimir el valor de una variable y un salto de línea.
      @Override
    public void casoAPrintlnVariable(APrintlnVarLine node) {
        System.out.println(variables.getOrDefault(node.getVar().getText(), "undefined"));
    }

    //imprimir un numero y salto de línea.
    @Override
    public void casoAPrintlnNumero(APrintlnNumberLine node) {
        System.out.println(node.getNumber().getText());
    }

    //imprimir una cadena y salto de línea.
    @Override
    public void casoAPrintlnString(APrintlnStringLine node) {
        System.out.println(node.getStringLiteral().getText().replace("\"", ""));
    }

    //imprimir el valor de una variable sin salto de línea.
    @Override
    public void casoAPrintVariable(APrintVarLine node) {
        System.out.println(variables.getOrDefault(node.getVar().getText(), "undefined"));
    }

    //imprimir un numero sin salto de línea.
    @Override
    public void casoAPrintNumero(APrintNumberLine node) {
        System.out.println(node.getNumber().getText());
    }

    //imprimir una cadena sin salto de línea.
    @Override
    public void casoAPrintString(APrintStringLine node) {
        System.out.println(node.getStringLiteral().getText().replace("\"", ""));
    }

    // ======= Líneas de entrada y control de flujo =======
    //lee una entrada del usuario y la asigna a una variable.
    @Override
    public void casoAEntrada(AInputLine node) {
        String input = scanner.nextLine();
        variables.put(node.getVar().getText(), input);
    }

    //control de flujo: líneas de control como if, if/else, while.
    @Override
    public void casoAControlFlujo(AFlowControlLine node) {
        node.getFlowControl().apply(this);
    }

    //ejecuta una línea de asignación.
    @Override
    public void casoALineaAsignacion(AAssignmentLine node) {
        node.getAssignment().apply(this);
    }

    //ejecuta el bloque if
    @Override
    public void casoAIf(AIfFlowControl node) {
        if (evalCondicion(node.getCondition())) {
            for (PLine line : node.getLine()) {
                line.apply(this);
            }
        }
    }

    //ejecuta el bloque if-else
    @Override
    public void casoAIfElse(AIfElseFlowControl node) {
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

    //ejecuta el bloque while
    @Override
    public void casoAWhile(AWhileFlowControl node) {
        while (evalCondicion(node.getCondition())) {
            for (PLine line : node.getLine()) {
                line.apply(this);
            }
        }
    }

    // ======= Evaluación de condiciones y expresiones =======

    //evaluar una condición logica.
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

    //evaluar una segunda condición.
    private boolean evalSecondCondicion(PSecondCondition cond) {
        if (cond instanceof ADoubleEqualsSecondCondition) {
            ADoubleEqualsSecondCondition eq = (ADoubleEqualsSecondCondition) cond;
            Object left = getValorItem(eq.getItem1());
            Object right = getValorItem(eq.getItem2());
            return left.equals(right);
        } else if (cond instanceof ANotEqualsSecondCondition) {
            ANotEqualsSecondCondition neq = (ANotEqualsSecondCondition) cond;
            Object left = getValorItem(neq.getItem1());
            Object right = getValorItem(neq.getItem2());
            return !left.equals(right);
        } else if (cond instanceof AGreaterSecondCondition) {
            AGreaterSecondCondition gt = (AGreaterSecondCondition) cond;
            Object left = getValorItem(gt.getItem1());
            Object right = getValorItem(gt.getItem2());
            return toDouble(left) > toDouble(right);
        } else if (cond instanceof AGreaterEqSecondCondition) {
            AGreaterEqSecondCondition ge = (AGreaterEqSecondCondition) cond;
            Object left = getValorItem(ge.getItem1());
            Object right = getValorItem(ge.getItem2());
            return toDouble(left) >= toDouble(right);
        } else if (cond instanceof ALessSecondCondition) {
            ALessSecondCondition lt = (ALessSecondCondition) cond;
            Object left = getValorItem(lt.getItem1());
            Object right = getValorItem(lt.getItem2());
            return toDouble(left) < toDouble(right);
        } else if (cond instanceof ALessEqSecondCondition) {
            ALessEqSecondCondition le = (ALessEqSecondCondition) cond;
            Object left = getValorItem(le.getItem1());
            Object right = getValorItem(le.getItem2());
            return toDouble(left) <= toDouble(right);
        } else if (cond instanceof AGroupedSecondCondition) {
            AGroupedSecondCondition group = (AGroupedSecondCondition) cond;
            return evalCondicion(group.getCondition());
        } else if (cond instanceof ADoubleEqualsSecondCondition) {
            ADoubleEqualsSecondCondition eq = (ADoubleEqualsSecondCondition) cond;
            Object left = getValorItem(eq.getItem1());
            Object right = getValorItem(eq.getItem2());
            return left.equals(right);
        } else if (cond instanceof ANotEqualsSecondCondition) {
            ANotEqualsSecondCondition neq = (ANotEqualsSecondCondition) cond;
            Object left = getValorItem(neq.getItem1());
            Object right = getValorItem(neq.getItem2());
            return !left.equals(right);
        }

        throw new RuntimeException("Unknown condition type: " + cond.getClass().getSimpleName());
    }

    //obtener el valor de un item, ya sea variable, número o cadena.
    private Object getValorItem(PItem1 item) {
        if (item instanceof AVarItem1) {
            return variables.getOrDefault(((AVarItem1) item).getVar().getText(), 0);
        } else if (item instanceof ANumberItem1) {
            return Double.parseDouble(((ANumberItem1) item).getNumber().getText());
        } else if (item instanceof AStrItem1) {
            return ((AStrItem1) item).getStringLiteral().getText().replace("\"", "");
        }
        return 0;
    }

    //obtener el valor de un item 2, ya sea variable, número o cadena.
    private Object getValorItem(PItem2 item) {
        if (item instanceof AVarItem2) {
            return variables.getOrDefault(((AVarItem2) item).getVar().getText(), 0);
        } else if (item instanceof ANumberItem2) {
            return Double.parseDouble(((ANumberItem2) item).getNumber().getText());
        } else if (item instanceof AStrItem2) {
            return ((AStrItem2) item).getStringLiteral().getText().replace("\"", "");
        }
        return 0;
    }

    //convertir un valor a double.
    private double toDouble(Object val) {
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        }
        try {
            return Double.parseDouble(val.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    //evaluar una expresion aritmética.
    private Object evalExpr(PExpr expr) {
        if (expr instanceof AFactorExpr) {
            return evalFactor(((AFactorExpr) expr).getFactor());
        } else if (expr instanceof APlusExpr) {
            return toDouble(evalExpr(((APlusExpr) expr).getExpr())) +
                    toDouble(evalFactor(((APlusExpr) expr).getFactor()));
        } else if (expr instanceof AMinusExpr) {
            return toDouble(evalExpr(((AMinusExpr) expr).getExpr())) -
                    toDouble(evalFactor(((AMinusExpr) expr).getFactor()));
        }
        return 0;
    }

    //evaluar un factor, que puede ser un término o una operación aritmética.
    private double evalFactor(PFactor factor) {
        if (factor instanceof ATermFactor) {
            return evalTerm(((ATermFactor) factor).getTerm());
        } else if (factor instanceof AMultFactor) {
            return evalFactor(((AMultFactor) factor).getFactor()) *
                    evalTerm(((AMultFactor) factor).getTerm());
        } else if (factor instanceof ADivFactor) {
            return evalFactor(((ADivFactor) factor).getFactor()) /
                    evalTerm(((ADivFactor) factor).getTerm());
        } else if (factor instanceof AModFactor) {
            return evalFactor(((AModFactor) factor).getFactor()) %
                    evalTerm(((AModFactor) factor).getTerm());
        }
        return 0;
    }

    //evaluar un término, que puede ser un número, una variable o una expresión.
    private double evalTerm(PTerm term) {
        if (term instanceof ANumberTerm) {
            return Double.parseDouble(((ANumberTerm) term).getNumber().getText());
        } else if (term instanceof AVarTerm) {
            Object val = variables.getOrDefault(((AVarTerm) term).getVar().getText(), 0);
            return toDouble(val);
        } else if (term instanceof AExprTerm) {
            return toDouble(evalExpr(((AExprTerm) term).getExpr()));
        }
        return 0;
    }

    // ======= Incremento y decremento =======
    //incrementa el valor de una variable en 1.
    @Override
    public void casoAIncrementar(AIncrementLine node) {
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

    //decrementa el valor de una variable en 1.
    @Override
    public void casoADecrementar(ADecrementLine node) {
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