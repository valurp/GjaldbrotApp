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

import is.hi.hbv601g.gjaldbrotapp.Services.UserService;

public class LoginActivity
        extends AppCompatActivity
        implements RegisterFragment.RegisterCallbacks,
        LoginFragment.LoginCallbacks {

    ViewPager viewPager; // Container fyrir login og register fragment

    @Override
    public void onRegister() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onLogin() {
        Intent intent = NavigationActivity.newIntent(this);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewPager = findViewById(R.id.login_viewPager);

        // Athuga eftir token í SharedPreferences, ef það er til gerum við ráð fyrir að
        // notandi sé skráður inn
        /** FOR DEV PURPOSES WE ALWAYS GO TO LOGIN SCREEN FIRST, MAYBE WE WANT A MORE COMPLEX CHECK
         * WITH LIKE A WEB CALL*/
        /*SharedPreferences sharedPreferences = viewPager.getContext()
                        .getSharedPreferences(
                                getString(R.string.shared_preferences),
                                Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(
                getString(R.string.token_file_key),
                null);
        if (token != null) {
            UserService.getInstance().setToken(token);
            onLogin();
            return;
        }
        */

        AuthenticationPagerAdapter pagerAdapter =
                new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);
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