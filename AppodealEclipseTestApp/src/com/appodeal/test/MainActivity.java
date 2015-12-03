package com.appodeal.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.VideoCallbacks;

public class MainActivity extends Activity {

	String appKey = "fee50c333ff3825fd6ad6d38cff78154de3025546d47a84f"; //replace appkey with your's from Appodeal dashboard.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Appodeal.disableNetwork(this, "admob"); use this if you don't need some networks.
		Appodeal.initialize(this, appKey, Appodeal.ANY);
		//Appodeal.setTesting(true); //use this to test your integration.
		Appodeal.setBannerCallbacks(bannerListener);
		Appodeal.setInterstitialCallbacks(interstitialListener);
		Appodeal.setVideoCallbacks(videoListener);
		Appodeal.setRewardedVideoCallbacks(rewardedVideoLister);
		
	}
	
	public void interstitial(View v){
		Appodeal.show(this, Appodeal.INTERSTITIAL);
	}
	
	public void videoorinterstitial(View v){
		Appodeal.show(this, Appodeal.VIDEO | Appodeal.INTERSTITIAL);
	}
	
	public void rewardedvideo(View v){
		Appodeal.show(this, Appodeal.REWARDED_VIDEO);
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
			//showToast(MainActivity.this, "onInterstitialShown");
		}
		
		@Override
		public void onInterstitialLoaded(boolean arg0) {
			//showToast(MainActivity.this, "onInterstitialLoaded");
		}
		
		@Override
		public void onInterstitialFailedToLoad() {
			//showToast(MainActivity.this, "onInterstitialFailedToLoad");
		}
		
		@Override
		public void onInterstitialClosed() {
			//showToast(MainActivity.this, "onInterstitialClosed");
		}
		
		@Override
		public void onInterstitialClicked() {
			//showToast(MainActivity.this, "onInterstitialClicked");
		}
	};
	
	public BannerCallbacks bannerListener = new BannerCallbacks() {
		
		@Override
		public void onBannerShown() {
			//showToast(MainActivity.this, "onBannerShown");
		}
		
		@Override
		public void onBannerLoaded() {
			//showToast(MainActivity.this, "onBannerLoaded");
		}
		
		@Override
		public void onBannerFailedToLoad() {
			//showToast(MainActivity.this, "onBannerFailedToLoad");
		}
		
		@Override
		public void onBannerClicked() {
			//showToast(MainActivity.this, "onBannerClicked");
		}
	};
	
	public VideoCallbacks videoListener = new VideoCallbacks() {
		
		@Override
		public void onVideoShown() {
			//showToast(MainActivity.this, "onVideoShown");
		}
		
		@Override
		public void onVideoLoaded() {
			//showToast(MainActivity.this, "onVideoLoaded");
		}
		
		@Override
		public void onVideoFinished() {
			//showToast(MainActivity.this, "onVideoFinished");
		}
		
		@Override
		public void onVideoFailedToLoad() {
			//showToast(MainActivity.this, "onVideoFailedToLoad");
		}
		
		@Override
		public void onVideoClosed() {
			
		}
	};
	
	public RewardedVideoCallbacks rewardedVideoLister = new RewardedVideoCallbacks() {
		
		@Override
		public void onRewardedVideoShown() {
			//showToast(MainActivity.this, "onRewardedVideoShown");
		}
		
		@Override
		public void onRewardedVideoLoaded() {
			//showToast(MainActivity.this, "onRewardedVideoLoaded");
		}
		
		@Override
		public void onRewardedVideoFinished(int amount, String name) {
			showToast(MainActivity.this, "reward: " + amount + " " + name);
		}
		
		@Override
		public void onRewardedVideoFailedToLoad() {
			//showToast(MainActivity.this, "onRewardedVideoFailedToLoad");
		}
		
		@Override
		public void onRewardedVideoClosed() {
			//showToast(MainActivity.this, "onRewardedVideoClosed");
		}
	};
	
	public void showToast(Activity activity, String message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}
		
}
