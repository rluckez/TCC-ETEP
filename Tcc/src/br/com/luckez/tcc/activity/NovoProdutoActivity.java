package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoDAO;
import br.com.luckez.tcc.entity.Produto;
import br.com.luckez.tcc.utils.Utils;
import br.com.luckez.tcc.webservice.ProdutoWS;

public class NovoProdutoActivity extends Activity {

	private Toast toast;
	private String barCode;
	private DBHelper dbHelper;
	private ProdutoDAO produtoDAO;
	SharedPreferences preferences;
	private EditText nomeProduto;
	public static final int REQUEST_CODE = 99;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_novo_produto);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		dbHelper = new DBHelper(this);
		try {
			produtoDAO = new ProdutoDAO(dbHelper.getConnectionSource());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(getIntent().hasExtra("codigo")){
			barCode = getIntent().getStringExtra("codigo");
		}
		
		TextView codigoProduto = (TextView) findViewById(R.id.novo_produto_codigo_produto);
		StringBuilder str = new StringBuilder(barCode);
		str.insert(1, " ");
		str.insert(8, " ");
		codigoProduto.setText(str);
		
		nomeProduto = (EditText) findViewById(R.id.novo_produto_nome_produto);
		nomeProduto.requestFocus();
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		if(barCode != null && !barCode.equals("")){
			toast = Toast.makeText(getApplicationContext(), 
		            "Produto não encontrado, cadastre os dados do novo produto", Toast.LENGTH_LONG);
	        toast.show();
		}
	}
	
//	public void populaCategorias(){
//		try {
//						
//			categorias = categoriaDAO.queryForAll();
//			
//			mAdapterCategoria = new ArrayAdapter<Categoria>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
//			
//			Spinner categoriasListView = (Spinner) findViewById(R.id.novo_produto_spinner_categoria);
//			categoriasListView.setAdapter(mAdapterCategoria);
//					
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
//	}
	
//	@SuppressLint("InflateParams")
//	public void openPopUpAddCategoria(View view){
//		
//		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//		
//		popupView = layoutInflater.inflate(R.layout.popup_add_categoria, null);
//		
//		Button saveButton = (Button) popupView.findViewById(R.id.button_save_categoria);
//		
//		saveButton.setOnClickListener(new Button.OnClickListener(){
//
//		     @Override
//		     public void onClick(View v) {
//		      // TODO Auto-generated method stub
//	    	 saveCategoria();
//		      popupWindow.dismiss();
//		      clareia();
//		      populaCategorias();
//		      Toast toast = Toast.makeText(getApplicationContext(), 
//			            "Categoria criada com sucesso!", Toast.LENGTH_SHORT);
//		        toast.show();
//		     }});
//		
//		popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		popupWindow.setFocusable(true);		
//		popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//		escurece();
//	}

	public void cancel(View view){
//		onBackPressed();
		finish();
	}
	
	public void saveProduto(View view){
		EditText nomeProduto = (EditText) findViewById(R.id.novo_produto_nome_produto);
//		Spinner categoriaProduto = (Spinner) findViewById(R.id.novo_produto_spinner_categoria);
		
		String nome = nomeProduto.getText().toString();
//		Categoria cat = (Categoria) categoriaProduto.getSelectedItem();
		
		if(TextUtils.isEmpty(nome.trim())){
			nomeProduto.setError("Insira o nome do produto");
			return;
		}
		
		Produto produto = new Produto(Long.parseLong(barCode), nome );
		try {
			Boolean useWebService = preferences.getBoolean("usar_servidor", true);
			if(Utils.isOnline(this) && useWebService){
				new SendProductTask().execute(produto);
			}
			produtoDAO.create(produto);
			Intent returnIntent = new Intent();
			returnIntent.putExtra("result",produto);
			setResult(RESULT_OK,returnIntent);
			finish();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class SendProductTask extends AsyncTask<Produto, Integer, Void> {
	    protected Void doInBackground(Produto... args) {
	    	Produto produto = args[0];
	    	ProdutoWS produtoWS = new ProdutoWS();
			try{
				produtoWS.insereProduto(produto);
			}catch(Exception e){
				
			}
			return null;
	    }
	}
	
	public void onClick(View view){
		Utils.startVoiceRecognitionActivity(this, Utils.nomeProdutoMessage);
	}
	
//	public void saveCategoria(){
//		
//		EditText categoriaNome = (EditText) popupView.findViewById(R.id.nome_nova_categoria);
//		Categoria newCategoria = new Categoria(categoriaNome.getText().toString());
//
//		try {
//			categoriaDAO.create(newCategoria);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			}
	
//	public void closePopUp(View view){
//		popupWindow.dismiss();
//		clareia();	
//	}
	
//	public void escurece(){
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		ScrollView novoProdutoScrollView = (ScrollView) findViewById(R.id.novo_produto_scroll_view);
//		novoProdutoScrollView.setBackground(dw);
//	}
//	
//	public void clareia(){
//		ScrollView novoProdutoScrollView = (ScrollView) findViewById(R.id.novo_produto_scroll_view);
//		novoProdutoScrollView.setBackground(null);
//	}
	
//	public void onWindowFocusChanged(boolean hasFocus) {
//		if(hasFocus){
//			populaCategorias();
//		}
//	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.novo_produto, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			if (id == R.id.action_settings) {
				Intent intent = new Intent(this, ConfiguracaoActivity.class);
				startActivity(intent);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	 /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	//TODO Talvez tratar problemas aqui
        if (requestCode == Utils.VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> resultados = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String nome = resultados.get(0);
            //Faz o capitalize do primeiro caracter do nome do supermercado
            nomeProduto.setText( Character.toUpperCase(nome.charAt(0)) + nome.substring(1));
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
}
