package br.com.luckez.tcc.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.UnidadeDAO;
import br.com.luckez.tcc.entity.Produto;
import br.com.luckez.tcc.entity.Unidade;
import br.com.luckez.tcc.utils.Utils;
import br.com.luckez.tcc.webservice.ProdutoWS;

public class TesteActivity extends Activity implements OnClickListener {

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	private DBHelper dbHelper;
	private UnidadeDAO unidadeDAO;
	private List<Unidade> unidades;
	private ArrayAdapter<Unidade> mAdapter;
	private Toast toast;
	ProgressDialog barProgressDialog;
	Handler updateBarHandler;
	EditText etGtin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teste);

		dbHelper = new DBHelper(this);
		
		updateBarHandler = new Handler();
		
		EditText etGtin = (EditText) findViewById(R.id.et_gtin);
		etGtin.setText("7984900011593");

		// Check to see if a recognition activity is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	}

	/**
	 * Handle the click on the start recognition button.
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.btn_speak) {
			startVoiceRecognitionActivity();
		}
	}

	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	private void startVoiceRecognitionActivity() {
		// Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		// intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		// RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		// intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
		// "Diga o valor do produto");
		// intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "pt-BR");
		// // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
		// "en-US");
		// intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
		// true);
		// startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		Utils.startVoiceRecognitionActivity(this, Utils.nomeProdutoMessage);
	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it
			// could have heard
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void searchByCode(View view) {
		ProdutoWS produtoWS = new ProdutoWS();
		Produto produto = new Produto();
		try {
			produto = produtoWS.searchProdutoByCode(Long.parseLong("7896094208094"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		toast = Toast.makeText(this, "Código: " + produto.getCodigo()
				+ " Nome: " + produto.getNome(), Toast.LENGTH_LONG);
		toast.show();

	}

	public void searchById(View view) {
		ProdutoWS produtoWS = new ProdutoWS();
		Produto produto = new Produto();
		produto = produtoWS.searchProdutoById(1);

		if (produto != null) {
			toast = Toast.makeText(this, "Código: " + produto.getCodigo()
					+ " Nome: " + produto.getNome(), Toast.LENGTH_LONG);
			toast.show();
		} else {
			toast = Toast.makeText(this, "Retornou nulo", Toast.LENGTH_LONG);
			toast.show();
		}

	}

	public void progressDialog(View view) {

		final ProgressDialog ringProgressDialog = ProgressDialog.show(TesteActivity.this, 
				"Consultando servidor", "Aguarda enquanto o produto é buscado...",	true);

		ringProgressDialog.setCancelable(false);

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					Thread.sleep(5000);

				} catch (Exception e) {

				}

				ringProgressDialog.dismiss();

			}

		}).start();

	}
	
	public void progressDialog2(View view) {
		barProgressDialog = new ProgressDialog(TesteActivity.this);

		barProgressDialog.setTitle("Consultando servidor...");
		barProgressDialog.setMessage("Buscando produto...");
		barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		barProgressDialog.setProgress(0);
		barProgressDialog.setMax(20);
		barProgressDialog.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					// Here you should write your time consuming task...
					while (barProgressDialog.getProgress() <= barProgressDialog.getMax()) {

						Thread.sleep(300);

						updateBarHandler.post(new Runnable() {

                            public void run() {

                            	barProgressDialog.incrementProgressBy(2);

                              }

                          });

						if (barProgressDialog.getProgress() == barProgressDialog.getMax()) {

							barProgressDialog.dismiss();

						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	public void listarTudo(View view) {
		ProdutoWS produtoWS = new ProdutoWS();
		List<Produto> produtos = new ArrayList<Produto>();
//		produtos = produtoWS.listAll();

		String nomes = "";

		for (Produto produto : produtos) {
			nomes += produto.getNome() + ";";
		}
		toast = Toast.makeText(this, nomes, Toast.LENGTH_LONG);
		toast.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teste, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void testaConexao(View v){
		if(Utils.isOnline(this)){
			toast = Toast.makeText(this, "Conectado", Toast.LENGTH_LONG);
		}else{
			toast = Toast.makeText(this, "Não está Conectado", Toast.LENGTH_LONG);
		}
		toast.show();
	}
	
	public final static String EXTRA_MESSAGE = "";
	public String gtin = "";
	public Integer search_mode = 0; // 0 : manual search , 1: scanner

	/** Called when the user clicks the Send button */
	@SuppressWarnings("unused")
	public void sendMessage(View view) {
	    // Do something in response to button

		etGtin = (EditText) findViewById(R.id.et_gtin);
		gtin = etGtin.getText().toString();
		
	    int value_code = 99;
	    String json = "";

		//String imgpp1 = "http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin-309/3095757343101.jpg";
		// http://localhost/github/products/explorer/product/3095757343101
		// http://www.le10sport.com/img/ico/icon-club/as-monaco.png
		// http://product.okfn.org.s3.amazonaws.com/images/gtin/gtin/309/3095757343101.jpg		
		//json = "{\"gtin\":{\"code\":\"3095757343101\",\"name\":\"La c\u00f4tes de porc cuite et pr\u00e9par\u00e9e dans le filet\",\"product-line\":\"Charcuterie\",\"weight\":\"2 x 70 g\",\"volume\":null,\"alcool\":null,\"img\":\"http:--product.okfn.org.s3.amazonaws.com-images-gtin-gtin-309-3095757343101.jpg\",\"img-default\":\"http:--www.product-open-data.com-images-coming-soon.jpg\"},\"brand\":{\"code\":\"469\",\"name\":\"Fleury Michon\",\"type\":\"Manufacturer-brand\",\"link\":\"www.fleurymichon.fr\",\"img\":\"http:--product.okfn.org.s3.amazonaws.com-images-brand-00000469.jpg\",\"img-default\":\"http:--www.product-open-data.com-images-coming-soon.jpg\"},\"group\":{\"code\":null,\"name\":null},\"gcp\":{\"code\":\"309575\"},\"gpc\":{\"img\":\"http:--www.product-open-data.com-images-gpc-50000000.jpg\",\"img-default\":\"http:--www.product-open-data.com-images-coming-soon.jpg\",\"segment\":{\"code\":\"50000000\",\"nom\":\"Food-Beverage-Tobacco\"},\"family\":{\"code\":\"50240000\",\"nom\":\"Meat-Poultry\"},\"class\":{\"code\":\"50240100\",\"nom\":\"Meat-Poultry - Prepared-Processed\"},\"brick\":{\"code\":null,\"nom\":null}},\"gepir\":{\"return-code\":{\"code\":\"0\",\"name\":\"No error\"},\"source\":\"GEPIR\",\"gln\":{\"code\":\"3010957500109\",\"name\":\"FLEURY MICHON CHARCUTERIE SAS\",\"address-1\":\"BP 1\",\"address-2\":\"\",\"address-3\":\"\",\"cp\":\"85700\",\"city\":\"POUZAUGES\",\"country\":\"FR\"}}}";

		if(gtin.length() > 0) {
			
			// CALL WEBSERVICE		
			try {
				json = new MyTask().execute("http://www.product-open-data.com/product/"+gtin).get();
				//Toast.makeText(this,"ok"+json,1000).show();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject object;
			try {
				object = new JSONObject(json);
				value_code = object.getInt("code");
				// GTIN
				JSONObject gtin = object.getJSONObject("gtin");
				String value_gtin_code = gtin.getString("code");
				String value_gtin_name = gtin.getString("name");			

				JSONObject brand = object.getJSONObject("brand");		
				String value_brand_code = brand.getString("code");
				String value_brand_name = brand.getString("name");
				String value_brand_type = brand.getString("type");
				String value_brand_link = brand.getString("link");	
				String value_brand_img = brand.getString("img");
				
				switch(value_code){
				    case 0 : 
				    	etGtin.setText(value_gtin_code);
						break;
				    case 1:
				    	Toast.makeText(this,"Não Achou",Toast.LENGTH_LONG).show();
						break; 
				    case 2:
				    	Toast.makeText(this,"Inválido",Toast.LENGTH_LONG).show();
						break; 
				    case 3:
				    	Toast.makeText(this,"Não Achou sei lá o q",Toast.LENGTH_LONG).show();
						break;
				    case 98:
						Toast.makeText(this,"Gtin code required",Toast.LENGTH_LONG).show();
						break;
				    case 99:
						Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
						break;
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} else {
			value_code = 98;
		}
	}
	
	
}
