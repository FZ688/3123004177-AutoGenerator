package com.fz.utils;

import lombok.Getter;
import lombok.EqualsAndHashCode;

/**
 * @Author： fz
 * @Date： 2025/10/19 20:21
 * @Describe：
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Expression {
    private final String expressionString;
    @EqualsAndHashCode.Include
    private final String normalizedString;
    private final Fraction result;

    public Expression(String expressionString) {
        this.expressionString = expressionString;
        this.normalizedString = normalize(expressionString);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        this.result = evaluator.evaluate(expressionString);
    }

    /**
     * 标准化表达式，用于判断题目是否重复
     * 对于加法和乘法，交换操作数位置后视为相同表达式
     */
    private String normalize(String expression) {
        // 移除所有括号
        String normalized = expression.replaceAll("[()]", "");

        // 处理加法交换律
        if (normalized.contains(" + ")) {
            String[] parts = normalized.split(" \\+ ");
            if (parts.length == 2) {
                // 递归标准化子表达式
                Expression left = new Expression(parts[0]);
                Expression right = new Expression(parts[1]);

                // 比较子表达式的标准化形式，按字典序排序
                if (left.getNormalizedString().compareTo(right.getNormalizedString()) > 0) {
                    return right.getNormalizedString() + " + " + left.getNormalizedString();
                }
                return left.getNormalizedString() + " + " + right.getNormalizedString();
            }
        }

        // 处理乘法交换律
        if (normalized.contains(" × ")) {
            String[] parts = normalized.split(" × ");
            if (parts.length == 2) {
                // 递归标准化子表达式
                Expression left = new Expression(parts[0]);
                Expression right = new Expression(parts[1]);

                // 比较子表达式的标准化形式，按字典序排序
                if (left.getNormalizedString().compareTo(right.getNormalizedString()) > 0) {
                    return right.getNormalizedString() + " × " + left.getNormalizedString();
                }
                return left.getNormalizedString() + " × " + right.getNormalizedString();
            }
        }

        return normalized;
    }
}
