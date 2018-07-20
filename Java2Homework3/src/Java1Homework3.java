import java.util.Random;
import java.util.Scanner;
/**
 * Java 1 Homework 3
 *
 * @version dated Jul 18, 2018
 * @authors Alexey Usmanov
 */

public class Java1Homework3 {
    static Scanner scanner = new Scanner(System.in);

    static boolean isGameOver() {
        System.out.println("Повторить игру еще раз? 1 – да / 0 – нет");
        return (scanner.nextInt()==0);
    }
    static void playGuessNumber() {
        byte number = (byte)(new Random()).nextInt(10);
        byte rounds = 3;
        for (byte round = 0;round<rounds;round++) {
            System.out.println("Введите число");
            byte guessNumber = scanner.nextByte();
            if (guessNumber==number) {
                System.out.println("Поздравляю, вы выиграли!");
                return;
            }
            if (guessNumber>number) System.out.println("Загаданное число меньше");
            else System.out.println("Загаданное число больше");
        }
        System.out.println("Вы проиграли. Было загадано число "+number);
    }
    static void task1() {
        System.out.println("1. Написать программу, которая загадывает случайное число от 0 до 9, и пользователю дается 3 попытки угадать это число. " +
                "При каждой попытке компьютер должен сообщить больше ли указанное пользователем число чем загаданное, или меньше. " +
                "После победы или проигрыша выводится запрос – " +
                "«Повторить игру еще раз? 1 – да / 0 – нет»(1 – повторить, 0 – нет).");
        do {
            playGuessNumber();
        }while (!isGameOver());
    }
    static void  printDifferences(String guessWord,String word) {
        String differences = "";
        String mask = "#";
        for (int i = 0;i<15;i++) {
            char c1 = (i<guessWord.length())    ? guessWord.charAt(i)    : Character.MIN_VALUE;
            char c2 = (i<word.length())         ? word.charAt(i)         : Character.MIN_VALUE;
            differences += (c1!=Character.MIN_VALUE && c1==c2)? c1:mask;
        }
        System.out.println("Не угадали: " + differences + ", попробуйте еще раз...");
    }
    static void playGuessWord() {
        String[] words = {"apple", "orange", "lemon", "banana", "apricot", "avocado", "broccoli", "carrot", "cherry", "garlic", "grape", "melon", "leak", "kiwi", "mango", "mushroom", "nut", "olive", "pea", "peanut", "pear", "pepper", "pineapple", "pumpkin", "potato"};
        String word = words[(new Random()).nextInt(words.length)];
        while(true) {
            System.out.println("Введите слово");
            String guessWord =  scanner.next();
            if (word.equals(guessWord)) {
                System.out.println("Поздравляю, вы выиграли!");
                return;
            }
            printDifferences(guessWord,word);
        }
    }
    static void task2() {
        System.out.println("2 * Создать массив из слов String[] words = {\"apple\", \"orange\", \"lemon\", \"banana\", \"apricot\", \"avocado\", \"broccoli\", \"carrot\", \"cherry\", \"garlic\", \"grape\", \"melon\", \"leak\", \"kiwi\", \"mango\", \"mushroom\", \"nut\", \"olive\", \"pea\", \"peanut\", \"pear\", \"pepper\", \"pineapple\", \"pumpkin\", \"potato\"}");
                System.out.println("При запуске программы компьютер загадывает слово, запрашивает ответ у пользователя," +
                "сравнивает его с загаданным словом и сообщает правильно ли ответил пользователь. Если слово не угадано, компьютер показывает буквы которые стоят на своих местах.");
        do {
            playGuessWord();
        }while (!isGameOver());
    }

    public static void main(String... args) {
        while (true) {
            System.out.println("Выберите задание (1-2)");
            int task = Integer.parseInt(scanner.next());
            switch (task) {
                case 1:
                    task1();
                    break;
                case 2:
                    task2();
                    break;
                default:
                    scanner.close();
                    return;
            }
        }
    }
}
