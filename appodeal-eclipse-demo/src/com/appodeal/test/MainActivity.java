package com.appodeal.test;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdBox;
import com.appodeal.ads.UserSettings;
import com.appodeal.ads.utils.Log;
import com.appodeal.test.layout.AdTypeViewPager;
import com.appodeal.test.layout.HorizontalNumberPicker;
import com.appodeal.test.layout.SlidingTabLayout;

import java.util.List;


public class MainActivity extends FragmentActivity {
    private static final String APP_KEY = "fee50c333ff3825fd6ad6d38cff78154de3025546d47a84f";
    private String[] interstitial_networks, video_networks, mrec_networks, native_networks, banner_networks;
    boolean[] interstitialNetworks;
    boolean[] bannerNetworks;
    boolean[] mrecNetworks;
    boolean[] nonRewardedNetworks;
    boolean[] rewardedNetworks;
    boolean[] nativeNetworks;
    boolean[] checkedValues;


    public enum AdType {
        Interstitial(Appodeal.INTERSTITIAL), Video(Appodeal.SKIPPABLE_VIDEO), RVideo(Appodeal.REWARDED_VIDEO), Banner(Appodeal.BANNER), Mrec(Appodeal.MREC), Native(Appodeal.NATIVE);
        private final int mValue;

        AdType(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        public static AdType fromInteger(Integer x) {
            if (x == null) {
                return null;
            }
            switch(x) {
                case Appodeal.INTERSTITIAL: return Interstitial;
                case Appodeal.SKIPPABLE_VIDEO: return Video;
                case Appodeal.REWARDED_VIDEO: return RVideo;
                case Appodeal.BANNER: return Banner;
                case Appodeal.MREC: return Mrec;
                case Appodeal.NATIVE: return Native;
            }
            return null;
        }
    }

    public enum BannerPosition {
        BANNER(Appodeal.BANNER), BOTTOM(Appodeal.BANNER_BOTTOM), TOP(Appodeal.BANNER_TOP), VIEW(Appodeal.BANNER_VIEW);
        private final int mValue;

        BannerPosition(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public enum AdTypePages {
        Interstitial(R.layout.interstitial, R.id.interstitialLayout), Video(R.layout.video, R.id.videoLayout),
        RVideo(R.layout.rewarded_video, R.id.rewardedVideoLayout), Banner(R.layout.banner, R.id.bannerLayout),
        MREC(R.layout.mrec, R.id.MrecLayout), Native(R.layout.native_ad, R.id.nativeLayout);

        private final int mLayout;
        private final int mId;

        AdTypePages(int layout, int id) {
            mLayout = layout;
            mId = id;
        }

        public int getLayout() {
            return mLayout;
        }

        public int getId() {
            return mId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interstitial_networks = getResources().getStringArray(R.array.interstitial_networks);
        interstitialNetworks = new boolean[interstitial_networks.length];
        for (int i = 0; i < interstitial_networks.length; i++) {
            interstitialNetworks[i] = true;
        }
        mrec_networks = getResources().getStringArray(R.array.mrec_networks);
        mrecNetworks = new boolean[mrec_networks.length];
        for (int i = 0; i < mrec_networks.length; i++) {
            mrecNetworks[i] = true;
        }
        banner_networks = getResources().getStringArray(R.array.banner_networks);
        bannerNetworks = new boolean[banner_networks.length];
        for (int i = 0; i < banner_networks.length; i++) {
            bannerNetworks[i] = true;
        }
        video_networks = getResources().getStringArray(R.array.video_networks);
        nonRewardedNetworks = new boolean[video_networks.length];
        rewardedNetworks = new boolean[video_networks.length];
        for (int i = 0; i < video_networks.length; i++) {
            nonRewardedNetworks[i] = true;
            rewardedNetworks[i] = true;
        }
        native_networks = getResources().getStringArray(R.array.native_networks);
        nativeNetworks = new boolean[native_networks.length];
        for (int i = 0; i < native_networks.length; i++) {
            nativeNetworks[i] = true;
        }

        if (Build.VERSION.SDK_INT >= 23 && (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
        		ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Appodeal.requestAndroidMPermissions(this, new AppodealPermissionCallbacks(this));
        }

        TextView sdkTextView = (TextView) findViewById(R.id.sdkTextView);
        sdkTextView.setText(getString(R.string.sdkTextView, Appodeal.getVersion()));

        CompoundButton testModeSwitch = (CompoundButton) findViewById(R.id.testModeSwitch);
        testModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appodeal.setTesting(isChecked);
            }
        });

        CompoundButton loggingSwitch = (CompoundButton) findViewById(R.id.loggingSwitch);
        loggingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if(isChecked)
            		Appodeal.setLogLevel(Log.LogLevel.verbose);
            	else
            		Appodeal.setLogLevel(Log.LogLevel.none);
            }
        });

