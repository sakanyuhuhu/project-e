package th.ac.mahidol.rama.emam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.LoginUserActivity;

public class BuildLoginFragment extends Fragment{
    private EditText edUsername, edPassword;
    private Button btnLogin;
    private String sdlocID, wardName;


    public BuildLoginFragment() {
        super();
    }

    public static BuildLoginFragment newInstance(String sdlocID, String wardName) {
        BuildLoginFragment fragment = new BuildLoginFragment();
        Bundle args = new Bundle();
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        edUsername = (EditText) rootView.findViewById(R.id.edUsername);
        edPassword = (EditText) rootView.findViewById(R.id.edPassword);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(edUsername.getText().toString().equals("") | edPassword.getText().toString().equals(""))){
                    Intent intent = new Intent(getContext(), LoginUserActivity.class);
                    intent.putExtra("username", edUsername.getText().toString());
                    intent.putExtra("password", edPassword.getText().toString());
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
                else
                    Toast.makeText(getActivity(), "กรุณาใส่ Username และ Password ให้ถูกต้อง", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }



}
