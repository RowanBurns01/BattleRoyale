public class Phrases {

    public void welcomeText() {
        System.out.println("\nWelcome to the hunger games, tributes are as follows:\n");
    }

    public void dayText(Day d) {
        System.out.print("\nDay " + d.getDay());
    }

    public void remainingText(int num, int num2) {
        if (num2 == 1) {
            System.out.print("A single cannon shot fires, ");
        }else if( num2== 0){
            System.out.print("There are no cannon shots tonight, ");
        } else {
            System.out.print("There are " + num2 + " cannon shots in the night, ");
        }
        if (num < 6) {
            System.out.println("only " + num + " tributes remain.");
        } else {
            System.out.println(num + " tributes remain.");
        }
    }
}
