package is.hi.hbv601g.gjaldbrotapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import is.hi.hbv601g.gjaldbrotapp.Services.HttpManager;
import is.hi.hbv601g.gjaldbrotapp.Services.UserService;

public class RegisterFragment extends Fragment {

    View view;
    EditText mUsernameField;
    EditText mPasswordField;
    EditText mReenterPasswordField;
    Button mRegisterButton;

    private RegisterCallbacks mCallbacks;

    public interface RegisterCallbacks {
        void onRegister();
    }

    public RegisterFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (RegisterCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);

        mUsernameField = (EditText) view.findViewById(R.id.et_name);
        mPasswordField = (EditText) view.findViewById(R.id.et_password);
        mReenterPasswordField = (EditText) view.findViewById(R.id.et_repassword);

        mRegisterButton = (Button) view.findViewById(R.id.btn_register);
        mRegisterButton.setOnClickListener(new RegisterListener());

        return view;
    }

    class RegisterListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String username = mUsernameField.getText().toString();
            String password = mPasswordField.getText().toString();
            String reenterPassword = mReenterPasswordField.getText().toString();

            if(username.length() == 0 || password.length() == 0 || reenterPassword.length() == 0) {
                Toast.makeText(view.getContext(), "No field can be left blank", Toast.LENGTH_SHORT);
                return;
            }
            if(!password.equals(reenterPassword)) {
                Toast.makeText(view.getContext(), "Password do not match", Toast.LENGTH_SHORT);
                return;
            }
            new RegisterTask().execute(username, password);
        }
    }

    class RegisterTask extends AsyncTask<String, Void, Boolean> {
        @Override
        public Boolean doInBackground(String... params) {
            Log.i("REGISTER CALL", params[0]+" "+params[1]);
            return new UserService().createUser(params[0], params[1]);
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                mCallbacks.onRegister();
            }
            else {
                Toast.makeText(view.getContext(), "Error creating user", Toast.LENGTH_SHORT);
            }
        }
    }
}