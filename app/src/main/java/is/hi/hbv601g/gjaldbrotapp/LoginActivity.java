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

    ViewPager loginViewPager; // Container fyrir login og register fragment

    @Override
    public void onRegister() {
        loginViewPager.setCurrentItem(0);
    }

    @Override
    public void onLogin() {
        Intent intent = NavigationActivity.newIntent(this);
        startActivity(intent); // TODO láta navigationActivity vera neðst á stakknum
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewPager = findViewById(R.id.loginViewPager);

        // Athuga eftir token í SharedPreferences, ef það er til gerum við ráð fyrir að
        // notandi sé skráður inn
        /** FOR DEV PURPOSES WE ALWAYS GO TO LOGIN SCREEN FIRST, MAYBE WE WANT A MORE COMPLEX CHECK
         * WITH LIKE A WEB CALL*/
        SharedPreferences sharedPreferences = loginViewPager.getContext()
                        .getSharedPreferences(
                                getString(R.string.shared_preferences),
                                Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(
                getString(R.string.token_file_key),
                null);
        if (token != null) {
            Intent intent = NavigationActivity.newIntent(this);
            startActivity(intent); //TODO láta NavigationActivity vera neðst á activity stakknum
        }


        AuthenticationPagerAdapter pagerAdapter =
                new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        loginViewPager.setAdapter(pagerAdapter);
    }

    // Innri klasi sem að heldur utan um hvaða fragment er í focus.
    private class AuthenticationPagerAdapter extends FragmentPagerAdapter {
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

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}