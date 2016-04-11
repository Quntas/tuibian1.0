package com.tuibian.venue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tuibian.R;
import com.tuibian.activity.VenueInfoActivity;

import com.tuibian.business.model.CVenue_Sport_List;
import com.tuibian.business.model.CVenue_pinglun;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.main.BaseActivity;
import com.tuibian.model.CVenueListMgr;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Venue_pinglun_all extends BaseActivity {
	private String mId;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ListView h_venue_pinglun = null;
	private List<CVenue_pinglun> cVenuepinglunList;
	private VenueAdapter venueAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	mId = getIntent().getStringExtra("venue_id");
    	addView(R.layout.venue_comments_all);
    	mTitleBar.hideAll();
    	
    }
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(Venue_pinglun_all.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();
		h_venue_pinglun = (ListView) findViewById(R.id.venue_comments_listview);
		cVenuepinglunList = new ArrayList<CVenue_pinglun>();
		new GetDataTask().execute();
		venueAdapter = new VenueAdapter(this, cVenuepinglunList);
		h_venue_pinglun.setAdapter(venueAdapter);
		
	}
	
	
	
	private class GetDataTask extends AsyncTask<String, String, JSONObject> {
		CustomDialogCircle dialog = null;

		@Override
		protected JSONObject doInBackground(String... params) {
			// Simulates a background job.
			JSONObject object = CVenueListMgr.VenueCommentsList(mId);
			return object;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// 从result中获取到值，并且根据响应跳转界面 成功就跳转 失败不跳转
			if (result == null) {
				Toast.makeText(Venue_pinglun_all.this, "网络访问异常！",
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
					Toast.makeText(Venue_pinglun_all.this, a+"", Toast.LENGTH_SHORT).show();
					JSONArray PL_array = a.getJSONArray("list");
					Toast.makeText(Venue_pinglun_all.this, PL_array+"", Toast.LENGTH_SHORT).show();
					// 先清空
					cVenuepinglunList.clear();
					Log.i("WYQ", PL_array + "");
					// 构造评论表
					for (int i = 0; i < PL_array.length(); i++) {
						CVenue_pinglun pinglun = new CVenue_pinglun();
						JSONObject object = PL_array.getJSONObject(i);
						pinglun.content = object.getString("content");
						Log.i("WYQ", pinglun.content + "aaa");
						pinglun.uesrName = object.getString("user_nickname");
						pinglun.Date = object.getString("createtime");
						cVenuepinglunList.add(pinglun);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(Venue_pinglun_all.this, "获取失败," + Err,
						Toast.LENGTH_LONG).show();
			}
			venueAdapter.notifyDataSetChanged();
			h_venue_pinglun.invalidate();

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
}
