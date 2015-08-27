package com.appodeal.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.VideoCallbacks;

public class MainActivity extends Activity {

	String appKey = "fee50c333ff3825fd6ad6d38cff78154de3025546d47a84f"; //replace appkey with your's from Appodeal dashboard.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Appodeal.disableNetwork(this, "admob"); use this if you don't need some networks.
		Appodeal.initialize(this, appKey);
		//Appodeal.setTesting(true); //use this to test your integration.
		Appodeal.setBannerCallbacks(bannerListener);
		Appodeal.setInterstitialCallbacks(interstitialListener);
		Appodeal.setVideoCallbacks(videoListener);
		
	}
	
	public void interstitial(View v){
		Appodeal.show(this, Appodeal.INTERSTITIAL);
	}
	
	public void video(View v){
		Appodeal.show(this, Appodeal.VIDEO);
	}
	
	public void banner(View v){
		Appodeal.show(this, Appodeal.BANNER_BOTTOM);
	}
	
	public void hidebanner(View v){
		Appodeal.hide(this, Appodeal.BANNER);
	}
	
	public InterstitialCallbacks interstitialListener = new InterstitialCallbacks() {
		
		@Override
		public void onInterstitialShown() {
			Toast.makeText(MainActivity.this, "onInterstitialShown", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onInterstitialLoaded(boolean arg0) {
			Toast.makeText(MainActivity.this, "onInterstitialLoaded", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onInterstitialFailedToLoad() {
			Toast.makeText(MainActivity.this, "onInterstitialFailedToLoad", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onInterstitialClosed() {
			Toast.makeText(MainActivity.this, "onInterstitialClosed", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onInterstitialClicked() {
			Toast.makeText(MainActivity.this, "onInterstitialClicked", Toast.LENGTH_SHORT).show();
		}
	};
	
	public BannerCallbacks bannerListener = new BannerCallbacks() {
		
		@Override
		public void onBannerShown() {
			Toast.makeText(MainActivity.this, "onBannerShown", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onBannerLoaded() {
			Toast.makeText(MainActivity.this, "onBannerLoaded", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onBannerFailedToLoad() {
			Toast.makeText(MainActivity.this, "onBannerFailedToLoad", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onBannerClicked() {
			Toast.makeText(MainActivity.this, "onBannerClicked", Toast.LENGTH_SHORT).show();
		}
	};
	
	public VideoCallbacks videoListener = new VideoCallbacks() {
		
		@Override
		public void onVideoShown() {
			Toast.makeText(MainActivity.this, "onVideoShown", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onVideoLoaded() {
			Toast.makeText(MainActivity.this, "onVideoLoaded", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onVideoFinished() {
			Toast.makeText(MainActivity.this, "onVideoFinished", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onVideoFailedToLoad() {
			Toast.makeText(MainActivity.this, "onVideoFailedToLoad", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onVideoClosed() {
			Toast.makeText(MainActivity.this, "onVideoClosed", Toast.LENGTH_SHORT).show();
		}
	};
		
}
