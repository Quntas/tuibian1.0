package com.tuibian.main;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.SupportMapFragment;
import com.tuibian.aboutandset.About;
import com.tuibian.aboutandset.Set;

import com.tuibian.common.CGlobal;
import com.tuibian.common.Configure;
import com.tuibian.dialog.CustomDialog;
import com.tuibian.dialog.CustomDialogCircle;

import com.tuibian.fragment.TribeFragment;
import com.tuibian.fragment.VenueFragment;
import com.tuibian.fragment.ShopFragment;
import com.tuibian.fragment.MeFragment;

import com.tuibian.login.LoginActivity;
import com.tuibian.model.CContext;
import com.tuibian.model.CUser;
import com.tuibian.model.CUserMgr;
import com.tuibian.update.ApkDownLoadUtils;
import com.tuibian.update.ApplactionVersion;
import com.tuibian.update.VisionInfo;
import com.tuibian.util.Constants;
import com.tuibian.util.ToastUtil;
import com.tuibian.R;

public class MainActivity extends BaseActivity implements OnClickListener {

	private static boolean isExit = false;
	private static VisionInfo info;
	private static final int ACTIVITY_SHOW_INFOLAYOUT = 2;
	private static final int EVENT_UPDATE_VERSION = 3;
	private static final int EVENT_UPDATE_VERSION_SET = 4;
	private static final int EVENT_UPDATE_VERSION_MUST = 5;
	private static final int EVENT_UPDATE_ISNEW = 6;

