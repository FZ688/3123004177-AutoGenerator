package com.fz;

import com.fz.generator.ProblemGenerator;
import com.fz.utils.Expression;
import com.fz.utils.ExpressionEvaluator;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ProblemGeneratorTest {

    @Test
    public void generateProblems_sanity_and_noDuplicates() {
        ProblemGenerator gen = new ProblemGenerator(10);
        List<Expression> list = gen.generateProblems(20);
        assertEquals(20, list.size());

        // 所有表达式可被计算且无重复（按 Expression.equals）
        Set<Expression> set = new HashSet<>(list);
        assertEquals(list.size(), set.size());

        ExpressionEvaluator ev = new ExpressionEvaluator();
        for (Expression e : list) {
            assertNotNull(e.getExpressionString());
            assertNotNull(e.getNormalizedString());
            assertNotNull(e.getResult());
            // 再用 evaluator 计算一次应不抛异常
            assertNotNull(ev.evaluate(e.getExpressionString()));
        }
    }
}

