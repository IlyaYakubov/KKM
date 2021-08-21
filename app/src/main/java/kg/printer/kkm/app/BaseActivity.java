package kg.printer.kkm.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    public abstract void initView();

    public abstract void addListener();

    public abstract void init();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void turnToActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
    }

    public void turnToActivityWithPosition(Class<?> cls, int position_on_list, int new_element) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("position_on_list", position_on_list);
        intent.putExtra("new_element", new_element);
        startActivity(intent);
    }

    public void turnToActivityWithUser(Class<?> cls, String position, String surname, String name, String secondName) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("position", position);
        intent.putExtra("surname", surname);
        intent.putExtra("name", name);
        intent.putExtra("secondName", secondName);
        startActivity(intent);
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog == null){
                    progressDialog = new ProgressDialog(BaseActivity.this);
                }
                if(!TextUtils.isEmpty(str)){
                    progressDialog.setMessage(str);
                }else{
                    progressDialog.setMessage("Загрузка...");
                }
                progressDialog.show();
            }
        });

    }

    public void hideProgressDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.hide();
                }
            }
        });
    }

}