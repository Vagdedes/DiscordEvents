package ai.idealistic.bdw.utils;

public class DiscordUtils {

    public static String escapeMarkdown(String input) {
        if (input == null) {
            return null;
        }
        String[] markdownChars = {
                "\\", "`", "*", "_", "~", ">",
                "|", "[", "]", "(", ")", "#",
                "+", "-", "=", "!", ".", "@"
        };
        String escaped = input;

        for (String ch : markdownChars) {
            escaped = escaped.replace(ch, "\\" + ch);
        }
        return escaped;
    }

}
