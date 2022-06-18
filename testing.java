
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;


public class testing {

    int currentSeconds = 15;
    Timer myTimer = new Timer();

    TimerTask task = new TimerTask() {
        public void run() {
            if (currentSeconds < 0) {
                myTimer.cancel();
            } else {
                System.out.println("Seconds: " + currentSeconds);
                // GUI: timer.setText("Timer: 00:" + currentSeconds);
                if (currentSeconds <= 3) {
                    // turn text red in gui
                    // timer.setForeground(Color.RED);
                }
            }
            currentSeconds--;
        }
    };

    public void start() {
        myTimer.schedule(task, 0, 1000);
    }

    public void pause() {
        myTimer.cancel();
    }

//	public void resume() {
//		myTimer = new Timer();
//		myTimer.schedule(task, 0, 1000);
//	}

    public static void main(String[] args) {
        testing testing = new testing();
        testing.start();

        // test
        Scanner input = new Scanner(System.in);
        if (input.nextInt() == 1) {
            testing.pause();
        }

        /*
         * IllegalStateException if task was already scheduled or cancelled, timer was
         * cancelled, or timer thread terminated.
         */

        if (input.nextInt() == 2) // error here
            testing.start();
    }

}

