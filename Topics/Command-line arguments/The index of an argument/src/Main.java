import java.util.Objects;

class Problem {
    public static void main(String[] args) {

        int result = -1;
        for (int i = 0; i < args.length; i++) {

            if (Objects.equals(args[i], "test")) {
                result = i;
                break;
            }
        }

        System.out.println(result);

    }
}