        ViewPager pager = (AdTypeViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(AdTypePages.values().length);
        pager.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child.findViewById(AdTypePages.Interstitial.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheInterstitialSwitch = (CompoundButton) findViewById(R.id.autoCacheInterstitialSwitch);
                    autoCacheInterstitialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.setAutoCache(Appodeal.INTERSTITIAL, isChecked);
                            Button interstitialCacheButton = (Button) findViewById(R.id.interstitialCacheButton);
                            if (isChecked) {
                                interstitialCacheButton.setVisibility(View.GONE);
                            } else {
                                interstitialCacheButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    final CompoundButton onLoadedSwitch = (CompoundButton) findViewById(R.id.onLoadedInterstitialSwitch);
                    onLoadedSwitch.setText(getString(R.string.onLoadedInterstitialSwitch, "expensive"));
                    onLoadedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                onLoadedSwitch.setText(getString(R.string.onLoadedInterstitialSwitch, "both"));
                            } else {
                                onLoadedSwitch.setText(getString(R.string.onLoadedInterstitialSwitch, "expensive"));
                            }
                            Appodeal.setOnLoadedTriggerBoth(Appodeal.INTERSTITIAL, isChecked);
                        }
                    });
                }

                if (child.findViewById(AdTypePages.Video.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheVideoSwitch = (CompoundButton) findViewById(R.id.autoCacheVideoSwitch);
                    autoCacheVideoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.setAutoCache(Appodeal.SKIPPABLE_VIDEO, isChecked);
                            Button videoCacheButton = (Button) findViewById(R.id.videoCacheButton);
                            if (isChecked) {
                                videoCacheButton.setVisibility(View.GONE);
                            } else {
                                videoCacheButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

                if (child.findViewById(AdTypePages.RVideo.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheRewardedVideoSwitch = (CompoundButton) findViewById(R.id.autoCacheRewardedVideoSwitch);
                    autoCacheRewardedVideoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.setAutoCache(Appodeal.REWARDED_VIDEO, isChecked);
                            Button rewardedVideoCacheButton = (Button) findViewById(R.id.rewardedVideoCacheButton);
                            if (isChecked) {
                                rewardedVideoCacheButton.setVisibility(View.GONE);
                            } else {
                                rewardedVideoCacheButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

                if (child.findViewById(AdTypePages.Native.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheNativeSwitch = (CompoundButton) findViewById(R.id.autoCacheNativeSwitch);
                    autoCacheNativeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.setAutoCache(Appodeal.NATIVE, isChecked);
                        }
                    });

                    Spinner nativeTemplateSpinner = (Spinner) findViewById(R.id.native_template_list);
                    ArrayAdapter<String> nativeTemplateAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.nativeTemplates));
                    nativeTemplateSpinner.setAdapter(nativeTemplateAdapter);
                    nativeTemplateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            updateNativeList(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    Spinner nativeTypeSpinner = (Spinner) findViewById(R.id.native_type_list);
                    ArrayAdapter<String> nativeTypeAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.nativeTypes));
                    nativeTypeSpinner.setAdapter(nativeTypeAdapter);
                    nativeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    Appodeal.setNativeAdType(Native.NativeAdType.Auto);
                                    break;
                                case 1:
                                    Appodeal.setNativeAdType(Native.NativeAdType.NoVideo);
                                    break;
                                case 2:
                                    Appodeal.setNativeAdType(Native.NativeAdType.Video);
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (child.findViewById(AdTypePages.Banner.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheBannerSwitch = (CompoundButton) findViewById(R.id.autoCacheBannerSwitch);
                    autoCacheBannerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.setAutoCache(Appodeal.BANNER, isChecked);
                            Button bannerCacheButton = (Button) findViewById(R.id.bannerCacheButton);
                            if (isChecked) {
                                bannerCacheButton.setVisibility(View.GONE);
                            } else {
                                bannerCacheButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    CompoundButton smartBannersSwitch = (CompoundButton) findViewById(R.id.smartBannersSwitch);
                    smartBannersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.setSmartBanners(isChecked);
                        }
                    });

                    CompoundButton bigBannersSwitch = (CompoundButton) findViewById(R.id.bigBannersSwitch);
                    bigBannersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.set728x90Banners(isChecked);
                        }
                    });

                    CompoundButton bannersAnimateSwitch = (CompoundButton) findViewById(R.id.bannersAnimateBannersSwitch);
                    bannersAnimateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.setBannerAnimation(isChecked);
                        }
                    });

                    Spinner bannerPositionSpinner = (Spinner) findViewById(R.id.bannerPositionList);
                    ArrayAdapter<BannerPosition> bannerPositionsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, BannerPosition.values());
                    bannerPositionSpinner.setAdapter(bannerPositionsAdapter);
                }


                if (child.findViewById(AdTypePages.MREC.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheMrecSwitch = (CompoundButton) findViewById(R.id.autoCacheMrecSwitch);
                    autoCacheMrecSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Appodeal.setAutoCache(Appodeal.MREC, isChecked);
                            Button MrecCacheButton = (Button) findViewById(R.id.mrecCacheButton);
                            if (isChecked) {
                                MrecCacheButton.setVisibility(View.GONE);
                            } else {
                                MrecCacheButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });
        AdTypeAdapter adTypeAdapter = new AdTypeAdapter(getSupportFragmentManager());
        pager.setAdapter(adTypeAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(pager);
    }


    @Override
    public void onResume() {
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER);
        Appodeal.onResume(this, Appodeal.MREC);
    }

    @Override
    public void onBackPressed() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            Object tag = child.getTag();
            if (tag != null && tag.equals("appodeal")) {
                root.removeView(child);
                return;
            }
        }
        super.onBackPressed();
    }

    public void initSdkButton(View v) {

        //Add user settings
        Appodeal.getUserSettings(this)
                .setAge(25)
                .setAlcohol(UserSettings.Alcohol.NEGATIVE)
                .setSmoking(UserSettings.Smoking.NEUTRAL)
                .setBirthday("17/06/1990") .setEmail("ru@appodeal.com")
                .setGender(UserSettings.Gender.MALE)
                .setRelation(UserSettings.Relation.DATING)
                .setInterests("reading, games, movies, snowboarding")
                .setOccupation(UserSettings.Occupation.WORK);
        Appodeal.trackInAppPurchase(this, 10.0, "UAH");
        Appodeal.initialize(this, APP_KEY, Appodeal.NONE);
    }

    public void disableNetworks(boolean[] adNetworks, String[] networksList, AdType adType) {
        for (int i = 0; i < adNetworks.length; i++) {
            if (!adNetworks[i]) {
                Appodeal.disableNetwork(this, networksList[i], adType.getValue());
            }
        }
    }


    public void interstitialChooseNetworks(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        checkedValues = interstitialNetworks.clone();
        builder.setTitle(getString(R.string.selectNetworks)).setMultiChoiceItems(interstitial_networks, checkedValues,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                interstitialNetworks = checkedValues;
                disableNetworks(interstitialNetworks, interstitial_networks, AdType.Interstitial);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void initInterstitialSdkButton(View v) {
        Appodeal.initialize(this, APP_KEY, Appodeal.INTERSTITIAL);
        Appodeal.setInterstitialCallbacks(new AppodealInterstitialCallbacks(this));
    }

    public void isInterstitialLoadedButton(View v) {
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
    }

    public void isInterstitialLoadedPrecacheButton(View v) {
        if (Appodeal.isPrecache(Appodeal.INTERSTITIAL)) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
    }

    public void interstitialCacheButton(View v) {
        Appodeal.cache(this, Appodeal.INTERSTITIAL);
    }

    public void interstitialShowButton(View v) {
        boolean isShown = Appodeal.show(this, Appodeal.INTERSTITIAL);
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    public void videoChooseNetworks(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        checkedValues = nonRewardedNetworks.clone();
        builder.setTitle(getString(R.string.selectNetworks)).setMultiChoiceItems(video_networks, checkedValues,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nonRewardedNetworks = checkedValues;
                disableNetworks(nonRewardedNetworks, video_networks, AdType.Video);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void initVideoSdkButton(View v) {
        Appodeal.confirm(Appodeal.SKIPPABLE_VIDEO);
        Appodeal.initialize(this, APP_KEY, Appodeal.SKIPPABLE_VIDEO);
        Appodeal.setSkippableVideoCallbacks(new AppodealSkippableVideoCallbacks(this));
    }

    public void isVideoLoadedButton(View v) {
        if (Appodeal.isLoaded(Appodeal.SKIPPABLE_VIDEO)) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
    }

    public void videoCacheButton(View v) {
        Appodeal.cache(this, Appodeal.SKIPPABLE_VIDEO);
    }

    public void videoShowButton(View v) {
        boolean isShown = Appodeal.show(this, Appodeal.SKIPPABLE_VIDEO);
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }


    public void rewardedVideoChooseNetworks(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        checkedValues = rewardedNetworks.clone();
        builder.setTitle(getString(R.string.selectNetworks)).setMultiChoiceItems(video_networks, checkedValues,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rewardedNetworks = checkedValues;
                disableNetworks(rewardedNetworks, video_networks, AdType.RVideo);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void initRewardedVideoSdkButton(View v) {
        Appodeal.initialize(this, APP_KEY, Appodeal.REWARDED_VIDEO);
        Appodeal.setRewardedVideoCallbacks(new AppodealRewardedVideoCallbacks(this));
    }

    public void isRewardedVideoLoadedButton(View v) {
        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
    }

    public void rewardedVideoCacheButton(View v) {
        Appodeal.cache(this, Appodeal.REWARDED_VIDEO);
    }

    public void rewardedVideoShowButton(View v) {
        boolean isShown = Appodeal.show(this, Appodeal.REWARDED_VIDEO);
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    public void mrecChooseNetworks(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        checkedValues = mrecNetworks.clone();
        builder.setTitle(getString(R.string.selectNetworks)).setMultiChoiceItems(mrec_networks, checkedValues,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mrecNetworks = checkedValues;
                disableNetworks(mrecNetworks, mrec_networks, AdType.Mrec);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void initMrecSdkButton(View v) {
        Appodeal.setMrecViewId(R.id.appodealMrecView);
        Appodeal.initialize(this, APP_KEY, Appodeal.MREC);
        Appodeal.setMrecCallbacks(new AppodealMrecCallbacks(this));
    }

    public void isMrecLoadedButton(View v) {
        if (Appodeal.isLoaded(Appodeal.MREC)) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
    }

    public void mrecCacheButton(View v) {
        Appodeal.cache(this, Appodeal.MREC);
    }

    public void mrecShowButton(View v) {
        Appodeal.setMrecViewId(R.id.appodealMrecView);
        boolean isShown = Appodeal.show(this, Appodeal.MREC);
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    public void mrecHideButton(View v) {
        Appodeal.hide(this, Appodeal.MREC);
    }


    public void bannerChooseNetworks(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        checkedValues = bannerNetworks.clone();
        builder.setTitle(getString(R.string.selectNetworks)).setMultiChoiceItems(banner_networks, checkedValues,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bannerNetworks = checkedValues;
                disableNetworks(bannerNetworks, banner_networks, AdType.Banner);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void initBannerSdkButton(View v) {
        Appodeal.setBannerViewId(R.id.appodealBannerView);
        Appodeal.initialize(this, APP_KEY, Appodeal.BANNER);
        Appodeal.setBannerCallbacks(new AppodealBannerCallbacks(this));
    }

    public void isBannerLoadedButton(View v) {
        if (Appodeal.isLoaded(Appodeal.BANNER)) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
    }

    public void bannerCacheButton(View v) {
        Appodeal.cache(this, Appodeal.BANNER);
    }

    public void bannerShowButton(View v) {
        Spinner bannerPositionSpinner = (Spinner) findViewById(R.id.bannerPositionList);
        BannerPosition bannerPosition = (BannerPosition) bannerPositionSpinner.getSelectedItem();
        boolean isShown = Appodeal.show(this, bannerPosition.getValue());
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    public void bannerHideButton(View v) {
        Appodeal.hide(this, Appodeal.BANNER);
    }


    public void initNativeSdkButton(View v) {
        Appodeal.setNativeCallbacks(new AppodealNativeCallbacks(this));
        Appodeal.initialize(this, APP_KEY, Appodeal.NATIVE);
        Appodeal.setAutoCacheNativeIcons(true);
        Appodeal.setAutoCacheNativeMedia(true);
    }

    public void nativeChooseNetworks(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        checkedValues = nativeNetworks.clone();
        builder.setTitle(getString(R.string.selectNetworks)).setMultiChoiceItems(native_networks, checkedValues,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nativeNetworks = checkedValues;
                disableNetworks(nativeNetworks, native_networks, AdType.Native);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }


    public void nativeCacheButton(View view) {
        hideNativeAds();

        HorizontalNumberPicker numberPicker = (HorizontalNumberPicker) findViewById(R.id.nativeAdsCountPicker);
        Appodeal.setNativeCallbacks(new AppodealNativeCallbacks(this));
        if (numberPicker.getNumber() == 1) {
            Appodeal.cache(this, Appodeal.NATIVE);
        } else {
            Appodeal.cache(this, Appodeal.NATIVE, numberPicker.getNumber());
        }
    }

    public void nativeHideButton(View v) {
        hideNativeAds();
    }

    public void hideNativeAds() {
        LinearLayout nativeListView = (LinearLayout) findViewById(R.id.nativeAdsListView);
        nativeListView.removeAllViews();
        NativeListAdapter nativeListViewAdapter = (NativeListAdapter) nativeListView.getTag();
        if (nativeListViewAdapter != null) {
            for (int i = 0; i < nativeListViewAdapter.getCount(); i++) {
                NativeAd nativeAd = (NativeAd) nativeListViewAdapter.getItem(i);
                nativeAd.unregisterViewForInteraction();
            }
            nativeListViewAdapter.clear();
        }
    }

    public void isNativeLoadedButton(View v) {
        if (Appodeal.isLoaded(Appodeal.NATIVE)) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateNativeList(int position) {
        LinearLayout nativeListView = (LinearLayout) findViewById(R.id.nativeAdsListView);
        NativeListAdapter nativeListViewAdapter = (NativeListAdapter) nativeListView.getTag();
        if (nativeListViewAdapter != null) {
            nativeListViewAdapter.setTemplate(position);
            nativeListViewAdapter.rebuild();
        }
    }

    public void nativeLoadContainerButton(View v) {
        NativeAdBox nativeAdBox = Appodeal.getNativeAdBox();
        nativeAdBox.setSize(5);
        nativeAdBox.setListener(new AppodealNativeBoxCallbacks(this));
        nativeAdBox.load();
    }

    public void nativeShowContainerButton(View v) {
        hideNativeAds();
        HorizontalNumberPicker numberPicker = (HorizontalNumberPicker) findViewById(R.id.nativeAdsContainerShowPicker);
        List<NativeAd> nativeAds = Appodeal.getNativeAdBox().getNativeAds(numberPicker.getNumber());

        LinearLayout nativeAdsListView = (LinearLayout) findViewById(R.id.nativeAdsListView);
        Spinner nativeTemplateSpinner = (Spinner) findViewById(R.id.native_template_list);
        NativeListAdapter nativeListViewAdapter = new NativeListAdapter(nativeAdsListView, nativeTemplateSpinner.getSelectedItemPosition());
        for (NativeAd nativeAd : nativeAds) {
            nativeListViewAdapter.addNativeAd(nativeAd);
        }
        nativeAdsListView.setTag(nativeListViewAdapter);
        nativeListViewAdapter.rebuild();
    }

    public static class AdTypeAdapter extends FragmentPagerAdapter {

        public AdTypeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return AdTypePages.values().length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new AdTypeFragment();
            Bundle args = new Bundle();
            args.putInt("layout", AdTypePages.values()[position].getLayout());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return AdTypePages.values()[position].name();
        }
    }

    public static class AdTypeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int layoutId = args.getInt("layout");
            return inflater.inflate(layoutId, container, false);
        }
    }
}
