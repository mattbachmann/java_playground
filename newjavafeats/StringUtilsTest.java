package newjavafeats;

public class StringUtilsTest {
    public static void main(String[] args) {
        System.out.println(" ".isBlank());
        "Dumbo\nWinnie\nIgeli\nBobo".lines().forEach(System.out::println);
        "Dumbo\nWinnie\nIgeli\nBobo"
                .transform(String::toUpperCase)
                .lines()
                .forEach(System.out::println);
        System.out.println("Hallo %s!!!!!".formatted("Bobo"));

    }
}
