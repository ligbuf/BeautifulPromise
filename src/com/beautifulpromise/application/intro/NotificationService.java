package com.beautifulpromise.application.intro;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.beatutifulpromise.common.log.Microlog4Android;

public class NotificationService extends Service implements Runnable{
	
	final RemoteCallbackList<IRemoteServiceCallback> callbackList = new RemoteCallbackList<IRemoteServiceCallback>();
    private Handler mHandler;
    private static final int TIMER_PERIOD = 10 * 1000; 
    
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//Log.i("immk", "Service Start!");
		Microlog4Android.logger.info("immk"+" - "+ "Service Start!");

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	//	Log.i("immk", "Service Stop!");
		Microlog4Android.logger.info("immk"+" - "+ "Service Stop!");
		//TODO 서비스 중단
	}

	private final IRemoteService.Stub binder = new IRemoteService.Stub() {
		
		@Override
		public void unregisterCallback(IRemoteServiceCallback callback) throws RemoteException {
			// TODO Auto-generated method stub
			if(callback != null){
				//Log.i("immk", "1");
				Microlog4Android.logger.info("immk"+" - "+ "1");
			}
			
		}
		
		@Override
		public void registerCallback(IRemoteServiceCallback callback) throws RemoteException {
			if(callback != null){
				callbackList.register(callback);
				mHandler = new Handler();
				Thread thread = new Thread();
				//Log.i("immk", "2");
				Microlog4Android.logger.info("immk"+" - "+ "2");
				
			}
		}
		
		@Override
		public String onService(int message) throws RemoteException {
			
			switch (message) {
			case 0:
				//Log.i("immk", "message : " +message);
				Microlog4Android.logger.info("immk"+" - "+"message : " +message);
//				callbackList.getBroadcastItem(0);
//				int i = 0 ;
//				User user = Repository.getInstance().getUser();
//				Connection<Friends> friends = user.friends();
//				callbackList.getBroadcastItem(0).MessageCallback(message);
				break;

			default:
				break;
			}
			//Log.i("immk", "3");
			Microlog4Android.logger.info("immk"+" - "+"3");
			return null;
		}
	};
	
	
	@Override
	public void run() {
//		if (mHandler) {
//            return;
//        } else {
//            mHandler.postDelayed(this, TIMER_PERIOD);
//        }
	}
}
