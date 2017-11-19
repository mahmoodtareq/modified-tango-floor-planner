package com.projecttango.examples.java.newfloorplanreconstruction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
            File file = new File(dir, "polygon.txt");

            Log.d("Shonku", file.getAbsolutePath() + " >> " + file.isFile());

            askPermission(context, activity);

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            //fileOutputStream.write(("Size: " + polygonList.size() + "\n").getBytes());
            TangoPolygon roomPolygon = polygonList.get(0);
            double area = -1f;
            for(TangoPolygon polygon: polygonList)
            {
                //fileOutputStream.write((polygon.toString() + "\n").getBytes());

                if(polygon.layer == TangoPolygon.TANGO_3DR_LAYER_WALLS && polygon.area >= 0.0)
                {
                    if(polygon.area > area)
                    {
                        area = polygon.area;
                        roomPolygon = polygon;
                    }
                }
            }

            Log.d("Shonku", "MaxArea: " + area);

            for (int i = 0; i < roomPolygon.vertices2d.size(); i++) {
                float[] p = roomPolygon.vertices2d.get(i);
                float x = p[0];
                float y = -1 * p[1];
                fileOutputStream.write((x + ", " + y + "\n").getBytes());
            }

            fileOutputStream.close();

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
