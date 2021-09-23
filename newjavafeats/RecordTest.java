package newjavafeats;

public class RecordTest {
    record Person(String name, String email) {
        static String test = "Hallo";
        Person(String name, String email) {
            this.name = email;
            this.email = name;
        }
    }

    public static void main(String[] args) {
        Person person = new Person("Bobo", "bobo@fake.com");
        Person person2 = new Person("Coco", "coco@fake.com");
        System.out.println(person);
        System.out.println(person2);
        System.out.println(person.equals(person2));
    }
}
