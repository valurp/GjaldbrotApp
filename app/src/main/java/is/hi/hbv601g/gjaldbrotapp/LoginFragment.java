package is.hi.hbv601g.gjaldbrotapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.User;
import is.hi.hbv601g.gjaldbrotapp.Services.HttpManager;

public class LoginFragment extends Fragment {

    LoginCallbacks mLoginCallback;

    EditText usernameField;
    EditText passwordField;
    View view;

    public LoginFragment() {
    }

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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameField = (EditText) view.findViewById(R.id.et_username);
        passwordField = (EditText) view.findViewById(R.id.et_password);

        Button button = (Button) view.findViewById(R.id.btn_login);
        button.setOnClickListener(new LoginListener());
        return view;
    }

    class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            if (username.length() == 0 || password.length() == 0) {
                Toast.makeText(view.getContext(), "Username or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            new LoginTask().execute(username, password);
        }
    }

    class LoginTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            try {
                return new HttpManager().fetchUser(params[0], params[1]);
            }
            catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(
                        getString(R.string.shared_preferences), Context.MODE_PRIVATE);
                sharedPreferences.edit()
                        .putString(getString(R.string.token_file_key), user.getToken())
                        .apply();
                mLoginCallback.onLogin();
            }
            else {
                Toast.makeText(view.getContext(), "Could not log in", Toast.LENGTH_SHORT).show();
            }
        }
    }
}