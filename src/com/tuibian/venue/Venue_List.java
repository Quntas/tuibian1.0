package com.tuibian.venue;

import android.app.Activity;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
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
import com.tuibian.business.model.CVenue_All;
import com.tuibian.business.model.CVenue_pinglun;
import com.tuibian.common.CGlobal;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.main.BaseActivity;
import com.tuibian.main.TitleFragment;
import com.tuibian.model.CBaseObject;
import com.tuibian.model.CVenueListMgr;
import com.tuibian.model.CVenueMgr;
import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;
import com.tuibian.R;

//显示消息列表页
public class Venue_List extends BaseActivity {
	private PullToRefreshListView mPullRefreshListView;
	private View view;
	private ListView h_venue_lv = null;
	private LinearLayout nomsg_lv = null;
	private List<CVenue> cVenueList = null;
	//private List<CBaseObject> lists = null;
	private VenueAdapter venueAdapter = null;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	String city;
	String uuid;
	private Double latitude;
	private Double longitude;
	private String type_sport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent1 = getIntent();
		// 传给服务器的数据
		type_sport = intent1.getStringExtra("id");
		city = intent1.getStringExtra("city");
		uuid = intent1.getStringExtra("UUID");
		longitude = intent1.getDoubleExtra("Longitude", 0);
		latitude = intent1.getDoubleExtra("Latitude", 0);
		Log.i("WYQ", type_sport + "  " + longitude + "  " + latitude + "  "
				+ uuid + "  " + city);
		addView(R.layout.shop_muti_list);
		mTitleBar.hideAll();
	}



	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		// 初始化imageLoader
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		
		
		
		mPullRefreshListView = (PullToRefreshListView)
				findViewById(R.id.venue_listview1);
		h_venue_lv = mPullRefreshListView.getRefreshableView();
		//h_venue_lv.setVisibility(View.GONE);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(Venue_List.this,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel("更新时间：" + label);

						// Do work to refresh the list here.
						new GetDataTask().execute();
					}
				});
		
		
		//h_venue_lv = (ListView) findViewById(R.id.venue_listview1);
		nomsg_lv = (LinearLayout) findViewById(R.id.venue_bpush_msg_none_layout);

		cVenueList = new ArrayList<CVenue>();
		
		new GetDataTask().execute();
		// initData();

		venueAdapter = new VenueAdapter(this, cVenueList);

		h_venue_lv.setAdapter(venueAdapter);
		h_venue_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Venue_List.this,
						VenueInfoActivity.class);
				
				intent.putExtra("venue_id", cVenueList.get(position-1).Id);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	private class VenueAdapter extends BaseAdapter {
		private Context context = null;
		private List<CVenue> list = null;
		private LayoutInflater inflater = null;

		public VenueAdapter(Context context, List<CVenue> list) {
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
				convertView = inflater.inflate(R.layout.venue_list_item, null);

				chatHolder.imageView = (ImageView) convertView
						.findViewById(R.id.venue_img);
				chatHolder.Name = (TextView) convertView
						.findViewById(R.id.venue_name);
				chatHolder.Address = (TextView) convertView
						.findViewById(R.id.venue_address);
				chatHolder.Distance = (TextView) convertView
						.findViewById(R.id.venue_newdistance);

				convertView.setTag(chatHolder);
			} else {
				chatHolder = (ChatHolder) convertView.getTag();
			}
			imageLoader.displayImage(cVenueList.get(position).ImgUrl,
					chatHolder.imageView, options);

			chatHolder.Name.setText(cVenueList.get(position).Name);
			chatHolder.Address.setText(cVenueList.get(position).Address);
			chatHolder.Distance.setText(cVenueList.get(position).Distance);
			return convertView;
		}

		private class ChatHolder {
			private ImageView imageView;
			private TextView Name;
			private TextView Address;
			private TextView Distance;
		}

	}

	private class GetDataTask extends AsyncTask<String, String, JSONObject> {
		CustomDialogCircle dialog = null;

		@Override
		protected JSONObject doInBackground(String... params) {

			JSONObject object = CVenueListMgr.VenueList(city, type_sport,
					longitude, latitude, uuid);
			return object;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// 从result中获取到值，并且根据响应跳转界面 成功就跳转 失败不跳转
			if (result == null) {
				Toast.makeText(Venue_List.this, "网络访问异常！", Toast.LENGTH_LONG)
						.show();
				return;
			}
			String Status = result.optString("success");
			// 提示信息
			String Err = result.optString("msg");
			if (Status.equals("true")) {
				// 登录成功
				// 逻辑自己写
				try {
					JSONObject v_list = result.getJSONObject("result");

					JSONArray array = v_list.getJSONArray("list");
					// 先清空
					cVenueList.clear();
					// String t=CGlobal.m_User.token;
					for (int i = 0; i < array.length(); i++) {
						CVenue venuenew = new CVenue();
						JSONObject object = array.getJSONObject(i);
						venuenew.Id = object.getString("venu_id");
						venuenew.ImgUrl = "http://192.168.238.149:8080"
								+ object.getString("spty_img");
						venuenew.Name = object.getString("venu_name");
						venuenew.Distance = object.getString("dv_distance");
						venuenew.Address = object.getString("venu_address");
						cVenueList.add(venuenew);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(Venue_List.this, "获取失败," + Err,
						Toast.LENGTH_LONG).show();
			}
			mPullRefreshListView.onRefreshComplete();
			venueAdapter.notifyDataSetChanged();
		    h_venue_lv.invalidate();

		}
	}

}
