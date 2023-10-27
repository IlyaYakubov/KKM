package kg.printer.kkm.view.old;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;

public class BasicPassFragment extends DialogFragment implements View.OnClickListener {

    private String password, confirmPassword;

    private EditText etPassword, etConfirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public EditText getEtPassword() {
        return etPassword;
    }

    public void setEtPassword(EditText etPassword) {
        this.etPassword = etPassword;
    }

    public EditText getEtConfirmPassword() {
        return etConfirmPassword;
    }

    public void setEtConfirmPassword(EditText etConfirmPassword) {
        this.etConfirmPassword = etConfirmPassword;
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

        Button btn_ok = getDialog().findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        etPassword.setText(password);
        etConfirmPassword.setText(password);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            password = etPassword.getText().toString();
            confirmPassword = etConfirmPassword.getText().toString();
            if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                getDialog().dismiss();
            } else {
                UIViewController.ToastAdapter.showLong(getActivity(), "Не совпадает подтверждение пароля");
            }
        }
    }

}
