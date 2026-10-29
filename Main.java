import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File currentDir = new File(System.getProperty("user.home"));

        System.out.println("Welcome to OpenJSH! Type 'exit' to quit.");

        while (true) {
            System.out.print(currentDir.getName() + " $> ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            List<String> tokens = parseArguments(input);
            String command = tokens.get(0);

            if (command.equals("exit")) {
                System.out.println("Goodbye!");
                break;

            } else if (command.equals("cd")) {
                currentDir = handleCd(tokens, currentDir);
                continue;

            } else if (command.equals("ls")) {
                listFiles(currentDir);
                continue;

            } else if (command.equals("pwd")) {
                printWorkingDirectory(currentDir);
                continue;

            } else if (command.equals("help")) {
                System.out.println("Commands are: exit, cd, ls, pwd, help, go to https://github.com/iwankurniasteam-bit/OpenJSH for more information on the README.md");
                continue;

            } else if (command.equals("abouts")) {
                System.out.println("This is OpenJSH, a shell made with java made by a 6th grader in his free time, thanks for using OpenJSH as it helps a lot!");
                continue;

            } else if (command.equals("openjsh")) {
                System.out.println("Error: invalid, try typing OJ ") ;
                continue;

            } else if (command.equals("OJ")) {
                System.out.println("LIST OF OPENJSH COMMANDS: OJ, OJSH, OJLS, JRN");
                continue;

            } else if (command.equals("OJSH")) {
                System.out.println("OpenJSH version is 0.15.0 dev.1, Build 131026, build date is 13 Oct 2026") ;
                continue;

            } else if (command.equals("JRN")) {
                System.out.println("OpenJSH runs Java 17 LTE");
                continue;

            } else if (command.equals("pmt")) {
                System.out.println("pmt is WIP");
                continue;

            } else if (command.equals(""))
            try {
                executeCommand(tokens, currentDir);

            } catch (IOException e) {
                System.err.println(
                        "Command not found or failed to run: " + command
                );

            } catch (InterruptedException e) {
                System.err.println("Process was interrupted.");
                Thread.currentThread().interrupt();
            }
        }

        scanner.close();
    }

    private static void executeCommand(
            List<String> tokens,
            File currentDir
    ) throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(tokens);
        pb.directory(currentDir);
        pb.inheritIO();

        Process process = pb.start();
        process.waitFor();
    }

    private static File handleCd(
            List<String> tokens,
            File currentDir
    ) {
        if (tokens.size() < 2) {
            return new File(System.getProperty("user.home"));
        }

        String targetPath = tokens.get(1);

        File newDir;

        if (targetPath.startsWith("/")) {
            newDir = new File(targetPath);
        } else {
            newDir = new File(currentDir, targetPath);
        }

        if (newDir.exists() && newDir.isDirectory()) {
            try {
                return newDir.getCanonicalFile();
            } catch (IOException e) {
                return newDir;
            }
        } else {
            System.err.println(
                    "cd: no such file or directory: " + targetPath
            );

            return currentDir;
        }
    }

    private static void printWorkingDirectory(File directory) {
        try {
            System.out.println(directory.getCanonicalPath());
        } catch (IOException e) {
            System.err.println(
                    "pwd: unable to determine current directory"
            );
        }
    }

    private static List<String> parseArguments(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;

            } else if (c == ' ' && !insideQuotes) {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }

            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            tokens.add(sb.toString());
        }

        return tokens;
    }

    private static void listFiles(File directory) {
        File[] files = directory.listFiles();

        if (files == null) {
            System.err.println("ls: unable to read directory");
            return;
        }

        for (File file : files) {
            System.out.println(file.getName());
        }
    }
}
