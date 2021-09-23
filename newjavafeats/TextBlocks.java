package newjavafeats;

public class TextBlocks {
    public static void main(String[] args) {
        System.out.println("""
                Boo: %s
                
                Foo
                
                "Bar"
                """.formatted("Dave"));
    }
}
