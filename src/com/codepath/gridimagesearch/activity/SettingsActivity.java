package com.codepath.gridimagesearch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.SearchOptions;

public class SettingsActivity extends Activity implements OnItemSelectedListener {
	private Spinner spColor;
	private Spinner spSize;
	private Spinner spType;
	private EditText etSite;
	private SearchOptions mOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		setupViews();

		ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this, R.array.colors,
				android.R.layout.simple_spinner_item);
		colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spColor.setAdapter(colorAdapter);
		spColor.setOnItemSelectedListener(this);

		ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.size,
				android.R.layout.simple_spinner_item);
		sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSize.setAdapter(sizeAdapter);
		spSize.setOnItemSelectedListener(this);

		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.type,
				android.R.layout.simple_spinner_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spType.setAdapter(typeAdapter);
		spType.setOnItemSelectedListener(this);

		// Initial values
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mOptions = (SearchOptions) bundle.get("options");
			if (mOptions.color != null) {
				spColor.setSelection(colorAdapter.getPosition(mOptions.color));
			}
			if (mOptions.size != null) {
				spSize.setSelection(sizeAdapter.getPosition(mOptions.size));
			}
			if (mOptions.type != null) {
				spType.setSelection(typeAdapter.getPosition(mOptions.type));
			}
			if (mOptions.site != null) {
				etSite.setText(mOptions.site);
			}
		} else {
			mOptions = new SearchOptions();
		}
	}

	private void setupViews() {
		spColor = (Spinner) findViewById(R.id.spColor);
		spSize = (Spinner) findViewById(R.id.spSize);
		spType = (Spinner) findViewById(R.id.spType);
		etSite = (EditText) findViewById(R.id.etSite);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	public void onOk(View v) {
		Intent i = new Intent();
		mOptions.site = etSite.getText().toString();
		i.putExtra("options", mOptions);
		setResult(0, i);
		finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		String selectedItem = (String) parent.getItemAtPosition(pos);
		if (selectedItem != "unset") {
			if (parent == spColor) {
				mOptions.color = selectedItem;
			} else if (parent == spSize) {
				mOptions.size = selectedItem;
			} else if (parent == spType) {
				mOptions.type = selectedItem;
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
}
