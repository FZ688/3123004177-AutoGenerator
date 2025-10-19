import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class MainCliTest {

    @Before
    @After
    public void cleanup() throws Exception {
        // 清理可能存在的文件，避免相互干扰
        for (String name : new String[]{"Exercises.txt", "Answers.txt", "Grade.txt"}) {
            File f = new File(name);
            if (f.exists()) {
                assertTrue(f.delete());
            }
        }
    }

    @Test
    public void testGenerateAndValidateFlow() throws Exception {
        // 生成 5 道题
        Main.main(new String[]{"-n", "5", "-r", "10"});
        assertTrue(new File("Exercises.txt").exists());
        assertTrue(new File("Answers.txt").exists());

        // 验证生成的题与答案
        Main.main(new String[]{"-e", "Exercises.txt", "-a", "Answers.txt"});
        File grade = new File("Grade.txt");
        assertTrue(grade.exists());
        String content = Files.readString(grade.toPath());
        assertTrue(content.contains("Wrong: 0"));
    }

    @Test
    public void testInvalidArgsPrintHelp() {
        // 缺少参数
        Main.main(new String[]{});
        // 仅检查不抛异常并能执行到结束
    }
}

