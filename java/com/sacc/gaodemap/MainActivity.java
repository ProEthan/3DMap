package com.sacc.gaodemap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.sacc.gaodemap.opengl.cube.MapRender;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity implements View.OnClickListener{
    private MapView mapView;
    private AMap aMap;
    private Button basicmap;
    private Button rsmap;
    private Button nightmap;
    private Button navimap;
    private Button downloadmap;
    private Button autoDirection;

    private int autoDircNum=0; // 0代表当前为不锁定方向，1代表锁定方向为设备方向

    private UiSettings mUiSettings;//定义一个UiSettings对象

    MyLocationStyle myLocationStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basicmap_activity);

        mapView=findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        if(aMap==null){
            aMap = mapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setCompassEnabled(true); // 指南针
        mUiSettings.setScaleControlsEnabled(true); // 比例尺



        basicmap = (Button)findViewById(R.id.basicmap);
        basicmap.setOnClickListener(this);
        rsmap = (Button)findViewById(R.id.rsmap);
        rsmap.setOnClickListener(this);
        nightmap = (Button)findViewById(R.id.nightmap);
        nightmap.setOnClickListener(this);
        navimap = (Button)findViewById(R.id.navimap);
        navimap.setOnClickListener(this);
        downloadmap = (Button)findViewById(R.id.downloadmap);
        downloadmap.setOnClickListener(this);
        autoDirection = (Button)findViewById(R.id.autoDirection);
        autoDirection.setOnClickListener(this);

        Bitmap bitmap_cube = BitmapFactory.decodeResource(getResources(), R.drawable.guangao);
        Bitmap bitmap_arrow1 = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        Bitmap bitmap_arrow2 = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
        Bitmap bitmap_man = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        String arrow_1 = "arrow_1.obj";
        String arrow_2 = "arrow_2.obj";
        String man = "nanosuit.obj";
        InputStream inputStream_arrow_1 = null;
        InputStream inputStream_arrow_2 = null;
        InputStream inputStream_man = null;
        try {
            inputStream_arrow_1 = getResources().getAssets().open(arrow_1);
            inputStream_arrow_2 = getResources().getAssets().open(arrow_2);
            inputStream_man = getResources().getAssets().open(man);
        } catch (IOException e) {
            System.out.println("---------------------------------------");
            e.printStackTrace();
        }

        aMap.setCustomRenderer(new MapRender(aMap, bitmap_man, bitmap_cube, bitmap_arrow1,bitmap_arrow2, inputStream_arrow_1, inputStream_arrow_2, inputStream_man));
        aMap.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        return true;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    public void onClickBt(View view) {
        if(aMap != null) {
            aMap.runOnDrawFrame();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basicmap:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                break;
            case R.id.rsmap:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                break;
            case R.id.nightmap:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图模式
                break;
            case R.id.navimap:
                aMap.setMapType(AMap.MAP_TYPE_NAVI);//导航地图模式
                break;
            case R.id.downloadmap:
                //在Activity页面调用startActvity启动离线地图组件
                startActivity(new Intent(this.getApplicationContext(),
                        com.amap.api.maps.offlinemap.OfflineMapActivity.class));
                break;
            case R.id.autoDirection:
                if(autoDircNum==0){
                    System.out.println("0000");

                    if(aMap==null){
                        aMap = mapView.getMap();
                    }
                    myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
                    myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

                    aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                    aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
                    aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

                    mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
                    mUiSettings.setCompassEnabled(true); // 指南针
                    mUiSettings.setScaleControlsEnabled(true); // 比例尺



                    basicmap = (Button)findViewById(R.id.basicmap);
                    basicmap.setOnClickListener(this);
                    rsmap = (Button)findViewById(R.id.rsmap);
                    rsmap.setOnClickListener(this);
                    nightmap = (Button)findViewById(R.id.nightmap);
                    nightmap.setOnClickListener(this);
                    navimap = (Button)findViewById(R.id.navimap);
                    navimap.setOnClickListener(this);
                    downloadmap = (Button)findViewById(R.id.downloadmap);
                    downloadmap.setOnClickListener(this);
                    autoDirection = (Button)findViewById(R.id.autoDirection);
                    autoDirection.setOnClickListener(this);

                    autoDircNum=1;
                }else{
                    System.out.println("1111");

                    if(aMap==null){
                        aMap = mapView.getMap();
                    }
                    myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
                    myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

                    aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                    aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
                    aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

                    mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
                    mUiSettings.setCompassEnabled(true); // 指南针
                    mUiSettings.setScaleControlsEnabled(true); // 比例尺



                    basicmap = (Button)findViewById(R.id.basicmap);
                    basicmap.setOnClickListener(this);
                    rsmap = (Button)findViewById(R.id.rsmap);
                    rsmap.setOnClickListener(this);
                    nightmap = (Button)findViewById(R.id.nightmap);
                    nightmap.setOnClickListener(this);
                    navimap = (Button)findViewById(R.id.navimap);
                    navimap.setOnClickListener(this);
                    downloadmap = (Button)findViewById(R.id.downloadmap);
                    downloadmap.setOnClickListener(this);
                    autoDirection = (Button)findViewById(R.id.autoDirection);
                    autoDirection.setOnClickListener(this);

                    autoDircNum=0;
                }
                break;
        }

    }
}
