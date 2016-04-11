package com.tuibian.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tuibian.activity.MessageWebview;
import com.tuibian.activity.VenueInfoActivity;
import com.tuibian.business.model.CVenue;
import com.tuibian.business.model.CVenue_pinglun;
import com.tuibian.common.CGlobal;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.main.BaseActivity;
import com.tuibian.model.CBaseObject;
import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;
import com.tuibian.venue.Venue_List;

import com.tuibian.R;
//显示消息列表页
public class ShopFragment extends Fragment {

	private View view;
	private ListView h_venue_lv = null;
	private PullToRefreshListView mPullRefreshListView;
	private LinearLayout nomsg_lv = null;
	private List<CVenue> cVenueList = null;

	private List<CBaseObject> lists = null;

	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.shop_muti_list, null);

		return view;
	}

	private void initEvent() {
		
	}
	
	private void initData() {
		
		CVenue venue11 = new CVenue();
		venue11.Id = "11";
		venue11.Name="李宁篮球鞋";
		venue11.Address = "舒适度第一";
		venue11.Distance = "200人推荐";
		venue11.Price = "￥200";
		venue11.ImgUrl = "http://tse1.mm.bing.net/th?&id=OIP.M7093df21299ee56502720dd8e94826b9o0&w=300&h=300&c=0&pid=1.9&rs=0&p=0";
		
		CVenue venue12 = new CVenue();
		venue12.Id = "12";
		venue12.Name="耐克篮球鞋";
		venue12.Address = "国家队的选择";
		venue12.Distance = "100人推荐";
		venue12.Price = "￥200";
		venue12.ImgUrl = "http://tse1.mm.bing.net/th?&id=OIP.Ma9a135402464b9f39d678c90d91c3a4ao0&w=300&h=300&c=0&pid=1.9&rs=0&p=0";
		
		CVenue venue13 = new CVenue();
		venue13.Id = "13";
		venue13.Name="欧内斯篮球鞋";
		venue13.Address = "卫星路阳光大厦四楼";
		venue13.Distance = "50人推荐";
		venue13.Price = "￥200";
		venue13.ImgUrl = "http://tse1.mm.bing.net/th?&id=OIP.M20ee482e750939913bf10fd96bcdebcdo0&w=300&h=300&c=0&pid=1.9&rs=0&p=0";
		
		cVenueList.add(venue11);
		cVenueList.add(venue12);
		cVenueList.add(venue13);
		cVenueList.add(venue11);
		cVenueList.add(venue12);
		cVenueList.add(venue13);
		cVenueList.add(venue11);
		cVenueList.add(venue12);
		cVenueList.add(venue13);
		
	}
	private void initView() {
		
		// 初始化imageLoader
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		
	}



}