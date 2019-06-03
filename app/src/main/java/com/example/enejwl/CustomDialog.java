package com.example.enejwl;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enejwl.dlthgud.GameActivity;
import com.example.enejwl.dlthgud.Level;

public class CustomDialog {
    private Context context;
    private int flagNum;

    private int endType=0; //종료방식
    private int mapWidth; //맵 가로
    private int mapHeight; //맵 세로
    int[] mapUser=null;// 맵
    Button[][] btn=null;

    boolean aBoolean = false;

    final int MAX_HEIGHT = 300;

    final int MAP_MIN = 3;  // 최소 구멍

    // 맵 크기
    final int MAX = 6;
    final int MIN = 2;

    final int MAX_NUM = 5;  // 놓칠 수 있는 두더지 수

    // 시간 제한
    final int MAX_TIME = 180;
    final int MIN_TIME = 10;

    // 잡아야 하는 두더지 수
    final int MAX_T = 50;
    final int MIN_T = 10;

    // 올라와있는 시간
    final double MIN_UP = 0.5;
    final double MAX_UP = 3.0;

    // 내려가있는 시간
    final double MIN_DOWN = 0.5;
    final double MAX_DOWN = 2.0;

    int sksdleh; //난이도

    EditText width;
    EditText height;
    TextView text;
    EditText limit;
    EditText totalNum;  // 잡아야 하는 두더지 수
    EditText upTimeMin;
    EditText upTimeMax;
    EditText downTimeMin;
    EditText downTimeMax;
    TextView itemText;
    EditText itemProb;
    CheckBox itemCheck;

    public CustomDialog(Context context){
        this.context = context;
    }

    public void callFunction(final Level[] level){
        final Dialog dlg=new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_input);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        width = (EditText) dlg.findViewById(R.id.c_width);
        height = (EditText) dlg.findViewById(R.id.c_height);
        text = (TextView) dlg.findViewById(R.id.c_text);
        limit = (EditText) dlg.findViewById(R.id.c_limit);
        totalNum = (EditText) dlg.findViewById(R.id.c_totalNum);
        upTimeMin = (EditText) dlg.findViewById(R.id.c_upTimeMIN);
        upTimeMax = (EditText) dlg.findViewById(R.id.c_upTimeMAX);
        downTimeMin = (EditText) dlg.findViewById(R.id.c_downTimeMIN);
        downTimeMax = (EditText) dlg.findViewById(R.id.c_downTimeMAX);
        itemText = (TextView) dlg.findViewById(R.id.c_itemText);
        itemProb = (EditText) dlg.findViewById(R.id.c_itemProb);
        itemCheck = (CheckBox) dlg.findViewById(R.id.c_itemCheck);
        final TextView percent = (TextView) dlg.findViewById(R.id.c_percent);
        final Button okButton = (Button) dlg.findViewById(R.id.c_okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.c_cancelButton);
        final RadioButton option1 = (RadioButton) dlg.findViewById(R.id.c_endtime);
        final RadioButton option2 = (RadioButton) dlg.findViewById(R.id.c_endmiss);
        final RadioGroup difficulty = (RadioGroup) dlg.findViewById(R.id.c_difficulty);
        final Button mapBtn = (Button) dlg.findViewById(R.id.mapBtn);

