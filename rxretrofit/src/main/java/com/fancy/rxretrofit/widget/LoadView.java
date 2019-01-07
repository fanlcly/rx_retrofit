package com.fancy.rxretrofit.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.fancy.rxretrofit.R;
import com.fancy.rxretrofit.impl.ILoadView;

/**
 * 加载框
 *
 * @author fanlei
 * @version 1.0 2019\1\2 0002
 * @since JDK 1.7
 */
public class LoadView implements ILoadView {
    Window window;
    Dialog d;
    private Context context;
    private final CircleProgressView loadingView;

    public LoadView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.base_loading_view, null, Boolean.FALSE);
        loadingView = view.findViewById(R.id.loadingView);

        loadingView.setDuration(1500);

        d = new Dialog(context, R.style.TransDialog);// 加入样式
        d.setCanceledOnTouchOutside(false);
        window = d.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

    }


    @Override
    public void show() {
        loadingView.startAnim();
        if (null != d && !d.isShowing()) {
            d.show();
        }
    }

    @Override
    public void show(String msg) {
        loadingView.startAnim();
        if (null != d && !d.isShowing()) {
            d.show();
        }
    }

    @Override
    public void dismiss() {
        loadingView.stopAnim();
        if (d != null) {
            d.dismiss();
        }
    }
}
