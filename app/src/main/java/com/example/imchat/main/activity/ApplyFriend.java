package com.example.imchat.main.activity;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class ApplyFriend extends WearableActivity {

	private TextView mTextView;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_friend);

		mTextView = (TextView) findViewById(R.id.text);

		// Enables Always-on
		setAmbientEnabled();
	}
}