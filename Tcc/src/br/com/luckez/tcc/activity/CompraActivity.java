package br.com.luckez.tcc.activity;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.adapter.ProdutoPorCompraAdapter;
import br.com.luckez.tcc.dao.CompraDAO;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoDAO;
import br.com.luckez.tcc.dao.ProdutoPorCompraDAO;
import br.com.luckez.tcc.dao.UnidadeDAO;
import br.com.luckez.tcc.dialog.DialogInsereCodigo;
import br.com.luckez.tcc.dialog.DialogInsereProduto;
import br.com.luckez.tcc.dialog.DialogRenameProduto;
import br.com.luckez.tcc.entity.Compra;
import br.com.luckez.tcc.entity.Produto;
import br.com.luckez.tcc.entity.ProdutoPorCompra;
import br.com.luckez.tcc.entity.Supermercado;
import br.com.luckez.tcc.entity.Unidade;
import br.com.luckez.tcc.utils.Utils;
import br.com.luckez.tcc.webservice.ProdutoWS;

public class CompraActivity extends FragmentActivity {

	private DBHelper dbHelper;
	private ProdutoDAO produtoDAO;
	private ProdutoPorCompraDAO produtoPorCompraDAO;
	private CompraDAO compraDAO;
	private UnidadeDAO unidadeDAO;
	private List<Produto> produtos;
	private ArrayList<ProdutoPorCompra> produtosCompra;
	private ProdutoPorCompraAdapter mAdapter;
	private Compra compra;
	private Toast toast;
	private Produto produtoToAdd;
	private ListView lvProdutosCompra;
	private TextView mTVTotal;
	private Double precoTotal = 0.0;
	private Double quantidade = 0.0;
	private Integer editingPosition;
	private SharedPreferences preferences;
	boolean useWebService;
	public static final int REQUEST_CODE = 0;
	public static final boolean EDIT = false;
	public static final boolean INSERT = true;

