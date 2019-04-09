package e.jin.last;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_study:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dictionary:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav=findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StudyFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                  @Override
                  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                      Fragment selectedFragment = null; //처음에 선택된 액티비티 x

                      switch(item.getItemId()){
                          case R.id.navigation_study:
                              selectedFragment=new StudyFragment();
                              break;
                          case R.id.navigation_dictionary:
                              selectedFragment=new DictionaryFragment();
                              break;
                          case R.id.navigation_notifications:
                              selectedFragment=new NotificationsFragment();
                      }
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                      return true;
                    }
                };
}
