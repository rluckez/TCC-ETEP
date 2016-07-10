package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.dao.CompraDAO;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.SupermercadoDAO;
import br.com.luckez.tcc.dialog.DialogNovoSupermercado;
import br.com.luckez.tcc.entity.Compra;
import br.com.luckez.tcc.entity.Supermercado;
import br.com.luckez.tcc.utils.Utils;

public class SupermercadoListActivity extends Activity {

	private DBHelper dbHelper;
	private SupermercadoDAO supermercadoDAO;
	private CompraDAO compraDAO;
	private ArrayAdapter<Supermercado> mAdapterSupermercado;
	private List<Supermercado> supermercados;
	private TextView textoMenu;
	private List<Supermercado> supermercadosToDelete;
	private EditText nomeSupermercado;
	private ListView supermercadoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supermercado_list);
		
		dbHelper = new DBHelper(this);
		
		textoMenu = new TextView(this);
		
		try {
			supermercadoDAO = new SupermercadoDAO(dbHelper.getConnectionSource());
			compraDAO = new CompraDAO(dbHelper.getConnectionSource());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			populaListaSupermercado();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.supermercado_list, menu);
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
			return true;
		case R.id.action_add:
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			DialogNovoSupermercado dlg = new DialogNovoSupermercado();
			dlg.show(ft, "dialog");
			dlg.getDialog();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Método responsável por atualizar a lista de supermercados na tela
	 */
	public void populaListaSupermercado(){
		supermercados = new ArrayList<Supermercado>();
		try {
			supermercados = supermercadoDAO.queryForAll();
			Collections.sort(supermercados, new Comparator<Supermercado>() {
			    public int compare(Supermercado s1, Supermercado s2) {
			        return s1.getNome().compareTo(s2.getNome());
			    }
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		mAdapterSupermercado = new ArrayAdapter<Supermercado>(this, android.R.layout.simple_list_item_activated_1, supermercados);
		mAdapterSupermercado = new ArrayAdapter<Supermercado>(this, R.layout.list_view_custom_layout, android.R.id.text1, supermercados);
		
		supermercadoList = (ListView) findViewById(R.id.supermercado_supermercado_list);
		supermercadoList.setAdapter(mAdapterSupermercado);
		supermercadoList.setEmptyView(findViewById(R.id.activity_supermercado_list_empty_view));
		supermercadoList.setOnItemClickListener(goToCompraActivity(this));
		
		supermercadoList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		supermercadoList.setMultiChoiceModeListener(new MultiChoiceModeListener(){

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.supermercado_list_context, menu);				
				
				supermercadosToDelete = new ArrayList<Supermercado>();
				
				textoMenu.setText("1 Selecionado");				
				textoMenu.setTextColor(getResources().getColor(android.R.color.white));
				textoMenu.setClickable(false);				
				menu.add(Menu.NONE, 0, Menu.NONE, "").setActionView(textoMenu).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
				
				return true;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				int id = item.getItemId();
				switch (id) {
		            case R.id.delete_button:
					try {
						for(Supermercado supermercado : supermercadosToDelete){
							List<Compra> compra = compraDAO.queryForEq("supermercado_id", supermercado.getId());
							if(compra.size() == 0){
								supermercadoDAO.deleteById((supermercado.getId()));
							}else{
								Toast toast = Toast.makeText(getApplicationContext(),
										"Supermercados que tenham compras associadas a ele não podem ser apagados!", Toast.LENGTH_SHORT);
								toast.show();
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						populaListaSupermercado();
		                mode.finish(); // Action picked, so close the CAB
		                return true;
		            default:
		                return false;
				}
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				// Here you can do something when items are selected/de-selected,
		        // such as update the title in the menu
				if(checked){
					supermercadosToDelete.add(supermercados.get(position));
				}else{
					supermercadosToDelete.remove(supermercados.get(position));
				}
				
				if(supermercadosToDelete.size() == 1){
					textoMenu.setText(supermercadosToDelete.size() + " Selecionado");
				}else{
					textoMenu.setText(supermercadosToDelete.size() + " Selecionados");
				}
			}
		});
		
	}
	
	/**
	 * Método que redireciona para a interface de compras apóes selecionado um supermercado
	 * @param context
	 * @return
	 */
	public OnItemClickListener goToCompraActivity(final Context context) {
		return (new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> supermercadosList, View selectedItem,
					int position, long id) {
				Intent intent = new Intent(context, CompraActivity.class);
				intent.putExtra("supermercado", supermercados.get(position));
				startActivity(intent);
			}
		});
	}
	
	/**
	 * Método responsávle por pegar o click no botão de novo supermercado e exibir o popup para cadastro do novo supermercado
	 * @param view
	 */
	public void openDialogNovoSupermercado(View view){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogNovoSupermercado dlg = new DialogNovoSupermercado();
		dlg.show(ft, "dialog");
		dlg.getDialog();
	}
	
	public void saveSupermercado(String supermercadoName){
		Supermercado supermercado = new Supermercado(supermercadoName);
		try {
			supermercadoDAO.create(supermercado);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		populaListaSupermercado();
	}
	
	public void startVoice(View view){
		Utils.startVoiceRecognitionActivity(this, Utils.nomeSupermercadoMessage);
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
            nomeSupermercado.setText( Character.toUpperCase(nome.charAt(0)) + nome.substring(1));
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    public void setSupermercadoNameEditText(EditText inputVoice){
    	nomeSupermercado = inputVoice;
    }
	
}