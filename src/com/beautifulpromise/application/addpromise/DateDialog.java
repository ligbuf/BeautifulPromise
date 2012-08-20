package com.beautifulpromise.application.addpromise;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.beautifulpromise.R;

/**
 * @description 시작일과 종료일 선택
 * @author immk
 */
public class DateDialog extends Dialog{

	/**
	 * Custom Dialog 생성자
	 * @param context Context
	 * @param theme 테마 설정
	 */
    public DateDialog(Context context, int theme) {
        super(context, theme);
    }
	
    /**
     * Custom Dialog 생성자
     * @param context Context
     */
	public DateDialog(Context context) {
		super(context);
	}
	
	/**
	 * Custom Dialog View 설정
	 * @author immk
	 */
	public static class Builder {
    	 
        private Context context;
        private DateDialog dialog;
        View view;
        LinearLayout startDateLayout;
        LinearLayout endDateLayout;
        TextView startDateText;
        TextView endDateText;
        DatePicker datePicker;
    	Button okayBtn;
    	Button cancelBtn;
    	
    	int startYear;
    	int startMonth;
    	int startDay;
    	
    	int endYear;
    	int endMonth;
    	int endDay;

    	enum SELECT {start, end};
    	SELECT select = SELECT.start;
    	
    	/**
    	 * Custom Dialog Builder 생성자
    	 * @param context
    	 */
        public Builder(Context context) {
            this.context = context;
        }
        
        /**
         * 시작일과 종료일 설정
         * @param startYear 시작년도
         * @param startMonth 시작 월
         * @param startDay 시작일
         * @param endYear 끝나는 년도
         * @param endMonth 끝나는 월
         * @param endDay 끝나는 일
         */
        public void setDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
            this.startYear = startYear;
            this.startMonth = startMonth;
            this.startDay = startDay;
            this.endYear = endYear;
            this.endMonth = endMonth;
            this.endDay = endDay;
        }
       
        /**
         * DateDialog 생성
         * @return Dialog
         */
        public DateDialog create() {
        	        	
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	dialog = new DateDialog(context, R.style.Theme_Dialog);
            view = inflater.inflate(R.layout.addpromise_date_dialog, null);
            
            startDateText = (TextView) view.findViewById(R.id.start_date_text);
            endDateText = (TextView) view.findViewById(R.id.end_date_text);
            
            startDateLayout = (LinearLayout) view.findViewById(R.id.start_date_layout);
            endDateLayout = (LinearLayout) view.findViewById(R.id.end_date_layout);

            datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            okayBtn = (Button) view.findViewById(R.id.okay_button);
            cancelBtn = (Button) view.findViewById(R.id.cancel_button);
            
            startDateLayout.setOnClickListener(buttonClickListener);
            endDateLayout.setOnClickListener(buttonClickListener);
            
            okayBtn.setOnClickListener(buttonClickListener);
            cancelBtn.setOnClickListener(buttonClickListener);
            
			startDateLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_create_box1_selection));
			endDateLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_create_box1_nonselection));
			
			startDateText.setText("" + startYear + "년 " + (startMonth+1) + "월 " + startDay + "일");
			endDateText.setText("" + endYear + "년 " + (endMonth+1) + "월 " + endDay + "일");
			
			datePicker.init(startYear, startMonth, startDay, mDatePickerListener);
            dialog.setContentView(view);			
            return dialog;
        }
        
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.start_date_layout:
					select = SELECT.start;
					startDateLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_create_box1_selection));
					endDateLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_create_box1_nonselection));
					datePicker.init(startYear, startMonth, startDay, mDatePickerListener);
					break;
				case R.id.end_date_layout:
					select = SELECT.end;
					startDateLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_create_box1_nonselection));
					endDateLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_create_box1_selection));
					datePicker.init(endYear, endMonth, endDay, mDatePickerListener);
					break;
				case R.id.okay_button:
					((AddPromiseActivity) context).setDate(startYear, startMonth, startDay, endYear, endMonth, endDay);
					dialog.cancel();
					break;
				case R.id.cancel_button:
					dialog.cancel();
					break;
				default:
					break;
				}
			}
		};
		
		DatePicker.OnDateChangedListener mDatePickerListener = new DatePicker.OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				if(select == SELECT.start){
					startYear = year;
					startMonth = monthOfYear;
					startDay = dayOfMonth;
					startDateText.setText("" + startYear + "년 " + (startMonth+1) + "월 " + startDay + "일");
				}else if(select == SELECT.end){
					endYear = year;
					endMonth = monthOfYear;
					endDay = dayOfMonth;
					endDateText.setText("" + endYear + "년 " + (endMonth+1) + "월 " + endDay + "일");
				}
			}
		};
	}
}