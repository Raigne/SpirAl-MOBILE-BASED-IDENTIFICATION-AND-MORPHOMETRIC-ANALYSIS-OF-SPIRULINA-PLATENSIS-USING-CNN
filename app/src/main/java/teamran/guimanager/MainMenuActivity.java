package teamran.guimanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    Button startButton;
    Button viewButton;
    View backgroundcolor;
    final static int VIEW_IMAGE = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);


        backgroundcolor = this.getWindow().getDecorView();
        backgroundcolor.setBackgroundResource(R.color.sourlemon);

        startButton = findViewById(R.id.start_button);
        viewButton = findViewById(R.id.view_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApplication();
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, VIEW_IMAGE);
            }
        });


    }

    private void startApplication() {
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if(resultCode == RESULT_OK){

            if(requestCode == VIEW_IMAGE){
                Uri imageUri = data.getData();

                Intent intent = new Intent(this, ViewImageActivity.class);

                if (imageUri != null) {
                    intent.putExtra("View", imageUri.toString());
                }

                startActivity(intent);
            }
        }

    }
}
