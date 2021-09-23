package newjavafeats;

public class SwitchExpression {
    public static void main(String[] args) {
        var day = 2;
        var test = switch (day) {
            case 0 -> "Mäntig";
            case 1 -> "Zischtig";
            case 2 -> {
                yield "Mittwuch";
            }
            default -> "Suntig";
        };
        System.out.println(test);
    }
}
