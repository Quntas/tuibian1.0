package com.tuibian.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tuibian.R;
import com.tuibian.business.model.CVenue_All;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.model.CBaseObject;
import com.tuibian.model.CVenueMgr;
import com.tuibian.venue.Venue_List;

//显示消息列表页
public class VenueFragment extends Fragment {
	private View view;
	private PullToRefreshListView mPullRefreshListView;
	private ListView h_venue_lv = null;
	private LinearLayout nomsg_lv = null;
	private List<CVenue_All> cVenueList = null;
	private List<CBaseObject> lists = null;
	private VenueAdapter venueAdapter = null;
	private LocationManager locationManager; // 获取经纬度
	private String locationProvider;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	ArrayList<String> citys = new ArrayList<String>();
	Spinner spinner;
	String city;
	String uuid;
	private Double latitude;   
	private Double longitude; 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.venue_multi_all, null);
		citys.add("长春市");
		citys.add("吉林市");
		citys.add("四平市");
		citys.add("松原市");
		citys.add("延吉市");
		initView();
		initEvent();
		
		return view;
	}

	private void initEvent() {

	}

	
	
	private String getMyUUID(){
		  final TelephonyManager tm = (TelephonyManager)this.getActivity().getSystemService(Context.TELEPHONY_SERVICE);   
		  final String tmDevice, tmSerial, tmPhone, androidId;   
		  tmDevice = "" + tm.getDeviceId();  
		  tmSerial = "" + tm.getSimSerialNumber();   
		  androidId = "" + android.provider.Settings.Secure.getString(this.getActivity().getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);   
		  UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());   
		  String uniqueId = deviceUuid.toString();
		  Log.d("debug","uuid="+uniqueId);
		  return uniqueId;
		 }
	
	
	
	
	private void initView() {
		//加载地理位置
		locationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);

		List<String> providers = locationManager.getProviders(true);
		if (providers.contains(LocationManager.GPS_PROVIDER)) {
			// 如果是GPS
			locationProvider = LocationManager.GPS_PROVIDER;
		} else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
			// 如果是Network
			locationProvider = LocationManager.NETWORK_PROVIDER;
		} else {
			Toast.makeText(getActivity(), "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
			return;
		}
		// 获取Location
		Location location = locationManager
				.getLastKnownLocation(locationProvider);
		if (location != null) {
			// 不为空,显示地理位置经纬度
			latitude= location.getLatitude();   
             longitude=location.getLongitude();
		}
		// 监视地理位置变化
		locationManager.requestLocationUpdates(locationProvider, 3000, 1,
				locationListener);
		
		
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
		//加载下拉框
		spinner = (Spinner) view.findViewById(R.id.test);
		ArrayAdapter<String> adapterTwo = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_list, citys);
		adapterTwo.setDropDownViewResource(R.layout.checkboxspinner);
		spinner.setSelection(1, true);
		spinner.setAdapter(adapterTwo);
//加载listView
		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.venue_multi_all_listview);
		h_venue_lv = mPullRefreshListView.getRefreshableView();
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(getActivity(),
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

		nomsg_lv = (LinearLayout) view.findViewById(R.id.bpush_msg_none_layout);

		cVenueList = new ArrayList<CVenue_All>();

		new GetDataTask().execute();

		venueAdapter = new VenueAdapter(getActivity(), cVenueList);

		h_venue_lv.setAdapter(venueAdapter);
		h_venue_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String test = cVenueList.get(position - 1).Name;
				Toast.makeText(getActivity(), test, Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(getActivity(), Venue_List.class);
				intent.putExtra("id", cVenueList.get(position - 1).Id);
				city=spinner.getSelectedItem().toString();
				intent.putExtra("city", city);
				uuid=getMyUUID();
				intent.putExtra("UUID", uuid);
				intent.putExtra("Latitude", latitude);
				intent.putExtra("Longitude", longitude);
				//Log.i("WYQ", cVenueList.get(position - 1).Id + "aaaa"+latitude+"    "+uuid+city);
				startActivity(intent);
			}
		});
	}	
LocationListener locationListener =  new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle arg2) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			//如果位置发生变化,重新显示
			latitude= location.getLatitude();   
            longitude=location.getLongitude();
			
		}
	};

	private class VenueAdapter extends BaseAdapter {
		private Context context = null;
		private List<CVenue_All> list = null;
		private LayoutInflater inflater = null;

		public VenueAdapter(Context context, List<CVenue_All> list) {
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
				convertView = inflater.inflate(R.layout.venue_item_all, null);
				chatHolder.imageView = (ImageView) convertView
						.findViewById(R.id.venue_item_all_image);

				convertView.setTag(chatHolder);
			} else {
				chatHolder = (ChatHolder) convertView.getTag();
			}
			imageLoader.displayImage(cVenueList.get(position).ImgUrl,
					chatHolder.imageView, options);

			return convertView;
		}

		private class ChatHolder {
			private ImageView imageView;
		}

	}

	
	private class GetDataTask extends AsyncTask<String, String, JSONObject> {
		CustomDialogCircle dialog = null;

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject object = CVenueMgr.VenueTypes();
			return object;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// Do some stuff here
			super.onPostExecute(result);
			// dialog.dismiss();
			// 从result中获取到值，并且根据响应跳转界面 成功就跳转 失败不跳转
			if (result == null) {
				Toast.makeText(getActivity(), "网络访问异常！", Toast.LENGTH_LONG)
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
					JSONArray array = result.getJSONArray("result");
					// 先清空
					cVenueList.clear();
					// String t=CGlobal.m_User.token;
					for (int i = 0; i < array.length(); i++) {
						CVenue_All venuenew = new CVenue_All();
						JSONObject object = array.getJSONObject(i);
						venuenew.Id = object.getString("spty_id");
						venuenew.IconUrl = "http://192.168.238.149:8080"
								+ object.getString("spty_icon");
						venuenew.ImgUrl = "http://192.168.238.149:8080"
								+ object.getString("spty_img");
						venuenew.Name = object.getString("spty_name");
						cVenueList.add(venuenew);
					}
					// CGlobal.setCContext();// 设置Context。
					// CGlobal.ResetDataManager();//重置
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getActivity(), "获取失败," + Err, Toast.LENGTH_LONG)
						.show();
			}

			mPullRefreshListView.onRefreshComplete();
			venueAdapter.notifyDataSetChanged();
			h_venue_lv.invalidate();
		}
	}

}