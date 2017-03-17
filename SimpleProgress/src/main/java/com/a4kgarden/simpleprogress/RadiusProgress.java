package com.a4kgarden.simpleprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.a4kgarden.mysimpleprogress.R;

/**
 * Created by HY on 2017/3/6.
 */

public class RadiusProgress extends View {
    private Context context;
    /**
     * 背景填充图
     */
    private Bitmap mBackgroundBitmap;
    /**
     * 背景进度条未到达时颜色
     */
    private int baColor;
    /**
     * 当前的进度，为0-100
     */
    private int progress;
    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 用于图片平铺
     */
    private BitmapShader progShader;
    /**
     * 圆角半径
     */
    private int mRadius;
    private int max;//最大范围
    private RectF fillRectF;

    public RadiusProgress(Context context) {
        this(context, null);
        this.context = context;
    }

    public RadiusProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public RadiusProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RadiusProgress);
        mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.RadiusProgress_thumb_src, R.drawable.seek_thumb));
        baColor = array.getColor(R.styleable.RadiusProgress_bgColor, Color.GRAY);
        max = array.getInteger(R.styleable.RadiusProgress_max,100);
        mRadius = array.getDimensionPixelSize(R.styleable.RadiusProgress_radius, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
        progShader = new BitmapShader(mBackgroundBitmap, Shader.TileMode.REPEAT,
                Shader.TileMode.CLAMP);
        setProgress(progress);
        array.recycle();
        paint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas, baColor);
    }

    private void drawProgress(Canvas canvas, int baColor) {
        //绘制未填充进度条
        paint.reset();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(baColor);
        RectF backgroundRectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(backgroundRectF, mRadius, mRadius, paint);

        //绘制填充条
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setShader(progShader);
        float mprogress = ((float)progress / (float) max * getWidth());//实际进度
        if (mprogress<=mRadius*2&&mprogress>mRadius){
            fillRectF = new RectF(0, 0, mprogress, getHeight());
            canvas.drawRoundRect(fillRectF, mprogress, mprogress, paint);
        }else if (progress<mRadius&&progress>0){
            canvas.drawBitmap(GetRoundedCornerBitmap(mBackgroundBitmap),0,0,paint);
        }else {
            fillRectF = new RectF(0, 0, mprogress, getHeight());
            canvas.drawRoundRect(fillRectF, mRadius, mRadius, paint);
        }



    }

    /**
     * dp转px
     *
     * @param dpValue
     * @return
     */
    public float dip2px(float dpValue) {
        final float scale = this.context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
    public void setMax(int max){
        this.max = max;
        invalidate();
    }

    /**
     * 将bitmap处理为圆角
     * @param bitmap
     * @return
     */
    public  Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(progress,bitmap.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),bitmap.getHeight());

            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),getHeight()));

            final float roundPx =mRadius;

            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);

            paint.setColor(Color.WHITE);

            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }
}
