package com.example.mikio.kimya_3_0_Yevlakh;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utils {


    public static void openBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}
