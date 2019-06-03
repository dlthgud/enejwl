package com.example.enejwl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.enejwl.dlthgud.BackKeyClickHandler;
import com.example.enejwl.dlthgud.GameActivity;
import com.example.enejwl.dlthgud.Item;
import com.example.enejwl.dlthgud.Level;
import com.example.enejwl.dlthgud.Mole;

public class MainActivity extends AppCompatActivity {
    public static Activity mainActivity;
    private BackKeyClickHandler backKeyClickHandler;

    final static int MAX_LEVEL = 2;
    final static int MOLE_NUM = 1;
    public static Level level[] = new Level[MAX_LEVEL + 1];
    public static Mole mole[] = new Mole[MOLE_NUM];
    public static Item bomb = new Item("bomb", 1, 1, 5, 3, R.drawable.bomb,0);  // 폭탄
    public static Item bell = new Item("bell", 1, 0, 5,3, R.drawable.bell, 1);   // 비상벨
    public static Item[] items = {bomb, bell};    // 아이템 객체 생성

    int[] sksdlehs = {R.id.normal, R.id.hard, R.id.nightmare, R.id.korean};
    // 현재
    int curLevel;   // 레벨
    int end;    // 종료 방식
    static int cursksdleh;  // 난이도
    public static int lastLevel[][] = {{1,1,1,1},{1,1,1,1}};    // 클리어

    int dpi;

    RadioGroup radioGroup;
    RadioGroup end_mode;
    RadioGroup sksdleh;

    RadioButton[] radioButtons = new RadioButton[MAX_LEVEL];

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dpi = metrics.densityDpi;

        mainActivity = MainActivity.this;
        backKeyClickHandler = new BackKeyClickHandler(this);

        main();
    }

    @Override
    public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }

    private void main() {   // 전체적인 흐름을 진행하는 함수
        String package_name = getPackageName();

        init();

        // intent에서 "isWin" bool 값에 받기
        // 받기 실패 시 bool = -1
        int bool = getIntent().getIntExtra("isWin",-1);
        // intent에서 "curLevel" curLevel 값에 받기
        // 받기 실패 시 curLevel = 1
        curLevel = getIntent().getIntExtra("curLevel", 1);
        end = getIntent().getIntExtra("end", 0);
        cursksdleh = getIntent().getIntExtra("cursksdleh", 0);

        if (bool > -1){
            final ImageButton imageView = (ImageButton) findViewById(R.id.imageView);
            imageView.setVisibility(View.VISIBLE);
            if(bool == 0) {
                //  실패 화면
                start.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.loser);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView.setVisibility(View.GONE);
                        start.setVisibility(View.VISIBLE);
                    }
                });
            } else if(bool == 1) {
                //  성공 화면
                start.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.win);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView.setVisibility(View.GONE);
                        start.setVisibility(View.VISIBLE);
                    }
                });
                curLevel++; // 레벨 업
                if(curLevel > MAX_LEVEL) {
                    curLevel = MAX_LEVEL;
                }
                if (end > -1 && lastLevel[end][cursksdleh] < curLevel) {
                    lastLevel[end][cursksdleh] = curLevel;
                }

                Log.d("level", "main: " + end);
                Log.d("level", "main: " + curLevel);
                Log.d("level", "main: " + lastLevel[1]);
            }
        }

        //  두더지 객체 생성
        Log.d("두더지 객체 생성", "main: ");
        mole[0] = new Mole("두더지", 1, 1, 5, 2, R.drawable.enejwl);

        // 레벨 객체 생성
        int[] map_3 = new int[9];
        for (int i=0; i<9; i++) {
            map_3[i] = 1;
        }
