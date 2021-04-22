package is.hi.hbv601g.gjaldbrotapp.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import is.hi.hbv601g.gjaldbrotapp.Entities.User;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.UserService;

public class LoginFragment extends Fragment {

    LoginCallbacks mLoginCallback;

    EditText mUsernameField;
    EditText mPasswordField;
    View mView;

    public LoginFragment() { }

    // Callbacks til að LoginActivity geti byrjað NavigationActivity þegar notandi
    // loggar sig inn
    public interface LoginCallbacks {
        public void onLogin();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginCallback = (LoginCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginCallback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        mUsernameField = (EditText) mView.findViewById(R.id.login_et_username);
        mPasswordField = (EditText) mView.findViewById(R.id.login_et_password);

        Button button = (Button) mView.findViewById(R.id.login_btn_submit);
        button.setOnClickListener(new LoginListener());
        return mView;
    }

    // OnClickListener til að staðfesta innskráningu
    private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Sækjum gögnin úr viðmótinu
            String username = mUsernameField.getText().toString();
            String password = mPasswordField.getText().toString();

            if (username.length() == 0 || password.length() == 0) {
                Toast.makeText(mView.getContext(), "Username or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            new LoginTask().execute(username, password);
        }
    }

    // Innri klasi sem framkvæmir vefkall í öðrum þræði til að skrá notanda inn.
    // Vistar token í SharedPreferences ef innskráning virkaði, sýnir notanda annars villu.
    private class LoginTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            return UserService.getInstance().fetchUser(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                UserService.getInstance().setToken(user.getToken());
                SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(
                        getString(R.string.shared_preferences), Context.MODE_PRIVATE);
                sharedPreferences.edit()
                        .putString(getString(R.string.token_file_key), user.getToken())
                        .apply();
                mLoginCallback.onLogin();
            }
            else {
                Toast.makeText(mView.getContext(), "Could not log in", Toast.LENGTH_SHORT).show();
            }
        }
    }
}