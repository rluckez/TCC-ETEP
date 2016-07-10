package br.com.luckez.tcc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;

public class Utils {
	
	public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	public static final String nomeSupermercadoMessage = "Diga o nome do supermercado.";
	public static final String valorProdutoMessage = "Diga o valor do produto.";
	public static final String nomeProdutoMessage = "Diga o nome do produto.";
	
	public Utils() {
		// TODO Auto-generated constructor stub
	}

	public static void startVoiceRecognitionActivity(Activity calling, String messageToShow){
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, messageToShow);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "pt-BR");
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);
        calling.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}
	
	/**
	 * Verifica se o dispositivo está conectado a internet
	 * @return boolean informado se há conectividade ou não
	 */
	public static boolean isOnline(Context callingActivity) {
	    ConnectivityManager cm = (ConnectivityManager) callingActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    //netInfo.isConnected();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
	
}
