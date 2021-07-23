package com.example.memorygallarymvvm.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FollowingFAB extends FloatingActionButton {

    private static final String TAG = "FollowingFAB";

    private int parentHeight;
    private int parentWidth;
    private int slop;

    public FollowingFAB(Context context) {
        this(context,null);
    }

    public FollowingFAB(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public FollowingFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        slop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    private int lastX;
    private int lastY;

    private boolean isDrag = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: DOWN");
                setPressed(true);
                isDrag = false;
                Log.d(TAG, "onTouchEvent: DOWN: isDrag=false");
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if(getParent() != null){
                    parent= (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(parentHeight <= 0 || parentWidth <= 0){
                    //如果不存在父类的宽高则无法拖动，默认直接返回false
                    isDrag=false;
                    Log.d(TAG, "onTouchEvent: MOVE1: isDrag=false");
                    break;
                }

                int dx=rawX-lastX;
                int dy=rawY-lastY;
                //华为手机可能出现只点击按钮但执行了MOVE的蜜汁错误，且此时distance=0，故添加以下代码进行修复
                int distance= (int) Math.sqrt(dx*dx+dy*dy);
                if(distance <= 1){
                    isDrag=false;
                    Log.d(TAG, "onTouchEvent: MOVE2: isDrag=false");
                    break;
                }

                Log.d(TAG, "onTouchEvent: MOVE distance=" + distance + ", slop=" + slop);
                //程序到达此处一定是正在拖动了
                isDrag=true;
                float x=getX()+dx;
                float y=getY()+dy;
                //检测是否到达边缘 左上右下
                x = x<0?0:x>parentWidth-getWidth()?parentWidth-getWidth():x;
                y = getY()<0?0:getY()+getHeight()>parentHeight?parentHeight-getHeight():y;
                setX(x);
                setY(y);
                lastX=rawX;
                lastY=rawY;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: UP");
                if(isDrag()){
                    //恢复按压效果
                    setPressed(false);
                }
                welt(rawX);
                break;
            default:
                isDrag = true;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        Log.d(TAG, "onTouchEvent: isDrag:" + isDrag);
        return isDrag() || super.onTouchEvent(event);
    }

    private boolean isDrag(){
        return isDrag;
    }

    private boolean isLeftSide(){
        return getX()==0;
    }
    private boolean isRightSide(){
        return getX()==parentWidth-getWidth();
    }

    private void welt(int currentX){
        if(!isLeftSide()||!isRightSide()){
            if(currentX>=parentWidth/2){
                //靠右吸附
                animate().setInterpolator(new DecelerateInterpolator())
                        .setDuration(500)
                        .xBy(parentWidth-getWidth()-getX())
                        .start();
            }else {
                //靠左吸附
                ObjectAnimator oa=ObjectAnimator.ofFloat(this,"x",getX(),0);
                oa.setInterpolator(new DecelerateInterpolator());
                oa.setDuration(500);
                oa.start();
            }
        }
    }

}
