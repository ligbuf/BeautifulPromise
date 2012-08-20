package com.beautifulpromise.application.addpromise;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.beautifulpromise.R;
/**
 * @description 알람 반복주기 선택 Dialog
 * @author immk
 */
public class RepeatDayDialog extends Dialog{
	
	/**
	 * RepeatDayDialog 생성자
	 * @param context Context
	 * @param theme 테마 설정
	 */
    public RepeatDayDialog(Context context, int theme) {
        super(context, theme);
    }
	
    /**
     * RepeatDayDialog 생성자
     * @param context
     */
	public RepeatDayDialog(Context context) {
		super(context);
	}
	
	/**
	 * Custom Dialog View 설정
	 * @author immk
	 */
	public static class Builder {
    	 
        private Context context;
        View view;
    	private RepeatDayDialog dialog;
    	
    	Button allDayBtn;
    	Button weekdaysBtn;
    	Button weekendBtn;
    	
    	Button mondayBtn;
    	Button tuesdayBtn;
    	Button wednesdayBtn;
    	Button thursdayBtn;
    	Button fridayBtn;
    	Button saturdayBtn;
    	Button sundayBtn;
    	
    	Button okayBtn;
    	Button cancelBtn;
    	
    	boolean[] dayArr;
    	Button[] dayButtonArr;
    	
    	/**
    	 * Custom Dialog Builder 생성자
    	 * @param context
    	 */
        public Builder(Context context, boolean[] dayArr) {
            this.context = context;
            this.dayArr = dayArr;
        }
       
        /**
         * RepeatDayDialog 생성
         * @return Dialog
         */
        public RepeatDayDialog create() {
        	        	
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	dialog = new RepeatDayDialog(context, R.style.Theme_Dialog);
            view = inflater.inflate(R.layout.addpromise_repeat_day_dialog, null);
            
            allDayBtn = (Button) view.findViewById(R.id.all_day_button);
            weekdaysBtn = (Button) view.findViewById(R.id.weekdays_button);
            weekendBtn = (Button) view.findViewById(R.id.weekend_button);
            
            mondayBtn = (Button) view.findViewById(R.id.monday_button);
            tuesdayBtn = (Button) view.findViewById(R.id.tuesday_button);
            wednesdayBtn = (Button) view.findViewById(R.id.wednesday_button);
            thursdayBtn = (Button) view.findViewById(R.id.thursday_button);
            fridayBtn = (Button) view.findViewById(R.id.friday_button);
            saturdayBtn = (Button) view.findViewById(R.id.saturday_button);
            sundayBtn = (Button) view.findViewById(R.id.sunday_button);
            
            okayBtn = (Button) view.findViewById(R.id.okay_button);
            cancelBtn = (Button) view.findViewById(R.id.cancel_button);
            
            dayButtonArr= new Button[]{mondayBtn, tuesdayBtn, wednesdayBtn, thursdayBtn, fridayBtn, saturdayBtn, sundayBtn};
            
            for(int i = 0 ; i < 7 ; i++){
            	if(dayArr[i]){
            		dayButtonArr[i].setBackgroundResource(R.drawable.popup_create_box1_selection);
            	}else{
            		dayButtonArr[i].setBackgroundResource(R.drawable.popup_create_box1_nonselection);
            	}
            }
            
            allDayBtn.setOnClickListener(buttonClickListener);
            weekdaysBtn.setOnClickListener(buttonClickListener);
            weekendBtn.setOnClickListener(buttonClickListener);
            
            mondayBtn.setOnClickListener(buttonClickListener);
            tuesdayBtn.setOnClickListener(buttonClickListener);
            wednesdayBtn.setOnClickListener(buttonClickListener);
            thursdayBtn.setOnClickListener(buttonClickListener);
            fridayBtn.setOnClickListener(buttonClickListener);
            saturdayBtn.setOnClickListener(buttonClickListener);
            sundayBtn.setOnClickListener(buttonClickListener);
            
            okayBtn.setOnClickListener(buttonClickListener);
            cancelBtn.setOnClickListener(buttonClickListener);
            
            dialog.setContentView(view);			
            return dialog;
        }
        
		View.OnClickListener buttonClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.all_day_button:
					mondayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					tuesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					wednesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					thursdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					fridayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					saturdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					sundayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);	
					for(int i=0; i<7 ; i++)
						dayArr[i] = true;
					break;
					
				case R.id.weekdays_button:
					mondayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					tuesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					wednesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					thursdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					fridayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					saturdayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
					sundayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
					for(int i=0; i<5 ; i++)
						dayArr[i] = true;
					dayArr[5] = false;
					dayArr[6] = false;
					break;
					
				case R.id.weekend_button:
					mondayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
					tuesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
					wednesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
					thursdayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
					fridayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
					saturdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					sundayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
					for(int i=0; i<5 ; i++)
						dayArr[i] = false;
					dayArr[5] = true;
					dayArr[6] = true;
					break;
					
				case R.id.monday_button:
					if(dayArr[0]){
						mondayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
						dayArr[0] = false;
					}else{
						mondayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
						dayArr[0] = true;
					}
					break;
					
				case R.id.tuesday_button:
					if(dayArr[1]){
						tuesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
						dayArr[1] = false;
					}else{
						tuesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
						dayArr[1] = true;
					}
					break;
					
				case R.id.wednesday_button:
					if(dayArr[2]){
						wednesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
						dayArr[2] = false;
					}else{
						wednesdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
						dayArr[2] = true;
					}
					break;
					
				case R.id.thursday_button:
					if(dayArr[3]){
						thursdayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
						dayArr[3] = false;
					}else{
						thursdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
						dayArr[3] = true;
					}
					break;
					
				case R.id.friday_button:
					if(dayArr[4]){
						fridayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
						dayArr[4] = false;
					}else{
						fridayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
						dayArr[4] = true;
					}
					break;
					
				case R.id.saturday_button:
					if(dayArr[5]){
						saturdayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
						dayArr[5] = false;
					}else{
						saturdayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
						dayArr[5] = true;
					}
					break;
					
				case R.id.sunday_button:
					if(dayArr[6]){
						sundayBtn.setBackgroundResource(R.drawable.popup_create_box1_nonselection);
						dayArr[6] = false;
					}else{
						sundayBtn.setBackgroundResource(R.drawable.popup_create_box1_selection);
						dayArr[6] = true;
					}
					break;
					
				case R.id.okay_button:
					((AddPromiseActivity)context).setRepeatDay(dayArr);
					dialog.dismiss();
					break;
					
				case R.id.cancel_button:
					dialog.dismiss();
					break;
					
				default:
					break;
				}
			}
		};
	}

}
