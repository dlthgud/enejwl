package com.example.enejwl;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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

    private int endType=0; //종료방식
    private int mapWidth; //맵 가로
    private int mapHeight; //맵 세로
    int[] mapUser=null;// 맵
    Button[][] btn=null;

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
        final EditText width = (EditText) dlg.findViewById(R.id.c_width);
        final EditText height = (EditText) dlg.findViewById(R.id.c_height);
        final TextView text = (TextView) dlg.findViewById(R.id.c_text);
        final EditText limit = (EditText) dlg.findViewById(R.id.c_limit);
        final EditText totalNum = (EditText) dlg.findViewById(R.id.c_totalNum);
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
                endType = 0;
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("놓칠수 있는 최대 두더지 수:");
                endType = 1;
            }
        });
        final LinearLayout layout = (LinearLayout)dlg.findViewById(R.id.linearLayout);
        mapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                layout.removeAllViews();
                mapHeight = Integer.parseInt(height.getText().toString());
                mapWidth = Integer.parseInt(width.getText().toString());

                btn = new Button[mapWidth][mapHeight];
                LinearLayout linear[] = new LinearLayout[mapHeight];
                for(int j=0;j<mapHeight;j++){
                    linear[j] = new LinearLayout(context);
                    layout.addView(linear[j]);
                }
                for (int j = 0; j < mapHeight; j++) {
                    for(int i=0;i<mapWidth;i++)  {
                        btn[i][j] = new Button(context);
                        btn[i][j].setWidth(2); //버튼크기 제어 제대로해야함
                        btn[i][j].setId((i + 1) * (j + 1));
                        btn[i][j].setTag("1");
                        linear[j].addView(btn[i][j]);
                        btn[i][j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(v.getTag()=="0"){
                                    v.setTag("1");
                                    v.setBackgroundColor(Color.GREEN);
                                }
                                else {
                                    v.setTag("0");
                                    v.setBackgroundColor(Color.BLUE);
                                }
                            }
                        });
                    }
                }

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapUser = new int [mapWidth*mapHeight];
                String str = "";
                int num=0;
                for (int j = 0; j < mapHeight; j++){
                    for(int i=0;i<mapWidth;i++)  {
                        mapUser[num] = Integer.parseInt(btn[i][j].getTag().toString());
                        str +=  btn[i][j].getTag();
                        num++;
                    }
                }
                Log.d("custom", "mapUser: " + str);

                //level[0]에 정보 집어넣기
                //int[] map_3 = {0,1,0,1,1,1,0,1,0};

                Log.d("custom", "height: " + mapHeight);

                if(itemCheck.isChecked()){
                    level[0] = new Level(mapUser,
                            Integer.parseInt(width.getText().toString()),
                            Integer.parseInt(height.getText().toString()),
                            Integer.parseInt(limit.getText().toString()),
                            Integer.parseInt(totalNum.getText().toString()),
                            endType, MainActivity.mole,MainActivity.items);
                }else{
                    level[0] = new Level(mapUser,
                            Integer.parseInt(width.getText().toString()),
                            Integer.parseInt(height.getText().toString()),
                            Integer.parseInt(limit.getText().toString()),
                            Integer.parseInt(totalNum.getText().toString()),
                            endType, MainActivity.mole,null);
                }

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
                // intent에서 "curLevel" curLevel 값 보내기
                Intent intent = new Intent(context, GameActivity.class);

                intent.putExtra("curLevel", 0);
                context.startActivity(intent);  // GameActivity 전환
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });

    }
}
