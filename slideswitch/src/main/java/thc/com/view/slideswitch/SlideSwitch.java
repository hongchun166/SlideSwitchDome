package thc.com.view.slideswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import thc.com.slideswitch.R;
import thc.com.thcutils.DpSpPxConvertUtils;


/**
 * Created by Nicky on 2017/9/27.
 * 滑动,小块开关
 * 站在巨人的肩膀上撸代码
 * http://blog.csdn.net/chziroy/article/details/44146911
 * 小滑块改为宽度一半
 */
public class SlideSwitch extends View {

    public SlideSwitch(Context context) {
        this(context, null);
    }

    public SlideSwitch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private final int SHAPE_RECT = 1;
    private final int SHAPE_CIRCLE = 2;

    int RIM_SIZE = 4;   //拇指小块边距

    private Paint paint;
    Rect backRect;//背景大小

    Rect thumbRect;//拇指小块大小
    int thunmWidth;
    int thunmHeight;
    int thunmLeftLocation = 0;
    int thunmMaxLeft;
    int thunmMinLeft;
    int thunmLeftLocationBegin;//每次拇指小块其实位置



    /**
     * 0全透明,255不透明
     */
    int alpha = 255;

    boolean isOpen=false;
    int close_color;
    int open_color;
    int thunm_color;
    int shapeStype=SHAPE_CIRCLE;
    String textOpen,textClose;
    float textSize=0.0f;

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SlideSwitch);
        close_color=typedArray.getColor(R.styleable.SlideSwitch_mSS_close_color,Color.GRAY);
        open_color=typedArray.getColor(R.styleable.SlideSwitch_mSS_open_color,Color.GREEN);
        thunm_color=typedArray.getColor(R.styleable.SlideSwitch_mSS_thunm_color,Color.WHITE);
        shapeStype=typedArray.getInt(R.styleable.SlideSwitch_mSS_shape,SHAPE_CIRCLE);

        isOpen=typedArray.getBoolean(R.styleable.SlideSwitch_mSS_isOpen,false);

        textClose=typedArray.getString(R.styleable.SlideSwitch_mSS_textClose);
        textOpen=typedArray.getString(R.styleable.SlideSwitch_mSS_textOpen);
        textSize=typedArray.getDimension(R.styleable.SlideSwitch_mSS_textSize, DpSpPxConvertUtils.sp2px(context,14));

        typedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(DpSpPxConvertUtils.dip2px(context,1));//笔宽5像素
        RIM_SIZE=DpSpPxConvertUtils.dip2px(context,1);
        // paint.setStyle(Paint.Style.FILL_AND_STROKE);//设置非填充
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = appMeasure(widthMeasureSpec);
        int heightSize = appMeasure(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
        initDrawingVal();
    }
    private int appMeasure(int whMeasureSpec) {
        int whMode = MeasureSpec.getMode(whMeasureSpec);
        int whSize = MeasureSpec.getSize(whMeasureSpec);
        int valueSize = 0;
        if (whMode == MeasureSpec.EXACTLY) {
            valueSize = whSize;
        } else {
            if (whMode == MeasureSpec.AT_MOST) {
                valueSize = Math.min(valueSize, whSize);
            }
        }
        return valueSize;
    }

    /**
     * 初始化绘制参数
     */
    public void initDrawingVal() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        backRect = new Rect(0, 0, width, height);

        if (shapeStype == SHAPE_RECT) {
            thunmHeight = height - RIM_SIZE;
            thunmWidth = width / 2 - RIM_SIZE;
        } else {
            thunmHeight = height - RIM_SIZE;
            thunmWidth = width / 2 - RIM_SIZE;
        }
        thunmMaxLeft = width / 2;
        thunmMinLeft = RIM_SIZE;

        if (isOpen) {
            thunmLeftLocation = thunmMaxLeft;
            alpha = 255;
        } else {
            thunmLeftLocation = RIM_SIZE;
            alpha = 0;
        }

        thunmLeftLocationBegin=thunmLeftLocation;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (shapeStype == SHAPE_RECT) {
            //绘制底色
            paint.setColor(close_color);
            canvas.drawRect(backRect, paint);
            //绘制状态,按滑动距离设置透明度
            paint.setColor(open_color);
            paint.setAlpha(alpha);
            canvas.drawRect(backRect, paint);
        } else {
            paint.setColor(close_color);
            int radius = backRect.height() / 2 - RIM_SIZE;
            canvas.drawRoundRect(new RectF(backRect), radius, radius, paint);
            //绘制状态,按滑动距离设置透明度
            paint.setColor(open_color);
            paint.setAlpha(alpha);
            canvas.drawRoundRect(new RectF(backRect), radius, radius, paint);
        }
        drawaText(canvas);
        drawaThunm(shapeStype,canvas);
        drawaThunmLine(canvas);
    }
    private void drawaText(Canvas canvas){
        if(TextUtils.isEmpty(textClose)||TextUtils.isEmpty(textOpen)){
            return;
        }
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        float textW=paint.measureText(textOpen);
        float textH=paint.ascent()+paint.descent();
        //open 左，close右
        float openX=(getMeasuredWidth()-thunmWidth)/2-textW/2;
        float openY=getMeasuredHeight()/2-textH/2;
        float closeX=thunmWidth+(getMeasuredWidth()-thunmWidth)/2-textW/2;

        canvas.drawText(textClose,closeX,openY,paint);
        canvas.drawText(textOpen,openX,openY,paint);
    }
    private void drawaThunm(int shapeStype,Canvas canvas){
        if (shapeStype == SHAPE_RECT) {
            paint.setColor(thunm_color);
            thumbRect = new Rect(thunmLeftLocation, RIM_SIZE, thunmLeftLocation + thunmWidth, thunmHeight);
            canvas.drawRect(thumbRect, paint);
        }else {
            paint.setColor(thunm_color);
            thumbRect = new Rect(thunmLeftLocation, RIM_SIZE, thunmLeftLocation + thunmWidth, thunmHeight);
            int thumnRadius = thumbRect.height() / 2 - RIM_SIZE;
            canvas.drawRoundRect(new RectF(thumbRect), thumnRadius, thumnRadius, paint);
        }
    }
    private void drawaThunmLine(Canvas canvas){
        if(isOpen){
            paint.setColor(open_color);
        }else{
            paint.setColor(close_color);
        }
        int jianjuX= DpSpPxConvertUtils.dip2px(getContext(),6);
        int jianjuY=DpSpPxConvertUtils.dip2px(getContext(),4);
        int x1=thunmLeftLocation+thunmWidth/2-jianjuX;
        int x2=thunmLeftLocation+thunmWidth/2;
        int x3=thunmLeftLocation+thunmWidth/2+jianjuX;
        int y1=RIM_SIZE+jianjuY;
        int y2=RIM_SIZE+jianjuY;
        int y1End=thunmHeight-RIM_SIZE-jianjuY;
        int y2End=thunmHeight-RIM_SIZE-jianjuY;
        canvas.drawLine(x1,y1,x1,y1End,paint);
        canvas.drawLine(x2,y2,x2,y2End,paint);
        canvas.drawLine(x3,y1,x3,y1End,paint);
    }

    int downX = 0;
    int lastX;
    int diffX;
    long downTime;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled()){
            return true;
        }
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downTime=System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                lastX = (int) event.getRawX();
                diffX = lastX - downX;
                int tempX = diffX / 2 + thunmLeftLocationBegin;
                tempX = (tempX > thunmMaxLeft ? thunmMaxLeft : tempX);
                tempX = (tempX < thunmMinLeft ? thunmMinLeft : tempX);
                thunmLeftLocation = tempX;
                alpha = (int) (255 * (float) tempX / (float) thunmMaxLeft);
                invalidateView();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int upX= (int) event.getRawX();
                int tempDiffX = upX - downX;
                if(System.currentTimeMillis()-downTime<300 && Math.abs(tempDiffX)<10){
                    toggle();
                }else {
                    thunmLeftLocationBegin=thunmLeftLocation;
                    boolean isToRight=thunmLeftLocationBegin>thunmMaxLeft/2?true:false;
                    moveToDest(isToRight);
                }
                break;
        }
        return true;
    }

    public void toggle(){
        if(isOpen){
            moveToDest(false);
        }else {
            moveToDest(true);
        }
    }
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * draw again
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    final int HANDLER_CLOSE=1;
    final int  HANDLER_OPEN=2;
    final int  HANDLER_ANIM_END=3;
    /**
     * 松开，移动到最左或最右
     * @param toRight
     */
    public void moveToDest(final boolean toRight) {
        setEnabled(false);
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case HANDLER_OPEN:
                        isOpen=true;
                        if(onSlideListener!=null){
                            onSlideListener.onSlideChangCallback(SlideSwitch.this,true);
                        }
                        break;
                    case HANDLER_CLOSE:
                        isOpen=false;
                        if(onSlideListener!=null){
                            onSlideListener.onSlideChangCallback(SlideSwitch.this,false);
                        }
                        break;
                    case HANDLER_ANIM_END:
                        setEnabled(true);
                        break;
                }
            }
        };
        moveToDestThread(toRight,handler);
    }

    private void moveToDestThread(final boolean isToRight,final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
              if(isToRight){
                    while (thunmLeftLocation<thunmMaxLeft){
                        alpha = (int) (255 * (float) thunmLeftLocation / (float) thunmMaxLeft);
                        thunmLeftLocation += 3;
                        invalidateView();
                        try {
                            Thread.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                  alpha = 255;
                  thunmLeftLocation = thunmMaxLeft;
                  invalidateView();
                  thunmLeftLocationBegin=thunmMaxLeft;
                    handler.obtainMessage(HANDLER_OPEN).sendToTarget();
              }else {
                  while (thunmLeftLocation>thunmMinLeft){
                      alpha = (int) (255 * (float) thunmLeftLocation / (float) thunmMaxLeft);
                      thunmLeftLocation -= 3;
                      invalidateView();
                      try {
                          Thread.sleep(3);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
                  alpha = 0;
                  thunmLeftLocation = thunmMinLeft;
                  invalidateView();
                  thunmLeftLocationBegin=thunmMinLeft;
                  handler.obtainMessage(HANDLER_CLOSE).sendToTarget();
              }
                handler.obtainMessage(HANDLER_ANIM_END).sendToTarget();
            }
        }).start();
    }


    OnSlideListener onSlideListener;
    public interface OnSlideListener{
        void onSlideChangCallback(SlideSwitch slideSwitch, boolean isOpen);
    }
    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }
    public void setState(boolean isOpen) {
        this.isOpen = isOpen;
        initDrawingVal();
        invalidateView();
        if(onSlideListener != null){
            onSlideListener.onSlideChangCallback(this,isOpen);
        }
    }


}
