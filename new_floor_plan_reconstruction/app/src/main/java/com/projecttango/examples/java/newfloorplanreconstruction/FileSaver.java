package com.projecttango.examples.java.newfloorplanreconstruction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.atap.tango.reconstruction.TangoPolygon;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by HP on 19-Nov-17.
 */

public class FileSaver {

    private static float mMinAreaWall = 0f;

    public static void savePolygon(List<TangoPolygon> polygonList, Context context, FloorPlanReconstructionActivity activity)
    {
        Log.d("Shonku", "saving polygon");
        try
        {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dir, "data.txt");

            Log.d("Shonku", file.getAbsolutePath() + " >> " + file.isFile());

            askPermission(context, activity);

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            for(TangoPolygon polygon: polygonList)
            {
                if(polygon.layer != TangoPolygon.TANGO_3DR_LAYER_WALLS) continue;

                if(polygon.area < 0.1f) continue;
                fileOutputStream.write(("" + polygon.vertices2d.size() + "\n").getBytes());
                for (int i = 0; i < polygon.vertices2d.size(); i++) {
                    float[] p = polygon.vertices2d.get(i);
                    float x = p[0];
                    float y = -1 * p[1];
                    fileOutputStream.write((x + " " + y + "\n").getBytes());
                }
            }
            fileOutputStream.close();

            Toast toast = Toast.makeText(context, "Sucess", Toast.LENGTH_SHORT);
            toast.show();

            Log.d("Shonku", "Success!");
        }
        catch (Exception e)
        {
            Log.d("Shonku", e.toString());
        }
    }

    public static void askPermission(Context context, FloorPlanReconstructionActivity activity)
    {
        Log.d("Shonku", "Asking permission");
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("Shonku", "APK >= 23");
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
