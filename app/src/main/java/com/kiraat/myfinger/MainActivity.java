package com.kiraat.myfinger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFingerprintInfo(MainActivity.this);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyguardManager keyguardManager= (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                if(keyguardManager.isKeyguardSecure()){
                    Intent intent=keyguardManager.createConfirmDeviceCredentialIntent("Authentication Required","Provide Bio Metric");
                    startActivityForResult(intent,50);
                }else{
                    Toast.makeText(MainActivity.this, "This Is no FInger ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==50){
            Toast.makeText(this, "Proved", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFingerprintInfo(Context context)
    {
        try {
            FingerprintManager fingerprintManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
                Method method = FingerprintManager.class.getDeclaredMethod("getEnrolledFingerprints");
                Object obj = method.invoke(fingerprintManager);

                if (obj != null) {
                    Class<?> clazz = Class.forName("android.hardware.fingerprint.Fingerprint");
                    Method getFingerId = clazz.getDeclaredMethod("getFingerId");

                    for (int i = 0; i < ((List) obj).size(); i++) {
                        Object item = ((List) obj).get(i);
                        if (item != null) {
                            System.out.println("fkie4. fingerId: " + getFingerId.invoke(item));
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}