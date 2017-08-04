package com.example.hosen.googlelogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private TextView Name,Email;
    Button btLogout;
    SignInButton signInBt;
    ImageView prof_pic;
    GoogleApiClient googleApiClient;
    private ProgressDialog dialog;
    LinearLayout prof_section;
    public static final int REQ_CODE=9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Name=(TextView)findViewById(R.id.nameTv);
        Email=(TextView)findViewById(R.id.emailTv);
        btLogout=(Button)findViewById(R.id.logoutBt);
        prof_pic=(ImageView)findViewById(R.id.prof_pic);
        signInBt=(SignInButton)findViewById(R.id.btLogin);
        prof_section=(LinearLayout)findViewById(R.id.prof_section);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait!!");
        dialog.setCancelable(false);
        prof_section.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions ).build();
        signInBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i,REQ_CODE);
            }
        });
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUi(false);
                    }
                });
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("faildConnection",connectionResult.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);


            Log.i("googleApi",result.toString());
             handleRequest(result);
        }
        else
        Log.i("googleApi",resultCode+"");

    }
    public void handleRequest(GoogleSignInResult result){
        if(result.isSuccess())
        {
            GoogleSignInAccount account=result.getSignInAccount();
            String name=account.getDisplayName().toString();
            String email=account.getEmail().toString();
            String imgUrl=account.getPhotoUrl().toString();
            Glide.with(this).load(imgUrl).into(prof_pic);
            Name.setText(name);
            Email.setText(email);
            updateUi(true);
        }

    }
    public void updateUi(boolean isLogin)
    {
        if(isLogin)
        {
            prof_section.setVisibility(View.VISIBLE);
            signInBt.setVisibility(View.GONE);
        }
        else{
            prof_section.setVisibility(View.GONE);
            signInBt.setVisibility(View.VISIBLE);
        }
    }
}
