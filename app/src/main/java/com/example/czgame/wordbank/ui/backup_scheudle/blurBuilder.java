package com.example.czgame.wordbank.ui.backup_scheudle;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class blurBuilder {
    public static Bitmap blurBitmap(Context context, Bitmap bitmap) {

        //Create an empty bitmap with the need to create a Gaussian fuzzy bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Initialize Renderscript, which provides RenderScript context. Before creating other RS classes, you must create this class, which controls the initialization, resource management and release of Renderscript.
        RenderScript rs = RenderScript.create(context);

        // Creating Gauss Fuzzy Objects
        ScriptIntrinsicBlur blurScript =  ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // Create Allocations, the main way to pass data to the RenderScript kernel, and create a backup type to store a given type
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set ambiguity (Note: Radius can only be set to the maximum25.f)
        blurScript.setRadius(15.f);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // recycle the original bitmap
        // bitmap.recycle();

        // After finishing everything, we destroy the  Renderscript.
        rs.destroy();

        return outBitmap;

    }
}