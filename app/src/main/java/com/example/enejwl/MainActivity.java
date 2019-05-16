package com.example.enejwl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.enejwl.dlthgud.GameActivity;
import com.example.enejwl.dlthgud.Level;

public class MainActivity extends AppCompatActivity {
    public static Level level[] = new Level[3];

    int curLevel;
    public static int lastLevel = 1;
    final int MAX_LEVEL = 2;



    RadioGroup radioGroup;
    RadioGroup end_mode;

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        String package_name = getPackageName();

        // intent에서 "isWin" bool 값에 받기
        // 받기 실패 시 bool = -1
        int bool = getIntent().getIntExtra("isWin",-1);
        // intent에서 "curLevel" curLevel 값에 받기
        // 받기 실패 시 curLevel = -1
        curLevel = getIntent().getIntExtra("curLevel", -1);

        if (bool > -1){
            if(bool == 0) {
                // TODO 실패 화면
            } else if(bool == 1) {
                // TODO 성공 화면
                curLevel++; // 레벨 업
                lastLevel = curLevel;
            }
        }

        // TODO 두더지 객체 생성
        // TODO 아이템 객체 생성

        // 레벨 객체 생성
        int[] map_3 = {1,0,0,0,0,0,0,0,0};
//        int[] map_4 = {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0};
        int[] map_5 = {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,1,0,0,0,0,1,0};
        level[1] = new Level(map_3, 3, 3, 40, 1, 0);
        level[2] = new Level(map_5, 5, 5, 50, 1, 0);

        radioGroup = (RadioGroup) findViewById(R.id.level);

        for(int i=1; i<=lastLevel; i++) {
            if(i>MAX_LEVEL){
                break;
            }
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Level " + i);
            radioButton.setId(getResources().getIdentifier("level_" + i, "id", package_name));
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(radioButton, layoutParams);
        }
        end_mode = (RadioGroup) findViewById(R.id.end_mode);

        start=findViewById(R.id.start);

        if(curLevel < 0) {
            curLevel = 1;
        }

        if(curLevel == 0) {
            radioGroup.check(getResources().getIdentifier("user_mode", "id", package_name));
        } else {
            radioGroup.check(getResources().getIdentifier("level_" + curLevel, "id", package_name));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(id);
                String level_text = radioButton.getText().toString();
                String[] array = level_text.split(" ");
                curLevel = Integer.parseInt(array[1]);
                if(curLevel == 0) {
                    end_mode.setVisibility(View.INVISIBLE);
                    // TODO 레벨 조건 입력
                } else {
                    end_mode.setVisibility(View.VISIBLE);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {   // 시작 버튼 클릭 시
            @Override
            public void onClick(View v) {
//                int level_id = radioGroup.getCheckedRadioButtonId();
//                RadioButton radioButton = (RadioButton) findViewById(level_id);
//                String level_text = radioButton.getText().toString();
//                String[] array = level_text.split(" ");
//                curLevel = Integer.parseInt(array[1]);
//                if(curLevel == 0) {
//                    end_mode.setVisibility(View.INVISIBLE);
//                    // TODO 레벨 조건 입력
//                } else {
//                    end_mode.setVisibility(View.VISIBLE);
//                }
                if(curLevel != 0) {
                    int end_id = end_mode.getCheckedRadioButtonId();
                    RadioButton radioButton1 = (RadioButton) findViewById(end_id);
                    String end_text = radioButton1.getText().toString();
                    switch (end_text) {
                        case "시간 제한 방식":
                            level[curLevel].setEnd(0);
                            level[1].setCondition(40);
                            level[2].setCondition(50);
                            break;
                        case "놓친 두더지 방식":
                            level[curLevel].setEnd(1);
                            level[1].setCondition(5);
                            level[2].setCondition(3);
                            break;
                    }
                }
                // intent에서 "curLevel" curLevel 값 보내기
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("curLevel", curLevel);
                startActivity(intent);  // GameActivity 전환
            }
        });
    }
}

