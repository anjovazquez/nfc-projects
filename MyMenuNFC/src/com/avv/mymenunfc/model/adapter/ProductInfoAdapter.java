package com.avv.mymenunfc.model.adapter;

import java.util.ArrayList;

import com.avv.mymenunfc.model.vo.ProductInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductInfoAdapter extends ArrayAdapter<ProductInfo> {

	private final Context context;
	private final ArrayList<ProductInfo> travels;
	private static final int RESOURCE = android.R.layout.simple_list_item_2;

	public ProductInfoAdapter(Context context, ArrayList<ProductInfo> travels) {
		super(context, RESOURCE, travels);

		this.context = context;
		this.travels = travels;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout view;
		ViewHolder holder;

		if (convertView == null) {
			view = new LinearLayout(this.context);

			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(RESOURCE, view, true);

			holder = new ViewHolder();
			holder.text1 = (TextView) view.findViewById(android.R.id.text1);
			holder.text2 = (TextView) view.findViewById(android.R.id.text2);
			view.setTag(holder);

		} else {
			view = (LinearLayout) convertView;
			holder = (ViewHolder) view.getTag();
		}

		// Rellenamos la vista con los datos
		ProductInfo info = this.travels.get(position);
		holder.text1.setText(info.getName());
		holder.text2.setText(String.valueOf(info.getPrice()));

		return view;
	}

	static class ViewHolder {
		TextView text1;
		TextView text2;
	}
}
