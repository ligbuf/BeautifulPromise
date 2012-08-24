package com.beautifulpromise.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.beatutifulpromise.common.log.Microlog4Android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

/**
 * @description 이미지 정보에 관련된 클래스(url을 통해 Bitmap을 받아오거나 Bitmap의 형식 변환, Drawable의 형식 변환)
 * @author immk
 *
 */
public class ImageUtils {

	/**
	 * url을 통해 Bitmap 이미지 다운로드
	 * @param url
	 * @return Bitmap
	 */
	public static Bitmap downloadBitmap (String url) {

		try {
			URL u = new URL(url);
			URLConnection con = u.openConnection();
			InputStream is = con.getInputStream();
			Bitmap bmp = BitmapFactory.decodeStream(is);
			is.close();
			return bmp;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * url을 통해 Drawable 이미지 다운로드
	 * @param url
	 * @return Drawable
	 */
	public static Drawable downloadDrawable (String url) {

		try {
			URL u = new URL(url);
			URLConnection con = u.openConnection();
			InputStream is = con.getInputStream();
			Bitmap bmp = BitmapFactory.decodeStream(is);
			Drawable drawable =  new BitmapDrawable(bmp);
			is.close();
			return drawable;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 웹뷰의 사이즈에 따라 이미지 사이즈 변경을 위한 html 코드
	 * @param url
	 * @return
	 */
	public static String webViewImageReSize(String url){
		
		StringBuffer sb = new StringBuffer("<HTML>");
		sb.append("<HEAD>");
		sb.append("<META http-equiv=Content-Type content=\"text/html; charset=utf-8\">");
//		sb.append("<META content=\"TAGFREE Active Designer v3.0\" name=GENERATOR>");
//		sb.append("<META name=\"viewport\" content=\"user-scalable=yes, initial-scale=0.1, maximum-scale=0.0, minimum-scale=0.0");
		sb.append("</HEAD>");
		sb.append("<BODY style=\"margin:0;padding:0\">");  //  style=\"FONT-SIZE: 9pt; FONT-FAMILY: gulim \">");
		//sb.append("<OBJECT width=100% data=\"http://211.189.20.139/images/noImage.png\">");		
		sb.append("<img width=100%;height:100% src=\""+url+"\"/>");
		//sb.append("</OBJECT>");
		sb.append("</BODY>");
		sb.append("</HTML>");
		String htmlBody = sb.toString();
		return htmlBody;
	}
	
	/**
	 * 이미지 경로를 통해 Bitmap 이미지 생성
	 * @param path 이미지 경로
	 * @param sampleSize 이미지 압축 사이즈
	 * @return Bitmap
	 */
	public static Bitmap getBitmap(String path, int sampleSize){
		
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inSampleSize = sampleSize;
		Bitmap bitmap  = BitmapFactory.decodeFile(path, bfo);
		
		return bitmap;
	}

	/**
	 * 이미지 경로를 통해 Bitamp 이미지 생성
	 * @param path 이미지 경로
	 * @return Bitamp
	 */
	public static Bitmap getResizedBitmap(String path){
		int sampleSize = 4;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inSampleSize = sampleSize;
		Bitmap bitmap  = BitmapFactory.decodeFile(path, bfo);
		int dstWidth = 330;
		int dstHeight = 140;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if(w > dstWidth) {
			dstHeight = h - Math.round(((float)(w - dstWidth)/w)*dstHeight);
			//Log.d("haha", "dstHeight:" + dstHeight + ", h:" + h);
			Microlog4Android.logger.debug("haha"+" - "+ "dstHeight:" + dstHeight + ", h:" + h);
			Bitmap resized = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
			return resized;
		}else if(h > dstHeight) {
			dstWidth = w - Math.round(((float)(h - dstHeight)/h)*dstWidth);
			//Log.d("haha", "dstWidth:" + dstWidth + ", w:" + w);
			Microlog4Android.logger.debug("haha"+" - "+ "dstWidth:" + dstWidth + ", w:" + w);
			Bitmap resized = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
			return resized;
		}
		
		return bitmap;
	}
	
	/**
	 * Drawable 이미지를 Bitmap 이미지로 변경
	 * @param d Drawable
	 * @return Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable d) {
		int width = d.getIntrinsicWidth();
		int height = d.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, width, height);
		d.draw(canvas);
		return bitmap;
	}
	
	/**
	 * Bitmap 이미지를 Drawable 이미지로 변경
	 * @param bitmap
	 * @return Drawable
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		
		return new BitmapDrawable(bitmap);
	}
	
	/**
	 * 화면 캡쳐 후 Bitmap으로 변환
	 * @param view View
	 * @return Bitmap
	 */
	public static Bitmap capture(View view) {
		view.setDrawingCacheEnabled(true);
		return view.getDrawingCache();

	}
	
	/**
	 * 이미지 저장 후 이미지 경로 리턴
	 * @param context Context
	 * @param bitmap bitmap
	 * @return 이미지 경로
	 */
	public static String saveBitmap(Context context, Bitmap bitmap){
		try{
//			String path = Environment.getExternalStorageDirectory().toString() + "/BookieTalkie";
//			String filePath = path + "/" + ApplicationRepository.getInstance().getUserId();
			String filePath = StorageUtils.getFilePath(context);
			FileOutputStream fos = new FileOutputStream(filePath);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return filePath;
		} catch (Exception e) {
			return null;
		}
	}
}
