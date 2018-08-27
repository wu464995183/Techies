package ime.demo11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        findViewById(R.id.clickT).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        findViewById(R.id.clickT).setOnClickListener(this);
        findViewById(R.id.clickB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });



        getFragmentManager().beginTransaction().add(R.id.fc, new FragmentTest()).commit();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
