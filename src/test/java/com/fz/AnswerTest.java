package com.fz;

import com.fz.utils.ExpressionEvaluator;
import com.fz.utils.Fraction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @Author： fz
 * @Date： 2025/10/19 20:39
 * @Describe：测试 Fraction 和 ExpressionEvaluator 的功能
 */
public class AnswerTest {

    /**
     * 测试分数的加法运算
     */
    @Test
    public void testAddition() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction result = a.add(b);
        assertEquals(new Fraction(5, 6), result);
    }

    /**
     * 测试分数的减法运算
     */
    @Test
    public void testSubtraction() {
        Fraction a = new Fraction(5, 6);
        Fraction b = new Fraction(1, 3);
        Fraction result = a.subtract(b);
        assertEquals(new Fraction(1, 2), result);
    }

    /**
     * 测试分数的乘法运算
     */
    @Test
    public void testMultiplication() {
        Fraction a = new Fraction(2, 3);
        Fraction b = new Fraction(3, 4);
        Fraction result = a.multiply(b);
        assertEquals(new Fraction(1, 2), result);
    }

    /**
     * 测试分数的除法运算
     */
    @Test
    public void testDivision() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 4);
        Fraction result = a.divide(b);
        assertEquals(new Fraction(2, 3), result);
    }

    /**
     * 测试带分数的解析
     */
    @Test
    public void testParseMixedFraction() {
        Fraction f = Fraction.parse("2'3/8");
        assertEquals(new Fraction(19, 8), f);
    }

    /**
     * 测试分数转换为带分数字符串
     */
    @Test
    public void testToStringForMixedFraction() {
        Fraction f = new Fraction(19, 8);
        assertEquals("2'3/8", f.toString());
    }

    /**
     * 测试分数的比较
     */
    @Test
    public void testComparison() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 3);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(new Fraction(1, 2)));
    }

    /**
     * 测试简单加法表达式的计算
     */
    @Test
    public void testEvaluateSimpleAddition() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Fraction result = evaluator.evaluate("3 + 5");
        assertEquals(new Fraction(8), result);
    }

    /**
     * 测试分数表达式的计算
     */
    @Test
    public void testEvaluateFractionAddition() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Fraction result = evaluator.evaluate("1/6 + 1/8");
        assertEquals(new Fraction(7, 24), result);
    }

    /**
     * 测试括号表达式的计算
     */
    @Test
    public void testEvaluateWithParentheses() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Fraction result = evaluator.evaluate("(3 + 5) × 2");
        assertEquals(new Fraction(16), result);
    }


    /**
     * 测试混合带分数的计算
     */
    @Test
    public void testEvaluateMixedFractions() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Fraction result = evaluator.evaluate("2'1/2 + 1'1/3");
        assertEquals(new Fraction(23, 6), result);
        assertEquals("3'5/6", result.toString());
    }
}
