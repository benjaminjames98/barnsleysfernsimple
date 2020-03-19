package tech.bencloud.barnsleysfernsimple;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView fernImageView;
    private Bitmap fernBitmap;
    public Handler handler;

    private int delayMS = 0;
    private int periodMS = 2000;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call the Activity constructor
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int screenWidth = metrics.widthPixels;
        final int screenHeight = metrics.heightPixels;

        Bitmap.Config bitmapConf = Bitmap.Config.ARGB_8888;
        fernBitmap = Bitmap.createBitmap(screenWidth, screenHeight, bitmapConf);

        handler = new Handler();
        timer = new Timer();

        timerTask = new TimerTask() {
            Random random = new Random();

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        int colour = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                        addPointsToFern(200, colour, screenWidth, screenHeight);
                        updateFernImageView();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, delayMS, periodMS);
    }

    void addPointsToFern(int numPoints, int colour, int bitmapWidth, int bitmapHeight) {
        double x = 0.5;
        double y = 0.0;
        Random random = new Random();

        for (int i = 0; i < numPoints; i++) {
            double tempx, tempy;

            // Get a random number in the range 0.0 to 1.0
            double r = random.nextDouble();

            // Calculate the new point location to form the fern
            if (r <= 0.02) {
                tempx = 0.5;
                tempy = 0.27 * y;
            } else if (r <= 0.17) {
                tempx = -0.139 * x + 0.263 * y + 0.5700;
                tempy = 0.246 * x + 0.224 * y - 0.0360;
            } else if (r <= 0.30) {
                tempx = 0.170 * x - 0.215 * y + 0.4080;
                tempy = 0.222 * x + 0.176 * y + 0.0893;
            } else {
                tempx = 0.781 * x + 0.034 * y + 0.1075;
                tempy = -0.032 * x + 0.739 * y + 0.2700;
            }

            x = tempx;
            y = tempy;

            fernBitmap.setPixel((int) (x * bitmapWidth), (int) (y * bitmapHeight), colour);
        }
    }

    public void updateFernImageView() {
        LinearLayout lin = (LinearLayout) findViewById(R.id.mainLayout);
        fernImageView = new ImageView(this);
        fernImageView.setImageBitmap(fernBitmap);
        lin.addView(fernImageView);
        lin.invalidate();
    }

    @Override
    public void onPause() {
        super.onPause();
        timerTask.cancel();
    }

}
