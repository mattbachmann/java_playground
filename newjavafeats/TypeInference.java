package newjavafeats;

import java.util.List;

public class TypeInference {
    public static void main(String[] args) {
        List<String> names1 = List.of("Bobo", "Coco");
        List<String> names2 = List.of("Dumbo", "Winnie");

        var names = List.of(names1, names2);

        names.stream().forEach(System.out::println);
        for(var name:names) {
            System.out.println(name);
        }
    }
}
