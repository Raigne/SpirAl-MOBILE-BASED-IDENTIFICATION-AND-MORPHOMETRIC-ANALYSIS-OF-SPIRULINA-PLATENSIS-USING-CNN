package teamran.guimanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StartActivity extends AppCompatActivity {
    Bitmap bitmap;
    public static final int CAMERA_REQUEST_CODE = 228;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    Button captureImageButton;
    Button importImageButton;
    ImageView imageDisplay;
    View backgroundcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        backgroundcolor = this.getWindow().getDecorView();
        backgroundcolor.setBackgroundResource(R.color.sourlemon);

        imageDisplay = findViewById(R.id.image_display_start);
        captureImageButton = findViewById(R.id.capture_image_button);
        importImageButton = findViewById(R.id.import_image_button);

        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
                //     } else {
                // String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                // requestPermissions(permissionRequest, 4192);
                // }
            }
            }
        );
        // import image from gallery
        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirectoryPath = pictureDirectory.getPath();

                Uri data = Uri.parse(pictureDirectoryPath);

                photoPickerIntent.setDataAndType(data, "image/*");

                File f = new File(pictureDirectoryPath);


                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

            }
        });
    }
    // access camera
    private void invokeCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(takePictureIntent,CAMERA_REQUEST_CODE );

    }
    // Intent complete
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK) {

            // invokeCamera done
            if (requestCode == CAMERA_REQUEST_CODE) {

                Bundle extras = data.getExtras();
                Intent intent = new Intent(this, DisplayActivity.class);
                intent.putExtra("Capture", extras);
                startActivity(intent);

            // importImageButton done
            } else if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri targetUri = data.getData();
                String s = getRealPathFromURI(targetUri);
                /*try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), targetUri);
                    Mat image = new Mat();
                    Utils.bitmapToMat(bitmap, image);


                }catch (Exception e){

                }*/


                if (targetUri != null) {
                    Intent intent = new Intent(this, DisplayActivity.class);
                    intent.putExtra("Import", targetUri.toString());
                    intent.putExtra("pathname", s);
                    intent.putExtra("image", targetUri);
                    startActivity(intent);
                }


            }

        }
        }

    // get the path and return as String
    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
