package com.tuibian.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tuibian.aboutandset.About;
import com.tuibian.aboutandset.Set;
import com.tuibian.aboutandset.SetChangeUserinfo;
import com.tuibian.common.CGlobal;
import com.tuibian.common.Configure;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.login.LoginActivity;
import com.tuibian.model.CBaseObject;
import com.tuibian.model.CUser;
import com.tuibian.model.CUserInfo;
import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;
import com.tuibian.welcome.GuidePageActivity;
import com.tuibian.welcome.WelcomeActivity;
import com.tuibian.R;

/**
 * @author 李政龙
 */
public class MeFragment extends Fragment {

	private TextView Username, UserAge, Usersex;
	private View view;
	//private CircleImageView circleImageView;
	private ImageView circleImageView;
	/* 组件 */
	private RelativeLayout switchAvatar;
	private RelativeLayout layoutPerson;
	private RelativeLayout layoutAbout;
	private RelativeLayout layoutSet;
	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int LOGIN_REQUEST_CODE = 3;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final int RESULT_CANCELED = 3;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_me, null);
		initView();
		initEvent();
		return view;
	}

	private void initEvent() {

		//if(!hasLogin()) return;
		
		//DownAsyncTask downAsyncTask = new DownAsyncTask();
		//downAsyncTask.execute();
	}
	public void initEventByLogin() {

		if(!hasLogin()) return;
		
		DownAsyncTask downAsyncTask = new DownAsyncTask();
		downAsyncTask.execute();
		
		Username.setText("武术大师");
		Usersex.setText("男");
		UserAge.setText("1980-01-01");
	}
	private void initView() {
		// 人物信息
		Username = (TextView) view.findViewById(R.id.user_name);
		UserAge = (TextView) view.findViewById(R.id.user_age);
		Usersex = (TextView) view.findViewById(R.id.user_sex);

		//circleImageView = (CircleImageView) view.findViewById(R.id.record_head);
		circleImageView = (ImageView) view.findViewById(R.id.record_head);
		
		loadHeadImage();
		
		initdata();
		
		circleImageView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(!hasLogin())
			{
				login();
				return;
			}
			Intent intentFromGallery = new Intent();
			intentFromGallery.setType("image/*"); // 设置文件类型
			intentFromGallery
					.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intentFromGallery,
					IMAGE_REQUEST_CODE);
		}
		});
		layoutPerson = (RelativeLayout)view.findViewById(R.id.h_record_person);
		layoutAbout = (RelativeLayout)view.findViewById(R.id.h_record_about);
		layoutSet = (RelativeLayout) view.findViewById(R.id.h_record_set);

		layoutPerson.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!hasLogin())
				{
					login();
					return;
				}
				
				Intent intent = new Intent(getActivity(),SetChangeUserinfo.class);
				startActivity(intent);
			}
		});
		
		
		layoutAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),About.class);
				startActivity(intent);
			}
		});

		layoutSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(!hasLogin())
				{
					login();
					return;
				}
				
				Intent intent = new Intent(getActivity(),Set.class);
				startActivity(intent);
			}
		});
		
	}

	private void initdata() {
		
		/**
		 * 人物信息
		 */
		//String Name = CGlobal.m_User.TName;// 姓名
		//Username.setText("" + Name);
		//Username.setText("武术大师");

		//String mSex = CGlobal.m_User.Sex;
		//Usersex.setText("" + mSex);
		//Usersex.setText("男");
		
		//UserAge.setText("1980-01-01");

		/*
		List<CBaseObject> listAge = CGlobal.m_User.GetUserInfoMgr().GetList();
		if (listAge.size() > 0) {
			int year_now = 0;
			int userBirthday = 0;
			int userAge = 0;
			Calendar date_now = Calendar.getInstance();
			year_now = date_now.get(Calendar.YEAR);
			CUserInfo cUserInfo = (CUserInfo) listAge.get(listAge.size() - 1);
			//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String birthday = dateFormat.format(cUserInfo.Birthday);
			UserAge.setText(birthday);
			//userBirthday = Integer.parseInt(birthday);
			//userAge = year_now - userBirthday;
			//UserAge.setText("" + userAge);
		}
		*/
		
	}
	public void login()
	{
		Intent intent = new Intent(getActivity(),
				LoginActivity.class);
		startActivityForResult(intent,LOGIN_REQUEST_CODE);
		return;
	}
	public void loadHeadImage()
	{
		if(!hasLogin())
		{
			circleImageView.setBackgroundResource(R.drawable.default_head);
			return;
		}
		
		File cachedir = MeFragment.this.getActivity().getExternalCacheDir();
		File savedir = new File(cachedir, "faceimages");
		File tempFile = new File(savedir,IMAGE_FILE_NAME);
		if(tempFile.exists())
		{
			Uri  uri = Uri.fromFile(tempFile);
			circleImageView.setImageURI(uri);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == LOGIN_REQUEST_CODE)
		{
			initEventByLogin();
			return;
		}
		if(data == null || resultCode == 0)
		{
			//Toast.makeText(getActivity(), "您取消了！",
			//		Toast.LENGTH_LONG).show();
			return;
		}
		//结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			
			case LOGIN_REQUEST_CODE:
				initEventByLogin();
				break;
				
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
				
			case CAMERA_REQUEST_CODE:
				if(data.hasExtra("data")){  
                    Bitmap thumbnail = data.getParcelableExtra("data");  
                    //得到bitmap后的操作
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(MeFragment.this.getActivity().getContentResolver(), thumbnail, null,null));
                    startPhotoZoom(uri);
                }  
				break;
				
			case RESULT_REQUEST_CODE:
				if (data != null) {
					saveImageToFileAndUpload(data);
					getImageToView(data);
				}
				break;
			}
		} 
		
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra("scale", true);
		// intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(photo);
			circleImageView.setImageDrawable(drawable);
		}
	}
	/**
	 * 保存裁剪之后的图片数据至文件
	 * 
	 * @param picdata
	 */
	public void DownloadAndSaveImageToFile()
	{
		String sUrl = "http://118.126.142.43/HealthWeb/Health/Service/GetUserFace.ashx";
		
		if(CGlobal.isHeadImageDownloaded)
		{
			return;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", CGlobal.m_User.token.toString());
		try {
			String sContent = CommUtils.doPost(sUrl, map);
			JSONObject jsonobj = JSONUtil.toObj(sContent);
			if(jsonobj == null)
				return;
			long Status = jsonobj.getLong("Status");
			if(Status == 1)
			{
				JSONArray entitys = jsonobj.getJSONArray("Ret");
				if(entitys!=null && entitys.length()>0)
				{
					JSONObject imginfo = entitys.getJSONObject(0);
					String imgurl = imginfo.getString("Url");
					
					File cachedir = MeFragment.this.getActivity().getExternalCacheDir();
					File savedir = new File(cachedir, "faceimages");
					if (!savedir.exists()) {
						savedir.mkdirs();
					}
					File tempFile = new File(savedir,IMAGE_FILE_NAME);
					
					CommUtils.doDownload(MeFragment.this.getActivity(), imgurl,
							tempFile.getAbsolutePath(), map, "jpg");
					
					CGlobal.isHeadImageDownloaded = true;
					
					MeFragment.this.getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() { 
							loadHeadImage();
						}
		            });
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void saveImageToFileAndUpload(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			File cachedir = MeFragment.this.getActivity().getExternalCacheDir();
			File savedir = new File(cachedir, "faceimages");
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
			File tempFile = new File(savedir,IMAGE_FILE_NAME);
			try 
			{
				FileOutputStream out = new FileOutputStream(tempFile);
				photo.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				Log.e("saveImageToFile", e.toString());
			}
			new UploadAsyncTask().execute(tempFile.getAbsolutePath(),CGlobal.m_User.Name);
		}
	}
	private class DownAsyncTask extends AsyncTask<String, Void, CUser> {

		@Override
		protected CUser doInBackground(String... params) {
			CGlobal.m_User.GetUserInfoMgr().Download(0);
			
			DownloadAndSaveImageToFile();
			
			return CGlobal.GetCtx().GetUserMgr()
					.SearchUserInfo(CGlobal.m_User.Id);
		};

		@Override
		protected void onPostExecute(CUser cuser) {
			if (cuser == null) {
				return;
			}
			initView();
		}
	}
	private class UploadAsyncTask extends AsyncTask<String, String, JSONObject> {

		CustomDialogCircle dialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new CustomDialogCircle(MeFragment.this.getActivity(),
					R.layout.h_dialog_layout_circle, R.style.DialogTheme);
			dialog.setTitle("正在上传头像，请稍候...");
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			String filePath = params[0];
			String CardNo = params[1];
			
			JSONObject jsonobj = null;
			
			String sUrl = "http://118.126.142.43/HealthWeb/Health/Service/PostUserFace.ashx";

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token", CGlobal.m_User.token.toString());
			map.put("CardNo", CardNo);
			try {
				String sContent = CommUtils.uploadFile(sUrl, filePath, map);
				if(sContent.equalsIgnoreCase("ok"))
				{
					jsonobj = new JSONObject();
					jsonobj.put("ret", "success");
				}
				//jsonobj = JSONUtil.toObj(sContent);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return jsonobj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dialog.dismiss();
			// 从result中获取到值，并且根据响应跳转界面 成功就跳转 失败不跳转
			if (result == null) {
				Toast.makeText(MeFragment.this.getActivity(), "网络访问异常！", Toast.LENGTH_LONG)
						.show();
				return;
			}
			String ret = result.optString("ret");
			if (ret.equals("success")) {
				Toast.makeText(MeFragment.this.getActivity(), "头像上传成功!",
						Toast.LENGTH_LONG).show();
			} 
			else {
				Toast.makeText(MeFragment.this.getActivity(), "网络访问异常!",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		initdata();
		initEvent();
	}
	public boolean hasLogin()
	{
		if(CGlobal.m_User == null || CGlobal.m_User.Name == null || CGlobal.m_User.Name.equals("") )
			return false;
		
		return true;
	}
}