        difficulty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {    // 난이도
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.c_normal:
                        sksdleh = 0;
                        break;
                    case R.id.c_hard:
                        sksdleh = 1;
                        break;
                    case R.id.c_nightmare:
                        sksdleh = 2;
                        break;
                    case R.id.c_korean:
                        sksdleh = 3;
                        break;
                }
            }
        });
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("총 제한시간:");
                endType = GameActivity.END_TIME;
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("놓칠수 있는 최대 두더지 수:");
                endType = GameActivity.END_COUNT;
            }
        });

        // 아이템 사용 체크/체크해제 시 확률 입력란 표시/비표시
        itemCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemCheck.isChecked()){
                    itemText.setVisibility(View.VISIBLE);
                    itemProb.setVisibility(View.VISIBLE);
                    percent.setVisibility(View.VISIBLE);
                }
                else{
                    itemText.setVisibility(View.INVISIBLE);
                    itemProb.setVisibility(View.INVISIBLE);
                    percent.setVisibility(View.INVISIBLE);
                }
            }
        });

        final LinearLayout layout = (LinearLayout)dlg.findViewById(R.id.linearLayout);
        mapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!width.getText().toString().equals("") && !height.getText().toString().equals("")) {
                    layout.removeAllViews();
                    mapHeight = Integer.parseInt(height.getText().toString());
                    mapWidth = Integer.parseInt(width.getText().toString());

                    if(mapWidth>MAX || mapWidth<MIN || mapHeight>MAX || mapHeight<MIN){
                        Toast.makeText(context, "잘못된 크기입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        btn = new Button[mapWidth][mapHeight];
                        LinearLayout linear[] = new LinearLayout[mapHeight];
                        for (int j = 0; j < mapHeight; j++) {
                            linear[j] = new LinearLayout(context);
                            layout.addView(linear[j]);
                        }
                        int h = MAX_HEIGHT / mapHeight;
                        final int WIDTH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics());
                        final int HEIGHT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, context.getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                WIDTH, HEIGHT, 1f
                        );
                        flagNum = mapHeight*mapWidth;
                        for (int j = 0; j < mapHeight; j++) {
                            for (int i = 0; i < mapWidth; i++) {
                                btn[i][j] = new Button(context);
                                btn[i][j].setLayoutParams(params);
                                btn[i][j].setId((i + 1) * (j + 1));
                                btn[i][j].setTag(String.valueOf(GameActivity.EMPTY));
                                linear[j].addView(btn[i][j]);
                                btn[i][j].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (v.getTag() == String.valueOf(GameActivity.NONE)) {
                                            v.setTag(String.valueOf(GameActivity.EMPTY));
                                            v.setAlpha(1);
                                            flagNum++;
                                        } else {
                                            v.setTag(String.valueOf(GameActivity.NONE));
                                            v.setAlpha(0.1f);
                                            flagNum--;
                                        }
                                    }
                                });
                            }
                        }
                        aBoolean = true;
                    }
                }

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("구멍", "onClick: " + flagNum);
                if(flagNum<MAP_MIN){
                    aBoolean = false;
                    Toast.makeText(context, "맵에 3개 이상의 구멍이 있어야합니다.", Toast.LENGTH_SHORT).show();
                }
                else if(test()) {   // 공란이 없음을 검사
                    int limitInt = Integer.parseInt(limit.getText().toString());    // 제한 조건
                    int totalNumInt = Integer.parseInt(totalNum.getText().toString());  // 잡아야 하는 두더지 수
                    double minUp = Double.parseDouble(upTimeMin.getText().toString());  // 올라와 있는 시간 최소
                    double maxUp = Double.parseDouble(upTimeMax.getText().toString());  // 올라와 있는 시간 최대
                    double minDown = Double.parseDouble(downTimeMin.getText().toString());  // 내려가 있는 시간 최소
                    double maxDown = Double.parseDouble(downTimeMax.getText().toString());  // 내려가 있는 시간 최대
                    int item = Integer.parseInt(itemProb.getText().toString());    // 아이템 확률
                    if ((endType == GameActivity.END_COUNT && limitInt > MAX_NUM)   // 놓칠 수 있는 두더지 수 검사
                            || (endType == GameActivity.END_TIME && (limitInt>MAX_TIME || limitInt<MIN_TIME))   // 제한 시간 검사
                            || totalNumInt > MAX_T  || totalNumInt<MIN_T  // 두더지수 검사
                            || minUp>maxUp || minUp<MIN_UP || maxUp>MAX_UP  // 올라와있는 시간
                            || minDown>maxDown || minDown<MIN_DOWN || maxDown>MAX_DOWN  // 내려가있는 시간
                            || (itemCheck.isChecked() && (item>100 || item<0))   // 아이템 확률
                    ) {
                        Toast.makeText(context, "입력이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("입력", "onClick: " +
                                endType + limitInt + MAX_NUM + MAX_TIME + MIN_TIME
                                + totalNumInt + MAX_T  + totalNumInt + MIN_T
                                + minUp + maxUp + minUp + MIN_UP + maxUp + MAX_UP
                                + item + 1);

                    } else {
                        mapUser = new int[mapWidth * mapHeight];
                        String str = "";
                        int num = 0;
                        for (int j = 0; j < mapHeight; j++) {
                            for (int i = 0; i < mapWidth; i++) {
                                mapUser[num] = Integer.parseInt(btn[i][j].getTag().toString());
                                str += btn[i][j].getTag();
                                num++;
                            }
                        }
                        Log.d("custom", "mapUser: " + str);

                        //level[0]에 정보 집어넣기
                        //int[] map_3 = {0,1,0,1,1,1,0,1,0};

                        Log.d("custom", "height: " + mapHeight);

                        for (int i=0; i<MainActivity.mole.length; i++) {
                            MainActivity.mole[i].setUpMin(minUp);
                            MainActivity.mole[i].setUpMax(maxUp);
                        }
                        for (int i=0; i<MainActivity.items.length; i++) {
                            MainActivity.items[i].setUpMin(minUp);
                            MainActivity.items[i].setUpMax(maxUp);
                        }
//                        MainActivity.mole[0] = new Mole("두더지", 1, 1, Integer.parseInt(upTimeMax.getText().toString()), Integer.parseInt(upTimeMin.getText().toString()), R.drawable.enejwl);

                        // level[0]에 사용자로부터 받은 값 레벨 객체로 저장
                        if (itemCheck.isChecked()) {
                            level[0] = new Level(mapUser,
                                    mapWidth, mapHeight, limitInt, totalNumInt, endType, MainActivity.mole, MainActivity.items,
                                    minDown, maxDown, item*0.01);
                        } else {
                            level[0] = new Level(mapUser,
                                    mapWidth, mapHeight, limitInt, totalNumInt, endType, MainActivity.mole, null,
                                    minDown,maxDown,0);
                        }

                        // 커스텀 다이얼로그를 종료한다.
                        dlg.dismiss();
                        MainActivity.startGame(context, 0, sksdleh);
                    }
                }
                else{
                    Toast.makeText(context, "입력을 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });

    }

    private boolean test() {    // 공란이 있으면 false 반환, 없으면 aBoolean 반환
        if (width.getText().toString().equals("")) return false;
        if (height.getText().toString().equals("")) return false;
        if (limit.getText().toString().equals("")) return false;
        if (totalNum.getText().toString().equals("")) return false;
        if (mapWidth != Integer.parseInt(width.getText().toString())) return false;
        if (mapHeight != Integer.parseInt(height.getText().toString())) return false;
        if (upTimeMin.getText().toString().equals("")) return false;
        if (upTimeMax.getText().toString().equals("")) return false;
        if (downTimeMin.getText().toString().equals("")) return false;
        if(downTimeMax.getText().toString().equals("")) return false;
        if(itemCheck.isChecked() && itemProb.getText().toString().equals("")) return false;
        else return aBoolean;
    }
}