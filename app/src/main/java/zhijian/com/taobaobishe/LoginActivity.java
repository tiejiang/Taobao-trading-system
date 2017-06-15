package zhijian.com.taobaobishe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


import zhijian.com.taobaobishe.http.OkHttpHelper;
import zhijian.com.taobaobishe.utils.Utils;
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
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        HttpPost mPost = new HttpPost(Utils.primitiveUrl+"login.php");
        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        pairs.add(new BasicNameValuePair("username", account));
        pairs.add(new BasicNameValuePair("password", password));

        try {
            mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            HttpResponse response = mHttpClient.execute(mPost);
            int res = response.getStatusLine().getStatusCode();

            if (res == 200) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String info = EntityUtils.toString(entity);
                    System.out.println("服务端返回数据info-----------"+info);
                    //以下主要是对服务器端返回的数据进行解析
                    JSONObject jsonObject=null;
                    //flag为登录成功与否的标记,从服务器端返回的数据
                    String flag="";
                    String name="";
                    String userid="";
                    try {
                        jsonObject = new JSONObject(info);
                        flag = jsonObject.getString("flag");
                        name = jsonObject.getString("name");
                        userid = jsonObject.getString("userid");

                        System.out.println("falg:" + flag);
                        System.out.println("name:" + name);
                        System.out.println("userid:" + userid);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //根据服务器端返回的标记,判断服务端端验证是否成功
                    if(flag.equals("success")){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
