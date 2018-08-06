import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 *  Java 1. Homework 7
 * @author A.Usmanov
 * @version dated Jul 30,2018
 */

/*
1. Расширить задачу про котов и тарелки с едой.
2. Сделать так, чтобы в тарелке с едой не могло получиться отрицательного количества еды
(например, в миске 10 еды, а кот пытается покушать 15-20).
3. Каждому коту нужно добавить поле сытость (когда создаем котов, они голодны). Если коту
удалось покушать (хватило еды), сытость = true.
4. Считаем, что если коту мало еды в тарелке, то он её просто не трогает, то есть не может быть
наполовину сыт (это сделано для упрощения логики программы).
5. Создать массив котов и тарелку с едой, попросить всех котов покушать из этой тарелки и
потом вывести информацию о сытости котов в консоль.
6. Добавить в тарелку метод, с помощью которого можно было бы добавлять еду в тарелку.
 */
public class Java1Homework7 implements Runnable {

    public static void main(String[] args) {
        new Java1Homework7().run();
    }

    @Override
    public void run() {
        Plate plate = new Plate(100);
        System.out.println(plate);
        List<Cat> cats = new ArrayList<Cat>();
        Random random = new Random();
        //Create cats
        for (int i = 0;i<10;i++) {
            cats.add(new Cat(String.format("Cat%s",i+1),random.nextInt(20)+5));
        }
        //Feed all cats
        int demand = 0;
        for (Cat cat:cats) {
            cat.eat(plate);
            if (cat.isHungry()) demand+=cat.getAppetite();
        }
        //Show cats & plate
        for (Cat cat:cats) {
            System.out.println(cat);
        }
        System.out.println(plate);
        //Feed hungry cats
        if (demand>0) {
            int extraFood = demand - plate.getFood();
            System.out.println(String.format(Locale.getDefault(),"extra feed: %d",extraFood));
            plate.addFood(extraFood);
            for (Cat cat:cats) {
                if (cat.isHungry()) {
                    cat.eat(plate);
                    System.out.println(cat);
                }
            }
            System.out.println(plate);
        }
        else
            System.out.println("no hungry cats");


    }

    public class Plate {
        private int food;
        public Plate(int food) {
            this.food = food;
        }
        public boolean removeFood(int n) {
            if (n>food) return false;
            food -= n;
            return true;
        }
        public int getFood(){
            return food;
        }
        public void addFood(int n) {
            food += n;

        }
        public String toString() {
            return "plate: " + food;
        }
    }
    public class Cat {
        private String name;
        private int appetite;
        private boolean satiety = false;
        public Cat(String name, int appetite) {
            this.name = name;
            this.appetite = appetite;
        }
        public void eat(Plate p) {
            if (!satiety)
                satiety=p.removeFood(appetite);
        }

        public boolean isHungry() {
            //if (!satiety) System.out.println(String.format(Locale.getDefault(),"%s MEOW!",name)); //hungry cat always say 'meow' if you ask about its satiety :-)
            return !satiety;
        }

        public int getAppetite() {
            return appetite;
        }

        @Override
        public String toString() {
            return String.format(Locale.getDefault(),"%s appetite:%d is hungry: %b",name,appetite,isHungry());
        }
    }
}
