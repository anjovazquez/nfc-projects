package com.avv.mymenunfc;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avv.mymenunfc.model.adapter.ProductInfoAdapter;
import com.avv.mymenunfc.model.operations.SendOrderTask;
import com.avv.mymenunfc.model.vo.ProductInfo;
import com.avv.mymenunfc.view.dialog.InfoDialog;
import com.avv.mymenunfc.view.dialog.OrderDialog;
import com.avv.mymenunfc.view.listeners.OnAcceptListener;
import com.avv.mymenunfc.view.listeners.OnOrderFinished;

public class MenuList extends ActionBarActivity implements OnAcceptListener,
		OnOrderFinished {

	private ArrayList<ProductInfo> productInfoList = null;
	private ProductInfoAdapter adapter;
	private ListView listView;
	private ProgressBar progressBar;
	private TextView tWellcome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_menu_list);
		this.productInfoList = new ArrayList<ProductInfo>();
		this.adapter = new ProductInfoAdapter(this, this.productInfoList);

		this.listView = (ListView) this.findViewById(R.id.listView);
		this.listView.setAdapter(this.adapter);

		this.progressBar = (ProgressBar) this.findViewById(R.id.circular);
		this.progressBar.setVisibility(View.GONE);

		this.tWellcome = (TextView) this.findViewById(R.id.wellcome);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			String readed = this.readTag(intent);
			this.analise(readed);
		}
	}

	private void analise(String readed) {
		ProductInfo pInfo = new ProductInfo();
		if ((readed != null) && (readed.length() > 0)) {
			readed = readed.substring(2);
			String[] productData = readed.split("/");
			pInfo.setProductId(Integer.parseInt(productData[0]));
			pInfo.setName(productData[1]);
			pInfo.setPrice(new BigDecimal(productData[2]));
			this.productInfoList.add(pInfo);
			this.tWellcome.setVisibility(View.GONE);
			this.adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_send_order) {
			if (!this.productInfoList.isEmpty()) {
				OrderDialog dialog = new OrderDialog(this.getResources()
						.getString(R.string.send_data), "Aceptar",
						this.getTotalAmount(), this);

				dialog.show(this.getSupportFragmentManager(), "send_data");
			} else {
				InfoDialog dialog = new InfoDialog(
						"No hay productos para hacer su pedido", "Aceptar");

				dialog.show(this.getSupportFragmentManager(), "show_result");
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private Number getTotalAmount() {
		BigDecimal totalAmount = new BigDecimal("0");
		for (int i = 0; i < this.productInfoList.size(); i++) {
			totalAmount = totalAmount.add((BigDecimal) this.productInfoList
					.get(i).getPrice());
		}
		return totalAmount;
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

	@Override
	public void onAcceptListener() {
		this.progressBar.setVisibility(View.VISIBLE);
		SendOrderTask orderTask = new SendOrderTask(this, this.productInfoList);
		orderTask.execute("");
	}

	@Override
	public void orderAccepted(String reference, int time) {
		this.progressBar.setVisibility(View.GONE);
		String composedMessage = "Su pedido está en marcha, referencia "
				+ reference + "\n Listo en " + time + " minutos";
		InfoDialog dialog = new InfoDialog(composedMessage, "Aceptar");

		dialog.show(this.getSupportFragmentManager(), "show_result");
	}

	@Override
	public void orderRejected(String problem) {
		this.progressBar.setVisibility(View.GONE);
		String composedMessage = "Ha habido un problema con su pedido, alguien de nuestro personal se dirigirá a usted";
		InfoDialog dialog = new InfoDialog(composedMessage, "Aceptar");

		dialog.show(this.getSupportFragmentManager(), "show_result");
	}
}
