package teamran.guimanager.FrontEnd_Object;

import org.opencv.core.Core;
import android.graphics.Bitmap;
import java.util.ArrayList;

public class FrontEndManager {
    public String image_input;
    Preprocessor preprocessor;  /** object for the Preprocessor class */
    public ArrayList<double[][]> front_end_manager_output = new ArrayList<>(); /** Output of the managaer **/
    public double[][][] image_output;
    public Bitmap bitmap;

    public void Execute(){
        try{
            this.preprocessor.SetImage(bitmap);
            this.preprocessor.SetImageSize(30, 30);
            this.preprocessor.SetImageHSV();
            this.preprocessor.SplitImageChannel();
            this.preprocessor.SetImageThreshold();
            this.preprocessor.SetImageMedianBlur(3);
            this.preprocessor.SetImageOutput();
            this.image_output = new double[1][this.preprocessor.GetImageOutput().length][this.preprocessor.GetImageOutput()[0].length];
            this.image_output[0] = this.preprocessor.GetImageOutput();

        }catch (Exception e) {
            System.out.println(e);
        }
    }

    public void Initialize(String pathname){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.preprocessor = new Preprocessor();
    }
}