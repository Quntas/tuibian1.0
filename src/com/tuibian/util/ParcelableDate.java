package com.tuibian.util;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableDate implements Parcelable { // 声明实现接口Parcelable

	// 这里定义了两个变量来说明读和写的顺序要一致
	public long mId;
	public Date mDate;

	public ParcelableDate(long id, long time) {
		mId = id;
		mDate = new Date(time);
	}

	public ParcelableDate(Parcel source) {
		// 先读取mId，再读取mDate
		mId = source.readLong();
		mDate = new Date(source.readLong());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// 实现Parcelable的方法writeToParcel，将ParcelableDate序列化为一个Parcel对象
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// 先写入mId，再写入mDate
		dest.writeLong(mId);
		dest.writeLong(mDate.getTime());
	}

	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Parcelable.Creator<ParcelableDate> CREATOR = new Creator<ParcelableDate>() {

		@Override
		public ParcelableDate[] newArray(int size) {
			return new ParcelableDate[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public ParcelableDate createFromParcel(Parcel source) {
			return new ParcelableDate(source);
		}
	};
}
