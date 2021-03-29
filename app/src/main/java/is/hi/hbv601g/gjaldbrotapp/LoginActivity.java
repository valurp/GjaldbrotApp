package is.hi.hbv601g.gjaldbrotapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class LoginActivity
        extends AppCompatActivity
        implements RegisterFragment.RegisterCallbacks,
        LoginFragment.LoginCallbacks {

    private TextView mTextView;

    @Override
    public void onRegister() {
        // todo change fragment to be LoginFragment
    }

    @Override
    public void onLogin() {
        Intent intent = NavigationActivity.newIntent(this);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewPager loginViewPager = findViewById(R.id.loginViewPager);
        /*Við geymum token í sharedPreferences og ef að það er strengur vistaður þar nú þegar viljum
         við fara beint í NavigationActivity*/

        /** FOR DEV PURPOSES WE ALWAYS GO TO LOGIN SCREEN FIRST, MAYBE WE WANT A MORE COMPLEX CHECK
         * WITH LIKE A WEB CALL
        SharedPreferences sharedPreferences =
                loginViewPager.getContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.token_file_key), null);
        if (token != null) {
            Intent intent = NavigationActivity.newIntent(this);
            startActivity(intent);
        }*/

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragmet(new LoginFragment());
        pagerAdapter.addFragmet(new RegisterFragment());
        loginViewPager.setAdapter(pagerAdapter);
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}