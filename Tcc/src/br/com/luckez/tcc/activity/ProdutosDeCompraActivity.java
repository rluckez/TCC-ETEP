package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.adapter.ProdutoPorCompraAdapter;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoPorCompraDAO;
import br.com.luckez.tcc.entity.Compra;
import br.com.luckez.tcc.entity.ProdutoPorCompra;

public class ProdutosDeCompraActivity extends Activity {

	private DBHelper dbHelper;
	private ProdutoPorCompraDAO produtoPorCompraDAO;
	private ProdutoPorCompraAdapter mAdapter;
	private List<ProdutoPorCompra> mListProdutosPorCompra; 
	private ListView lvProdutosPorCompra;
	private Compra compraSelecionada;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produtos_de_compra);
		
		dbHelper = new DBHelper(this);
		try {
			produtoPorCompraDAO = new ProdutoPorCompraDAO(dbHelper.getConnectionSource());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(getIntent().hasExtra("compra")){
			compraSelecionada = getIntent().getParcelableExtra("compra");
		}else{
			compraSelecionada = null;
		}
		
		lvProdutosPorCompra = (ListView) findViewById(R.id.activity_produtos_de_compra_list_produtos);
		
		
		if(compraSelecionada != null){
			try{
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("compra_id", compraSelecionada.getId());
				mListProdutosPorCompra = produtoPorCompraDAO.queryForFieldValues(value);
				mAdapter = new ProdutoPorCompraAdapter(this, new ArrayList<ProdutoPorCompra>( mListProdutosPorCompra));
				lvProdutosPorCompra.setAdapter(mAdapter);
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.produtos_de_compra, menu);
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
}
