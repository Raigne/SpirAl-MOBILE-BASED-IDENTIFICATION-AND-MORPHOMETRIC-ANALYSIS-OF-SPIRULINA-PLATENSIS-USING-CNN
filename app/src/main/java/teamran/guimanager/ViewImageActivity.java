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

import java.io.FileNotFoundException;

public class ViewImageActivity extends AppCompatActivity {

    ImageView displayImage;
    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        displayImage = findViewById(R.id.view_image_display);
        backButton = findViewById(R.id.back_button);

        Intent intentReceived = getIntent();

        Bundle extras = intentReceived.getExtras();


        String uriString = extras.getString("View");

        Uri pathToFile = Uri.parse(uriString);

        try{
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(pathToFile));
            displayImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack() {

        Intent intent = new Intent(this,MainMenuActivity.class);
        startActivity(intent);
    }
}
