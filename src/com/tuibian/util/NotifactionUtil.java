package com.tuibian.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotifactionUtil {

	public static void showNotifaction(Context context, Class<?> cls,
			int drawable, String title, String mContentMsg) {

		Notification notification = new Notification(drawable, mContentMsg,
				System.currentTimeMillis());

		Intent openintent = new Intent(context, cls);
		PendingIntent contentIntent = PendingIntent.getActivity(
				context.getApplicationContext(), 0, openintent, 0);
		notification.setLatestEventInfo(context.getApplicationContext(), title,
				mContentMsg, contentIntent);

		NotificationManager noTify = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		noTify.notify(0, notification);

	}
}
