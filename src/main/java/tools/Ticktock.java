package tools;

public class Ticktock {
    private long start;
    private long stop=-1;

    /**
     * Initializes a new stopwatch.
     */
    public Ticktock()
    {
        start = System.currentTimeMillis();
    }


    /**
     * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in seconds) since the stopwatch was created
     */
    public double elapsedTime()
    {
        if(stop<0)
            setStop();
        return (stop - start) / 1000.0;
    }

    public  void setStart()
    {
        start = System.currentTimeMillis();
    }

    public void setStop()
    {
         stop = System.currentTimeMillis();
    }

    public String simpleFormat()
    {
        String str = String.format("%e (%.2f seconds)",elapsedTime());
        return str;
    }

    public String betterFormat()
    {
        if(stop<0)
            setStop();
        long different = stop - start;



        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String str = String.format(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
        return str;
    }
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        // sum of square roots of integers from 1 to n using Math.sqrt(x).
        Ticktock timer1 = new Ticktock();
        double sum1 = 0.0;
        for (int i = 1; i <= n; i++) {
            sum1 += Math.sqrt(i);
        }
        double time1 = timer1.elapsedTime();
        System.out.printf("%e (%.2f seconds)\n", sum1, time1);

        // sum of square roots of integers from 1 to n using Math.pow(x, 0.5).
        Ticktock timer2 = new Ticktock();
        double sum2 = 0.0;
        for (int i = 1; i <= n; i++) {
            sum2 += Math.pow(i, 0.5);
        }
        double time2 = timer2.elapsedTime();
        System.out.printf("%e (%.2f seconds)\n", sum2, time2);
    }
}

