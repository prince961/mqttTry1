package com.example.mohit.mqtttry1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    private Button BLogin;
    private SignInButton gButton;
    private EditText ETUsername;
    private EditText ETPassword;
    private TextView TVRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    GoogleApiClient googleApiClient ;
    private final static int RC_SIGN_IN = 2;
    GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        gButton = (SignInButton) findViewById(R.id.googleSign);
        gButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("ClickListner", "button pressed");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");

                myRef.setValue("Hello, World!");
                Log.w("ClickListner", "Data sent");


            }
        });

       /* progressDialog = new ProgressDialog(this);
        BLogin = (Button) findViewById(R.id.Loginbutton);
        ETUsername = (EditText) findViewById(R.id.userIdET);
        ETPassword = (EditText) findViewById(R.id.passwordET);
        TVRegister = (TextView) findViewById(R.id.RegisterText); */

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient =GoogleSignIn.getClient(this,gso);
    }



    public void LoginButton(View view) {
        Log.w("TAG", "button pressed");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            String Name = account.getEmail();
            Log.w("Email ID", Name);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            //check if the user is old or new

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }


    public void RegisterText(View view) {
        //register user
        String username = ETUsername.getText().toString().trim();
        String password = ETPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)){
            //username is empty
            Log.e("loginLog", "email is empty" );
            Toast.makeText(this,"please enter Username",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            //username is empty
            Log.e("loginLog", "password is empty" );
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
            //stopping the function to execute function
            return;
        }

        //this code will run after the textfields are validated\
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

    }

    public void GoogleSign(View view) {
        Log.w("TAG", "button pressed");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
