import com.fz.generator.ProblemGenerator;
import com.fz.utils.Expression;
import com.fz.vaildator.AnswerValidator;
import org.apache.commons.cli.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @Author： fz
 * @Date： 2025/10/19 20:29
 * @Describe：
 */
public class Main {
    public static void main(String[] args) {
        // 定义命令行参数
        Options options = new Options();

        options.addOption("n", true, "生成题目的个数");
        options.addOption("r", true, "题目中数值的范围");
        options.addOption("e", true, "题目文件");
        options.addOption("a", true, "答案文件");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            // 处理生成题目模式
            if (cmd.hasOption("n") && cmd.hasOption("r")) {
                try {
                    int count = Integer.parseInt(cmd.getOptionValue("n"));
                    int range = Integer.parseInt(cmd.getOptionValue("r"));

                    if (count <= 0 || range <= 0) {
                        System.err.println("错误：n和r必须为正整数");
                        printHelp(options);
                        return;
                    }

                    generateProblems(count, range);

                } catch (NumberFormatException e) {
                    System.err.println("错误：n和r必须为整数");
                    printHelp(options);
                }
            }
            // 处理验证答案模式
            else if (cmd.hasOption("e") && cmd.hasOption("a")) {
                String exerciseFile = cmd.getOptionValue("e");
                String answerFile = cmd.getOptionValue("a");

                validateAnswers(exerciseFile, answerFile);
            }
            // 参数错误
            else {
                System.err.println("错误：参数不正确");
                printHelp(options);
            }

        } catch (ParseException e) {
            System.err.println("解析命令行参数时出错：" + e.getMessage());
            printHelp(options);
        }
    }

    /**
     * 生成题目并写入文件
     */
    private static void generateProblems(int count, int range) {
        System.out.println("正在生成" + count + "道题目，数值范围为" + range + "...");

        ProblemGenerator generator = new ProblemGenerator(range);
        List<Expression> problems = generator.generateProblems(count);

        // 写入题目文件
        try (BufferedWriter exerciseWriter = new BufferedWriter(new FileWriter("Exercises.txt"));
             BufferedWriter answerWriter = new BufferedWriter(new FileWriter("Answers.txt"))) {

            for (int i = 0; i < problems.size(); i++) {
                Expression expr = problems.get(i);
                // 写入题目，格式如："1. 3 + 5 = "
                exerciseWriter.write((i + 1) + ". " + expr.getExpressionString() + " =");
                exerciseWriter.newLine();

                // 写入答案，格式如："1. 8"
                answerWriter.write((i + 1) + ". " + expr.getResult().toString());
                answerWriter.newLine();
            }

            System.out.println("题目已生成到Exercises.txt和Answers.txt");

        } catch (IOException e) {
            System.err.println("写入文件时出错：" + e.getMessage());
        }
    }

    /**
     * 验证答案并生成评分
     */
    private static void validateAnswers(String exerciseFile, String answerFile) {
        System.out.println("正在验证答案...");

        AnswerValidator validator = new AnswerValidator();
        validator.validate(exerciseFile, answerFile);

        System.out.println("验证完成，结果已保存到Grade.txt");
    }

    /**
     * 打印帮助信息
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar Myapp.jar", "自动生成小学四则运算题目", options,
                "生成题目: java -jar Myapp.jar -n 10 -r 10\n验证答案: java -jar Myapp.jar -e Exercises.txt -a Answers.txt");
    }
}
