package com.example.dung.togetherfinal11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dung.togetherfinal11.Config.ConfigsApi;
import com.example.dung.togetherfinal11.Config.ModelManager;
import com.example.dung.togetherfinal11.Interface.ModelManagerListener;
import com.example.dung.togetherfinal11.SharedPreferences.AppPreference;

import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    EditText _input_name ,_passwordText ;
    Button _loginButton;
    private AppPreference preference;
    TextView _signupLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        _input_name = (EditText) findViewById(R.id.input_name);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_signup);
        _signupLink = (TextView) findViewById(R.id.link_login);
        setLogin();
    }
    private  void setLogin(){
        preference = new AppPreference(Login.this);
        final String username = preference.getUsername();
        final String password = preference.getPassword();
        Log.d("Login","result" + username+"/"+password);
        if (!preference.getUsername().equals("") && !preference.getPassword().equals("")){
            ModelManager.getInstance(getApplication()).login(ConfigsApi.LOGIN_URL, preference.getUsername(), preference.getPassword(), new ModelManagerListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    preference.putUsername(username);
                    preference.putPassword(password);
                    Intent intent = new Intent(getApplication(),Navigation.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onError(String error) {

                }
            });
        }else {
           seteventLogin();
        }
    }

    private  void seteventLogin(){
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

    }
    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }else {
            String userName = _input_name.getText().toString();
            String passWord = _passwordText.getText().toString();
            preference.putUsername(userName);
            preference.putPassword(passWord);
            ModelManager.getInstance(getApplication()).login(ConfigsApi.LOGIN_URL, userName, passWord, new ModelManagerListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    _loginButton.setEnabled(false);
                    Intent intent = new Intent(getApplication(),Navigation.class);
                    startActivity(intent);
                    final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    // TODO: Implement your own authentication logic here.
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onLoginSuccess();
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;
        String password = _passwordText.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
}
