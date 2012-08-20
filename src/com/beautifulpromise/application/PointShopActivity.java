package com.beautifulpromise.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.beautifulpromise.R;
import com.beautifulpromise.application.HomeActivity.MyListAdapter;
import com.beautifulpromise.application.addpromise.DonationDTO;
import com.beautifulpromise.application.checkpromise.CycleCheckActivity;
import com.beautifulpromise.application.checkpromise.EtcCheckActivity;
import com.beautifulpromise.application.checkpromise.WorkCheckActivity;
import com.beautifulpromise.common.alarm.Alarm;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.common.utils.ImageUtils;
import com.beautifulpromise.database.CheckDAO;
import com.beautifulpromise.database.CheckDBHelper;
import com.beautifulpromise.database.DatabaseHelper;
import com.beautifulpromise.database.GoalsDAO;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author ou 포인트샵 엑티비티 클래스
 * 
 */
public class PointShopActivity extends BeautifulPromiseActivity {
	/** Called when the activity is first created. */
	ListView ItemListView;
	MyListAdapter MyAdapter;
	ArrayList<PointItemDTO> ItemList;

	/**
	 * 포인트샵 엑티비티의 onCreate메소드
	 * 포인트샵에 들어가는 뷰들 세팅하고
	 * 각 아이템을 loadItem메소드 호출하여 ArrayList<PointItemDTO> ItemList에 넣음
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = (LinearLayout) View.inflate(this,
				R.layout.pointshopactivity, null);
		setActivityLayout(layout);

		ItemListView = (ListView) findViewById(R.id.point_list);

		ItemList = loadItem();
	}

	/**
	 * 리스트뷰의 OnClickListener
	 * @author ou
	 *
	 */
	class MyListAdapter extends BaseAdapter implements OnClickListener {
		Context maincon;
		LayoutInflater Inflater;
		ArrayList<PointItemDTO> arSrc;
		int layout;

		/**
		 * 리스트뷰 어뎁터 객체
		 * @param context
		 * @param alayout
		 * @param aarSrc
		 */
		public MyListAdapter(Context context, int alayout,
				ArrayList<PointItemDTO> aarSrc) {
			maincon = context;
			Inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arSrc = aarSrc;
			layout = alayout;
		}

		/**
		 * 아이템의 갯수 가져옴
		 */
		public int getCount() {
			return arSrc.size();
		}

		/**
		 * 선택한 아이템객체를 리턴
		 */
		public PointItemDTO getItem(int position) {
			return arSrc.get(position);
		}

		/**
		 * 선택한 아이템의 위치 리턴
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 각 아이템의 세부정보 뷰에 설정
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = Inflater.inflate(layout, parent, false);
			}

			TextView itemtxt = (TextView) convertView
					.findViewById(R.id.point_item_name);
			TextView pricetxt = (TextView) convertView
					.findViewById(R.id.point_item_price);
			ImageView checkimg = (ImageView) convertView
					.findViewById(R.id.point_item_image);

			itemtxt.setText(arSrc.get(position).getTitle());
			pricetxt.setText(arSrc.get(position).getprice());
			checkimg.setImageDrawable(arSrc.get(position).getDrawable());

			convertView.setTag(position);
			convertView.setOnClickListener(this);

			return convertView;
		}

		/**
		 * 각 아이템에 대한 리스트뷰 클릭이벤트
		 */
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();

		}
	}

	/**
	 * 포인트샵 엑티비티가 다시 활성화 될때 아이템이 갱신된 내용을 다시 출력
	 */
	protected void onResume() {
		super.onResume();

		MyAdapter = new MyListAdapter(this, R.layout.pointshopactivity_list,
				ItemList);

		ItemListView.setAdapter(MyAdapter);
		ItemListView.setItemsCanFocus(false);
		ItemListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	/**
	 * pointshopitem.xml파일에 있는 아이템의 정보를 PointItemDTO에 넣는다.
	 * "pointshopitem"문자열을 이용하여 파싱
	 * @return list
	 */
	protected ArrayList<PointItemDTO> loadItem() {
		ArrayList<PointItemDTO> list = new ArrayList<PointItemDTO>();
		XmlPullParser parser = PointShopActivity.this.getResources().getXml(
				R.xml.pointshopitem);
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				if ("pointshopitem".equals(parser.getName())) {
					list.add(add(parser));
					parser.next();
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * xml에서 가져온 정보를 파싱하여 객체에 정보 넣음
	 * @param parser
	 * @return
	 */
	private PointItemDTO add(XmlPullParser parser) {

		PointItemDTO item = new PointItemDTO();
		item.setId(Integer.parseInt(parser.getAttributeValue(0)));
		item.setTitle(parser.getAttributeValue(1));
		item.setprice(parser.getAttributeValue(2));
		item.setDrawable(loadResource(parser.getAttributeValue(3)));

		return item;
	}

	/**
	 * 각 아이템의 그림 가져옴
	 * @param path
	 * @return
	 */
	public Drawable loadResource(String path) {
		Drawable drawable = null;

		try {
			AssetManager assetManager = PointShopActivity.this.getAssets();
			InputStream is = assetManager.open(path);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return ImageUtils.bitmapToDrawable(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drawable;
	}
}
