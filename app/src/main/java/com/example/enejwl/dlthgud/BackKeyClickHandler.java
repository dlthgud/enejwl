package com.example.enejwl.dlthgud;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class BackKeyClickHandler {
    private long backKeyClickTime = 0;
    private Activity activity;

    public BackKeyClickHandler(Activity activity) {
        this.activity = activity;
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 2000) {
            backKeyClickTime = System.currentTimeMillis();
            showToast();
            return;
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000) {
            ActivityCompat.finishAffinity(activity);
        }
    }

    private void showToast() {
        Toast.makeText(activity, "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.",
                Toast.LENGTH_SHORT).show();
    }
}
