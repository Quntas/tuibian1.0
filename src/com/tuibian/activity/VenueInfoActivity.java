package com.tuibian.activity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tuibian.business.model.CNews;
import com.tuibian.business.model.CVenue;
import com.tuibian.business.model.CVenue_Sport_List;
import com.tuibian.business.model.CVenue_pinglun;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.main.BaseActivity;
import com.tuibian.map.Venue_getMap;
import com.tuibian.model.CVenueListMgr;
import com.tuibian.venue.Venue_List;
import com.tuibian.venue.Venue_pinglun_all;
import com.tuibian.view.bannerview.CircleFlowIndicator;
import com.tuibian.view.bannerview.ImagePagerAdapter;
import com.tuibian.view.bannerview.ViewFlow;
import com.tuibian.R;

/**
 * @Description:场馆详情展示页
 */
public class VenueInfoActivity extends BaseActivity {

	private ViewFlow mViewFlow;
	private View view;
	private CircleFlowIndicator mFlowIndicator;
	private ArrayList<String> imageUrlList = new ArrayList<String>();
	ArrayList<String> linkUrlArray = new ArrayList<String>();
	ArrayList<String> titleList = new ArrayList<String>();
	private LinearLayout notice_parent_ll;
	private LinearLayout notice_ll;
	private ViewFlipper notice_vf;
	private int mCurrPos;
	private List<CVenue_pinglun> cVenuepinglunList;
	private List<CVenue_Sport_List> cVenuesporttype;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private VenueAdapter venueAdapter = null;
	private VenueTypeAdapter venuetypeadpter = null;
	private String mId;
	private ListView h_venue_pinglun = null;
	private ListView h_venue_sport = null;
	// 页面显示控件
	private TextView Name;
	private Button Address;
	private Button Phone;
	private TextView venue_content;
	private ImageView ishavewifi;
	private ImageView ishaveshower;
	private String hasbath;
	private String haswifi;
	private Double longitude;
	private Double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mId = getIntent().getStringExtra("venue_id");
		//Toast.makeText(this, mId, Toast.LENGTH_SHORT).show();
		addView(R.layout.venue_new_info);
		mTitleBar.hideAll();

	}

	private void initData() {

	}

	@Override
	protected void initViews() {

		Name = (TextView) findViewById(R.id.venue_new_name);
		Address = (Button) findViewById(R.id.venue_new_address);
		Phone = (Button) findViewById(R.id.venue_new_phone);

		venue_content = (TextView) findViewById(R.id.venue_new_content);
		ishavewifi = (ImageView) findViewById(R.id.wifi);
		ishaveshower = (ImageView) findViewById(R.id.shower);

		// 初始化imageLoader
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(VenueInfoActivity.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		mViewFlow = (ViewFlow) findViewById(R.id.viewflow);
		mFlowIndicator = (CircleFlowIndicator) findViewById(R.id.viewflowindic);

		h_venue_pinglun = (ListView) findViewById(R.id.venue_pinglun_listview);
		h_venue_sport = (ListView) findViewById(R.id.venue_xiangmu_listview);
		// wyq
		cVenuepinglunList = new ArrayList<CVenue_pinglun>();
		cVenuesporttype = new ArrayList<CVenue_Sport_List>();

		new GetDataTask().execute();

		venueAdapter = new VenueAdapter(this, cVenuepinglunList);
		venuetypeadpter = new VenueTypeAdapter(this, cVenuesporttype);
		h_venue_pinglun.setAdapter(venueAdapter);
		h_venue_sport.setAdapter(venuetypeadpter);
		// 评论列表的
		h_venue_pinglun.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(VenueInfoActivity.this,
						Venue_pinglun_all.class);
				intent.putExtra("venue_id",mId);
				startActivity(intent);
				

			}
		});
		// 地址点击按钮控价，点击出现百度地图
		Address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(VenueInfoActivity.this,
						Venue_getMap.class);	
				intent.putExtra("venue_longitude",longitude);
				intent.putExtra("venue_latitude",latitude);
				startActivity(intent);

			}
		});
		// 电话按钮控件，点击直接拨号
		Phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String number = Phone.getText().toString();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + number));
				startActivity(intent);

			}
		});

	}

	private void initBanner(ArrayList<String> imageUrlList) {

		mViewFlow.setAdapter(new ImagePagerAdapter(VenueInfoActivity.this,
				imageUrlList).setInfiniteLoop(true));
		mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
														// 我的ImageAdapter实际图片张数为3
		mViewFlow.setFlowIndicator(mFlowIndicator);
		mViewFlow.setTimeSpan(4500);
		mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
	}

	// 数据传输，主要构造这个页面的动态数据，包括两个listview以及基本信息
	private class GetDataTask extends AsyncTask<String, String, JSONObject> {
		CustomDialogCircle dialog = null;

		@Override
		protected JSONObject doInBackground(String... params) {
			// Simulates a background job.
			JSONObject object = CVenueListMgr.Venueinfo(mId);
			return object;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// 从result中获取到值，并且根据响应跳转界面 成功就跳转 失败不跳转
			if (result == null) {
				Toast.makeText(VenueInfoActivity.this, "网络访问异常！",
						Toast.LENGTH_LONG).show();
				return;
			}
			String Status = result.optString("success");
			// 提示信息
			String Err = result.optString("msg");
			if (Status.equals("true")) {
				// 登录成功
				// 逻辑自己写
				try {
					JSONObject a = result.getJSONObject("result");
					JSONObject b = a.getJSONObject("venueComments");
					JSONArray PL_array = b.getJSONArray("list");
					JSONArray SportTy_array = a.getJSONArray("venueSports");
					JSONObject venue_info = a.getJSONObject("venue");
					// 先清空
					cVenuepinglunList.clear();
					cVenuesporttype.clear();
					Log.i("WYQ", PL_array + "");
					Log.i("WYQ", SportTy_array + "");
					// 构造评论表
					for (int i = 0; i < PL_array.length(); i++) {
						CVenue_pinglun pinglun = new CVenue_pinglun();
						JSONObject object = PL_array.getJSONObject(i);
						pinglun.content = object.getString("content");

						Log.i("WYQ", pinglun.content + "");
						pinglun.uesrName = object.getString("user_nickname");
						pinglun.Date = object.getString("createtime");
						cVenuepinglunList.add(pinglun);
					}
					// 构造运动项目表
					for (int i = 0; i < SportTy_array.length(); i++) {
						CVenue_Sport_List sporttype = new CVenue_Sport_List();
						JSONObject object = SportTy_array.getJSONObject(i);
						sporttype.Sport_price = object.getString("vesp_price");
						sporttype.Sport_name = object.getString("spty_title");
						cVenuesporttype.add(sporttype);
					}
					// 构造场馆主要内容
					String s = venue_info.getString("venu_name")
							+ venue_info.getString("venu_address")
							+ venue_info.getString("venu_info")
							+ venue_info.getString("venu_tel");

					Name.setText(venue_info.getString("venu_name"));
					Address.setText(venue_info.getString("venu_address"));
					venue_content.setText(venue_info.getString("venu_info"));
					Phone.setText(venue_info.getString("venu_tel"));

					hasbath = venue_info.getString("hasbath");
					haswifi = venue_info.getString("haswifi");
					
					longitude=venue_info.getDouble("venu_longitude");
					latitude=venue_info.getDouble("venu_latitude");
					
					if (hasbath.equals("0")) {

					}
					if (haswifi.equals("0")) {
						ishavewifi.setImageResource(R.drawable.haswifi);
					}
					else
					{
						ishavewifi.setImageResource(R.drawable.hasnowifi);
					}
					// 设置顶部图片
					imageUrlList.add("http://192.168.238.149:8080"
							+ venue_info.getString("img1"));
					imageUrlList.add("http://192.168.238.149:8080"
							+ venue_info.getString("img2"));
					imageUrlList.add("http://192.168.238.149:8080"
							+ venue_info.getString("img3"));
					imageUrlList.add("http://192.168.238.149:8080"
							+ venue_info.getString("img4"));
					imageUrlList.add("http://192.168.238.149:8080"
							+ venue_info.getString("img0"));
					imageUrlList.add("http://192.168.238.149:8080"
							+ venue_info.getString("img5"));
					fixListViewHeight(h_venue_pinglun);
					fixsportListViewHeight(h_venue_sport);
					initBanner(imageUrlList);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(VenueInfoActivity.this, "获取失败," + Err,
						Toast.LENGTH_LONG).show();
			}
			venueAdapter.notifyDataSetChanged();
			h_venue_pinglun.invalidate();
			h_venue_sport.invalidate();

		}
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	// 评论列表的listview
	private class VenueAdapter extends BaseAdapter {
		private Context context = null;
		private List<CVenue_pinglun> list = null;
		private LayoutInflater inflater = null;

		public VenueAdapter(Context context, List<CVenue_pinglun> list) {
			this.context = context;
			this.list = list;
			inflater = LayoutInflater.from(this.context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatHolder chatHolder = null;
			if (convertView == null) {
				chatHolder = new ChatHolder();
				convertView = inflater.inflate(
						R.layout.venue_pinglun_list_item, null);

				chatHolder.imageView = (ImageView) convertView
						.findViewById(R.id.venue_pinglun_img);
				chatHolder.Name = (TextView) convertView
						.findViewById(R.id.venue_pinglun_username);
				chatHolder.Date = (TextView) convertView
						.findViewById(R.id.venue_pinglun_data);
				chatHolder.content = (TextView) convertView
						.findViewById(R.id.venue_pinglun_content);
				convertView.setTag(chatHolder);
			} else {
				chatHolder = (ChatHolder) convertView.getTag();
			}
			imageLoader.displayImage(cVenuepinglunList.get(position).ImgUrl,
					chatHolder.imageView, options);

			chatHolder.Name.setText(cVenuepinglunList.get(position).uesrName);
			chatHolder.Date.setText(cVenuepinglunList.get(position).Date);
			chatHolder.content.setText(cVenuepinglunList.get(position).content);

			return convertView;
		}

		private class ChatHolder {
			private ImageView imageView;
			private TextView Name;
			private TextView Date;
			private TextView content;

		}
	}

	// 运动类别列表的listview
	private class VenueTypeAdapter extends BaseAdapter {
		private Context context = null;
		private List<CVenue_Sport_List> list = null;
		private LayoutInflater inflater = null;

		public VenueTypeAdapter(Context context, List<CVenue_Sport_List> list) {
			this.context = context;
			this.list = list;
			inflater = LayoutInflater.from(this.context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatHolder chatHolder = null;
			if (convertView == null) {
				chatHolder = new ChatHolder();
				convertView = inflater.inflate(R.layout.venue_sport_type, null);//

				chatHolder.Name = (TextView) convertView
						.findViewById(R.id.venue_sport_name);
				chatHolder.price = (TextView) convertView
						.findViewById(R.id.venue_sport_price);
				convertView.setTag(chatHolder);
			} else {
				chatHolder = (ChatHolder) convertView.getTag();
			}
			chatHolder.Name.setText(cVenuesporttype.get(position).Sport_name);
			chatHolder.price.setText(cVenuesporttype.get(position).Sport_price);

			return convertView;
		}

		private class ChatHolder {
			private TextView Name;
			private TextView price;

		}
	}

	// 显示评论listView 最多显示五条
	public void fixListViewHeight(ListView listView) {
		// 如果没有设置数据适配器，则ListView没有子项，返回。
		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		if (listAdapter == null) {
			return;
		}
		int len = 0;
		if (listAdapter.getCount() < 5) {
			len = listAdapter.getCount();
		} else {
			len = 5;
		}
		for (int index = 0; index < len; index++) {
			View listViewItem = listAdapter.getView(index, null, listView);
			// 计算子项View 的宽高
			listViewItem.measure(0, 0);
			// 计算所有子项的高度和
			totalHeight += listViewItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// listView.getDividerHeight()获取子项间分隔符的高度
		// params.height设置ListView完全显示需要的高度
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// 显示运动listView 全部显示
	public void fixsportListViewHeight(ListView listView) {
		// 如果没有设置数据适配器，则ListView没有子项，返回。
		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		if (listAdapter == null) {
			return;
		}
		int len = 0;
		len = listAdapter.getCount();
		for (int index = 0; index < len; index++) {
			View listViewItem = listAdapter.getView(index, null, listView);
			// 计算子项View 的宽高
			listViewItem.measure(0, 0);
			// 计算所有子项的高度和
			totalHeight += listViewItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// listView.getDividerHeight()获取子项间分隔符的高度
		// params.height设置ListView完全显示需要的高度
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
