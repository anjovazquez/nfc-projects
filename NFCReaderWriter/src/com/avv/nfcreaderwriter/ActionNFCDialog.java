package com.avv.nfcreaderwriter;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ActionNFCDialog extends DialogFragment {

	private TextView textView;
	private final String text;
	private Button bButton;
	private final String textButton;

	public interface OnNFCDetected {
		void nfcDetected(int size);
	}

	public ActionNFCDialog(String text, String textButton) {
		this.text = text;
		this.textButton = textButton;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.getDialog().setTitle("Acción NFC");
		View view = inflater.inflate(R.layout.nfc_write_dialog, container,
				false);

		this.textView = (TextView) view.findViewById(R.id.textInfo);
		this.textView.setText(this.text);

		this.bButton = (Button) view.findViewById(R.id.bCancel);
		this.bButton.setText(this.textButton);
		this.bButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActionNFCDialog.this.getDialog().dismiss();
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
