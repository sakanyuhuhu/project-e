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
import android.widget.TextView;
import android.widget.Toast;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.AdministrationActivity;
import th.ac.mahidol.rama.emam.activity.DoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.LoginUserCenterActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;

public class BuildLoginCenterFragment extends Fragment implements View.OnClickListener {
    private TextView tvStatusLogin;
    private EditText edUsername, edPassword;
    private Button btnLogin, btnCancel;
    private String wardID, sdlocID, wardName, time, login;
    private int timeposition;


    public BuildLoginCenterFragment() {
        super();
    }

    public static BuildLoginCenterFragment newInstance(String wardID, String sdlocID, String wardName, int timeposition, String time, String login) {
        BuildLoginCenterFragment fragment = new BuildLoginCenterFragment();
        Bundle args = new Bundle();
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
        args.putString("login", login);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_center_login, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        wardID = getArguments().getString("wardId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("position");
        time = getArguments().getString("time");
        login = getArguments().getString("login");

        tvStatusLogin = (TextView) rootView.findViewById(R.id.tvStatusLogin);
        edUsername = (EditText) rootView.findViewById(R.id.edUsername);
        edPassword = (EditText) rootView.findViewById(R.id.edPassword);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);

        if(login != null) {
            if (login.equals("Preparation")) {
                tvStatusLogin.setText("กรุณาลงชื่อเข้าใช้เพื่อเตรียมยา");
            } else if (login.equals("DoubleCheck")) {
                tvStatusLogin.setText("กรุณาลงชื่อเข้าใช้เพื่อตรวจสอบการเตรียมยา");
            } else if (login.equals("Administration")) {
                tvStatusLogin.setText("กรุณาลงชื่อเข้าใช้เพื่อบริหารยา");
            }
        }

        btnLogin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            if (!(edUsername.getText().toString().equals("") | edPassword.getText().toString().equals(""))) {
                Intent intent = new Intent(getContext(), LoginUserCenterActivity.class);
                intent.putExtra("username", edUsername.getText().toString());
                intent.putExtra("password", edPassword.getText().toString());
                intent.putExtra("wardId", wardID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("position", timeposition);
                intent.putExtra("time", time);
                intent.putExtra("login", login);
                getActivity().startActivity(intent);
                getActivity().finish();
            } else
                Toast.makeText(getActivity(), "กรุณาใส่ Username และ Password ให้ถูกต้อง", Toast.LENGTH_LONG).show();
        } else if (view.getId() == R.id.btnCancel) {
            if (login.equals("Preparation")) {
                Intent intent = new Intent(getContext(), PreparationActivity.class);
                intent.putExtra("wardId", wardID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("position", timeposition);
                intent.putExtra("time", time);
                getActivity().startActivity(intent);
                getActivity().finish();
            } else if (login.equals("DoubleCheck")) {
                Intent intent = new Intent(getContext(), DoubleCheckActivity.class);
                intent.putExtra("wardId", wardID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("position", timeposition);
                intent.putExtra("time", time);
                getActivity().startActivity(intent);
                getActivity().finish();
            } else if (login.equals("Administration")) {
                Intent intent = new Intent(getContext(), AdministrationActivity.class);
                intent.putExtra("wardId", wardID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("position", timeposition);
                intent.putExtra("time", time);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        }
    }
}
