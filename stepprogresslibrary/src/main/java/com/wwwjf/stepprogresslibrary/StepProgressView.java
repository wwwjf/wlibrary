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
    private float startX;
    private float stopX;
    private float bgCenterY;
    private int lineBgWidth;
    private int circleInnerRadius;
    private int circleInnerColor;
    private int circleOutRadius;
    private int circleOutColor;
    private int circleOutFinishColor;
    private int circleProgressOutColor;
    private int circleStrokeWidth;
    private int lineColor;
    private int lineProgressColor;
    private float textSize;
    private float textPaddingTop;
    private int textColor;
    private int maxStep;
    private int progressStep;
    private String progressStepText = "进度文字";
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
        lineColor = ta.getColor(R.styleable.StepProgressView_spv_lineColor, Color.parseColor("#d2d4da"));
        lineProgressColor = ta.getColor(R.styleable.StepProgressView_spv_lineProgressColor, Color.parseColor("#6E93F1"));
        lineBgWidth = (int) ta.getDimension(R.styleable.StepProgressView_spv_lineWidth, Util.dip2px(getContext(), 6f));
        circleInnerRadius = (int) ta.getDimension(R.styleable.StepProgressView_spv_circleInnerRadius, (float) lineBgWidth);
        circleInnerColor = ta.getColor(R.styleable.StepProgressView_spv_circleInnerColor, Color.WHITE);
        circleOutRadius = (int) ta.getDimension(R.styleable.StepProgressView_spv_circleOutRadius, Util.dip2px(getContext(), 7f));
        circleOutColor = ta.getColor(R.styleable.StepProgressView_spv_circleOutColor, Color.parseColor("#9FA5B4"));
        circleOutFinishColor = ta.getColor(R.styleable.StepProgressView_spv_circleOutFinishColor, Color.parseColor("#3B55E6"));
        circleProgressOutColor = (int) ta.getColor(R.styleable.StepProgressView_spv_circleProgressOutColor, Color.parseColor("#de3f3d"));
        circleStrokeWidth = (int) ta.getDimension(R.styleable.StepProgressView_spv_circleStrokeWidth, Util.dip2px(getContext(), 4f));
        maxStep = ta.getInteger(R.styleable.StepProgressView_spv_maxStep, 6);
        progressStep = ta.getInteger(R.styleable.StepProgressView_spv_progress, 2);
        progressStepText = ta.getString(R.styleable.StepProgressView_spv_progressText);
        textSize = (int) ta.getDimension(R.styleable.StepProgressView_spv_textSize, Util.dip2px(getContext(), 12f));
        textPaddingTop = (int) ta.getDimension(R.styleable.StepProgressView_spv_textPaddingTop, Util.dip2px(getContext(), 6f));
        textColor = ta.getColor(R.styleable.StepProgressView_spv_textColor, circleProgressOutColor);
        ta.recycle();
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineBgWidth);
        linePaint.setTextSize(textSize);
        linePaint.setTextAlign(Paint.Align.CENTER);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.LEFT);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(circleInnerColor);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    public void setProgressInfo(int progress, String progressText) {
        progressStep = progress;
        progressStepText = progressText;
    }

    public int getLineBgWidth() {
        return lineBgWidth;
    }

    public void setLineBgWidth(int lineBgWidth) {
        this.lineBgWidth = lineBgWidth;
    }

    public int getCircleInnerRadius() {
        return circleInnerRadius;
    }

    public void setCircleInnerRadius(int circleInnerRadius) {
        this.circleInnerRadius = circleInnerRadius;
    }

    public int getCircleInnerColor() {
        return circleInnerColor;
    }

    public void setCircleInnerColor(int circleInnerColor) {
        this.circleInnerColor = circleInnerColor;
    }

    public int getCircleOutRadius() {
        return circleOutRadius;
    }

    public void setCircleOutRadius(int circleOutRadius) {
        this.circleOutRadius = circleOutRadius;
    }

    public int getCircleOutColor() {
        return circleOutColor;
    }

    public void setCircleOutColor(int circleOutColor) {
        this.circleOutColor = circleOutColor;
    }

    public int getCircleOutFinishColor() {
        return circleOutFinishColor;
    }

    public void setCircleOutFinishColor(int circleOutFinishColor) {
        this.circleOutFinishColor = circleOutFinishColor;
    }

    public int getCircleProgressOutColor() {
        return circleProgressOutColor;
    }

    public void setCircleProgressOutColor(int circleProgressOutColor) {
        this.circleProgressOutColor = circleProgressOutColor;
    }

    public int getCircleStrokeWidth() {
        return circleStrokeWidth;
    }

    public void setCircleStrokeWidth(int circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineProgressColor() {
        return lineProgressColor;
    }

    public void setLineProgressColor(int lineProgressColor) {
        this.lineProgressColor = lineProgressColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getTextPaddingTop() {
        return textPaddingTop;
    }

    public void setTextPaddingTop(float textPaddingTop) {
        this.textPaddingTop = textPaddingTop;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
    }

    public int getProgressStep() {
        return progressStep;
    }

    public void setProgressStep(int progressStep) {
        this.progressStep = progressStep;
    }

    public String getProgressStepText() {
        return progressStepText;
    }

    public void setProgressStepText(String progressStepText) {
        this.progressStepText = progressStepText;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int bgWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            bgWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        } else {
            bgWidth = Util.dip2px(getContext(), 311);
        }

        stopX = bgWidth;
        startX = getPaddingStart() + circleOutRadius + circleStrokeWidth / 2;
        bgCenterY = getPaddingTop() + (float) lineBgWidth / 2;

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = MeasureSpec.getSize(measureSpec) + getPaddingTop() + getPaddingBottom();
        } else {
            result = Util.dip2px(getContext(), 64);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    private int measureWidth(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 75;//根据自己的需要更改
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        interval = (int) ((stopX - startX) / (maxStep - 1));
        drawBg(canvas);
        drawText(canvas);
    }


    /**
     * 画圆形、画线
     *
     * @param canvas canvas
     */
    private void drawBg(Canvas canvas) {

        for (int i = 0; i < maxStep; i++) {
            circlePaint.setColor(circleInnerColor);
            circlePaint.setStyle(Paint.Style.FILL);
            //内圈小圆
            canvas.drawCircle(startX + interval * i, bgCenterY, (float) circleInnerRadius, circlePaint);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(circleStrokeWidth);
            if (i < progressStep) {
                circlePaint.setColor(circleOutFinishColor);
            } else if (i == progressStep) {
                circlePaint.setColor(circleProgressOutColor);
            } else {
                circlePaint.setColor(circleOutColor);
            }
            if (i > progressStep - 1 && i <= maxStep - 1) {
                linePaint.setColor(lineColor);
            } else {
                linePaint.setColor(lineProgressColor);
            }
            //线
            if (i < maxStep - 1) {
                canvas.drawLine(startX + interval * i + circleOutRadius, bgCenterY, stopX, bgCenterY, linePaint);
            }
            //外圈大圆
            canvas.drawCircle(startX + interval * i, bgCenterY, (float) (circleOutRadius), circlePaint);

        }
    }

    /**
     * 画文字
     *
     * @param canvas canvas
     */
    private void drawText(Canvas canvas) {
        //文字高度
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading;
        if (progressStep == 0) {
            textPaint.setTextAlign(Paint.Align.LEFT);
            startX = startX - Util.dip2px(getContext(), 8f);
        } else if (progressStep >= maxStep - 1) {
            textPaint.setTextAlign(Paint.Align.RIGHT);
            startX = startX + Util.dip2px(getContext(), 8f);
        } else {
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
        canvas.drawText(progressStepText,
                startX + interval * progressStep,
                bgCenterY + (float) lineBgWidth / 2 + textHeight + textPaddingTop,
                textPaint);
    }
}
