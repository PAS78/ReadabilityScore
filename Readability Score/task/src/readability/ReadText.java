package readability;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadText {
    private final String text;
    private String[] sentences;
    private String[] words;
    private final int wordsCount;
    private final int charactersCount;
    private final int syllablesCount;
    private final int polysyllablesCount;

    public ReadText(String fileName) {
        this.text = readFile(fileName);
        this.sentences = splitSentences(text);
        this.wordsCount = splitWords(text).length;
        this.charactersCount = countChars();
        this.syllablesCount = countSyllablesAndPolysyllables()[0];
        this.polysyllablesCount = countSyllablesAndPolysyllables()[1];

        result();
        String choice = inputScoreCalculate();
        System.out.println();
        switch (choice) {
            case "ARI" -> getARI();
            case "FK" -> getFK();
            case "SMOG" -> getSMOG();
            case "CL" -> getCL(sentences.length, words.length, charactersCount);
            case "all" -> getAll();

        }
    }

    private void getCL(float sentencesCount, float wordsCounts, float charactersCount) {

        float cl = (float) (0.0588 * charactersCount / (wordsCounts / 100) - 0.296 * sentencesCount / (wordsCounts / 100) - 15.8);

        System.out.printf("Coleman-Liau index: %.2f (about %s-year-olds).\n", cl, getAge(cl));
    }

    private void getSMOG() {
        double smog = 1.043 * Math.sqrt(polysyllablesCount * (double) (30 / sentences.length)) + 3.1291;

        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", smog, getAge(smog));
    }

    private void getFK() {
        double fk = 0.39 * words.length / sentences.length + 11.8 * syllablesCount / words.length - 15.59;

        System.out.printf("Flesch-Kincaid readability tests: %.2f (about %d-year-olds).\n", fk, getAge(fk));
    }

    private int[] countSyllablesAndPolysyllables() {
        int[] counts = new int[2];
        String regex = "(?i)[aiouy][aeiouy]*|e[aeiouy]*(?!\\b)";

        for (String word : words) {

            int tmpPolysyllables = 0;

            Matcher m = Pattern.compile(regex).matcher(word);

            while (m.find()) {
                counts[0]++;
                tmpPolysyllables++;
            }

            if (tmpPolysyllables > 2) {
                counts[1]++;
            }

        }

        return counts;
    }

    private void result() {
        System.out.printf("The text is:\n%s\n\n", text);
        System.out.printf("Words: %d\n", words.length);
        System.out.printf("Sentences: %d\n", sentences.length);
        System.out.printf("Characters: %d\n", charactersCount);
        System.out.printf("Syllables: %d\n", syllablesCount);
        System.out.printf("Polysyllables: %d\n", polysyllablesCount);
    }

    private void getARI() {
        float ari = (float) (4.71 * charactersCount / words.length + 0.5 * words.length / sentences.length - 21.43);

        System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n", ari, getAge(ari));
    }

    private void getAll() {
        getARI();
        getFK();
        getSMOG();
        getCL(sentences.length, words.length, charactersCount);
    }

    private static int getAge(double score) {
        return Math.min((int) Math.ceil(score) + 4, 18);
    }

    private int countChars() {
        String regex = "\\s";
        return text.replaceAll(regex, "").length();
    }

    private String[] splitWords(String text) {
        String regex = "\\s+";

        words = text.replace(".", "").replace(",", "").replace("!", "").replaceAll("\\?", "").split(regex);

        return words;
    }

    private String[] splitSentences(String text) {
        String regex = "[.!?]";

        return sentences = text.split(regex);
    }

    private String readFile(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(fileName);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append(" ");
            }
            return stringBuilder.toString().trim();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private String inputScoreCalculate() {
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

}
