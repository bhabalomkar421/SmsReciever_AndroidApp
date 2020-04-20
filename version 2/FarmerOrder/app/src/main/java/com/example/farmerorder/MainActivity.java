package com.example.farmerorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextView t;
    Button msg;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=(TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //FirebaseDatabase database=FirebaseDatabase.getInstance();
        //myRef=database.getReference("User");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
            if(bundle != null){
                String number = bundle.getString("msg from");


                String content = bundle.getString("msg body");
                String s="Message from "+number.toString()+" \n content " + content;
                t.setText(s);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        if(requestCode==1000){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
                t.setText(getIntent().getStringExtra("msg from"));

            }else{
                Toast.makeText(this,"PERMISSION NOT GRANTED",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }
}