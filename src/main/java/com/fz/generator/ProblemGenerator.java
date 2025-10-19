package com.fz.generator;

import com.fz.utils.Expression;
import com.fz.utils.ExpressionEvaluator;
import com.fz.utils.Fraction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author： fz
 * @Date： 2025/10/19 20:21
 * @Describe：
 */
public class ProblemGenerator {
    private final int range;
    private final Random random = new Random();
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private static final char[] OPERATORS = {'+', '-', '×', '÷'};

    public ProblemGenerator(int range) {
        this.range = range;
    }

    /**
     * 生成指定数量的题目
     */
    public List<Expression> generateProblems(int count) {
        List<Expression> problems = new ArrayList<>(count);

        while (problems.size() < count) {
            // 随机生成1-3个运算符的表达式
            int operatorCount = random.nextInt(3) + 1;
            Expression expr = generateExpression(operatorCount);

            // 检查是否重复
            if (!isDuplicate(expr, problems)) {
                problems.add(expr);
            }
        }

        return problems;
    }

    /**
     * 生成单个表达式
     */
    private Expression generateExpression(int operatorCount) {
        while (true) {
            // 生成操作数和运算符
            List<String> elements = new ArrayList<>();
            elements.add(generateNumber().toString());

            for (int i = 0; i < operatorCount; i++) {
                char operator = OPERATORS[random.nextInt(OPERATORS.length)];
                Fraction nextNumber = generateNumber();

                // 检查减法和除法的有效性
                if (operator == '-' || operator == '÷') {
                    String currentExpr = String.join(" ", elements) + " " + operator + " " + nextNumber;
                    try {
                        Fraction currentValue = evaluator.evaluate(currentExpr);

                        // 减法结果不能为负数
                        if (operator == '-' && currentValue.compareTo(new Fraction(0)) < 0) {
                            continue;
                        }

                        // 除法结果必须是真分数
                        if (operator == '÷') {
                            BigInteger num = currentValue.getNumerator();
                            BigInteger den = currentValue.getDenominator();
                            // 分子不能为负，且分母必须大于分子绝对值（真分数）
                            if (num.signum() < 0 || den.compareTo(num.abs()) <= 0) {
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }

                elements.add(String.valueOf(operator));
                elements.add(nextNumber.toString());
            }

            // 随机添加括号
            String expression = addParentheses(String.join(" ", elements));

            try {
                // 验证表达式是否有效
                evaluator.evaluate(expression);
                return new Expression(expression);
            } catch (Exception e) {
                // 表达式无效，重新生成
            }
        }
    }

    /**
     * 随机生成数字（自然数或真分数）
     */
    private Fraction generateNumber() {
        // 50%概率生成自然数，50%概率生成真分数
        if (random.nextBoolean()) {
            // 生成自然数
            return new Fraction(random.nextInt(range));
        } else {
            // 生成真分数
            // 分子 1-range
            long numerator = random.nextInt(range) + 1;
            // 分母 2-range
            long denominator = random.nextInt(range - 1) + 2;

            // 确保是真分数
            if (numerator >= denominator) {
                long temp = numerator;
                numerator = denominator;
                denominator = temp;
            }

            // 可能生成带分数
            if (random.nextBoolean() && numerator < denominator) {
                long whole = random.nextInt(range - 1) + 1;  // 整数部分 1-(range-1)
                return new Fraction(whole * denominator + numerator, denominator);
            }

            return new Fraction(numerator, denominator);
        }
    }

    /**
     * 随机为表达式添加括号
     */
    private String addParentheses(String expression) {
        // 简单实现：有30%的概率不添加括号
        if (random.nextDouble() < 0.3) {
            return expression;
        }

        // 这里可以实现更复杂的括号添加逻辑
        // 简化版本：随机选择一个子表达式添加括号
        String[] tokens = expression.split(" ");
        if (tokens.length <= 3) {  // 只有一个运算符，不需要括号
            return expression;
        }

        // 随机选择起始和结束位置添加括号
        int start = random.nextInt(tokens.length / 2) * 2;  // 确保是操作数位置
        int end = start + 2 + random.nextInt(Math.min(3, (tokens.length - start) / 2) * 2);  // 确保是操作数位置

        if (start >= end || end >= tokens.length) {
            return expression;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            if (i == start) {
                sb.append("(").append(tokens[i]).append(" ");
            } else if (i == end) {
                sb.append(tokens[i]).append(") ");
            } else {
                sb.append(tokens[i]).append(" ");
            }
        }

        return sb.toString().trim();
    }

    /**
     * 检查表达式是否与已存在的表达式重复
     */
    private boolean isDuplicate(Expression newExpr, List<Expression> existing) {
        for (Expression expr : existing) {
            if (expr.equals(newExpr)) {
                return true;
            }
        }
        return false;
    }
}
