package com.cryallen.commonlib.widgets;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.cryallen.commonlib.R;

/**
 * 带有进度变化的button
 * Created by chenran on 2017/4/5.
 *
 */
public class ProgressButton extends AppCompatButton {
    public static final int MAX_PROGRESS = 1000;
    private boolean isAdding = false;
    private int progress = 0;
    private int progressStrokeWidth = 6;

    private Paint mPaint;
    private RectF dst;

    private Context context;

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        dst = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(progressStrokeWidth);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        mPaint.setColor(context.getResources().getColor(R.color.dark_grey));
        canvas.drawColor(Color.TRANSPARENT);

        dst.left = progressStrokeWidth / 2; // 左上角x
        dst.top = progressStrokeWidth / 2; // 左上角y
        dst.right = width - progressStrokeWidth / 2; // 左下角x
        dst.bottom = height - progressStrokeWidth / 2; // 右下角y

        canvas.drawArc(dst, -90, 360, false, mPaint);

        if (isAdding) {
            mPaint.setColor(context.getResources().getColor(R.color.light_yellow));
            canvas.drawArc(dst, -90, ((float) progress / MAX_PROGRESS) * 360, false, mPaint);
        }
    }

    /**
     * 非ＵＩ线程调用
     */
    public void setProgressNotInUiThread(int progress, boolean isAdding) {
        this.isAdding = isAdding;
        this.progress = progress;
        this.postInvalidate();
    }
}