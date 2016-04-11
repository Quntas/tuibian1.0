package com.tuibian.map;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;


import com.tuibian.R;

import android.app.Activity;
import android.os.Bundle;

public class Venue_getMap extends Activity {
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	LatLng point = null;
	BitmapDescriptor bitmap = null;
	OverlayOptions option = null;
	Double longitude;
	Double latitude;
	private Marker marker1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		longitude = getIntent().getDoubleExtra("venue_longitude",0.0);
		latitude = getIntent().getDoubleExtra("venue_latitude",0.0);
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.venuemap);
		mMapView = (MapView) findViewById(R.id.bmapView);
		//mMapView = (MapView) findViewById(R.id.bmapView);  
		mBaiduMap = mMapView.getMap();  
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		// 构建MarkerOption，用于在地图上添加Marker
		LatLng cenpt =  new LatLng(43.8290560000,125.2873520000);  
		 MapStatus mMapStatus = new MapStatus.Builder()
	        .target(cenpt)
	        .zoom(18)
	        .build();
	        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
	        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	        //改变地图状态
	        mBaiduMap.setMapStatus(mMapStatusUpdate);
		// 在地图上添加Marker，并显示
	        OverlayOptions option = new MarkerOptions().position(cenpt)
					.icon(bitmap);
			mBaiduMap.addOverlay(option);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
}
