package zhijian.com.taobaobishe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zhijian.com.taobaobishe.bean.Order;
import zhijian.com.taobaobishe.http.OkHttpHelper;
import zhijian.com.taobaobishe.http.SpotsCallBack;
import zhijian.com.taobaobishe.widget.CNiaoToolBar;
import zhijian.com.taobaobishe.widget.ClearEditText;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;
    private EditText userEditText, pwdEditText;
    private String username, pwd;
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;
    private Button mLoginButton ;
    private boolean isConnect;
    public static String primitiveUrl = "http://192.168.10.241/web/php/"+"login.php";


    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        initToolBar();
        userEditText = (EditText)findViewById(R.id.etxt_phone);
        pwdEditText = (EditText)findViewById(R.id.etxt_pwd);
        mLoginButton = (Button)findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(this);

        //检查网络状态
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet){
            //执行相关操作
            isConnect = true;
        }else{
            Toast.makeText(getApplicationContext(),  "请检查网络连接", Toast.LENGTH_LONG).show();
            isConnect = false;
        }

        findViewById(R.id.txt_toReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this,RegActivity.class));  //进入注册解界面
            }
        });
    }


    private void initToolBar(){


        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public void onClick(View v) {
        if (isConnect) {
            if (validate()) {
                if (!queryForHttpPost(username, pwd)) {
                    Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                }else {
//                    saveUserMsg(username, pwd);
                    //登陆条件符合 进入主界面
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提醒")
                    .setMessage("请检查是否连接网络")
                    .setPositiveButton("确定", null);
            builder.setCancelable(false);
            builder.create()
                    .show();
        }
    }
    // 验证方法  输入是否为空
    private boolean validate(){
        username = userEditText.getText().toString().trim();
        if(username.equals("")){
            Toast.makeText(LoginActivity.this, "用户名称是必填项！", Toast.LENGTH_SHORT).show();
            return false;
        }
        pwd = pwdEditText.getText().toString().trim();
        if(pwd.equals("")){
            Toast.makeText(LoginActivity.this, "用户密码是必填项!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // 请求服务端 获得响应
    private boolean queryForHttpPost(String account, String password){
        boolean isSuccess = false;
//        Long userId = CniaoApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();
        Log.d("TIEJIANG", "account= " + account + " password= " + password);
        params.put("username",account);
        params.put("password",password);


        okHttpHelper.post(primitiveUrl, params, new SpotsCallBack<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
//                showOrders(orders);
                Log.d("TIEJIANG", "onSuccess response: "+response.toString());
                Log.d("TIEJIANG", "orders= " + orders.get(0));
                if (response.toString().contains("success")){
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
//                    isSuccess = true;
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.d("TIEJIANG", "onError response: "+response.toString());
                Log.d("TIEJIANG", "code: "+code);
                if (response.toString().contains("error")){
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
//                    isSuccess = false;
                }
            }
        });
        return isSuccess;
    }
}
