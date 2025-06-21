package work;

import work.analysis.DepthFirstAdapter;
import work.node.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Interpreter extends DepthFirstAdapter {

    private final Map<String, Object> variables = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void caseAIntDeclarationDeclaration(AIntDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), 0);
    }

    @Override
    public void caseADoubleDeclarationDeclaration(ADoubleDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), 0.0);
    }

    @Override
    public void caseAStringDeclarationDeclaration(AStringDeclarationDeclaration node) {
        variables.put(node.getVar().getText(), "");
    }

    @Override
    public void caseAIntDeclarationAssignmentDeclaration(AIntDeclarationAssignmentDeclaration node) {
        node.getAssignment().apply(this);
    }

    @Override
    public void caseADoubleDeclarationAssignmentDeclaration(ADoubleDeclarationAssignmentDeclaration node) {
        node.getAssignment().apply(this);
    }

    @Override
    public void caseAStringDeclarationAssignmentDeclaration(AStringDeclarationAssignmentDeclaration node) {
        node.getAssignment().apply(this);
    }

    @Override
    public void caseAStrAssignmentAssignment(AStrAssignmentAssignment node) {
        String varName = node.getVar().getText();
        String value = node.getStringLiteral().getText().replace("\"", "");
        variables.put(varName, value);
    }

    @Override
    public void caseAExprAssignmentAssignment(AExprAssignmentAssignment node) {
        String varName = node.getVar().getText();
        Object value = evalExpr(node.getExpr());
        variables.put(varName, value);
    }

      @Override
    public void caseAPrintlnVarLine(APrintlnVarLine node) {
        System.out.println(variables.getOrDefault(node.getVar().getText(), "undefined"));
    }

    @Override
    public void caseAPrintlnNumberLine(APrintlnNumberLine node) {
        System.out.println(node.getNumber().getText());
    }

    @Override
    public void caseAPrintlnStringLine(APrintlnStringLine node) {
        System.out.println(node.getStringLiteral().getText().replace("\"", ""));
    }


    @Override
    public void caseAPrintVarLine(APrintVarLine node) {
        System.out.println(variables.getOrDefault(node.getVar().getText(), "undefined"));
    }

    @Override
    public void caseAPrintNumberLine(APrintNumberLine node) {
        System.out.println(node.getNumber().getText());
    }

    @Override
    public void caseAPrintStringLine(APrintStringLine node) {
        System.out.println(node.getStringLiteral().getText().replace("\"", ""));
    }

    @Override
    public void caseAInputLine(AInputLine node) {
        System.out.print("Ingrese valor para " + node.getVar().getText() + ": ");
        String input = scanner.nextLine();
        variables.put(node.getVar().getText(), input);
    }

    @Override
    public void caseAFlowControlLine(AFlowControlLine node) {
        node.getFlowControl().apply(this);
    }

    @Override
    public void caseAAssignmentLine(AAssignmentLine node) {
        node.getAssignment().apply(this);
    }

    @Override
    public void caseAIfFlowControl(AIfFlowControl node) {
        if (evalCondition(node.getCondition())) {
            for (PLine line : node.getLine()) {
                line.apply(this);
            }
        }
    }

    @Override
    public void caseAIfElseFlowControl(AIfElseFlowControl node) {
        if (evalCondition(node.getCondition())) {
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

    @Override
    public void caseAWhileFlowControl(AWhileFlowControl node) {
        while (evalCondition(node.getCondition())) {
            for (PLine line : node.getLine()) {
                line.apply(this);
            }
        }
    }

    // ======= Evaluación de condiciones y expresiones =======

    private boolean evalCondition(PCondition cond) {
        if (cond instanceof ASecondConditionCondition) {
            return evalSecondCondition(((ASecondConditionCondition) cond).getSecondCondition());
        } else if (cond instanceof AAndCondition) {
            AAndCondition and = (AAndCondition) cond;
            return evalCondition(and.getCondition()) && evalSecondCondition(and.getSecondCondition());
        } else if (cond instanceof AOrCondition) {
            AOrCondition or = (AOrCondition) cond;
            return evalCondition(or.getCondition()) || evalSecondCondition(or.getSecondCondition());
        }
        return false;
    }

    private boolean evalSecondCondition(PSecondCondition cond) {
        if (cond instanceof ADoubleEqualsSecondCondition) {
            ADoubleEqualsSecondCondition eq = (ADoubleEqualsSecondCondition) cond;
            Object left = getItemValue(eq.getItem1());
            Object right = getItemValue(eq.getItem2());
            return left.equals(right);
        } else if (cond instanceof ANotEqualsSecondCondition) {
            ANotEqualsSecondCondition neq = (ANotEqualsSecondCondition) cond;
            Object left = getItemValue(neq.getItem1());
            Object right = getItemValue(neq.getItem2());
            return !left.equals(right);
        } else if (cond instanceof AGreaterSecondCondition) {
            AGreaterSecondCondition gt = (AGreaterSecondCondition) cond;
            Object left = getItemValue(gt.getItem1());
            Object right = getItemValue(gt.getItem2());
            return toDouble(left) > toDouble(right);
        } else if (cond instanceof AGreaterEqSecondCondition) {
            AGreaterEqSecondCondition ge = (AGreaterEqSecondCondition) cond;
            Object left = getItemValue(ge.getItem1());
            Object right = getItemValue(ge.getItem2());
            return toDouble(left) >= toDouble(right);
        } else if (cond instanceof ALessSecondCondition) {
            ALessSecondCondition lt = (ALessSecondCondition) cond;
            Object left = getItemValue(lt.getItem1());
            Object right = getItemValue(lt.getItem2());
            return toDouble(left) < toDouble(right);
        } else if (cond instanceof ALessEqSecondCondition) {
            ALessEqSecondCondition le = (ALessEqSecondCondition) cond;
            Object left = getItemValue(le.getItem1());
            Object right = getItemValue(le.getItem2());
            return toDouble(left) <= toDouble(right);
        } else if (cond instanceof AGroupedSecondCondition) {
            AGroupedSecondCondition group = (AGroupedSecondCondition) cond;
            return evalCondition(group.getCondition());
        } else if (cond instanceof ADoubleEqualsSecondCondition) {
            ADoubleEqualsSecondCondition eq = (ADoubleEqualsSecondCondition) cond;
            Object left = getItemValue(eq.getItem1());
            Object right = getItemValue(eq.getItem2());
            return left.equals(right);
        } else if (cond instanceof ANotEqualsSecondCondition) {
            ANotEqualsSecondCondition neq = (ANotEqualsSecondCondition) cond;
            Object left = getItemValue(neq.getItem1());
            Object right = getItemValue(neq.getItem2());
            return !left.equals(right);
        }

        throw new RuntimeException("Unknown condition type: " + cond.getClass().getSimpleName());
    }

    private Object getItemValue(PItem1 item) {
        if (item instanceof AVarItem1) {
            return variables.getOrDefault(((AVarItem1) item).getVar().getText(), 0);
        } else if (item instanceof ANumberItem1) {
            return Double.parseDouble(((ANumberItem1) item).getNumber().getText());
        } else if (item instanceof AStrItem1) {
            return ((AStrItem1) item).getStringLiteral().getText().replace("\"", "");
        }
        return 0;
    }

    private Object getItemValue(PItem2 item) {
        if (item instanceof AVarItem2) {
            return variables.getOrDefault(((AVarItem2) item).getVar().getText(), 0);
        } else if (item instanceof ANumberItem2) {
            return Double.parseDouble(((ANumberItem2) item).getNumber().getText());
        } else if (item instanceof AStrItem2) {
            return ((AStrItem2) item).getStringLiteral().getText().replace("\"", "");
        }
        return 0;
    }

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