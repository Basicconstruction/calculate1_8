package parser;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    protected static final boolean debug = true;
    protected String operations;

    public Parser() {}

    public Parser(String operations) {
        this.operations = operations;
    }

    public int getIntegerResult() {
        return parseResultInteger(operations);
    }

    protected int parseResultInteger(String operations) {
        return (int) evaluateExpression(operations);
    }

    protected boolean isDigit(char c) {
        return c >= '0' && c <= '9' || c == '.';
    }

    protected boolean isSpace(char c) {
        return c == ' ';
    }

    protected ArrayList<String> splitExpression(String expression) {
        ArrayList<String> v = new ArrayList<>();
        StringBuilder numberString = new StringBuilder();
        boolean instart = true;
        boolean inStatusFix = false;
        for (int i = 0; i < expression.length(); i++) {
            if (isDigit(expression.charAt(i))) {
                numberString.append(expression.charAt(i));
                instart = false;
            } else {
                if (numberString.toString().length() > 0) {
                    v.add(numberString.toString());
                    numberString = new StringBuilder();
                }
                if (instart) {
                    if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                        inStatusFix = true;
                        numberString.append(expression.charAt(i) == '-' ? '-' : "");
                        instart = false;
                    } else if (expression.charAt(i) == '(') {
                        instart = true;
                        v.add("(");
                    } else if (expression.charAt(i) != ' ') {
                        v.add("" + expression.charAt(i));
                    }
                } else {
                    if (numberString.toString().length() > 0) {
                        v.add(numberString.toString());
                        numberString = new StringBuilder();
                    }
                    if (expression.charAt(i) == '(') {
                        instart = true;
                        v.add("(");
                    } else if (expression.charAt(i) != ' ') {
                        v.add("" + expression.charAt(i));
                    }
                }
            }
        }
        if (numberString.toString().length() > 0) {
            v.add(numberString.toString());
        }
        if (debug) {
            for (String s : v) {
                System.out.println("v: " + s);
            }
        }
        return v;
    }

    protected double evaluateExpression(String expression) {
        Stack<Integer> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        ArrayList<String> tokens = splitExpression(expression);
        for (String token : tokens) {
            if (token.charAt(0) == '+' || token.charAt(0) == '-') {
                while (!operatorStack.isEmpty()
                        && (operatorStack.peek() == '+'
                        || operatorStack.peek() == '-'
                        || operatorStack.peek() == '*'
                        || operatorStack.peek() == '/')) {
                    processAnOperatorInteger(operandStack, operatorStack);
                }
                operatorStack.push(token.charAt(0));
            } else if (token.charAt(0) == '*' || token.charAt(0) == '/') {
                while (!operatorStack.isEmpty()
                        && (operatorStack.peek() == '*' || operatorStack.peek() == '/')) {
                    processAnOperatorInteger(operandStack, operatorStack);
                }
                operatorStack.push(token.charAt(0));
            } else if (token.charAt(0) == '(') {
                operatorStack.push('(');
            } else if (token.charAt(0) == ')') {
                while (operatorStack.peek() != '(') {
                    processAnOperatorInteger(operandStack, operatorStack);
                }
                operatorStack.pop();
            } else {
                operandStack.push(Integer.parseInt(token));
            }
        }
        if (debug) {
            for (Integer i : operandStack) {
                System.out.println("operandSTack: " + i);
            }
            for (char c : operatorStack) {
                System.out.println("operatorStack: " + c);
            }
        }
        while (!operatorStack.isEmpty()) {
            processAnOperatorInteger(operandStack, operatorStack);
        }
        return (double) operandStack.pop();
    }

    protected void processAnOperatorInteger(
            Stack<Integer> operandStack, Stack<Character> operatorStack) {
        char op = operatorStack.pop();
        int op1 = operandStack.pop();
        int op2 = operandStack.pop();
        if (op == '+') {
            operandStack.push(op2 + op1);
        } else if (op == '-') {
            operandStack.push(op2 - op1);
        } else if (op == '*') {
            operandStack.push(op2 * op1);
        } else if (op == '/') {
            operandStack.push(op2 / op1);
        }
    }
}

//    protected ArrayList<String> splitExpression(String expression){
//        ArrayList<String> v = new ArrayList<>();
//        StringBuilder numberString = new StringBuilder();
//        for(int i = 0;i<expression.length(); i++){
//            if(isDigit(expression.charAt(i))){
//                numberString.append(expression.charAt(i));
//            }else{
//                if(numberString.toString().length()>0){
//                    v.add(numberString.toString());
//                    numberString = new StringBuilder();
//                }
//                if(!isSpace(expression.charAt(i))){
//                    String s = ""+expression.charAt(i);
//                    v.add(s);
//                }
//            }
//        }
//        if(numberString.toString().length()>0){
//            v.add(numberString.toString());
//        }
//        if(debug){
//            for(String s:v){
//                System.out.println("v: "+s);
//            }
//        }
//        return v;
//    }
