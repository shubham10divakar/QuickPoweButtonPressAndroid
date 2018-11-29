package subhamdivakar.sd.etc.quickpowebuttonpressandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Subham Divakar on 3/2/2018.
 */
public class MyReceiver extends BroadcastReceiver {
    private MediaPlayer player;
    private MyReceiver myReceiver;
    private static int countPowerOff = 0;
    static int ctr = 0;
    Context cntx;
    Vibrator vibe;
    long a, seconds_screenoff, OLD_TIME, seconds_screenon, actual_diff;
    boolean OFF_SCREEN, ON_SCREEN, sent_msg;


    @Override
    public void onReceive(final Context context, final Intent intent) {
        cntx = context;
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.v("onReceive", "Power button is pressed.");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            a = System.currentTimeMillis();
            seconds_screenoff = a;
            OLD_TIME = seconds_screenoff;
            OFF_SCREEN = true;

            new CountDownTimer(500, 200) {

                public void onTick(long millisUntilFinished) {


                    if (ON_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {

                            actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 2000) {
                                sent_msg = true;
                                if (sent_msg)
                                {
                                    Toast.makeText(cntx, " POWER BUTTON CLICKED 2 TIMES ", Toast.LENGTH_LONG).show();
                                    vibe.vibrate(100);
                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;
                                    sent_msg = false;
                                }
                            }
                            else
                                {
                                seconds_screenon = 0L;
                                seconds_screenoff = 0L;

                            }
                        }
                    }
                }

                public void onFinish() {

                    seconds_screenoff = 0L;
                }
            }.start();


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            a = System.currentTimeMillis();
            seconds_screenon = a;
            OLD_TIME = seconds_screenoff;

            new CountDownTimer(500, 200) {

                public void onTick(long millisUntilFinished) {
                    if (OFF_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {
                            actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 4000) {
                                sent_msg = true;
                                if (sent_msg) {

                                    Toast.makeText(cntx, "POWER BUTTON CLICKED 2 TIMES", Toast.LENGTH_LONG).show();
                                    vibe.vibrate(100);
                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;
                                    sent_msg = false;


                                }
                            } else {
                                seconds_screenon = 0L;
                                seconds_screenoff = 0L;

                            }
                        }
                    }

                }

                public void onFinish() {

                    seconds_screenon = 0L;
                }
            }.start();


        }
    }

    private long cal_diff(long seconds_screenon2, long seconds_screenoff2) {
        long diffrence;
        if (seconds_screenon2 >= seconds_screenoff2) {
            diffrence = (seconds_screenon2) - (seconds_screenoff2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        } else {
            diffrence = (seconds_screenoff2) - (seconds_screenon2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        }

        return diffrence;
    }
}

