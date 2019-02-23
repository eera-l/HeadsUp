package cloud.headsup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

   private static final int BG_INTENT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Heads up is running. Press the home button to interact with your other apps.", Toast.LENGTH_LONG).show();
        Intent mServiceIntent = new Intent();

        BackgroundIntent.enqueueWork(this, BackgroundIntent.class, BG_INTENT, mServiceIntent);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
