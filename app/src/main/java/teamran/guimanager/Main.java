package teamran.guimanager;

import teamran.guimanager.FrontEnd_Object.FrontEndManager;
import teamran.guimanager.Measurement_Object.Vertex;
import teamran.guimanager.Cnn_Object.CnnManager;
import teamran.guimanager.Ann_Object.ANNManager;
import teamran.guimanager.Cross_Entropy.Softmax;
import android.graphics.Bitmap;

public class Main {
    public void Process(Bitmap bitmap) {

        FrontEndManager fem = new FrontEndManager();
        CnnManager cnn = new CnnManager();
        ANNManager ann = new ANNManager();
             Softmax softmax = new Softmax();

        fem.bitmap = bitmap;
        fem.Execute();

        Vertex ver = new Vertex(bitmap);

        cnn.image_matrix_input = fem.image_output;

        cnn.CnnManagerInit();
        cnn.ExecuteCnnManager();

        ann.input = cnn.image_matrix_output;

        ann.AnnManagerInit();
        ann.ExecuteAnnManager();

        softmax.softmax_input = ann.previous_layer_out;

        softmax.ExecuteBackEnd();

    }
}