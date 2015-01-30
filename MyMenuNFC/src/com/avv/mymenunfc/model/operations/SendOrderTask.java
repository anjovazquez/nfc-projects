package com.avv.mymenunfc.model.operations;

import java.util.ArrayList;

import com.avv.mymenunfc.model.vo.ProductInfo;
import com.avv.mymenunfc.view.listeners.OnOrderFinished;

import android.os.AsyncTask;

public class SendOrderTask extends AsyncTask<String, Void, Integer> {

	private final OnOrderFinished listener;
	private String problem;
	private int time;
	private String reference;

	public SendOrderTask(OnOrderFinished listener,
			ArrayList<ProductInfo> products) {
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			this.composeJSON();
			this.sendHttpToServer();
			Thread.sleep(5000);
			this.time = 20;
			this.reference = "102323";
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.problem = "Problema de conexión";
			return -1;
		}
		return 0;
	}

	private void composeJSON() {
	}

	private void sendHttpToServer() {

	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (result.intValue() == -1) {
			this.listener.orderRejected(this.problem);
		} else {
			this.listener.orderAccepted(this.reference, this.time);
		}
	}

}
