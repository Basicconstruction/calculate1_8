package parser;

import java.util.ArrayList;
import java.util.Stack;

public class DoubleParser extends Parser{
    public DoubleParser(){

    }
    public DoubleParser(String operations){
        this.operations = operations;
        if(debug){
            System.out.println(operations);
        }
    }
    public double getDoubleResult(){
        return parseDoubleResult(operations);
    }
    protected double parseDoubleResult(String operations){
        return evaluateExpression(operations);
    }
    protected double evaluateExpression(String expression){
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        ArrayList<String> tokens = splitExpression(expression);
        for (String token : tokens) {
            if ((token.charAt(0) == '+' || token.charAt(0) == '-')&&token.length()==1) {
                while (!operatorStack.isEmpty() && (operatorStack.peek() == '+' || operatorStack.peek() == '-'
                        || operatorStack.peek() == '*' || operatorStack.peek() == '/')) {
                    processAnOperatorDouble(operandStack, operatorStack);
                }
                operatorStack.push(token.charAt(0));
            } else if (token.charAt(0) == '*' || token.charAt(0) == '/') {
                while (!operatorStack.isEmpty() && (operatorStack.peek() == '*' || operatorStack.peek() == '/')) {
                    processAnOperatorDouble(operandStack, operatorStack);
                }
                operatorStack.push(token.charAt(0));
            } else if(token.charAt(0)=='('){
                operatorStack.push('(');
            }else if(token.charAt(0)==')'){
                while(operatorStack.peek()!='('){
                    processAnOperatorDouble(operandStack,operatorStack);
                }
                operatorStack.pop();
            }else {
                operandStack.push(Double.parseDouble(token));
            }
        }
        if(debug){
            System.out.println("finally,the value in the Stacks are:");
            for(Double i:operandStack){
                System.out.println("operandSTack: "+i);
            }
            for(char c:operatorStack){
                System.out.println("operatorStack: "+c);
            }
        }
        while(!operatorStack.isEmpty()){
            processAnOperatorDouble(operandStack,operatorStack);
        }
        return operandStack.pop();
    }
    protected void processAnOperatorDouble(Stack<Double> operandStack, Stack<Character> operatorStack){
        char op = operatorStack.pop();
        double op1 = operandStack.pop();
        double op2 = operandStack.pop();
        if(op=='+'){
            operandStack.push(op2+op1);
        } else if (op =='-') {
            operandStack.push(op2-op1);
        }else if(op=='*'){
            operandStack.push(op2*op1);
        }else if(op=='/'){
            operandStack.push(op2/op1);
        }
    }
}