	// 升级对话框弹出需要的Context
	public Context m_updateContext = null;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case EVENT_UPDATE_VERSION:
				showUpdateDialog(m_updateContext);
				break;
			case EVENT_UPDATE_VERSION_MUST:
				showUpdateDialogMust(m_updateContext);
				break;
			case EVENT_UPDATE_ISNEW:
				if (m_updateContext != MainActivity.this) {
					ToastUtil.show(m_updateContext, "已经是最新的版本！");
				}
				break;
			default:
				break;
			}
		}
	};

	private MeFragment me_fragment;
	private VenueFragment venue_fragment;
	private TribeFragment tribe_fragment;
	private ShopFragment shop_fragment;

	private Button btn_page_venue;
	private Button btn_page_tribe;
	private Button btn_page_shop;
	private Button btn_page_me;

	private TextView btn_page_venue_text;
	private TextView btn_page_tribe_text;
	private TextView btn_page_shop_text;
	private TextView btn_page_me_text;

	private View btn_page_venue_view;
	private View btn_page_tribe_view;
	private View btn_page_shop_view;
	private View btn_page_me_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_updateContext = this;

		addView(R.layout.activity_main);
		//mTitleBar.setTitleText("首页");
		//mTitleBar.btnBack.setVisibility(View.GONE);
		mTitleBar.hideAll();
		
		tribe_fragment = (TribeFragment) getSupportFragmentManager()
				.findFragmentById(R.id.tribe_fragment);
		
		venue_fragment = (VenueFragment) getSupportFragmentManager()
				.findFragmentById(R.id.venue_fragment);
		
		shop_fragment = (ShopFragment) getSupportFragmentManager()
				.findFragmentById(R.id.shop_fragment);
		
		me_fragment = (MeFragment) getSupportFragmentManager()
				.findFragmentById(R.id.me_fragment);

		showFragment(tribe_fragment);
		initTabback(0);
		
		autoLogin();
		
		//updateVersion();
		
	}

	/** 使用Intent启动activity */
	public void myStartActivity(Class<?> cls) {
		Intent intent = new Intent(getApplicationContext(), cls);
		startActivity(intent);
	}
	
	/*自动登录*/
	public void autoLogin()
	{
		//取保存的登录信息
		String sUserName = getSharedPreferences(Configure.mSharePName, 0)
				.getString(Configure.mUserName, "");
		String sPwd = getSharedPreferences(Configure.mSharePName, 0).getString(
				Configure.mUserPwd, "");

		if (sUserName!=null && !sUserName.equals("") && sPwd!=null && !sPwd.equals(""))
		{
			new loginAsyncTask().execute(sUserName, sPwd);
		}
		
	}

	public void updateVersion() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					info = ApplactionVersion.getVersionInfo();
					if (info == null) {
						return;
					}
					//得到服务器版本的int数组
					String[] infoArrayS = info.AndroidVer.split("\\.",3);
					int[] infoArrayI = new int[3];
					for(int i=0;i<3;i++){
						infoArrayI[i]=Integer.parseInt(infoArrayS[i]);
					}
					
					//得到当前版本的int数组
					String versionNumber = getVersion();
					String[] vesionArrayS = versionNumber.split("\\.",3);
					int[] vesionArrayI = new int[3];
					for(int i=0;i<3;i++){
						vesionArrayI[i]=Integer.parseInt(vesionArrayS[i]);
					}
					
					for(int i=0;i<3;i++){
						if(infoArrayI[i]>vesionArrayI[i]){
							if (info.AndroidUpdate.equals("0")) {
								mHandler.sendEmptyMessage(EVENT_UPDATE_VERSION);
								break;
							} else {
								mHandler.sendEmptyMessage(EVENT_UPDATE_VERSION_MUST);
								break;
							}
						}else if(i==2){
							mHandler.sendEmptyMessage(EVENT_UPDATE_ISNEW);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/***/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			tack.exit();
			System.exit(0);
		}
	}

	public FragmentManager getManager() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		return fragmentManager;
	}

	/** 显示fragment */
	private void showFragment(Fragment fragment) {
		FragmentManager fragmentManager = getManager();
		// fragmentManager.popBackStack();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (fragment != null) {
			
			transaction.hide(tribe_fragment);
			transaction.hide(venue_fragment);
			transaction.hide(shop_fragment);
			transaction.hide(me_fragment);
			
			transaction.show(fragment);
		}
		transaction.commitAllowingStateLoss();

	}

	/** 初始化控件 */
	@Override
	protected void initViews() {

		Log.i("MainActivity2", "initViews");

		btn_page_venue = (Button) this.findViewById(R.id.btn_page_venue);
		btn_page_shop = (Button) this.findViewById(R.id.btn_page_shop);
		btn_page_tribe = (Button) this.findViewById(R.id.btn_page_tribe);
		btn_page_me= (Button) this.findViewById(R.id.btn_page_me);
		
		btn_page_venue_text = (TextView) this
				.findViewById(R.id.btn_page_venue_text);
		btn_page_shop_text = (TextView) this
				.findViewById(R.id.btn_page_shop_text);
		btn_page_tribe_text = (TextView) this
				.findViewById(R.id.btn_page_tribe_text);
		btn_page_me_text = (TextView) this
				.findViewById(R.id.btn_page_me_text);
		
		btn_page_venue_view = this.findViewById(R.id.btn_page_venue_view);
		btn_page_tribe_view = this.findViewById(R.id.btn_page_tribe_view);
		btn_page_shop_view = this.findViewById(R.id.btn_page_shop_view);
		btn_page_me_view = this.findViewById(R.id.btn_page_me_view);
	}

	/** 给控件添加监听器 */
	@Override
	protected void initEvents() {
		btn_page_venue_view.setOnClickListener(this);
		btn_page_tribe_view.setOnClickListener(this);
		btn_page_shop_view.setOnClickListener(this);
		btn_page_me_view.setOnClickListener(this);

	}

	@Override
	protected void init() {
		// initTabbac k(-1);

	}

	/** 启动activity */
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	/** 监听事件 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_page_tribe_view:
			initTabback(0);
			//showAll();
			//mTitleBar.setTitleText("赛事");
			showFragment(tribe_fragment);
			break;
		case R.id.btn_page_venue_view:
			initTabback(1);
			//showAll();
			//mTitleBar.setTitleText("首页");
			showFragment(venue_fragment);
			break;
		case R.id.btn_page_shop_view:
			initTabback(2);
			//showAll();
			//mTitleBar.setTitleText("运动员");
			showFragment(shop_fragment);
			break;
		case R.id.btn_page_me_view:
			initTabback(3);
			//showAll();
			//mTitleBar.setTitleText("我的");
			showFragment(me_fragment);
			break;
		}
	}

	/** 设置button的选择背景 */
	public void initTabback(int index) {

		btn_page_tribe.setSelected(false);
		btn_page_venue.setSelected(false);
		btn_page_shop.setSelected(false);
		btn_page_me.setSelected(false);

		btn_page_tribe_text.setSelected(false);
		btn_page_venue_text.setSelected(false);
		btn_page_shop_text.setSelected(false);
		btn_page_me_text.setSelected(false);
		
		if (index == 0) {
			btn_page_tribe.setSelected(true);
			btn_page_tribe_text.setSelected(true);

		} else if (index == 1) {
			btn_page_venue.setSelected(true);
			btn_page_venue_text.setSelected(true);

		} else if (index == 2) {
			btn_page_shop.setSelected(true);
			btn_page_shop_text.setSelected(true);

		} else if (index == 3) {
			btn_page_me.setSelected(true);
			btn_page_me_text.setSelected(true);

		}

	}

	 OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
	        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
	            {
	             return true;
	            }
	            else
	            {
	             return false;
	            }
	        }
	    } ;
	
	public void showUpdateDialog(Context context) {

		CustomDialog alertDialog = new CustomDialog.Builder(context)
				.setTitle("通知").setMessage("亲！想更新软件吗？")
				.setPositiveButton("欣然接受", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						ApkDownLoadUtils.downloadApk(MainActivity.this,
								info.AndroidUrl, "tuibian"
										+ info.AndroidVer + ".apk");
						dialog.cancel();
						ToastUtil.show(m_updateContext, "已在后台开始下载...");
					}
				}).

				setNegativeButton("残忍拒绝", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				}).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setOnKeyListener(keylistener);
		alertDialog.show();

	}

	public void showUpdateDialogMust(Context context) {

		CustomDialog alertDialog = new CustomDialog.Builder(context)
				.setTitle("通知").setMessage("请立即更新软件，否则程序将无法正常运行。")
				.setPositiveButton("接受", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						ApkDownLoadUtils.downloadApk(MainActivity.this,
								info.AndroidUrl, "tuibian"
										+ info.AndroidVer + ".apk");
						dialog.cancel();
						ToastUtil.show(m_updateContext, "已在后台开始下载...");
						tack.exit();
						getSharedPreferences(Configure.mSharePName, 0).edit()
						.remove(Configure.mUserPwd).commit();
						startActivity(LoginActivity.class);
					}
				}).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setOnKeyListener(keylistener);
		alertDialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent data) {
		super.onActivityResult(requestCode, responseCode, data);

	}
	private class loginAsyncTask extends AsyncTask<String, String, JSONObject> {

		CustomDialogCircle dialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new CustomDialogCircle(MainActivity.this,
					R.layout.h_dialog_layout_circle, R.style.DialogTheme);
			dialog.setTitle("正在登录，请稍候...");
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			String username = params[0];
			String password = params[1];
			int Type = 0;

			CGlobal.m_User = new CUser();
			CGlobal.m_User.Name = username;
			CGlobal.m_User.Pwd = password;
			// 调用登陆方法
			JSONObject object = CUserMgr.Login(username, password, Type, null);

			return object;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dialog.dismiss();

			if (result == null) {
				Toast.makeText(MainActivity.this, "网络访问异常！", Toast.LENGTH_LONG)
						.show();
				return;
			}
			String Status = result.optString("Status");
			String Err = result.optString("Err");

			if (Status.equals("1")) {
				// 登录成功
				try {
					JSONObject entitys = result.getJSONObject("Ret");

					String uid = entitys.optString("id");
					CContext.DATABASE_NAME = uid + ".db";
					
					CGlobal.m_User.Id = UUID.fromString(uid);
					CGlobal.m_User.mqttid = entitys.optString("mqttid");
					CGlobal.m_User.token = entitys.optString("token");
					CGlobal.m_User.TName = entitys.optString("TName");
					CGlobal.m_User.Sex = entitys.optString("Sex");

					CGlobal.setCContext();// 设置Context。
					CGlobal.ResetDataManager();//重置
					
					me_fragment.initEventByLogin();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(MainActivity.this, "登录失败," + Err,
						Toast.LENGTH_LONG).show();
			}

		}

	}
}
