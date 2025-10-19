package com.fz;

import com.fz.utils.ExpressionEvaluator;
import com.fz.utils.Fraction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @Author： fz
 * @Date： 2025/10/19 20:39
 * @Describe：
 */
public class AnswerTest {

    @Test
    public void testAddition() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction result = a.add(b);
        assertEquals(new Fraction(5, 6), result);
    }

    @Test
    public void testSubtraction() {
        Fraction a = new Fraction(5, 6);
        Fraction b = new Fraction(1, 3);
        Fraction result = a.subtract(b);
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    public void testMultiplication() {
        Fraction a = new Fraction(2, 3);
        Fraction b = new Fraction(3, 4);
        Fraction result = a.multiply(b);
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    public void testDivision() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 4);
        Fraction result = a.divide(b);
        assertEquals(new Fraction(2, 3), result);
    }

    @Test
    public void testParseMixedFraction() {
        Fraction f = Fraction.parse("2'3/8");
        assertEquals(new Fraction(19, 8), f);
    }

    @Test
    public void testToStringForMixedFraction() {
        Fraction f = new Fraction(19, 8);
        assertEquals("2'3/8", f.toString());
    }

    @Test
    public void testComparison() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 3);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(new Fraction(1, 2)));
    }

    @Test
    public void testEvaluateSimpleAddition() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Fraction result = evaluator.evaluate("3 + 5");
        assertEquals(new Fraction(8), result);
    }

    @Test
    public void testEvaluateFractionAddition() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Fraction result = evaluator.evaluate("1/6 + 1/8");
        assertEquals(new Fraction(7, 24), result);
    }

    @Test
    public void testEvaluateWithParentheses() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Fraction result = evaluator.evaluate("(3 + 5) × 2");
        assertEquals(new Fraction(16), result);
    }

    @Test
    public void testEvaluateMixedFractions() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Fraction result = evaluator.evaluate("2'1/2 + 1'1/3");
        assertEquals(new Fraction(23, 6), result);
        assertEquals("3'5/6", result.toString());
    }
}
