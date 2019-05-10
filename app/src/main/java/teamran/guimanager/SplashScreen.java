package teamran.guimanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(), "OpenCV is loaded successfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Cannot load OpenCV", Toast.LENGTH_SHORT).show();
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i < 100; i++){
                        progressBar.setProgress(i);
                        sleep(20);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
