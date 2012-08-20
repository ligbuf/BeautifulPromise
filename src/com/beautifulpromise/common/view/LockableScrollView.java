package com.beautifulpromise.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.beautifulpromise.common.Var;

/**
 * side navigation에서 side에서만 동작하도록 scroll을 lock 하는 클래스
 * @author JM
 *
 */
public class LockableScrollView extends HorizontalScrollView {

	/**
	 * constructor
	 * @param context
	 * @param attrs
	 */
	public LockableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// true if we can scroll (not locked) 
    // false if we cannot scroll (locked) 
    private boolean mScrollable = true; 
 
    /**
     * scroll 가능/불가능 세팅
     * @param scrollable
     */
    public void setIsScrollable(boolean scrollable) { 
        mScrollable = scrollable; 
    }
    
    /**
     * scroll 가능 여부 리턴
     * @return
     */
    public boolean getIsScrollable()  {
        return mScrollable; 
    } 
 
    /**
     * 화면 touch event catch
     */
    @Override 
    public boolean onTouchEvent(MotionEvent ev) { 
        switch (ev.getAction()) { 
            case MotionEvent.ACTION_DOWN: 
                // if we can scroll pass the event to the superclass 
                if (mScrollable) return super.onTouchEvent(ev); 
                // only continue to handle the touch event if scrolling enabled 
                return mScrollable; // mScrollable is always false at this point 
            default: 
                return super.onTouchEvent(ev); 
        } 
    } 
    
    /**
     * touch event intercept
     */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (Var.menuShowFlag) { // 메뉴바 show 상태
			if (ev.getX() > Var.LEFT_MENUBAR_CLOSE_RANGE) { // 메뉴바 넓이넘게 터치하면 touch event 뺐음 + 횡스크롤 lock
				mScrollable = true;
				
				if(ev.getY() < Var.LEFT_MENUBAR_HEIGHT) //메뉴바 높이 안쪽으로 터치하면 touch event 준다.
					return false;
				
				return true;
			} else { // 메뉴바 범위내에서 터치하면 터치이벤트 뺏지않음
				mScrollable = true;
				return false;
			}
		} else { // 메뉴바 gone 상태
			if (ev.getX() > Var.LEFT_MENUBAR_OPEN_RANGE) { // 끝에 터치하지 않으면 lock함
				mScrollable = false;
				return false;
			} else { // 끝에 터치하면 unlock 함
				mScrollable = true;
				return true;
			}
			
		}
	}

} 


