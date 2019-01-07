package com.fancy.retrofit_client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fancy.androidutils.utils.ToastUtils;
import com.fancy.retrofit_client.entity.ListEntity;
import com.fancy.retrofit_client.net.HttpClient;
import com.fancy.rxretrofit.HttpRetrofit;
import com.fancy.rxretrofit.base.RxBaseCallBack;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn1);
        tv1 = findViewById(R.id.tv1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
    }

    private void requestData() {
        Observable observable = HttpClient.getInstance().getRetrofitService().totalEmployInfo();
        HttpRetrofit.getInstance().toSubscribe(observable, new RxBaseCallBack<List<ListEntity>>(getApplicationContext()) {
            @Override
            public void onSuc(Response<List<ListEntity>> response) {
                List<ListEntity> model = response.body();
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < model.size(); i++) {
                    Log.i("TAG", "onSuc: " + model.get(i).getTitle());
                    builder.append(model.get(i).getTitle());
                }

                tv1.setText(builder.toString());

            }

            @Override
            public void onFail(Response response, String message, int failCode) {
                ToastUtils.init(MainActivity.this).show("onFail");
            }

        });

    }


}
