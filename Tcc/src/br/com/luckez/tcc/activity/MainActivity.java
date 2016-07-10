package br.com.luckez.tcc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import br.com.luckez.tcc.R;

public class MainActivity extends Activity {

	public static final int REQUEST_CODE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/**
		 * Código responsável por fazer com que o aplicativo consiga se comunicar com o web service, necessário para SDK's > 9
		 */
		if(android.os.Build.VERSION.SDK_INT > 9){
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, ConfiguracaoActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToSupermercadoActivity(View view){
		Intent intent = new Intent(this, SupermercadoListActivity.class);
		startActivity(intent);
	}
	
	public void goToMinhasCompras(View view){
		Intent intent = new Intent(this, MinhasComprasListActivity.class);
		startActivity(intent);
	}
	
	public void goToMeusProdutos(View view){
		Intent intent = new Intent(this, MeusProdutosActivity.class);
		startActivity(intent);
	}
	
	public void goToUtilidades(View view){
		Intent intent = new Intent(this, UtilsActivity.class);
		startActivity(intent);
	}
	
	public void goToAbout(View view){
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(REQUEST_CODE == requestCode && RESULT_OK == resultCode){
		}
	}
	
	
}
