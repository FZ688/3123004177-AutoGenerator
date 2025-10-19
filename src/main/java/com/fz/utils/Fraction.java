package com.fz.utils;

import java.math.BigInteger;

/**
 * @Author： fz
 * @Date： 2025/10/19 20:23
 * @Describe：
 */
public class Fraction implements Comparable<Fraction> {
    /**
     * 分子
     */
    private BigInteger numerator;

    /**
     * 分母
     */
    private BigInteger denominator;

    public Fraction(BigInteger numerator, BigInteger denominator) {
        if (BigInteger.ZERO.equals(denominator)) {
            throw new ArithmeticException("分母不能为零");
        }

        // 确保分母为正数
        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        // 约分
        BigInteger gcd = numerator.abs().gcd(denominator.abs());
        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd);
    }

    public Fraction(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public Fraction(long wholeNumber) {
        this(BigInteger.valueOf(wholeNumber), BigInteger.ONE);
    }

    // 加法
    public Fraction add(Fraction other) {
        BigInteger newNumerator = this.numerator.multiply(other.denominator).add(other.numerator.multiply(this.denominator));
        BigInteger newDenominator = this.denominator.multiply(other.denominator);
        return new Fraction(newNumerator, newDenominator);
    }

    // 减法
    public Fraction subtract(Fraction other) {
        BigInteger newNumerator = this.numerator.multiply(other.denominator).subtract(other.numerator.multiply(this.denominator));
        BigInteger newDenominator = this.denominator.multiply(other.denominator);
        return new Fraction(newNumerator, newDenominator);
    }

    // 乘法
    public Fraction multiply(Fraction other) {
        BigInteger newNumerator = this.numerator.multiply(other.numerator);
        BigInteger newDenominator = this.denominator.multiply(other.denominator);
        return new Fraction(newNumerator, newDenominator);
    }

    // 除法
    public Fraction divide(Fraction other) {
        BigInteger newNumerator = this.numerator.multiply(other.denominator);
        BigInteger newDenominator = this.denominator.multiply(other.numerator);
        return new Fraction(newNumerator, newDenominator);
    }

    // 比较大小
    @Override
    public int compareTo(Fraction other) {
        BigInteger thisValue = this.numerator.multiply(other.denominator);
        BigInteger otherValue = other.numerator.multiply(this.denominator);
        return thisValue.compareTo(otherValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Fraction fraction = (Fraction) obj;
        return numerator.equals(fraction.numerator) && denominator.equals(fraction.denominator);
    }

    @Override
    public int hashCode() {
        return numerator.hashCode() + denominator.hashCode();
    }

    // 转换为字符串表示
    @Override
    public String toString() {
        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        }

        // 处理带分数
        if (numerator.abs().compareTo(denominator) > 0) {
            BigInteger whole = numerator.divide(denominator);
            BigInteger remainder = numerator.abs().remainder(denominator);
            return whole.toString() + "'" + remainder.toString() + "/" + denominator.toString();
        }

        return numerator.toString() + "/" + denominator.toString();
    }

    // 解析字符串为Fraction对象
    public static Fraction parse(String s) {
        s = s.trim();
        // 处理带分数，如 "2'3/8" 或 "-2'3/8"
        if (s.contains("'")) {
            String[] parts = s.split("'");
            String wholePart = parts[0].trim();
            Fraction fraction = parse(parts[1].trim());
            BigInteger den = fraction.denominator;
            BigInteger numPart = fraction.numerator;

            BigInteger whole = new BigInteger(wholePart);
            BigInteger numerator;
            if (whole.signum() < 0) {
                // 负的带分数，整体为 -(abs(whole)*den + numPart)
                numerator = whole.abs().multiply(den).add(numPart).negate();
            } else {
                numerator = whole.multiply(den).add(numPart);
            }
            return new Fraction(numerator, den);
        }

        // 处理普通分数，如 "3/5"
        if (s.contains("/")) {
            String[] parts = s.split("/");
            BigInteger numerator = new BigInteger(parts[0].trim());
            BigInteger denominator = new BigInteger(parts[1].trim());
            return new Fraction(numerator, denominator);
        }

        // 处理整数
        return new Fraction(Long.parseLong(s));
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }
}
