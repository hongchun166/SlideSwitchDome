package thc.com.view.slideswitch.alphabetView;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import thc.com.thcutils.DpSpPxConvertUtils;

/**
 * Created by TianHongChun on 2016/4/11.
 */
public  class DefultOnTouchAssortListenerImp implements OnTouchAssortListener {



    Context context;
    PopupWindow popupWindow;

    TextView textViewContent;
    boolean isShowCallbackPopupWindow=true;
    RelativeLayout relativeLayout;

    PopViewConfig popViewConfig;

    public static class PopViewConfig{
        public  int bgColor=-1;
        public  int textColor=-1;
        public  float textSize=-1;
        public  int w=-1;
        public  int h=-1;
    }

    public DefultOnTouchAssortListenerImp(Context context){
        this.context=context;
    }
    public DefultOnTouchAssortListenerImp(Context context, boolean isShowCallbackPopupWindow){
        this.context=context;
        this.isShowCallbackPopupWindow=isShowCallbackPopupWindow;
        popViewConfig=new PopViewConfig();
        popViewConfig.bgColor=Color.parseColor("#70000000");
        popViewConfig.textColor=Color.parseColor("#ffffff");
        popViewConfig.textSize= DpSpPxConvertUtils.dip2px(context,20);
        popViewConfig.w= DpSpPxConvertUtils.dip2px(context,100);
        popViewConfig.h= DpSpPxConvertUtils.dip2px(context,100);
    }
    public void setPopViewConfig(PopViewConfig popViewConfig){
        if(popViewConfig.bgColor!=-1){
            this.popViewConfig.bgColor=popViewConfig.bgColor;
        }
        if(popViewConfig.textColor!=-1){
            this.popViewConfig.textColor=popViewConfig.textColor;
        }
        if(popViewConfig.textSize!=-1){
            this.popViewConfig.textSize=popViewConfig.textSize;
        }
        if(popViewConfig.w!=-1){
            this.popViewConfig.w=popViewConfig.w;
        }
        if(popViewConfig.h!=-1){
            this.popViewConfig.h=popViewConfig.h;
        }

    }
    @Override
    public void onTouchAssortChanged(String s) {
        if(isShowCallbackPopupWindow){
            if(popupWindow==null){
                relativeLayout=new RelativeLayout(context);
                relativeLayout.setBackgroundColor(popViewConfig.bgColor);
                relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textViewContent=new TextView(context);
                textViewContent.setId(0x00000000);
                textViewContent.setTextColor(popViewConfig.textColor);
                textViewContent.setTextSize(popViewConfig.textSize);
                textViewContent.setGravity(Gravity.CENTER);
                relativeLayout.addView(textViewContent, new ViewGroup.LayoutParams(popViewConfig.w, popViewConfig.h));

                popupWindow=new PopupWindow(relativeLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        false);
            }
            textViewContent.setText(s);
            popupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER,0,0);
        }
    }
    @Override
    public void onTouchAssortUP() {
        if(popupWindow!=null){
            popupWindow.dismiss();
            popupWindow=null;
            relativeLayout=null;
            textViewContent=null;
        }
    }

}