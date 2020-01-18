public class Day {

    private int day;
    private int hour;

    public Day(){

        // Starting Time
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
        this.hour = 0;
    }

    public int getHour() {
        return this.hour;
    }

    public String getTimeOfDay() {
        if(hour == 0){
            return "Midnight";
        } else if (hour == 12){
            return "Noon";
        } else if (hour > 11){
            return hour-12 + ":00pm";
        } else {
            return hour + ":00am";
        }
    }

    public void nextDay(){
        this.day ++;
    }

    public int getDay(){
        return this.day;
    }

}
