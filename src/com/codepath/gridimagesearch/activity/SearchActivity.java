package com.codepath.gridimagesearch.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.gridimagesearch.EndlessScrollListener;
import com.codepath.gridimagesearch.ImageResult;
import com.codepath.gridimagesearch.ImageResultArrayAdapter;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.SearchOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	private static int RESULTS_PER_PAGE = 8;

	private EditText etQuery;
	private GridView gvResults;
	private List<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageAdapter;
	private SearchOptions mSearchOptions;
	private String mCurrentQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadImages(mCurrentQuery, page - 1);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}

	public void onImageSearch(View v) {
		mCurrentQuery = etQuery.getText().toString();
		Toast.makeText(this, "Searching for " + mCurrentQuery, Toast.LENGTH_SHORT).show();
		loadImages(mCurrentQuery, 0);
	}

	public void onSettingsItemSelected(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
		if (mSearchOptions != null) {
			i.putExtra("options", mSearchOptions);
		}
		startActivityForResult(i, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			SearchOptions searchOptions = (SearchOptions) data.getExtras().get("options");
			if (searchOptions != null) {
				mSearchOptions = searchOptions;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void loadImages(String query, final int page) {
		String targetUrl = "https://ajax.googleapis.com/ajax/services/search/images?" + "start=" + RESULTS_PER_PAGE * page + "&v=1.0&rsz=" + RESULTS_PER_PAGE;
		if (mSearchOptions != null) {
			String size = mSearchOptions.size;
			String color = mSearchOptions.color;
			String type = mSearchOptions.type;
			String site = mSearchOptions.site;
			if (size != null) {
				targetUrl += "&imgsz=" + size;
			}
			if (color != null) {
				targetUrl += "&imgcolor=" + color;
			}
			if (type != null) {
				targetUrl += "&imgtype=" + type;
			}
			if (site != null) {
				targetUrl += "&as_sitesearch=" + site;
			}
		}
		targetUrl += "&q=" + Uri.encode(query);
		Log.d("SearchActivity", "targetUrl=" + targetUrl);

		// https://ajax.googleapis.com/ajax/services/search/images?imgcolor=black&imgsz=large&rsz=8&start=0&v=1.0&q=cars"
		if (page == 0) {
			imageAdapter.clear();
		}

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(targetUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
