package com.avv.mymenunfc.view.dialog;

import com.avv.mymenunfc.R;
import com.avv.mymenunfc.R.id;
import com.avv.mymenunfc.R.layout;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class InfoDialog extends DialogFragment {

	private TextView textView;
	private final String text;
	private Button bButton;
	private final String textButton;

	public InfoDialog(String text, String textButton) {
		this.text = text;
		this.textButton = textButton;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.getDialog().setTitle("Situación Pedido");
		View view = inflater.inflate(R.layout.order_dialog, container, false);

		this.textView = (TextView) view.findViewById(R.id.textInfo);
		this.textView.setText(this.text);

		this.bButton = (Button) view.findViewById(R.id.bAceptar);
		this.bButton.setText(this.textButton);
		this.bButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InfoDialog.this.getDialog().dismiss();
			}
		});

		return view;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(savedInstanceState);
	}

}
