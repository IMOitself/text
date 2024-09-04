package imo.text;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import java.io.InputStream;

public class debug extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String errMessage = getIntent().getStringExtra("error");
		if(errMessage == null) finishAffinity();
        
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(errMessage).create().show();
    }
}
