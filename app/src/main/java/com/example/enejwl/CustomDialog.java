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

    final int MAP_MIN = 3;
    final int MAX = 6;
    final int MIN = 2;

    final int MAX_NUM = 5;

    final int MAX_TIME = 180;
    final int MIN_TIME = 10;

    final int MAX_T = 50;
    final int MIN_T = 10;

    EditText width;
    EditText height;
    TextView text;
    EditText limit;
    EditText totalNum;

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
        final CheckBox itemCheck = (CheckBox) dlg.findViewById(R.id.c_itemCheck);
        final Button okButton = (Button) dlg.findViewById(R.id.c_okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.c_cancelButton);
        final RadioButton option1 = (RadioButton) dlg.findViewById(R.id.c_endtime);
        final RadioButton option2 = (RadioButton) dlg.findViewById(R.id.c_endmiss);
        final Button mapBtn = (Button) dlg.findViewById(R.id.mapBtn);
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
                else if(test()) {
                    int limitInt = Integer.parseInt(limit.getText().toString());
                    int totalNumInt = Integer.parseInt(totalNum.getText().toString());
                    if ((endType == GameActivity.END_COUNT && limitInt > MAX_NUM)   // limit 검사
                            || (endType == GameActivity.END_TIME && (limitInt>MAX_TIME || limitInt<MIN_TIME))
                            || totalNumInt > MAX_T  || totalNumInt<MIN_T  // 두더지수 검사
                    ) {
                        Toast.makeText(context, "하나 이상의 입력이 잘못되었습니다.", Toast.LENGTH_SHORT).show();

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

                        // level[0]에 사용자로부터 받은 값 레벨 객체로 저장
                        if (itemCheck.isChecked()) {
                            level[0] = new Level(mapUser,
                                    Integer.parseInt(width.getText().toString()),
                                    Integer.parseInt(height.getText().toString()),
                                    Integer.parseInt(limit.getText().toString()),
                                    Integer.parseInt(totalNum.getText().toString()),
                                    endType, MainActivity.mole, MainActivity.items);
                        } else {
                            level[0] = new Level(mapUser,
                                    Integer.parseInt(width.getText().toString()),
                                    Integer.parseInt(height.getText().toString()),
                                    Integer.parseInt(limit.getText().toString()),
                                    Integer.parseInt(totalNum.getText().toString()),
                                    endType, MainActivity.mole, null);
                        }

                        // 커스텀 다이얼로그를 종료한다.
                        dlg.dismiss();
                        MainActivity.startGame(context, 0);
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

    private boolean test() {
        if (width.getText().toString().equals("")) return false;
        if (height.getText().toString().equals("")) return false;
        if (limit.getText().toString().equals("")) return false;
        if (totalNum.getText().toString().equals("")) return false;
        if (mapWidth != Integer.parseInt(width.getText().toString())) return false;
        if (mapHeight != Integer.parseInt(height.getText().toString())) return false;
        else return aBoolean;
    }
}
