public class Day {

    private int day;
    private int hour;

    public Day(){
        this.day = 1;
        this.hour = 6;
    }

    public void nextHour() {
        if (hour < 24) {
            this.hour++;
        } else {
           this.hour = 0;
        }
    }

    public void resetHour(){
        getTimeOfDay();
        this.hour = 0;
    }

    public int getHour() {
        return this.hour;
    }

    public void getTimeOfDay() {
        System.out.println();
        if(hour == 24){
            System.out.println("Midnight");
        } else if (hour == 12){
            System.out.println("Noon");
        } else if (hour > 11){
            System.out.println(hour-12 + ":00pm");
        } else {
            System.out.println(hour + ":00am");
        }
    }

    public void nextDay(){
        this.day ++;
    }

    public int getDay(){
        return this.day;
    }

}
