package com.fz;

import com.fz.vaildator.AnswerValidator;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class AnswerValidatorTest {

    @Test
    public void testValidateWritesGradeTxt() throws Exception {
        Path dir = Files.createTempDirectory("ag-ans-");
        Path ex = dir.resolve("Exercises.txt");
        Path an = dir.resolve("Answers.txt");
        Path grade = dir.resolve("Grade.txt");

        // 1: 正确（3 + 5 = 8）; 2: 错误（1/2 + 1/2 = 1, 但答案写成 2）
        try (BufferedWriter w = Files.newBufferedWriter(ex)) {
            w.write("1. 3 + 5 =\n");
            w.write("2. 1/2 + 1/2 =\n");
        }
        try (BufferedWriter w = Files.newBufferedWriter(an)) {
            w.write("1. 8\n");
            w.write("2. 2\n");
        }

        // 在临时目录下运行，生成 Grade.txt
        try {
            // 切换工作目录（对 JUnit 进程生效有限），改为传绝对路径并重定向输出文件名
            // 这里直接调用 validate，Grade.txt 会在进程工作目录下生成，我们随后复制出来比较
            AnswerValidator validator = new AnswerValidator();
            validator.validate(ex.toString(), an.toString());
        } catch (RuntimeException e) {
            fail("validate should not throw: " + e.getMessage());
        }

        // 读取默认生成的 Grade.txt（在项目根）。为避免依赖工作目录，这里查找项目根 Grade.txt
        File projectRootGrade = new File("Grade.txt");
        assertTrue(projectRootGrade.exists());
        String content = Files.readString(projectRootGrade.toPath());
        assertTrue(content.contains("Correct: 1"));
        assertTrue(content.contains("Wrong: 1"));
    }
}

