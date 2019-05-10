package teamran.guimanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

import teamran.guimanager.Cross_Entropy.Softmax;

public class DisplayActivity extends AppCompatActivity {

    ImageView imageDisplay;
    Button processButton;

    Main main = new Main();

    Bitmap bit2send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        imageDisplay = findViewById(R.id.image_display2);
        processButton = findViewById(R.id.process_button);
        Intent intentReceived = getIntent();

        Bundle extras = intentReceived.getExtras();

        if(extras != null && extras.containsKey("Capture")){
            Bundle extra = getIntent().getBundleExtra("Capture");
            Bitmap imageBitmap = (Bitmap)  extra.get("data");
            imageDisplay.setImageBitmap(imageBitmap);
            bit2send = (Bitmap) imageBitmap;

        }
        else if(extras != null && extras.containsKey("URI")){
            String uriString = extras.getString("URI");

            Uri pathToFile = Uri.parse(uriString);

            try{
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(pathToFile));
                imageDisplay.setImageBitmap(bitmap);
                bit2send = (Bitmap) bitmap;

            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main.Process(bit2send);

                startProcessScreen();
            }
        });

    }

    private void startProcessScreen() {
        Softmax soft = new Softmax();
        TextView Classification = (TextView) findViewById(R.id.display_classification);
        TextView Measurements = (TextView) findViewById(R.id.display_measurements);

        String identified = soft.output;
        Classification.setText(identified);
    }
}


