package com.tuibian.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter.LengthFilter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tuibian.R;
import com.tuibian.business.model.CTribe_Hot_JGG;
import com.tuibian.business.model.CTribe_Latest;
import com.tuibian.model.CBaseObject;

//显示消息列表页
public class TribeHotTab extends Fragment {

	private View view;

	private GridView h_tribe_lv2 = null;
	private GridView h_tribe_lv1 = null;
	private PullToRefreshGridView h_tribe_lv = null;
	private ListView h_taekwondo_lv = null;

	private LinearLayout nomsg_lv = null;
	private List<CTribe_Latest> CTribe_HotListxia = null;//备注下不跳虫和最新一样 使用同一个businessmodel
	//和同一个填充文件
	private List<CTribe_Hot_JGG> CTribe_HotListshang = null;
	private List<CBaseObject> lists = null;
	private TribeAdapter tribeAdapter = null;
	private TribeAdapter1 tribeAdapter1 = null;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tribe_muti_list2, null);
		initView();
		initEvent();
		return view;
	}

	private void initEvent() {

	}

	// 初始化
	private void initData() {
		// 上面初始化
		CTribe_Hot_JGG post0 = new CTribe_Hot_JGG();
		post0.Id = "1";
		post0.ImgUrl1 = "http://192.168.238.163:8080/png_1.png";
		CTribe_Hot_JGG post4 = new CTribe_Hot_JGG();
		post4.Id = "1";
		post4.ImgUrl1 = "http://192.168.238.163:8080/png_2.png";
		CTribe_Hot_JGG post5 = new CTribe_Hot_JGG();
		post5.Id = "1";
		post5.ImgUrl1 = "http://192.168.238.163:8080/png_1.png";
		
		CTribe_Hot_JGG post6 = new CTribe_Hot_JGG();
		post6.Id = "1";
		post6.ImgUrl1 = "http://192.168.238.163:8080/png_2.png";
		CTribe_Hot_JGG post7 = new CTribe_Hot_JGG();
		post7.Id = "1";
		post7.ImgUrl1 = "http://192.168.238.163:8080/png_1.png";
		CTribe_Hot_JGG post8 = new CTribe_Hot_JGG();
		post8.Id = "1";
		post8.ImgUrl1 = "http://192.168.238.163:8080/png_2.png";
		
		CTribe_HotListshang.add(post0);
		CTribe_HotListshang.add(post4);
		CTribe_HotListshang.add(post5);
		CTribe_HotListshang.add(post6);
		CTribe_HotListshang.add(post7);
		CTribe_HotListshang.add(post8);
		CTribe_HotListshang.add(post8);
		CTribe_HotListshang.add(post8);
		CTribe_HotListshang.add(post8);
		
		
		

		CTribe_Latest post1 = new CTribe_Latest();
		post1.Id = "11";
		post1.Name = "蔡依林";
		post1.ImgUrl1 = "http://192.168.238.163:8080/item1_1.png";
		post1.ImgUrl2 = "http://192.168.238.163:8080/item1_2.png";
		post1.ImgUrl3 = "http://192.168.238.163:8080/dianzan.png";
		post1.ImgUrl4 = "http://192.168.238.163:8080/pinglun.png";
		CTribe_Latest post2 = new CTribe_Latest();
		post2.Id = "12";
		post2.Name = "林志颖";
		post2.ImgUrl1 = "http://192.168.238.163:8080/item2_1.png";
		post2.ImgUrl2 = "http://192.168.238.163:8080/item2_2.png";
		post2.ImgUrl3 = "http://192.168.238.163:8080/dianzan.png";
		post2.ImgUrl4 = "http://192.168.238.163:8080/pinglun.png";

		CTribe_HotListxia.add(post1);
		CTribe_HotListxia.add(post2);
		CTribe_HotListxia.add(post1);
		CTribe_HotListxia.add(post2);
		CTribe_HotListxia.add(post1);

		CTribe_HotListxia.add(post1);

		CTribe_HotListxia.add(post2);

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
		h_tribe_lv1=(GridView)view.findViewById(R.id.buluo_tuijian_gridviewshang); 
		/* h_tribe_lv = (ListView) view.findViewById(R.id.tribe_listview); */
		// 自定义代码
		h_tribe_lv = (PullToRefreshGridView) view
				.findViewById(R.id.buluo_tuijian_gridviewxia);
		nomsg_lv = (LinearLayout) view.findViewById(R.id.bpush_msg_none_layout);
		h_tribe_lv2 = h_tribe_lv.getRefreshableView();// 通过这个进行后续操作//点击
		h_tribe_lv.setOnRefreshListener(new OnRefreshListener<GridView>() {
			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
						"更新时间：" + label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
		CTribe_HotListxia = new ArrayList<CTribe_Latest>();
		CTribe_HotListshang = new ArrayList<CTribe_Hot_JGG>();

		initData();

		tribeAdapter = new TribeAdapter(getActivity(), CTribe_HotListxia);
		tribeAdapter1=new TribeAdapter1(getActivity(), CTribe_HotListshang);
		// taekwondoAdapter = new TaekwondoAdapter(getActivity(),
		// cPlayersTaekwondo);
		h_tribe_lv1.setAdapter(tribeAdapter1);
		h_tribe_lv.setAdapter(tribeAdapter);
		
		h_tribe_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*
				 * Intent intent = new Intent(getActivity(),
				 * VenueInfoActivity.class); intent.putExtra("id",
				 * CTribe_HotListxia.get(position).Id); intent.putExtra("name",
				 * CTribe_HotListxia.get(position).Name);
				 * intent.putExtra("address",
				 * CTribe_HotListxia.get(position).Sign); startActivity(intent);
				 */
			}
		});

	}

	// 推荐下面列表适配器
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

		/*
		 * @Override public View getView(int position, View convertView,
		 * ViewGroup parent) { ChatHolder chatHolder = null; if (convertView ==
		 * null) { chatHolder = new ChatHolder(); convertView =
		 * inflater.inflate(R.layout.tribe_list_item, null);
		 * chatHolder.imageView = (ImageView) convertView
		 * .findViewById(R.id.tribe_img); chatHolder.Name = (TextView)
		 * convertView .findViewById(R.id.tribe_name); chatHolder.Sign =
		 * (TextView) convertView .findViewById(R.id.tribe_sign);
		 * chatHolder.Time = (TextView) convertView
		 * .findViewById(R.id.tribe_time); convertView.setTag(chatHolder); }
		 * else { chatHolder = (ChatHolder) convertView.getTag(); }
		 * 
		 * chatHolder.Name.setText(CTribe_HotListxia.get(position).Name);
		 * chatHolder.Sign.setText(CTribe_HotListxia.get(position).Sign);
		 * chatHolder.Time.setText(CTribe_HotListxia.get(position).Time);
		 * 
		 * imageLoader.displayImage( CTribe_HotListxia.get(position).ImgUrl,
		 * chatHolder.imageView, options);
		 * 
		 * return convertView; }
		 * 
		 * private class ChatHolder { private ImageView imageView; private
		 * TextView Name; private TextView Sign; private TextView Time; }
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int position1=position;
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
			// 设置数据
			chatHolder.Name.setText(CTribe_HotListxia.get(position).Name);

			imageLoader.displayImage(CTribe_HotListxia.get(position).ImgUrl1,
					chatHolder.imageView, options);
			imageLoader.displayImage(CTribe_HotListxia.get(position).ImgUrl2,
					chatHolder.imageView1, options);
			imageLoader.displayImage(CTribe_HotListxia.get(position).ImgUrl3,
					chatHolder.imageView2, options);
			imageLoader.displayImage(CTribe_HotListxia.get(position).ImgUrl4,
					chatHolder.imageView3, options);
			chatHolder.imageView2.setOnTouchListener(new OnTouchListener() {
			

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				//得到id更新服务器
				String temp=CTribe_HotListxia.get(position1).Id;
				Toast.makeText(getActivity(), "id"+temp,Toast.LENGTH_LONG).show();
				/*CTribe_HotListxia.remove(position1);*/
				TribeAdapter.this.notifyDataSetChanged();
				return true;
			}
			});

			return convertView;
		}

		private class ChatHolder {
			private ImageView imageView;// 头像
			private TextView Name;// 用户名
			private ImageView imageView1;// 主照片
			private ImageView imageView2;// 点赞
			private ImageView imageView3;// 评论
		}

	}

	// 给上面列表填充的适配器
	private class TribeAdapter1 extends BaseAdapter {
		private Context context = null;
		private List<CTribe_Hot_JGG> list = null;
		private LayoutInflater inflater = null;

		public TribeAdapter1(Context context, List<CTribe_Hot_JGG> list) {
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

		/*
		 * @Override public View getView(int position, View convertView,
		 * ViewGroup parent) { ChatHolder chatHolder = null; if (convertView ==
		 * null) { chatHolder = new ChatHolder(); convertView =
		 * inflater.inflate(R.layout.tribe_list_item, null);
		 * chatHolder.imageView = (ImageView) convertView
		 * .findViewById(R.id.tribe_img); chatHolder.Name = (TextView)
		 * convertView .findViewById(R.id.tribe_name); chatHolder.Sign =
		 * (TextView) convertView .findViewById(R.id.tribe_sign);
		 * chatHolder.Time = (TextView) convertView
		 * .findViewById(R.id.tribe_time); convertView.setTag(chatHolder); }
		 * else { chatHolder = (ChatHolder) convertView.getTag(); }
		 * 
		 * chatHolder.Name.setText(CTribe_HotListxia.get(position).Name);
		 * chatHolder.Sign.setText(CTribe_HotListxia.get(position).Sign);
		 * chatHolder.Time.setText(CTribe_HotListxia.get(position).Time);
		 * 
		 * imageLoader.displayImage( CTribe_HotListxia.get(position).ImgUrl,
		 * chatHolder.imageView, options);
		 * 
		 * return convertView; }
		 * 
		 * private class ChatHolder { private ImageView imageView; private
		 * TextView Name; private TextView Sign; private TextView Time; }
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatHolder chatHolder = null;
			if (convertView == null) {
				chatHolder = new ChatHolder();
				convertView = inflater.inflate(
						R.layout.tribe_list_item_tuijian, null);
				chatHolder.imageView = (ImageView) convertView
						.findViewById(R.id.buluo_tujian_item_img);

				convertView.setTag(chatHolder);
			} else {
				chatHolder = (ChatHolder) convertView.getTag();
			}
			// 设置数据

			imageLoader.displayImage(CTribe_HotListshang.get(position).ImgUrl1,
					chatHolder.imageView, options);

			return convertView;
		}

		private class ChatHolder {
			private ImageView imageView;// 头像

		}

	}

	// 异步任务逻辑sp
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here

			CTribe_Latest post1 = new CTribe_Latest();
			post1.Id = "11";
			post1.Name = "隋鹏";
			post1.ImgUrl1 = "http://192.168.238.163:8080/item1_1.png";
			post1.ImgUrl2 = "http://192.168.238.163:8080/item1_2.png";
			post1.ImgUrl3 = "http://192.168.238.163:8080/dianzan.png";
			post1.ImgUrl4 = "http://192.168.238.163:8080/pinglun.png";

			CTribe_HotListxia.add(post1);
			CTribe_HotListxia.add(post1);
			CTribe_HotListxia.add(post1);
			CTribe_HotListxia.add(post1);

			h_tribe_lv.onRefreshComplete();
			tribeAdapter.notifyDataSetChanged();
			h_tribe_lv2.invalidate();

			super.onPostExecute(result);
		}
	}

}