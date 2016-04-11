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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tuibian.activity.MessageWebview;
import com.tuibian.activity.VenueInfoActivity;
import com.tuibian.business.model.CTribe_Latest;
import com.tuibian.business.model.CTribe_Latest;
import com.tuibian.common.CGlobal;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.main.BaseActivity;
import com.tuibian.model.CBaseObject;
import com.tuibian.model.CUser;
import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;
import com.tuibian.R;
	//显示消息列表页
	public class TribeFollowTab extends Fragment {

		private View view;
		private ListView h_tribe_lv1 = null;
		private GridView h_tribe_lv2 = null;
		
		private PullToRefreshGridView h_tribe_lv=null;
		private ListView h_taekwondo_lv = null;
		
		private LinearLayout nomsg_lv = null;
		private List<CTribe_Latest> CTribe_LatestList = null;
		private List<CBaseObject> lists = null;
		private TribeAdapter tribeAdapter = null;
		
		private ImageLoader imageLoader;
		private DisplayImageOptions options;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.tribe_muti_list, null);
			initView();
			initEvent();
			return view;
		}

		private void initEvent() {
			
		}
		//初始化
		private void initData() {
			
			CTribe_Latest post1 = new CTribe_Latest();
			post1.Id = "11";
			post1.Name="隋鹏";
			post1.ImgUrl1="http://192.168.238.152:8080/item1_1.png";
			post1.ImgUrl2="http://192.168.238.152:8080/item1_2.png";
			post1.ImgUrl3="http://192.168.238.152:8080/dianzan.png";
			post1.ImgUrl4="http://192.168.238.152:8080/pinglun.png";
			
			
			
			
			CTribe_LatestList.add(post1);
			CTribe_LatestList.add(post1);
			CTribe_LatestList.add(post1);
			CTribe_LatestList.add(post1);
			CTribe_LatestList.add(post1);
			
			CTribe_LatestList.add(post1);
			
			CTribe_LatestList.add(post1);
			
			
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
			
			/*h_tribe_lv = (ListView) view.findViewById(R.id.tribe_listview);*/
			h_tribe_lv = (PullToRefreshGridView) view.findViewById(R.id.tribe_gridview);
			nomsg_lv = (LinearLayout) view.findViewById(R.id.bpush_msg_none_layout);
			
			CTribe_LatestList = new ArrayList<CTribe_Latest>();
			
			initData();
			
			tribeAdapter = new TribeAdapter(getActivity(), CTribe_LatestList);
			//taekwondoAdapter = new TaekwondoAdapter(getActivity(), cPlayersTaekwondo);
			
			h_tribe_lv.setAdapter(tribeAdapter);
			h_tribe_lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					/*Intent intent = new Intent(getActivity(),
							VenueInfoActivity.class);
					intent.putExtra("id", CTribe_LatestList.get(position).Id);
					intent.putExtra("name", CTribe_LatestList.get(position).Name);
					intent.putExtra("address", CTribe_LatestList.get(position).Sign);
					startActivity(intent);*/
				}
			});
			
		}

		private class TribeAdapter extends BaseAdapter {
			private Context context = null;
			private List<CTribe_Latest> list = null;
			private LayoutInflater inflater = null;

			public TribeAdapter(Context context, List<CTribe_Latest> list) {
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

			/*@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ChatHolder chatHolder = null;
				if (convertView == null) {
					chatHolder = new ChatHolder();
					convertView = inflater.inflate(R.layout.tribe_list_item,
							null);
					chatHolder.imageView = (ImageView) convertView
							.findViewById(R.id.tribe_img);
					chatHolder.Name = (TextView) convertView
							.findViewById(R.id.tribe_name);
					chatHolder.Sign = (TextView) convertView
							.findViewById(R.id.tribe_sign);
					chatHolder.Time = (TextView) convertView
							.findViewById(R.id.tribe_time);
					convertView.setTag(chatHolder);
				} else {
					chatHolder = (ChatHolder) convertView.getTag();
				}

				chatHolder.Name.setText(CTribe_LatestList.get(position).Name);
				chatHolder.Sign.setText(CTribe_LatestList.get(position).Sign);
				chatHolder.Time.setText(CTribe_LatestList.get(position).Time);
				
				imageLoader.displayImage(
						CTribe_LatestList.get(position).ImgUrl,
						chatHolder.imageView, options);
				
				return convertView;
			}

			private class ChatHolder {
				private ImageView imageView;
				private TextView Name;
				private TextView Sign;
				private TextView Time;
			}*/
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ChatHolder chatHolder = null;
				if (convertView == null) {
					chatHolder = new ChatHolder();
					convertView = inflater.inflate(R.layout.tribe_list_item_zuixin,
							null);
					chatHolder.imageView = (ImageView) convertView
							.findViewById(R.id.buluo_zuixin_item_img1_1);
					chatHolder.Name = (TextView) convertView
							.findViewById(R.id.buluo_zuixin_item_text1_2);
					chatHolder.imageView1 = (ImageView) convertView
							.findViewById(R.id.buluo_zuixin_item_img1_3);
					chatHolder.imageView2 = (ImageView) convertView
							.findViewById(R.id.buluo_zuixin_item_img1_4);
					chatHolder.imageView3 = (ImageView) convertView
							.findViewById(R.id.buluo_zuixin_item_img1_5);
					convertView.setTag(chatHolder);
				} else {
					chatHolder = (ChatHolder) convertView.getTag();
				}
				//设置数据
				chatHolder.Name.setText(CTribe_LatestList.get(position).Name);			
				
				imageLoader.displayImage(
						CTribe_LatestList.get(position).ImgUrl1,
						chatHolder.imageView, options);
				imageLoader.displayImage(
						CTribe_LatestList.get(position).ImgUrl2,
						chatHolder.imageView1, options);
				imageLoader.displayImage(
						CTribe_LatestList.get(position).ImgUrl3,
						chatHolder.imageView2, options);
				imageLoader.displayImage(
						CTribe_LatestList.get(position).ImgUrl4,
						chatHolder.imageView3, options);
				
				return convertView;
			}

			private class ChatHolder {
				private ImageView imageView;//头像
				private TextView Name;//用户名
				private ImageView imageView1;//主照片
				private ImageView imageView2;//点赞
				private ImageView imageView3;//评论
			}

		}

			

	}