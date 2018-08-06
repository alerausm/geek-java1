/**
 * JAVA 1.Lesson 6. Homework
 * @author A.Usmanov
 * @version dated Jul 27,2018
 *
 */
/*
1 Создать классы Собака и Кот с наследованием от класса Животное.
2 Животные могут выполнять действия: бежать, плыть, перепрыгивать препятствие. В качестве
параметра каждому методу передается величина, означающая или длину препятствия (для
бега и плавания), или высоту (для прыжков).
3 У каждого животного есть ограничения на действия (бег: кот 200 м., собака 500 м.; прыжок: кот
2 м., собака 0.5 м.; плавание: кот не умеет плавать, собака 10 м.).
4 При попытке животного выполнить одно из этих действий, оно должно сообщить результат в
консоль. (Например, dog1.run(150); -> результат: run: true)
5 * Добавить животным разброс в ограничениях. То есть у одной собаки ограничение на бег
может быть 400 м., у другой 600 м.
 */

import java.util.Locale;
import java.util.Random;


public class Java1Homerwork6 {
    public static void main(String[] args) {
        System.out.println("Default animals:");
        Animal[] animals = {new Cat(),new Dog()};
        controlAnimals(animals);
        System.out.println();
        System.out.println("Random animals:");
        animals = new Animal[10];
        Random rand = new Random();
        for (int i = 0;i<animals.length;i++) {
            if (rand.nextInt(2)==0)
                animals[i] = Cat.createInstance();
            else
                animals[i] = Dog.createInstance();
        }
        controlAnimals(animals);


    }

    private static void controlAnimals(Animal[] animals) {
        Random rand = new Random();
        for (Animal animal:animals) {
            System.out.println();
            System.out.println(animal.toString()+String.format(Locale.getDefault(),"'s run distance: %.0fm, jump height: %.1fm, swim distance: %.0fm",animal.getMaxRunDistance(),animal.getMaxJumpDistance(),animal.getMaxSwimDistance()));
            animal.run(rand.nextInt(600));
            animal.swim(rand.nextInt(15));
            animal.jump(rand.nextFloat()*3f);
        }
    }

    abstract static class Animal  {

        public final int ACTION_RUN = 0;
        public final int ACTION_JUMP = 1;
        public final int ACTION_SWIM = 2;

        private float maxRunDistance;
        private float maxSwimDistance;
        private float maxJumpDistance;

        final protected int id;

        Animal(int id, float maxRunDistance,float maxSwimDistance,float maxJumpDistance) {
            this.id = id;
            setMaxRunDistance(maxRunDistance);
            setMaxSwimDistance(maxSwimDistance);
            setMaxJumpDistance(maxJumpDistance);
        }

        boolean checkAction(int action,float param) {
            switch (action) {
                case ACTION_RUN:
                    return !(param>getMaxRunDistance());
                case ACTION_JUMP:
                    return !(param>getMaxJumpDistance());
                case ACTION_SWIM:
                    return !(param>getMaxSwimDistance());
                default:
                    return false;
            }
        }

        protected float getMaxSwimDistance() {
            return maxSwimDistance;
        }


        protected float getMaxJumpDistance() {
            return maxJumpDistance;
        }


        protected float getMaxRunDistance() {
            return maxRunDistance;
        }


        protected void setMaxSwimDistance(float value) {
            maxSwimDistance = value;
        }

        protected void setMaxJumpDistance(float value) {
            maxJumpDistance = value;
        }

        protected void setMaxRunDistance(float value) {
            maxRunDistance = value;

        }

        public boolean run(float distance) {
            boolean res = checkAction(ACTION_RUN, distance);
            System.out.println(this.toString()+".run("+distance+")->result:"+res);
            return res;
        }
        public boolean swim(float distance) {
            boolean res = checkAction(ACTION_SWIM, distance);
            System.out.println(this.toString()+".swim("+distance+")->result:"+res);
            return res;
        }
        public boolean jump(float height) {
            boolean res = checkAction(ACTION_JUMP, height);
            System.out.println(this.toString()+".jump("+height+")->result:"+res);
            return res;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+id;
        }
    }

    static class Cat extends Animal {
        private static int INSTANCE_COUNTER = 0;

        final public static float DEFAULT_RUN_DISTANCE = 200f;
        final public static float DEFAULT_JUMP_DISTANCE = 2f;

        private Cat(float maxRunDistance,float maxJumpDistance) { //end user can create default or random cat only
            super( ++INSTANCE_COUNTER, maxRunDistance,0,maxJumpDistance);
        }
        public Cat() {
            this(DEFAULT_RUN_DISTANCE,DEFAULT_JUMP_DISTANCE);
        }


        @Override
        protected void setMaxSwimDistance(float value) {
            if (value>0f)
                System.out.println(this.toString()+".setMaxSwimDistance("+value+")-> please, don't try force cat to swim!");
            else
                super.setMaxSwimDistance(value);
        }
        public static Cat createInstance() {
            Random rand = new Random();
            int run = rand.nextInt((int)(DEFAULT_RUN_DISTANCE*1.5))+(int)DEFAULT_RUN_DISTANCE/2;
            float jump = rand.nextFloat()*DEFAULT_JUMP_DISTANCE*1.5f+DEFAULT_JUMP_DISTANCE*0.5f;
            return new Cat(run,jump);
        }
    }

    static class Dog extends Animal {
        private static int INSTANCE_COUNTER = 0;

        final public static float DEFAULT_RUN_DISTANCE = 500f;
        final public static float DEFAULT_SWIM_DISTANCE = 10f;
        final public static float DEFAULT_JUMP_DISTANCE = 0.5f;

        private Dog(float maxRunDistance,float maxSwimDistance,float maxJumpDistance) { //end user can create default or random dog only
           super( ++INSTANCE_COUNTER, maxRunDistance,maxSwimDistance,maxJumpDistance);
        }
        Dog() {
            this(DEFAULT_RUN_DISTANCE,DEFAULT_SWIM_DISTANCE,DEFAULT_JUMP_DISTANCE);
        }

        public static Dog createInstance() {
            Random rand = new Random();
            int run = rand.nextInt((int)(DEFAULT_RUN_DISTANCE*1.5))+(int)DEFAULT_RUN_DISTANCE/2;
            int swim = rand.nextInt((int)(DEFAULT_SWIM_DISTANCE*1.5))+(int)DEFAULT_SWIM_DISTANCE/2;
            float jump = rand.nextFloat()*DEFAULT_JUMP_DISTANCE*1.5f+DEFAULT_JUMP_DISTANCE*0.5f;
            return new Dog(run,swim,jump);
        }

    }


}
