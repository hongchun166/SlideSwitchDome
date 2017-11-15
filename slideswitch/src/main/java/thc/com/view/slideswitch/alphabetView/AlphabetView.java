package thc.com.view.slideswitch.alphabetView;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import thc.com.slideswitch.R;
import thc.com.thcutils.DpSpPxConvertUtils;

/**
 * Created by TianHongChun on 2016/4/11.
 *  字母列表view
 *
 */
public class AlphabetView extends View {

    final String[] ALPHABETS= {"★", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z", "#" };

    int selectIndex=-1; // 当前选中的字母
    Context context;
    Paint paint=new Paint();
    float textSize=30;        //没选中文字大小
    float textSizeSelect=30;// 点击的文字大小
    int textColor=Color.BLACK;        //没选中文字颜色
    int textColorSelect=Color.RED;// 点击的文字颜色
    int bgColor=Color.TRANSPARENT;        //没选中时view背景色
    int bgColorSelect=Color.GRAY;// 点击时view背景色

   DefultOnTouchAssortListenerImp.PopViewConfig popViewConfig;

    private OnTouchAssortListener onTouchAssortListener=null;// 选中回掉监听

    public AlphabetView(Context context) {
        this(context,null);
    }

    public AlphabetView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AlphabetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     * @param context
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        this.context=context;
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.AlphabetView);
        bgColor=typedArray.getColor(R.styleable.AlphabetView_mAV_bgColor,Color.parseColor("#50000000"));
        bgColorSelect=typedArray.getColor(R.styleable.AlphabetView_mAV_bgColorSelect,Color.GREEN);
        textColor=typedArray.getColor(R.styleable.AlphabetView_mAV_textColor,Color.WHITE);
        textColorSelect=typedArray.getColor(R.styleable.AlphabetView_mAV_textColorSelect,Color.GREEN);
        textSize=typedArray.getDimension(R.styleable.AlphabetView_mAV_textSize, DpSpPxConvertUtils.sp2px(context,14));
        textSizeSelect=typedArray.getDimension(R.styleable.AlphabetView_mAV_textSizeSelect, DpSpPxConvertUtils.sp2px(context,14));

        int popBgColor=typedArray.getColor(R.styleable.AlphabetView_mAV_popBgColor,-1);
        int popTextColor=typedArray.getColor(R.styleable.AlphabetView_mAV_popTextColor,-1);
        float popTextSize=typedArray.getDimension(R.styleable.AlphabetView_mAV_popTextSize,-1);
        float popW=typedArray.getDimension(R.styleable.AlphabetView_mAV_popW,-1);
        float popH=typedArray.getDimension(R.styleable.AlphabetView_mAV_popH,-1);
        typedArray.recycle();

        popViewConfig=new DefultOnTouchAssortListenerImp.PopViewConfig();
        popViewConfig.bgColor=popBgColor;
        popViewConfig.textColor=popTextColor;
        popViewConfig.textSize=popTextSize;
        popViewConfig.w= (int) popW;
        popViewConfig.h= (int) popH;

        setBackgroundColor(bgColor);
    }

    public void setOnTouchAssortListener(OnTouchAssortListener impOnTouchAssortListener) {
        this.onTouchAssortListener = impOnTouchAssortListener;
    }
    DefultOnTouchAssortListenerImp defultOnTouchAssortListenerImp;
    public void setDefultImpOnTouchAssortListener(){
        defultOnTouchAssortListenerImp=new DefultOnTouchAssortListenerImp(context,true);
        defultOnTouchAssortListenerImp.setPopViewConfig(popViewConfig);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int  width= getWidth();
        int height=getHeight();
        int interval=height/(ALPHABETS.length);
        for (int i=0;i<ALPHABETS.length;i++){
            if(i==selectIndex){
                paint.setColor(textColorSelect);
                paint.setTextSize(textSizeSelect);
//                    paint.setTypeface(Typeface.DEFAULT_BOLD);// 粗体
            }else {
                paint.setColor(textColor);
                paint.setTextSize(textSize);
            }
            paint.setAntiAlias(true);// 抗锯齿
            float x=width/2-paint.measureText(ALPHABETS[i])/2;
            float y=(i+1)*interval;
            canvas.drawText(ALPHABETS[i],x,y,paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //当前Y除每一分的值,就得出Y所在的值位置
        int currentIndex=(int) (event.getY() /(getHeight()/ ALPHABETS.length));

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(bgColorSelect);
                if(currentIndex>=0&&currentIndex<ALPHABETS.length){
                    selectIndex=currentIndex;
                }
                if(selectIndex==-1){
                    selectIndex=0;
                }
                if(defultOnTouchAssortListenerImp!=null){
                    defultOnTouchAssortListenerImp.onTouchAssortChanged(ALPHABETS[selectIndex]);
                }
                if(onTouchAssortListener!=null){
                    onTouchAssortListener.onTouchAssortChanged(ALPHABETS[selectIndex]);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if(currentIndex!=selectIndex){
                    if(currentIndex>=0&&currentIndex<ALPHABETS.length){
                        selectIndex=currentIndex;
                    }
                    if(selectIndex==-1){
                        selectIndex=0;
                    }
                    if(defultOnTouchAssortListenerImp!=null){
                        defultOnTouchAssortListenerImp.onTouchAssortChanged(ALPHABETS[selectIndex]);
                    }
                    if(onTouchAssortListener!=null){
                        onTouchAssortListener.onTouchAssortChanged(ALPHABETS[selectIndex]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(bgColor);
                selectIndex=-1;
                if(defultOnTouchAssortListenerImp!=null){
                    defultOnTouchAssortListenerImp.onTouchAssortUP();
                }
                if(onTouchAssortListener!=null){
                    onTouchAssortListener.onTouchAssortUP();
                }
                break;
        }
        invalidate();
        return true;
    }

}