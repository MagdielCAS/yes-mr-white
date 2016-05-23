package com.equipe.yesmrwhite.main;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.equipe.yesmrwhite.canhao.MainActivityCanhao;
import com.equipe.yesmrwhite.quiz.MainActivityQuiz;
import com.example.syllas.yesmrwhite.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btQuiz = (Button) findViewById(R.id.btQuiz);
    }

    public void entrarOnClick(View v) {
        if(v.getId()==R.id.btQuiz){
            startActivity(new Intent(this, MainActivityQuiz.class));
        }else{
            startActivity(new Intent(this, MainActivityCanhao.class));
        }
    }

}
