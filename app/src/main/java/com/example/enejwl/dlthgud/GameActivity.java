package com.example.enejwl.dlthgud;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enejwl.MainActivity;
import com.example.enejwl.R;

import java.util.Random;


public class GameActivity extends AppCompatActivity {
    TextView time ;
    TextView scoreView;

    int score; //현재점수
    int missed; // 놓친 두더지의 수
    int condition; // 총 제한 시간 또는 놓칠 수 있는 최대 두더지 수
    int count; //목표점수
    int width; //맵의 가로크기
    int height; //맵의 세로크기
    int[] map;  // 맵
    int end_method; // 종료 방식

    Thread thread = null;

    final String TAG_ON = "on"; //태그용
    final String TAG_OFF = "off";
    final String TAG_NONE = "none";

    ImageButton[] hole;

    final int LOWER_BOUND = -1;
    final int UPPER_BOUND = 3;

    final int NONE = 0; // 안 하는 거
    final int EMPTY = 1;    // 사용하는 거
    final int MOLE = 2; // 두더지
    final int BOMB = 3; // 아이템

    final int END_TIME = 0;
    final int END_COUNT = 1;

    int a_second;   // 속도

    int level;

    final int MAX_HEIGHT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        play();
    }

    private void play() {
        init();

        level = getIntent().getIntExtra("curLevel",-1);
        if(level > LOWER_BOUND && level < UPPER_BOUND) {
            Level curLevel = MainActivity.level[level];

            // 게임 시작시 필요한 조건을 준비
            score = 0;
            missed = 0;
            condition = curLevel.getCondition();
            count = curLevel.getMoleNum();
            width = curLevel.getWidth();
            height = curLevel.getHeight();
            map = curLevel.getMap();
            end_method = curLevel.getEnd();

            hole = new ImageButton[width * height];

            // curLevel.map에 따라 hole 초기화
            String pkgName = getPackageName();

            int h = MAX_HEIGHT / height;
            final int WIDTH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
            final int HEIGHT = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, getResources().getDisplayMetrics());
            final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    WIDTH, HEIGHT, 1f
            );

            int n = 0;
            for (int i = 0; i < height; i++) {
                LinearLayout linearLayout1 = new LinearLayout(this);
                linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

                for(int j = 0; j < width; j++) {
                    final ImageButton button = new ImageButton(GameActivity.this);
                    button.setId(n+1);
                    button.setLayoutParams(params);
                    button.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    hole[n] = button;

                    if (map[n] == EMPTY) {
                        hole[n].setVisibility(View.VISIBLE);
                        hole[n].setImageResource(R.drawable.ic_launcher_foreground);
                        hole[n].setTag(TAG_OFF);
                    } else if(map[n] == NONE) {
                        hole[n].setVisibility(View.INVISIBLE);
                        hole[n].setTag(TAG_NONE);
                    }

                    hole[n].setOnClickListener(new View.OnClickListener() { //두더지이미지에 온클릭리스너
                        // 화면 터치시 터치 수를 감소시키고, 터치 수가 0이 되면 해당하는
//점수를 score에 더한다.
                        @Override

                        public void onClick(View v) {
                            // TODO 터치 수 --
                            // TODO 터치 수 = 0?
                            if (((ImageButton) v).getTag().toString().equals(TAG_ON)) {
// TODO 두더지? 폭탄?
                                Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_LONG).show();
// TODO score += 점수
                                score++;
                                scoreView.setText(String.valueOf(score));

                                ((ImageButton) v).setImageResource(R.drawable.ic_launcher_foreground);

                                v.setTag(TAG_OFF);

                            } else {

                                Toast.makeText(getApplicationContext(), "bad", Toast.LENGTH_LONG).show();

                                if (score <= 0) {

                                    score = 0;

                                    scoreView.setText(String.valueOf(score));

                                } else {

                                    scoreView.setText(String.valueOf(--score));

                                }

//                        ((ImageView) v).setImageResource(R.drawable.moleup);

//                                v.setTag(TAG_ON);

                            }
                        }

                    });
                    linearLayout1.addView(hole[n]);

                    n++;
                }

                linearLayout.addView(linearLayout1);
            }

            // 사용자가 제한시간 방식을
            //선택했으면
            if (end_method == END_TIME) {
//            // timeCheck스레드, AThread 스레드와 IThread 시작 및 onClick
                thread = new Thread(new GameActivity.timeCheck(condition));
                thread.start();

//            //함수를 호출
            }

            // 놓친 두더지 방식을 선택했으면
            else if (end_method == END_COUNT){
                time.setText(condition + "마리");
                // AThread와 IThread 시작과
                //onClick 함수만 호출
            }

            for(int i = 0; i<hole.length; i++){
                if(curLevel.getMap()[i] == EMPTY) {
                    new Thread(new DThread(i)).start();
                }
            }
        }
    }

    private void init() {
        time=findViewById(R.id.time);

        scoreView=findViewById(R.id.count);
        scoreView.setText(0 + "점");

        a_second = 1000;
    }

    class timeCheck implements Runnable {   // 시간을 1초씩 감소시키는 쓰레드 클래스
        int MAXTIME;    // 총 게임 시간을 저장
        Message message; // i를 전달할 메세지 객체

        public timeCheck(int i) {
            this.MAXTIME = i;
        }

        @Override
        public void run() { // MAXTIME부터 0이 될 때까지 1초에 1씩 감소시키는 함수이다.
            for(int i = MAXTIME; i>=0; i--){
                message = new Message();    // 객체 생성
                message.arg1 = i;
                timeHandler.sendMessage(message);   // 시간 출력
                try {
                    Thread.sleep(a_second);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

            end(score);
            finish();   // 종료
        }
    }

    private int end(int score) {    // 게임이 끝났을 때 승패 판별을 담당하는 함수이다.
        MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;
        mainActivity.finish();

        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        intent.putExtra("curLevel", level);

//        int score; // 현재 점수를 매개변수로 받는다.
        if(score >= count){
            intent.putExtra("isWin", 1);

            startActivity(intent);
            return 1;
        } else {
            intent.putExtra("isWin", 0);

            startActivity(intent);
            return 0;
        }
    };

    private int bomb() {    // 폭탄 아이템을 사용할 때 맵 위에 있는 두더지의 총 점수를 계산하고         더하는 함수
      int hap = 0;  // 아이템을 사용해서 얻는 점수를 저장

      for(int i = 0; i<hole.length; i++) {
          if(map[i] == MOLE) {  // hole[i].getTag() == TAG_ON
              // TODO hap += 두더지의 점수
              map[i] = EMPTY;
//              hole[i].setTag(TAG_OFF);
              hole[i].setImageResource(R.drawable.ic_launcher_foreground);
// TODO
              hole[i].setTag(new String[]{"0", "0"});

          }
      }
      return hap;
    };

    Handler timeHandler = new Handler(){    // TextView에 시간을 출력하는 Handler 객체
        @Override
        public void handleMessage(Message msg) {
            time.setText(msg.arg1 + "초");

//            if(msg.arg1 == 0){
//                thread.interrupt();
//            }
        }
    };

    Handler onHandler = new Handler(){

        @Override

        public void handleMessage(Message msg) {

            hole[msg.arg1].setImageResource(R.drawable.enejwl);

            hole[msg.arg1].setTag(TAG_ON); //올라오면 ON태그 달아줌

        }

    };

//    Handler upHandler = new Handler(){
//
//        @Override
//
//        public void handleMessage(Message msg) {
//            // TODO
//            if(msg.obj == MOLE) {
//// 올라 온 두더지 이미지 출력
//                hole[msg.arg1].setImageResource(R.drawable.enejwl);
//// TODO 두더지 정보를 담고 있는 배열로 설정
//                hole[msg.arg1].setTag(TAG_ON); //올라오면 ON태그 달아줌
//                map[msg.arg1] = MOLE;
//            } else if(msg.obj == BOMB) {
//                // TODO 폭탄 이미지
//                // TODO 버튼 태그 값 아이템 정보를 담고 있는 배열로 설정
//                map[msg.arg1] = BOMB;
//            }
//
//        }
//
//    };
//
//    Handler downHandler = new Handler(){
//
//        @Override
//
//        public void handleMessage(Message msg) {
//// TODO
//            if(msg.obj == MOLE) {
//// 두더지가 내려 간 이미지 출력
//                hole[msg.arg1].setImageResource(R.drawable.ic_launcher_foreground);
//                missed++;
//                // TODO
//                hole[msg.arg1].setTag(new String[]{"0", "0"}); //올라오면 ON태그 달아줌
//                map[msg.arg1] = EMPTY;
//                if(end_method == END_COUNT) {
//                    if(missed == condition) {
//                        end(score); // 종료 함수 호출
//                    }
//                }
//            } else if(msg.obj == BOMB) {
//                // 아이템이 내려 간 이미지 출력
//                hole[msg.arg1].setImageResource(R.drawable.ic_launcher_foreground);
//// TODO
//                hole[msg.arg1].setTag(new String[]{"0", "0"}); //올라오면 ON태그 달아줌
//            }
//
//
//
//
//
//        }
//
//    };


    Handler offHandler = new Handler(){

        @Override

        public void handleMessage(Message msg) {

            hole[msg.arg1].setImageResource(R.drawable.ic_launcher_foreground);

            hole[msg.arg1].setTag(TAG_OFF); //내려오면 OFF태그 달아줌




        }

    };




    public class DThread implements Runnable{ //두더지를 올라갔다 내려갔다 해줌

        int index = 0; //두더지 번호




        DThread(int index){

            this.index=index;

        }




        @Override

        public void run() {

            while(true){

                try {

                    Message msg1 = new Message();

                    int offtime = new Random().nextInt(5000) + 500 ;
//                    int downtime = new Random().nextInt(10000);

                    Thread.sleep(offtime); //두더지가 내려가있는 시간
//                    Thread.sleep(downtime);




                    msg1.arg1 = index;

                    onHandler.sendMessage(msg1);




                    int ontime = new Random().nextInt(1000)+500;

                    Thread.sleep(ontime); //두더지가 올라가있는 시간

                    Message msg2 = new Message();

                    msg2.arg1= index;

                    offHandler.sendMessage(msg2);




                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

            }

        }

    }
}
