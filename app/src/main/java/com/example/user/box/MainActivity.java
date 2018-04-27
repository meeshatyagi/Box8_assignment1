package com.example.user.box;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    ViewPager mViewPager;
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private static DatabaseReference rootRef;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference();
        rootRef = mFirebaseDatabase.child("Users");


        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        for (int i = 0; i < 2; i++) {
            if(i==0) {
                actionBar.addTab(
                        actionBar.newTab()
                                .setText("Sign Up " + (i + 1))
                                .setTabListener(this));
            }
            else
            {
                actionBar.addTab(
                        actionBar.newTab()
                                .setText("Log In " + (i + 1))
                                .setTabListener(this));
            }
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }


    public class DemoCollectionPagerAdapter extends FragmentPagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SignUpFragment();

                case 1:
                    Fragment fragment = new DemoObjectFragment();
                    return fragment;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position);
        }
    }

    public static class SignUpFragment extends Fragment {

        static private EditText inputEmail, inputPassword;
        static private Button btnSignIn, btnSignUp, btnResetPassword;
        static private ProgressBar progressBar;
        static private FirebaseAuth auth;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
            View view = inflater.inflate(R.layout.layout_one, container, false);

            auth = FirebaseAuth.getInstance();

            btnSignUp = (Button) view.findViewById(R.id.sign_up_button);
            inputEmail = (EditText) view.findViewById(R.id.email);
            inputPassword = (EditText) view.findViewById(R.id.password);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.length() < 6) {
                        Toast.makeText(getActivity().getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(getActivity(), "User Registered Successfully.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Authentication Failed.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        getActivity().finish();
                                    }
                                }
                            });
                }
            });
            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            progressBar.setVisibility(View.GONE);
        }
    }


    public static class DemoObjectFragment extends Fragment {

        private EditText inputEmail, inputPassword;
        private FirebaseAuth auth;
        private ProgressBar progressBar;
        private Button btnLogin;

        @Override
        public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstance) {
            View rootView = inflator.inflate(R.layout.layout_two, container, false);

            auth = FirebaseAuth.getInstance();

            inputEmail = (EditText) rootView.findViewById(R.id.email);
            inputPassword = (EditText) rootView.findViewById(R.id.password);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            btnLogin = (Button) rootView.findViewById(R.id.btn_login);

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();


            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = inputEmail.getText().toString();
                    final String password = inputPassword.getText().toString();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(getActivity(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        getActivity().finish();
                                    }
                                }
                            });
                }
            });


            return rootView;


        }
    }
}