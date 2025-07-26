package DuAn2.hepler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HtmlEmailTemplateUtil {

    public static String loadTemplate(String path, Map<String, String> variables) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    HtmlEmailTemplateUtil.class.getClassLoader().getResourceAsStream(path),
                    StandardCharsets.UTF_8))) {

            String content = reader.lines().collect(Collectors.joining("\n"));
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return content;
        } catch (Exception e) {
            throw new RuntimeException("Không thể đọc template: " + path, e);
        }
    }
}
