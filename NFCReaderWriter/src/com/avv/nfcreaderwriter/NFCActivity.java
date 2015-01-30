package com.avv.nfcreaderwriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NFCActivity extends Activity {

	private TextView textViewInfo;
	private Button bWrite;
	private Tag tag;
	private ActionNFCDialog dialog;

	private final int READ_MODE = 0;
	private final int WRITE_MODE = 1;
	private final int WRITE_MODE_URI = 2;
	private int currentMode = this.READ_MODE;
	private Button bWriteURI;
	private String textToWrite;
	private TextView tText;
	private TextView tURI;
	private TextView tTechs;
	private TextView tMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_nfc);
		this.textViewInfo = (TextView) this.findViewById(R.id.textViewInfo);

		this.tText = (TextView) this.findViewById(R.id.tText);
		this.tURI = (TextView) this.findViewById(R.id.tURI);
		this.tTechs = (TextView) this.findViewById(R.id.techs);
		this.tMessage = (TextView) this.findViewById(R.id.message);

		this.bWrite = (Button) this.findViewById(R.id.bWrite);
		this.bWrite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				NFCActivity.this.dialog = new ActionNFCDialog(NFCActivity.this
						.getResources().getString(R.string.textInfo),
						"Cancelar");
				NFCActivity.this.dialog.show(
						NFCActivity.this.getFragmentManager(), "nfc_writer");
				NFCActivity.this.currentMode = NFCActivity.this.WRITE_MODE;
				if ((NFCActivity.this.tText.getText() != null)
						&& !NFCActivity.this.tText.getText().toString()
								.isEmpty()) {
					NFCActivity.this.textToWrite = NFCActivity.this.tText
							.getText().toString();
				}
			}
		});

		this.bWriteURI = (Button) this.findViewById(R.id.bWriteURI);
		this.bWriteURI.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				NFCActivity.this.dialog = new ActionNFCDialog(NFCActivity.this
						.getResources().getString(R.string.textInfo),
						"Cancelar");
				NFCActivity.this.dialog.show(
						NFCActivity.this.getFragmentManager(), "nfc_writer");
				NFCActivity.this.currentMode = NFCActivity.this.WRITE_MODE_URI;
				if ((NFCActivity.this.tURI.getText() != null)
						&& !NFCActivity.this.tURI.getText().toString()
								.isEmpty()) {
					NFCActivity.this.textToWrite = NFCActivity.this.tURI
							.getText().toString();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// this.getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if ((this.currentMode == this.WRITE_MODE)
				|| (this.currentMode == this.WRITE_MODE_URI)) {
			this.write(intent);
		} else {
			this.readTagMsg(intent);
		}
	}

	private void readTagMsg(Intent intent) {

		this.tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		StringBuffer sb = new StringBuffer();
		if ((this.tag != null) && (this.tag.getTechList() != null)) {
			for (int i = 0; i < this.tag.getTechList().length; i++) {
				sb.append(this.tag.getTechList()[i]).append("\n");
			}
		}
		this.tTechs.setText(this.getResources().getString(R.string.techs));
		this.tTechs.append("\n");
		this.tTechs.append(sb.toString());

		if (this.tag != null) {
			this.tMessage.setText(this.getResources().getString(
					R.string.message));
			this.tMessage.append("\n");
			this.tMessage.append(this.readTag(intent));
		}
	}

	public String readTag(Intent intent) {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] messages = null;
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				messages = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					messages[i] = (NdefMessage) rawMsgs[i];
				}
			}
			if (messages[0] != null) {
				String result = "";
				byte[] payload = messages[0].getRecords()[0].getPayload();
				for (int b = 1; b < payload.length; b++) {
					result += (char) payload[b];
				}
				return result;
			}
		}
		return "";
	}

	private NdefRecord createRecord() throws UnsupportedEncodingException {
		String text = this.textToWrite != null ? this.textToWrite
				: "Hola mundo";
		String lang = "es";
		byte[] textBytes = text.getBytes();
		byte[] langBytes = lang.getBytes("US-ASCII");
		int langLength = langBytes.length;
		int textLength = textBytes.length;
		byte[] payload = new byte[1 + langLength + textLength];
		payload[0] = (byte) langLength;
		System.arraycopy(langBytes, 0, payload, 1, langLength);
		System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);
		NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_TEXT, new byte[0], payload);
		return record;
	}

	private NdefRecord createRecordURI() throws UnsupportedEncodingException {

		String uriText = this.textToWrite != null ? this.textToWrite
				: "fic.udc.es";
		byte[] uriField = uriText.getBytes(Charset.forName("US-ASCII"));
		byte[] payload = new byte[uriField.length + 1];
		payload[0] = 0x01;
		System.arraycopy(uriField, 0, payload, 1, uriField.length);
		NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_URI, new byte[0], payload);

		return record;
	}

	private void write(Intent intent) {
		String accion = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(accion)) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			try {
				NdefRecord record = this.createRecord();
				if (this.currentMode == this.WRITE_MODE_URI) {
					record = this.createRecordURI();
				}
				NdefRecord[] records = { record };
				NdefMessage message = new NdefMessage(records);
				Ndef ndef = Ndef.get(tag);
				ndef.connect();
				ndef.writeNdefMessage(message);
				ndef.close();

				this.dialog.dismiss();

				NFCActivity.this.dialog = new ActionNFCDialog(NFCActivity.this
						.getResources().getString(R.string.written_tag),
						"Aceptar");
				NFCActivity.this.dialog.show(
						NFCActivity.this.getFragmentManager(), "nfc_writer");

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FormatException e) {
				e.printStackTrace();
			} finally {
				this.currentMode = this.READ_MODE;
			}
		}
	}
}
