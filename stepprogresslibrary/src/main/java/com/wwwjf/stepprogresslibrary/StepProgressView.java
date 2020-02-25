package com.wwwjf.stepprogresslibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author wengjianfeng on 2020/2/25.
 **/
public class StepProgressView extends View {

    private static final String TAG = StepProgressView.class.getSimpleName();
    private Paint textPaint;
    private Paint linePaint;
    private Paint circlePaint;
    private Paint proPaint;
    private float bgRadius;
    private float proRadius;
    private float startX;
    private float stopX;
    private float bgCenterY;
    private int lineBgWidth;
    private int circleBgInnerRadius;
    private int circleBgOutRadius;
    private int circleStrokeWidth;
    private int bgColor;
    private int lineProWidth;
    private int proColor;
    private int textPadding;
    private int timePadding;
    private float textSize;
    private int maxStep;
    private int proStep;
    private String proStepText = "选择充值币种";
    private int interval;


    public StepProgressView(Context context) {
        super(context);
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepProgressView);
        textSize = (int) ta.getDimension(R.styleable.StepProgressView_textSize, Util.dip2px(getContext(), 14f));
        bgColor = ta.getColor(R.styleable.StepProgressView_bgColor, Color.parseColor("#e8eaf2"));
        lineBgWidth = (int) ta.getDimension(R.styleable.StepProgressView_lineBgWidth, Util.dip2px(getContext(), 6f));
        circleBgInnerRadius = (int) ta.getDimension(R.styleable.StepProgressView_circleBgInnerRadius, (float) lineBgWidth);
        circleBgOutRadius = (int) ta.getDimension(R.styleable.StepProgressView_circleBgOutRadius, Util.dip2px(getContext(), 7f));
        circleStrokeWidth = (int) ta.getDimension(R.styleable.StepProgressView_circleStrokeWidth, Util.dip2px(getContext(), 4f));
        maxStep = ta.getInteger(R.styleable.StepProgressView_maxStep, 6);
        ta.recycle();
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(bgColor);
        linePaint.setStrokeWidth(lineBgWidth);
        linePaint.setTextSize(textSize);
        linePaint.setTextAlign(Paint.Align.CENTER);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.parseColor("#17364e"));
//        textPaint.setStrokeWidth(lineProWidth);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.LEFT);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    public void setProgressText(int progress, String progressText) {
        proStep = progress;
        proStepText = progressText;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int bgWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            bgWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        } else {
            bgWidth = Util.dip2px(getContext(), 311);
        }

        int bgHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            bgHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        } else {
            bgHeight = Util.dip2px(getContext(), 49);
        }
        Log.e(TAG, "=====onMeasure bgHeight=" + bgHeight + ",bgWidth=" + bgWidth);
        stopX = bgWidth;
        startX = getPaddingStart();
        bgCenterY = getPaddingTop() + (float) lineBgWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "=====onDraw");
        interval = (int) ((stopX - startX) / (maxStep - 1));
        drawBg(canvas);
//        drawProgress(canvas);
        drawText(canvas);
    }


    private void drawBg(Canvas canvas) {

        Log.e(TAG, "=====drawBg:" + bgCenterY);
        for (int i = 0; i < maxStep; i++) {

            circlePaint.setColor(Color.WHITE);
            circlePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(startX + interval * i, bgCenterY, (float) circleBgInnerRadius, circlePaint);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(circleStrokeWidth);
            if (i <= proStep) {
                circlePaint.setColor(Color.parseColor("#3b55e6"));
            } else {
                circlePaint.setColor(Color.parseColor("#9FA5B4"));
            }
            if (i > 0 && i < maxStep - 1) {
                linePaint.setColor(bgColor);
            } else {
                linePaint.setColor(Color.parseColor("#3b55e6"));
            }
            canvas.drawLine(startX + interval * i + circleBgOutRadius, bgCenterY, stopX, bgCenterY, linePaint);
            canvas.drawCircle(startX + interval * i, bgCenterY, (float) (circleBgOutRadius), circlePaint);

        }
    }

    private void drawText(Canvas canvas) {
        //文字高度
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading;
        if (proStep == 0) {
            textPaint.setTextAlign(Paint.Align.LEFT);
        } else if (proStep >= maxStep - 1) {
            textPaint.setTextAlign(Paint.Align.RIGHT);
        } else {
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
        canvas.drawText(proStepText, startX + interval * proStep, bgCenterY + (float) lineBgWidth / 2 + textHeight, textPaint);
    }
}
