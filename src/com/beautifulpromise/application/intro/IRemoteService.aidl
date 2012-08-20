package com.beautifulpromise.application.intro;
import com.beautifulpromise.application.intro.IRemoteServiceCallback;

interface IRemoteService {

	void registerCallback(IRemoteServiceCallback callback);
	void unregisterCallback(IRemoteServiceCallback callback);
	String onService(int message);
}
