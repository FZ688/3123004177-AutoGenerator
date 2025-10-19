package com.fz.utils;

import java.util.Stack;

/**
 * @Author： fz
 * @Date： 2025/10/19 20:23
 * @Describe：
 */
public class ExpressionEvaluator {

    /**
     * 计算表达式的值
     * @param expression 要计算的表达式字符串
     * @return 计算结果，以Fraction形式返回
     */
    public Fraction evaluate(String expression) {
        // 替换符号为Java可识别的运算符
        String processedExpr = expression.replace('×', '*').replace('÷', '/');
        return evaluatePostfix(infixToPostfix(processedExpr));
    }

    /**
     * 将中缀表达式转换为后缀表达式（逆波兰表达式）
     */
    private String[] infixToPostfix(String expression) {
        Stack<Character> stack = new Stack<>();
        StringBuilder output = new StringBuilder();
        int i = 0;

        while (i < expression.length()) {
            char c = expression.charAt(i);

            // 跳过空格
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // 如果是数字或分数，直接添加到输出
            if (Character.isDigit(c) || c == '/' || c == '\'') {
                int j = i;
                while (j < expression.length() &&
                        (Character.isDigit(expression.charAt(j)) ||
                                expression.charAt(j) == '/' ||
                                expression.charAt(j) == '\'')) {
                    j++;
                }
                output.append(expression, i, j).append(" ");
                i = j;
            }
            // 如果是左括号，入栈
            else if (c == '(') {
                stack.push(c);
                i++;
            }
            // 如果是右括号，弹出栈中元素直到遇到左括号
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop()).append(" ");
                }
                stack.pop(); // 弹出左括号
                i++;
            }
            // 如果是运算符
            else if (isOperator(c)) {
                while (!stack.isEmpty() && stack.peek() != '(' &&
                        precedence(stack.peek()) >= precedence(c)) {
                    output.append(stack.pop()).append(" ");
                }
                stack.push(c);
                i++;
            } else {
                i++;
            }
        }

        // 弹出剩余的运算符
        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }

        return output.toString().trim().split(" ");
    }

    /**
     * 计算后缀表达式的值
     */
    private Fraction evaluatePostfix(String[] postfix) {
        Stack<Fraction> stack = new Stack<>();

        for (String token : postfix) {
            if (isOperator(token.charAt(0)) && token.length() == 1) {
                Fraction b = stack.pop();
                Fraction a = stack.pop();
                stack.push(applyOperator(a, b, token.charAt(0)));
            } else {
                stack.push(Fraction.parse(token));
            }
        }

        return stack.pop();
    }

    /**
     * 判断字符是否为运算符
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * 返回运算符的优先级
     */
    private int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

    /**
     * 应用运算符计算结果
     */
    public Fraction applyOperator(Fraction a, Fraction b, char operator) {
        return switch (operator) {
            case '+' -> a.add(b);
            case '-' -> a.subtract(b);
            case '*' -> a.multiply(b);
            case '/' -> a.divide(b);
            default -> throw new IllegalArgumentException("不支持的运算符: " + operator);
        };
    }
}
