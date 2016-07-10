package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoDAO;
import br.com.luckez.tcc.dao.ProdutoPorCompraDAO;
import br.com.luckez.tcc.dialog.DialogRenameProduto;
import br.com.luckez.tcc.entity.Produto;
import br.com.luckez.tcc.entity.ProdutoPorCompra;

public class MeusProdutosActivity extends Activity {

	private DBHelper dbHelper;
	private ProdutoDAO produtoDAO;
	private ArrayAdapter<Produto> mAdapter;
	private List<Produto> meusProdutosList;
	private ListView mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meus_produtos);
		
		dbHelper = new DBHelper(this);
		try {
			produtoDAO = new ProdutoDAO(dbHelper.getConnectionSource());
			meusProdutosList = produtoDAO.queryForAll();
			Collections.sort(meusProdutosList, new Comparator<Produto>() {
			    public int compare(Produto p1, Produto p2) {
			        return p1.getNome().compareTo(p2.getNome());
			    }
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			meusProdutosList = new ArrayList<Produto>();
		}
		
		mList = (ListView) findViewById(R.id.activity_meus_produtos_list_produtos);
		
		//mAdapter = new ArrayAdapter<Produto>(this, android.R.layout.simple_list_item_1, meusProdutosList);
		mAdapter = new ArrayAdapter<Produto>(this, R.layout.list_view_custom_layout, android.R.id.text1, meusProdutosList);
		
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(goToProdutoAsGraphic(this));
		mList.setEmptyView(findViewById(R.id.emptyElement));
		
		registerForContextMenu(mList);
	}
	
	/**
	 * Método responsável por inflar o menu de contexto na lista de produtos da
	 * compra
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.activity_meus_produtos_list_produtos) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.meus_produtos_context_menu, menu);
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
		case R.id.rename:
			//Exibe o popup para renomear um produto
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			DialogRenameProduto dlg = new DialogRenameProduto(meusProdutosList.get(pos));
			dlg.show(ft, "dialog");
			dlg.getDialog();
			return true;
		case R.id.delete:
			try {
				ProdutoPorCompraDAO produtoPorCompraDAO = new ProdutoPorCompraDAO(dbHelper.getConnectionSource());
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("produto_id", meusProdutosList.get(pos));
				// Busca o produto com o código escaneado no banco
				List<ProdutoPorCompra> lista = produtoPorCompraDAO.queryForFieldValues(value);
				if(lista != null && lista.size() > 0){
					Toast.makeText(this, "Você não pode deletar um produto que tenha compras associadoa a ele.", Toast.LENGTH_LONG).show();;
				}else{
					produtoDAO.delete(meusProdutosList.get(pos));
					meusProdutosList.remove(pos);
					mAdapter.notifyDataSetChanged();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			produtosCompra.remove(pos);
//			populaLista();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	public OnItemClickListener goToProdutoAsGraphic(final Context context) {
		return (new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> payList, View selectedItem,
					int position, long id) {
				Intent intent = new Intent(context, ProductInformation.class);
				intent.putExtra("produtoSelecionado", meusProdutosList.get(position));
				startActivity(intent);
			}
		});
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.meus_produtos, menu);
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
