package com.avv.mymenunfc.view.listeners;

public interface OnOrderFinished {

	void orderAccepted(String reference, int time);

	void orderRejected(String problem);
}
