package zhijian.com.taobaobishe.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import zhijian.com.taobaobishe.AddressListActivity;
import zhijian.com.taobaobishe.CniaoApplication;
import zhijian.com.taobaobishe.Contants;
import zhijian.com.taobaobishe.LoginActivity;
import zhijian.com.taobaobishe.MyFavoriteActivity;
import zhijian.com.taobaobishe.MyOrderActivity;
import zhijian.com.taobaobishe.R;
import zhijian.com.taobaobishe.bean.User;
import de.hdodenhof.circleimageview.CircleImageView;


public class MineFragment extends BaseFragment{



    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void init() {

        showUser();
    }


    private  void showUser(){

        User user = CniaoApplication.getInstance().getUser();
//        if(user ==null){
        if(user == null){
            mbtnLogout.setVisibility(View.GONE);
            mTxtUserName.setText(R.string.to_login);

        }
        else{

            mbtnLogout.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(user.getLogo_url()))
              Picasso.with(getActivity()).load(Uri.parse(user.getLogo_url())).into(mImageHead);

            mTxtUserName.setText(user.getUsername());

        }

    }


    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLoginActivity(View view){


        Intent intent = new Intent(getActivity(), LoginActivity.class);

        startActivityForResult(intent, Contants.REQUEST_CODE);

    }

    @OnClick(R.id.txt_my_orders)
    public void toMyOrderActivity(View view){

        startActivity(new Intent(getActivity(), MyOrderActivity.class),true);
    }


    @OnClick(R.id.txt_my_address)
    public void toAddressActivity(View view){

        startActivity(new Intent(getActivity(), AddressListActivity.class),true);
    }

 @OnClick(R.id.txt_my_favorite)
    public void toFavoriteActivity(View view){

        startActivity(new Intent(getActivity(), MyFavoriteActivity.class),true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        showUser();
    }
}
