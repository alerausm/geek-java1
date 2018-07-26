import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *  course Java1 Homework 5
 * @author A.Usmanov
 * @version dated Jul, 26 2018 
 */
/*
* Создать класс "Сотрудник" с полями: ФИО, должность, email, телефон, зарплата, возраст;
* Конструктор класса должен заполнять эти поля при создании объекта;
* Внутри класса «Сотрудник» написать метод, который выводит информацию об объекте в консоль;
* Создать массив из 5 сотрудников
Пример:
Person[] persArray = new Person[5]; // Вначале объявляем массив объектов
persArray[0] = new Person("Ivanov Ivan", "Engineer", "ivivan@mailbox.com", "892312312", 30000, 30); // потом для каждой ячейки массива задаем объект
persArray[1] = new Person(...);
...
persArray[4] = new Person(...);

* С помощью цикла вывести информацию только о сотрудниках старше 40 лет;
 */
public class Java1Homework5 {
    public static void main(String[] args) {
        DateFormat sdf = SimpleDateFormat.getDateInstance(SimpleDateFormat.DEFAULT,Locale.getDefault());
        Person[] persons = new Person[6]; // Вначале объявляем массив объектов
        try {
            persons[0] = new Person("Balaganov Alexander", "Engineer", "balaganov@mailbox.com", "892312313", 50000f, sdf.parse("01.01.1992"));
            persons[1] = new Person("Kozlevich Adam", "Driver", "kozlevich@mailbox.com", "892312314", 50000f, sdf.parse("01.08.1975"));
            persons[2] = new Person("Bender Ostap", "Chief Officer", "bender@mailbox.com", "892312315", 70000f, sdf.parse("01.05.1985"));
            persons[3] = new Person("Panikovsky Mikhail", "Engeener", "panikovsky@mailbox.com", "892312316", 50000f, sdf.parse("14.06.1948"));
            persons[4] = new Person("Funt", "President", "funt@mailbox.com", "892312317", 10000f, sdf.parse("14.06.1928"));
            persons[5] = new Person("Vorobianinov Ippolit", "", "", "", 0f, sdf.parse("15.09.1959"));
        }
        catch (java.text.ParseException e) {
            System.out.println("Can't convert string to date: "+e.getLocalizedMessage());
        }
        System.out.println("All persons\n");
        for (Person person:persons) {
            if (person==null) continue;
            System.out.println(person);
        }
        System.out.println("\n");
        System.out.println("Persons with age>40\n");
        for (Person person:persons) {
            if (person==null || person.getAge()<40) continue;
            person.printToConsole();
        }
    }
    
    static class Person {
        public static String DEFAULT_NAME = "-";
        public static String DEFAULT_POSITION = "Jobless";
        public static String DEFAULT_EMAIL = "-";
        public static String DEFAULT_PHONE = "-";
        public static Float DEFAULT_SALARY = 0f;
        public static Float MAX_SALARY = 999999999f;
        private String name;
        private String position;
        private String email;
        private String phone;
        private Float salary;
        private Date birthday;
        private int  age;
        public Person( String name,String position,String email,String phone,Float salary,Date birthday) {
            this.setName(name);
            this.setPosition(position);
            this.setEmail(email);
            this.setPhone(phone);
            this.setSalary(salary);
            this.setBirthday(birthday);
        }
        public void printToConsole() {
            System.out.println(this);
        }

        @Override
        public String toString() {
            return getName()+ ", "+ getAge() + ", "+getPosition() + ", зарплата: " + String.format(Locale.getDefault(),"%.2f",getSalary()) + " (email: "+getEmail()+", phone: "+getPhone()+")";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if (name==null || name.isEmpty()) this.name = DEFAULT_NAME;
            else this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            if (position==null || position.isEmpty()) this.position = DEFAULT_POSITION;
            else this.position = position;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            if (email==null || email.isEmpty()) this.email = DEFAULT_EMAIL;
            else this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            if (phone==null || phone.isEmpty()) this.phone = DEFAULT_PHONE;
            else this.phone = phone;
        }

        public Float getSalary() {
            return salary;
        }

        public void setSalary(Float salary) {
            if (salary==null || salary<0 || salary>MAX_SALARY) this.salary = DEFAULT_SALARY;
            else this.salary = salary;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date value) {
            int age = getAge(value);
            if (age>-1) {
                this.birthday = value;
                this.age = age;
            }

        }

        public int getAge() {
            return age;
        }
        static private int getAge(Date date) {
            if (date==null) return -1;
            Calendar calendarBirthday = GregorianCalendar.getInstance(Locale.getDefault());
            Calendar calendarNow = GregorianCalendar.getInstance(Locale.getDefault());
            calendarBirthday.setTime(date);
            int age = calendarNow.get(Calendar.YEAR) - calendarBirthday.get(Calendar.YEAR);
            calendarBirthday.set(Calendar.YEAR,calendarNow.get(Calendar.YEAR));
            if (calendarNow.before(calendarBirthday)) age--;
            return age;
        }
    }
}
