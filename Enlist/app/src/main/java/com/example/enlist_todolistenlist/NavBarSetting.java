package com.example.enlist_todolistenlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavBarSetting extends AppCompatActivity {

    ImageButton back_arrow_btn;

    private GoogleSignInClient mGoogleSignInClient;

    BottomNavigationView bottomNavigationView;
    TextView profile_textView,privacy_policy_textView,github_textView,log_out_textView,support_textView,about_textView;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NavBarSetting.this, StudentGroup.class));
        finish();
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_setting);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("***")
                .requestEmail()
                .build();

        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(NavBarSetting.this, gso);

        privacy_policy_textView = findViewById(R.id.privacy_policy_textView);
        about_textView = findViewById(R.id.about_textView);
        github_textView = findViewById(R.id.github_textView);
        log_out_textView = findViewById(R.id.logout_textView);
        support_textView = findViewById(R.id.support_textView);
        profile_textView = findViewById(R.id.profile_textView);

        back_arrow_btn = findViewById(R.id.back_arrow_btn);

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavBarSetting.this, StudentGroup.class));
                finish();
            }
        });

        privacy_policy_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://enlist-563ad.web.app/privacy.html";
                Intent url_intent = new Intent(Intent.ACTION_VIEW);
                url_intent.setData(Uri.parse(url));
                startActivity(url_intent);
            }
        });

        about_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://enlist-563ad.web.app/about.html";
                Intent url_intent = new Intent(Intent.ACTION_VIEW);
                url_intent.setData(Uri.parse(url));
                startActivity(url_intent);
            }
        });

        github_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/prakhar-agarwall/enlist";
                Intent url_intent = new Intent(Intent.ACTION_VIEW);
                url_intent.setData(Uri.parse(url));
                startActivity(url_intent);
            }
        });

        log_out_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NavBarSetting.this);
                builder.setMessage("Are you sure you want to log out?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mGoogleSignInClient.signOut();
                        Source.flag=0;
                        startActivity(new Intent(NavBarSetting.this, GoogleSignIn.class));
                        finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        support_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gmail_intent = new Intent(Intent.ACTION_SEND);
                gmail_intent.putExtra(Intent.EXTRA_EMAIL,new String[] { "enlist.feedback@gmail.com" });
                gmail_intent.putExtra(Intent.EXTRA_SUBJECT,"Suggest a feature/Report a bug");
                gmail_intent.setType("message/rfc822");
                startActivity(Intent.createChooser(gmail_intent, "Choose an email client"));
            }
        });

        profile_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavBarSetting.this,Profile.class));
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_setting);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_double_tick:
                        startActivity(new Intent(NavBarSetting.this,MainActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_setting:
                        return true;
                }
                return false;
            }
        });
    }
}