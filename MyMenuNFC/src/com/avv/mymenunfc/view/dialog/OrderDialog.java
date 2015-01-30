package com.avv.mymenunfc.view.dialog;

import com.avv.mymenunfc.R;
import com.avv.mymenunfc.R.id;
import com.avv.mymenunfc.R.layout;
import com.avv.mymenunfc.view.listeners.OnAcceptListener;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class OrderDialog extends DialogFragment {

	private TextView textView;
	private final String text;
	private final Number total;
	private Button bButton;
	private final String textButton;
	private final OnAcceptListener listener;

	public OrderDialog(String text, String textButton, Number total,
			OnAcceptListener listener) {
		this.text = text;
		this.textButton = textButton;
		this.total = total;
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.getDialog().setTitle("Envío de Pedido");
		View view = inflater.inflate(R.layout.order_dialog, container, false);

		this.textView = (TextView) view.findViewById(R.id.textInfo);
		this.textView.setText(this.text);
		this.textView.append(" ");
		this.textView.append(this.total.toString());

		this.bButton = (Button) view.findViewById(R.id.bAceptar);
		this.bButton.setText(this.textButton);
		this.bButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderDialog.this.getDialog().dismiss();
				OrderDialog.this.listener.onAcceptListener();
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
