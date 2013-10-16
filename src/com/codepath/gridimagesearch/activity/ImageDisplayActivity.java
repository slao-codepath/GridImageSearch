package com.codepath.gridimagesearch.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.gridimagesearch.ImageResult;
import com.codepath.gridimagesearch.R;
import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {
	SmartImageView ivImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
		ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(result.getFullUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
	}

	private File saveBitmap(Bitmap bmp) {
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		OutputStream outStream = null;
		File file = new File(extStorageDirectory, "tmp.png");
		if (file.exists()) {
			file.delete();
			file = new File(extStorageDirectory, "tmp.png");
		}

		try {
			file.createNewFile();
			outStream = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	public void onShare(MenuItem mi) {
		ivImage.setDrawingCacheEnabled(true);
		Bitmap bitmap = ivImage.getDrawingCache();
		File file = saveBitmap(bitmap);

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		shareIntent.setType("image/jpeg");
		startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
	}
}
