package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
                .requestIdToken(getString(R.string.default_web_client_id))
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
                Toast.makeText(NavBarSetting.this, "Privacy Policy", Toast.LENGTH_SHORT).show();
            }
        });

        about_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavBarSetting.this, "About Us", Toast.LENGTH_SHORT).show();
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
                mGoogleSignInClient.signOut();
                Source.flag=0;
                Toast.makeText(NavBarSetting.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NavBarSetting.this, GoogleSignIn.class));
                finish();
            }
        });

        support_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavBarSetting.this, "Support", Toast.LENGTH_SHORT).show();
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