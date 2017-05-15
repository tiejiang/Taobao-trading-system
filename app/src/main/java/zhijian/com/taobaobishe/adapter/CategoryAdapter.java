package zhijian.com.taobaobishe.adapter;

import android.content.Context;

import java.util.List;

import zhijian.com.taobaobishe.R;
import zhijian.com.taobaobishe.bean.Category;

public class CategoryAdapter extends SimpleAdapter<Category> {


    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {


        viewHoder.getTextView(R.id.textView).setText(item.getName());

    }
}