//        int[] map_3 = {0,1,0,1,1,1,0,1,0};
//        int[] map_4 = {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0};
        int[] map_5 = new int[25];
        for (int i=0; i<25; i++) {
            map_5[i] = 1;
        }
        level[1] = new Level(map_3, 3, 3, 40, 30, 0, mole, null,
                1, 1.3, 0.17);
        level[2] = new Level(map_5, 5, 5, 50, 40, 0, mole, items,
                0.1, 1.2, 0.15);

        for(int i=1; i<=MAX_LEVEL; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Level " + i);
            radioButton.setId(i);
            // getResources().getIdentifier("level_" + i, "id", package_name)
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(radioButton, layoutParams);
            radioButtons[i-1] = radioButton;
        }

        controll();

        end_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = end_mode.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.time_mode:
                        end = 0;
                        break;
                    case R.id.miss_mode:
                        end = 1;
                        break;
                }

                Log.d("end mode", "onCheckedChanged: " + id);

                controll();
            }
        });

        sksdleh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = sksdleh.getCheckedRadioButtonId();
                for (int i=0; i<4; i++) {
                    if (id == sksdlehs[i]) {
                        Log.d("난이도", "onCheckedChanged: " + id);
                        cursksdleh = i;
                        controll();
                        break;
                    }
                }
            }
        });

        if(curLevel < 0) {
            curLevel = 1;
        }

        Toast.makeText(this, "레벨: " + curLevel, Toast.LENGTH_SHORT).show();

        if(curLevel == 0) {
            radioGroup.check(getResources().getIdentifier("user_mode", "id", package_name));
        } else {
            radioGroup.check(curLevel);
            // getResources().getIdentifier("level_" + curLevel, "id", package_name)
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radioGroup.getCheckedRadioButtonId();
                Log.d("level", "onCheckedChanged: " + id);
                RadioButton radioButton = (RadioButton) findViewById(id);
                String level_text = radioButton.getText().toString();
                Log.d("level", "onCheckedChanged: " + level_text);
                String[] array = level_text.split(" ");
                Log.d("level", "onCheckedChanged: " + array[1]);
                if(array[1].equals("모드")) {
                    curLevel = 0;
                } else {
                    curLevel = Integer.parseInt(array[1]);
                }
//                Toast.makeText(getApplicationContext(), "레벨: " + curLevel, Toast.LENGTH_SHORT).show();
                if(curLevel == 0) {
                    end_mode.setVisibility(View.INVISIBLE);
                    sksdleh.setVisibility(View.INVISIBLE);
                } else {
                    end_mode.setVisibility(View.VISIBLE);
                    sksdleh.setVisibility(View.VISIBLE);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {   // 시작 버튼 클릭 시
            @Override
            public void onClick(View v) {
                ImageButton imageButton = (ImageButton) findViewById(R.id.imageView);
                imageButton.setVisibility(View.GONE);
                if(curLevel != 0) {
                    level[curLevel].setEnd(end);
                    switch (end) {
                        case 0:
                            level[1].setCondition(40);
                            level[2].setCondition(50);
                            break;
                        case 1:
                            level[1].setCondition(5);
                            level[2].setCondition(3);
                            break;
                    }
                    startGame(getApplicationContext(),curLevel, cursksdleh);
                } else {    // 사용자 모드
                    CustomDialog customDialog = new CustomDialog(MainActivity.this);   // 다이얼로그 띄우기
                    customDialog.callFunction(level);                    //  레벨 조건 입력
                }

            }
        });
    }

    private void controll() {
        for (int i=0; i<MAX_LEVEL; i++) {
            radioButtons[i].setVisibility(View.GONE);
        }

        for (int i=0; i<lastLevel[end][cursksdleh]; i++) {
            radioButtons[i].setVisibility(View.VISIBLE);
        }
        radioGroup.check(lastLevel[end][cursksdleh]);
        switch (end) {
            case 0:
                end_mode.check(R.id.time_mode);
                break;
            case 1:
                end_mode.check(R.id.miss_mode);
                break;
        }
        sksdleh.check(sksdlehs[cursksdleh]);

        if(curLevel == 0) {
            end_mode.setVisibility(View.INVISIBLE);
            sksdleh.setVisibility(View.INVISIBLE);
        } else {
            end_mode.setVisibility(View.VISIBLE);
            sksdleh.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        start=findViewById(R.id.start);
        end_mode = (RadioGroup) findViewById(R.id.end_mode);
        sksdleh = (RadioGroup) findViewById(R.id.sksdleh);

        radioGroup = (RadioGroup) findViewById(R.id.level);
    }

    public static void startGame(Context context, int curLevel, int cursksdleh) {
        // intent에서 "curLevel" curLevel 값 보내기
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("curLevel", curLevel);
        intent.putExtra("cursksdleh", cursksdleh);
        context.startActivity(intent);  // GameActivity 전환
    }
}
