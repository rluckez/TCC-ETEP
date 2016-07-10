package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.adapter.CompraAdapter;
import br.com.luckez.tcc.dao.CompraDAO;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoPorCompraDAO;
import br.com.luckez.tcc.entity.Compra;

public class MinhasComprasListActivity extends Activity {

	private DBHelper dbHelper;
	private CompraDAO compraDAO;
	private List<Compra> mListCompra;
	private ListView mCompraList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_minhas_compras_list);

		dbHelper = new DBHelper(this);
		
		try {
			compraDAO = new CompraDAO(dbHelper.getConnectionSource());
			mListCompra = compraDAO.queryForAll();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mCompraList = (ListView) findViewById(R.id.activity_minhas_compras_list_compras);
		mCompraList.setAdapter(new CompraAdapter(this, mListCompra));
		mCompraList.setEmptyView(findViewById(R.id.emptyElement));
		mCompraList.setOnItemClickListener(goToProdutosDeCompra(this));
		registerForContextMenu(mCompraList);
	}

	/**
	 * Método responsável por inflar o menu de contexto na lista de produtos da
	 * compra
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.activity_minhas_compras_list_compras) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.minhas_compras_context_menu, menu);
		}
	}

	/**
	 * Método responsável por agir ao selecionar um item no menu de contexto
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int pos = info.position;

		switch (item.getItemId()) {
		case R.id.delete:
			try {
				Compra compra = mListCompra.get(pos);
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("compra_id", compra);
				ProdutoPorCompraDAO produtoPorCompraDAO = new ProdutoPorCompraDAO(dbHelper.getConnectionSource());
				produtoPorCompraDAO.delete(produtoPorCompraDAO.queryForFieldValues(value));
				compraDAO.delete(compra);
				mListCompra = compraDAO.queryForAll();
//				mListCompra.remove(pos);
				mCompraList.setAdapter(new CompraAdapter(this, mListCompra));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	public OnItemClickListener goToProdutosDeCompra(final Context context) {
		return (new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> payList, View selectedItem,
					int position, long id) {
				Intent intent = new Intent(context, ProdutosDeCompraActivity.class);
				intent.putExtra("compra", mListCompra.get(position));
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.minhas_compras_list, menu);
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
}
