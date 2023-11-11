package kg.printer.kkm.view.old;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.view.UserActivity;

public class BasicPassFragment extends DialogFragment implements View.OnClickListener {

    private String password;

    private EditText etPassword, etConfirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.old_dialog_pass, null));

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        etPassword = getDialog().findViewById(R.id.et_password);
        etConfirmPassword = getDialog().findViewById(R.id.et_confirm_password);

        Button btnOk = getDialog().findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        etPassword.setText(password);
        etConfirmPassword.setText(password);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            if (etPassword.getText().toString().equals(confirmPassword)) {
                getDialog().cancel();
            }
        }
    }

}