	private long lastBackPressTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compra);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		dbHelper = new DBHelper(this);
		try {
			produtoDAO = new ProdutoDAO(dbHelper.getConnectionSource());
			produtoPorCompraDAO = new ProdutoPorCompraDAO(
					dbHelper.getConnectionSource());
			compraDAO = new CompraDAO(dbHelper.getConnectionSource());
			unidadeDAO = new UnidadeDAO(dbHelper.getConnectionSource());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (getIntent().hasExtra("supermercado")) {
			Supermercado supermercado = (Supermercado) getIntent()
					.getParcelableExtra("supermercado");
			setTitle("Nova compra em " + supermercado.getNome());
			compra = new Compra(supermercado);
			precoTotal = 0.0;
			quantidade = 0.0;
			produtosCompra = new ArrayList<ProdutoPorCompra>();
			
			lvProdutosCompra = (ListView) findViewById(R.id.activity_compra_list_view_produtos_compra);
			lvProdutosCompra.setEmptyView(findViewById(R.id.activity_compra_list_empty_view));
			lvProdutosCompra.setOnItemClickListener(editaProduto(this));
			mAdapter = new ProdutoPorCompraAdapter(this, produtosCompra);
			lvProdutosCompra.setAdapter(mAdapter);
			// Indica que essa list view terá um context menu de listener para click
			// longo
			registerForContextMenu(lvProdutosCompra);

			mTVTotal = (TextView) findViewById(R.id.activity_compra_total_tv);
			mTVTotal.setText("Total de 0 de produto(s) por R$ 0.00");
			mTVTotal.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			mTVTotal.setSingleLine(true);
			mTVTotal.setMarqueeRepeatLimit(-1);
			mTVTotal.setSelected(true);
		}
	}

	/**
	 * Método responsável por inflar o menu de contexto na lista de produtos da
	 * compra
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.activity_compra_list_view_produtos_compra) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.compra_context_menu, menu);
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
		case R.id.info:
			Produto p = produtosCompra.get(pos).getProduto();
			toast = Toast.makeText(getApplicationContext(), "ID: " + p.getId()
					+ " - Nome: " + p.getNome(), Toast.LENGTH_LONG);
			toast.show();
			return true;
		case R.id.rename:
			//Exibe o popup para renomear um produto
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			DialogRenameProduto dlg = new DialogRenameProduto(produtosCompra.get(pos).getProduto());
			dlg.show(ft, "dialog");
			dlg.getDialog();
			return true;
		case R.id.report:
			//Verifica conexao com a internet
			if(!Utils.isOnline(this)){
				//Exibe popup informando que não há conexão
				new AlertDialog.Builder(this)
				.setTitle("Sem conexão...")
				.setMessage("Seu aparelho deve estar conectado a internet para reportar um produto.")
				.setCancelable(true)
				.setPositiveButton("Fechar", null).show();
			}else{
				//Exibe popup de confirmação para o usuário
				final int position = pos;
				new AlertDialog.Builder(this)
				.setTitle("Reportar produto")
				.setMessage("Você realmente deseja reportar este produto?")
				.setCancelable(true)
				.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						new ReportTask().execute(produtosCompra.get(position).getProduto().getCodigo());
						Toast.makeText(getApplicationContext(),	"O produto está sendo reportado, Obrigado!", Toast.LENGTH_SHORT).show();
					}
				 })
				 .setNegativeButton("Não", null).show();
			}
			return true;
		case R.id.delete:
			produtosCompra.remove(pos);
			populaLista();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public OnItemClickListener editaProduto(final Context context) {
		return (new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				lvProdutosCompra.setEnabled(false);
				showDialogToEdit(position);
			}
		});
	}

	public void populaLista() {
		mAdapter.notifyDataSetChanged();
		precoTotal = 0.0;
		quantidade = 0.0;
		for (ProdutoPorCompra produto : produtosCompra) {
			precoTotal += produto.getPrecoProduto() * produto.getQuantidade();
			if(produto.getUnidade().getCode().equalsIgnoreCase("Kg")){
				quantidade++;
			}else if(produto.getUnidade().getCode().equalsIgnoreCase("Dz")){
				quantidade += produto.getQuantidade() * 12;
			}else{
				quantidade += produto.getQuantidade();
			}
		}
		DecimalFormat df = new DecimalFormat("0.00");
		mTVTotal.setText("Total de " + quantidade.intValue() + " de produto(s) por " + "R$ " + df.format(precoTotal));
		
		enableListToClick();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compra, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			Intent intent = new Intent(this, ConfiguracaoActivity.class);
			startActivity(intent);
			break;
		case R.id.action_add:
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			DialogInsereCodigo dlg = new DialogInsereCodigo();
			dlg.show(ft, "dialog");
			dlg.getDialog();
			break;
		case R.id.action_capture:
			scanProduto();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Realiza o scan do código de barras
	 */
	public void scanProduto() {
		Intent it = new Intent(CompraActivity.this,
				com.google.zxing.client.android.CaptureActivity.class);
		startActivityForResult(it, REQUEST_CODE);
	}

	/**
	 * Pega o resultado do scan
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		// Verifica se a activity para resultado foi bem sucedida
		if (resultCode == RESULT_OK) {

			// Verifica qual foi o resultado, se é de um novo cadastro de
			// produto ou de uma nova inserção na lista
			if (requestCode == NovoProdutoActivity.REQUEST_CODE) {
				produtoToAdd = intent.getParcelableExtra("result");
				showDialogToInsert(null);

			} else if (requestCode == REQUEST_CODE) {

				// Pega os resultados do scan
				String barCode = intent.getStringExtra("SCAN_RESULT");
				String scanFormat = intent.getStringExtra("SCAN_FORMAT");

				// Verifica se o formato do código de barras é EAN_13
				if (!(scanFormat.equalsIgnoreCase("EAN_13"))) {
					toast = Toast.makeText(getApplicationContext(),
							"Formato de código de barras inválido!",
							Toast.LENGTH_LONG);
					toast.show();
				} else {
					//Executa o processamento com o código de barras
					processa(barCode);
				}
			}

		} else {
			toast = Toast.makeText(getApplicationContext(),
					"Nenhum código de barras encontrado", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * Método responsável por todo processamento após obtido um valor de código de barras
	 * @param barCode
	 */
	public void processa(final String barCode){
			new ConnectionTask().execute(barCode);
	}
	
	public void simulaAddProduto(View view) {
		Produto produto;
		try {
			List<Produto> produtos = produtoDAO.queryForAll();
			if (produtos.size() != 0) {
				Random random = new Random();
				produto = produtos.get(random.nextInt(produtos.size()));
				ProdutoPorCompra produtoToAdd = new ProdutoPorCompra(compra,
						produto, 1.0, 1.5);
				Unidade unidade = unidadeDAO.queryForId(1);
				produtoToAdd.setUnidade(unidade);
				produtosCompra.add(0,produtoToAdd);
				populaLista();
			} else {
				toast = Toast.makeText(getApplicationContext(),
						"Não existe nenhum produto cadastrado",
						Toast.LENGTH_LONG);
				toast.show();
			}
		} catch (SQLException e) {
			toast = Toast.makeText(getApplicationContext(),
					"Problemas ao simular", Toast.LENGTH_LONG);
			toast.show();
		}
	}

	/**
	 * Método que chama o dialog para inserir um produto na compra atual
	 * 
	 * @param view
	 */
	public void showDialogToInsert(View view) {
		ProdutoPorCompra ppc = new ProdutoPorCompra();
		ppc.setProduto(produtoToAdd);
		// ppc.setPrecoProduto(0.0);
		ppc.setQuantidade(1.0);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogInsereProduto dlg = new DialogInsereProduto(ppc, INSERT);
		dlg.show(ft, "dialog");
		dlg.getDialog();
	}

	/**
	 * Método que chama o dialog para editar o produto selecionado na atual
	 * compra
	 * 
	 * @param position
	 *            Posição do produto no vetor de produtos da compra que se
	 *            deseja editar
	 */
	public void showDialogToEdit(Integer position) {
		editingPosition = position;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogInsereProduto dlg = new DialogInsereProduto(
				produtosCompra.get(position), EDIT);
		dlg.show(ft, "dialog");
		dlg.getDialog();
	}

	@SuppressLint("CommitTransaction")
	public void dismissDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogInsereProduto dlg = (DialogInsereProduto) getFragmentManager()
				.findFragmentByTag("dialog");
		if (dlg != null) {
			dlg.dismiss();
			ft.remove(dlg);
		}
	}

	/**
	 * Método utilizado para ativar a lista de produtos para click novamente
	 */
	public void enableListToClick(){
		lvProdutosCompra.setEnabled(true);
	}
	
	/**
	 * Adiciona um produto na compra
	 * 
	 * @param quantidade
	 *            Quantidade para ser inserida
	 * @param valor
	 *            Valor do produto unitário
	 */
	public void insereProduto(ProdutoPorCompra produtoCompra) {
		if (compra != null && produtoToAdd != null) {
			produtoCompra.setCompra(compra);
			produtoCompra.setProduto(produtoToAdd);
			produtosCompra.add(0, produtoCompra);
			populaLista();
		}
	}

	/**
	 * Método responsável por atualizar o produto editado na lista de produtos
	 * na compra
	 * 
	 * @param produtoEditado
	 *            Produto que teve suas propriedades alteradas
	 * @param position
	 *            Posição do produto que deve ser atualizado
	 */
	public void salvaProdutoEditado(ProdutoPorCompra produtoEditado) {
		produtosCompra.set(editingPosition, produtoEditado);
		populaLista();
	}

	// @Override
	// public void onSaveInstanceState(Bundle outState){
	// super.onSaveInstanceState(outState);
	// outState.putParcelable("compra", compra);
	// outState.putParcelableArrayList("produtosCompra", produtosCompra);
	// }

	public void salvarCompra(View v){
		new AlertDialog.Builder(this)
		.setMessage("Você realmente deseja finalizar esta compra?")
		.setCancelable(true)
		.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				salvaCompra();
			}
		 })
		 .setNegativeButton("Não", null).show();
	}
	
	public void salvaCompra() {
		try {
			if (produtosCompra != null && produtosCompra.size() != 0) {
				// TODO Mudar a cotnagem de produtos
				compra.setNumeroDeProdutos(quantidade.intValue());
				compra.setValor(precoTotal);
				compraDAO.create(compra);
				for (ProdutoPorCompra ppc : produtosCompra) {
					ppc.setCompra(compra);
					produtoPorCompraDAO.create(ppc);
				}
				finish();
				//Fecha as acitivities na pilha e inicializa a home
				Intent intent = new Intent(this, MainActivity.class);
				intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
				startActivity(intent);
				toast = Toast.makeText(this, "Compra salva com sucesso!",
						Toast.LENGTH_LONG);
				toast.show();
			} else {
				toast = Toast.makeText(this,
						"Você não pode criar uma compra sem produtos!",
						Toast.LENGTH_LONG);
				toast.show();
			}
		} catch (Exception e) {
			// TODO blablabla
			e.printStackTrace();
			toast = Toast.makeText(this, "Problemas ao salvar",
					Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (this.lastBackPressTime < System.currentTimeMillis() - 3000) {
			toast = Toast.makeText(this, "Aperte voltar novamente para cancelar esta compra.",
					Toast.LENGTH_LONG);
			toast.show();
			this.lastBackPressTime = System.currentTimeMillis();
		} else {
			if (toast != null) {
				toast.cancel();
			}
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onNavigateUp() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
			.setMessage("Você realmente deseja cancelar esta compra?")
			.setCancelable(true)
			.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					CompraActivity.super.onNavigateUp();
				}
			 })
			 .setNegativeButton("Não", null).show();
		return false;
	}

	private class ConnectionTask extends AsyncTask<String, Integer, String> {
	   
		private ProgressDialog dialog = new ProgressDialog(CompraActivity.this);

		public Produto consultaProdutoBancoLocal(String barcode){
			
			Map<String, Object> value = new HashMap<String, Object>();
			value.put("codigo", barcode);
			// Busca o produto com o código escaneado no banco
			try {
				produtos = produtoDAO.queryForFieldValues(value);
				if(produtos.size() != 0){
					return produtos.get(0);
				}else{
					return null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		/**
		 * Varre a lista de produtos e verifica se algum produto já adicionado possui o código de barras
		 * passado como parâmetro
		 * @param barCode
		 * @return Index da posição que possui o mesmo valor de código de barras ou -1 se o código não for encontrado na lista de produtos
		 */
		protected int checkIfExistsOnCurrentCompra(String barCode){
			for (int i = 0; i < produtosCompra.size(); i++) {
				if (produtosCompra.get(i).getProduto().getCodigo() == Long.parseLong(barCode)) {
					return i;
				}
			}
			return -1;
		}
		
	    protected void onPreExecute() {
	        dialog.setMessage("Buscando produto no servidor...");
	        dialog.setIndeterminate(false);
	        dialog.setCancelable(false);
//	        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", (DialogInterface.OnClickListener) null);
	        dialog.show();
	    }

	    protected String doInBackground(String... args) {
	    	useWebService = preferences.getBoolean("usar_servidor",	true);
			
			String barCode = args[0];
			Produto produto = consultaProdutoBancoLocal(barCode);
			/**
			 * Verifica se é pra usar o servidor ou não
			 */
			if (useWebService && Utils.isOnline(CompraActivity.this)) {
				
				// Produto não existe no banco local?
				if (produto == null) {
					//Busca o produto pelo código no servidor
					try {
						produto = new ProdutoWS().searchProdutoByCode(Long.parseLong(barCode), 9500);
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return barCode;
					}
					/**
					 * Verifica se o servidor possui o produto com o
					 * código escaneado
					 */
					if (produto != null) {
						try {
							produto.setId(null);
							// Produto é adicionado no banco local
							produtoDAO.create(produto);
							produtoToAdd = produto;
							showDialogToInsert(null);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Intent novoProdutoIntent = new Intent(CompraActivity.this,	NovoProdutoActivity.class);
						novoProdutoIntent.putExtra("codigo", barCode);
						startActivityForResult(novoProdutoIntent,NovoProdutoActivity.REQUEST_CODE);
					}

				} else {
					// Caso o produto exista no banco ele verifica se o
					// mesmo já não foi inserido na compra atual
					
					int indexThatExists = checkIfExistsOnCurrentCompra(barCode);;
					// Se o produto ja existir na compra ele exibe um
					// popup para alterar este produto na compra, senão,
					// ele exibe o popup para adicioná-lo na compra
					if (indexThatExists >= 0) {
						showDialogToEdit(indexThatExists);
					} else {
						produtoToAdd = produtos.get(0);
						showDialogToInsert(null);
					}
				}

			/**
			 * Else do servidor não ativado
			 */
			} else {

				// Se o produto não existir no banco ele abre a activity
				// responsável por realizar o cadastro de um novo
				// produto
				if (produto == null) {
					Intent novoProdutoIntent = new Intent(CompraActivity.this,	NovoProdutoActivity.class);
					novoProdutoIntent.putExtra("codigo", barCode);
					startActivityForResult(novoProdutoIntent,NovoProdutoActivity.REQUEST_CODE);
				} else {
					// Caso o produto exista no banco ele verifica se o
					// mesmo já não foi inserido na compra atual
					int indexThatExists = checkIfExistsOnCurrentCompra(barCode);;
					// Se o produto ja existir na compra ele exibe um
					// popup para alterar este produto na compra, senão,
					// ele exibe o popup para adicioná-lo na compra
					if (indexThatExists >= 0) {
						showDialogToEdit(indexThatExists);
					} else {
						produtoToAdd = produtos.get(0);
						showDialogToInsert(null);
					}
				}
			}
	       return null; 
	    }
	    
	    @Override
	    protected void onProgressUpdate(Integer... args){
	         // TODO Auto-generated method stub  
	         super.onProgressUpdate();  
	           
	         if(dialog != null && dialog.isShowing()){  
	        	 dialog.incrementProgressBy(args[0]);  
	         }  
	    }  
	    
	    @Override
	    protected void onPostExecute(final String barcode) {
	        if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
	        if(barcode != null){
		        new AlertDialog.Builder(CompraActivity.this)
		        .setTitle("Problema na conexão")
					.setMessage("Não foi possível obter uma resposta do servidor. Deseja tentar novamente?")
					.setCancelable(true)
					.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							processa(barcode);
						}
					 })
					 .setNegativeButton("Não", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent novoProdutoIntent = new Intent(CompraActivity.this,	NovoProdutoActivity.class);
							novoProdutoIntent.putExtra("codigo", barcode);
							startActivityForResult(novoProdutoIntent,NovoProdutoActivity.REQUEST_CODE);
						}
					}).show();
			}
	    }
	}
	
	private class ReportTask extends AsyncTask<Long, Integer, Void> {
	    protected Void doInBackground(Long... args) {
	    	Long barCode = args[0];
			ProdutoWS produtoWS = new ProdutoWS();
			try{
				produtoWS.reportarProduto(barCode);
			}catch(Exception e){
				//TODO handle exception
			}
			return null;
	    }
	}
}
