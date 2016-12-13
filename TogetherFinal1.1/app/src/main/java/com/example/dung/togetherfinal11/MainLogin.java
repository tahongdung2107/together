package com.example.dung.togetherfinal11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by dung on 18/11/2016.
 */

public class MainLogin extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        Intent intent = new Intent(getApplication(),Login.class);
        startActivity(intent);
    }
}
