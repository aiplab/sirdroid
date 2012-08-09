package edu.uprm.aiplab;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity {
	private int record_state = 0;
	private Microphone mic;
	private SirlabAndroid sirlab;
	private ImageAdapter ia;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Log.v("REC","INIT...");
		// TEXT
		TextView tv = (TextView) findViewById(R.id.textview1);
		// ScrollView sv = (ScrollView) findViewById(R.id.scrollView1);
		tv.setText("Press Record");
		// sv.setVisibility(ScrollView.GONE);
		// setContentView(tv);

		// BUTTON
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				if (record_state == 0)
					do_start();
				else
					do_stop();
			}
		});

	}

	private void do_record() {
		mic = new Microphone();
		mic.start();
	}

	private void do_start() {
		Toast.makeText(TestActivity.this, "Recording...", Toast.LENGTH_SHORT)
				.show();
		TextView tv = (TextView) findViewById(R.id.textview1);
		tv.setText("Recording...");
		Button button = (Button) findViewById(R.id.button1);
		button.setText("STOP!!");
		record_state = 1;
		do_record();
	}

	private void do_stop() {
		mic.finish();
		TextView tv = (TextView) findViewById(R.id.textview1);
		tv.setText("Stoping...");
		while (!mic.getEnd()) {
			Thread.yield();
		}
		Toast.makeText(TestActivity.this, "Sending...", Toast.LENGTH_SHORT)
				.show();
		// mic.notify();
		Button button = (Button) findViewById(R.id.button1);
		button.setText("Record");
		button.setEnabled(false);
		/*
		 * try { mic.join(); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		do_process();
		// mic.stop();
	}

	
	private void do_process() {

		// Create a new HttpClient and Post Header

		TextView tv = (TextView) findViewById(R.id.textview1);
		tv.setText("Sending and processing...");
		sirlab = new SirlabAndroid(mic.getWav());
		sirlab.send();

		List<String> ls = sirlab.getImages();
		// GALLERY

		TextView tv2 = (TextView) findViewById(R.id.textview2);
		if (ls != null) {
			Gallery gallery = (Gallery) findViewById(R.id.gallery1);
			ia = new ImageAdapter(this, ls);
			gallery.setAdapter(ia);
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Toast.makeText(TestActivity.this, "" + position,
							Toast.LENGTH_SHORT).show();
				}
			});
			tv.setText(String.valueOf(ls.size() + " Images"));
			tv2.setText(sirlab.getLog().trim());
			tv2.scrollTo(0, 0);
		} else {
			tv.setText(String.valueOf(0 + " Images"));
			tv2.setText(sirlab.getResult().trim());
		}

		// gallery.setVisibility(0);

		Button button = (Button) findViewById(R.id.button1);
		button.setEnabled(true);
		record_state = 0;
	}
}
