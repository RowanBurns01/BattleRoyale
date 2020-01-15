public class Random {

    public int generateNumber(int n) {
        int max = n;
        int min = 0;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        return rand;
    }
}
