package kg.printer.kkm.controllers;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kg.printer.kkm.R;

public class BasicPassController extends DialogFragment implements View.OnClickListener {

    public String password, confirmPassword;
    public EditText etPassword, etConfirmPassword;
    private Button btn_ok;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_pass, null));

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        etPassword = getDialog().findViewById(R.id.et_password);
        etConfirmPassword = getDialog().findViewById(R.id.et_confirm_password);

        btn_ok = getDialog().findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        etPassword.setText(password);
        etConfirmPassword.setText(password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
                if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    getDialog().dismiss();
                } else {
                    ToastController.showLong(getActivity(), "Не совпадает подтверждение пароля");
                }
                break;
        }
    }

}
