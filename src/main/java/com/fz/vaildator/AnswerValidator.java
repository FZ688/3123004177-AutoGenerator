package com.fz.vaildator;

import com.fz.utils.ExpressionEvaluator;
import com.fz.utils.Fraction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author： fz
 * @Date： 2025/10/19 20:22
 * @Describe：
 */
public class AnswerValidator {
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    /**
     * 验证题目和答案，并生成评分结果
     */
    public void validate(String exerciseFile, String answerFile) {
        try {
            List<String> exercises = readExercises(exerciseFile);
            List<String> answers = readAnswers(answerFile);

            List<Integer> correct = new ArrayList<>();
            List<Integer> wrong = new ArrayList<>();

            // 比较每一道题的答案
            for (int i = 0; i < Math.min(exercises.size(), answers.size()); i++) {
                String exercise = exercises.get(i);
                String userAnswer = answers.get(i);

                try {
                    // 计算正确答案
                    Fraction correctAnswer = evaluator.evaluate(exercise);
                    // 解析用户答案
                    Fraction userFraction = Fraction.parse(userAnswer);

                    if (correctAnswer.equals(userFraction)) {
                        correct.add(i + 1);  // 题目编号从1开始
                    } else {
                        wrong.add(i + 1);
                    }
                } catch (Exception e) {
                    // 解析或计算出错，视为错误答案
                    wrong.add(i + 1);
                }
            }

            // 写入评分结果
            writeGrade(correct, wrong);

        } catch (IOException e) {
            throw new RuntimeException("验证过程中发生错误: " + e.getMessage(), e);
        }
    }

    /**
     * 读取题目文件
     */
    private List<String> readExercises(String filename) throws IOException {
        List<String> exercises = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    // 移除可能的题号和等号
                    if (line.contains(".")) {
                        line = line.substring(line.indexOf(".") + 1).trim();
                    }
                    if (line.contains("=")) {
                        line = line.substring(0, line.indexOf("=")).trim();
                    }
                    exercises.add(line);
                }
            }
        }

        return exercises;
    }

    /**
     * 读取答案文件
     */
    private List<String> readAnswers(String filename) throws IOException {
        List<String> answers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    // 移除可能的题号
                    if (line.contains(".")) {
                        line = line.substring(line.indexOf(".") + 1).trim();
                    }
                    answers.add(line);
                }
            }
        }

        return answers;
    }

    /**
     * 写入评分结果到Grade.txt
     */
    private void writeGrade(List<Integer> correct, List<Integer> wrong) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Grade.txt"))) {
            // 写入正确的题目
            bw.write("Correct: " + correct.size() + " (" + formatNumbers(correct) + ")");
            bw.newLine();

            // 写入错误的题目
            bw.write("Wrong: " + wrong.size() + " (" + formatNumbers(wrong) + ")");
            bw.newLine();
        }
    }

    /**
     * 将数字列表格式化为字符串
     */
    private String formatNumbers(List<Integer> numbers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(numbers.get(i));
        }
        return sb.toString();
    }
}
