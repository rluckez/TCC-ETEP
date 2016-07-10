package br.com.luckez.tcc.dialog;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoDAO;
import br.com.luckez.tcc.entity.Produto;

@SuppressLint("InflateParams")
public class DialogRenameProduto extends DialogFragment {
	
	EditText nomeProduto;
	Produto produtoToRename;
	
	public DialogRenameProduto(Produto produto){
		this.produtoToRename = produto;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view = inflater.inflate(R.layout.dialog_rename_produto, null);
		
		nomeProduto = (EditText) view.findViewById(R.id.dialog_rename_produto_nome);
		nomeProduto.setText(produtoToRename.getNome());
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Alterar nome produto");
			alert.setIcon(R.drawable.ic_launcher);
			alert.setView(view);
			alert.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {				
				
				@Override
	            public void onClick(DialogInterface dialog, int which)
	            {
	                //Do nothing here because we override this button later to change the close behaviour. 
	                //However, we still need this because on older versions of Android unless we 
	                //pass a handler the button doesn't get instantiated
	            }
			});
			
		alert.setNegativeButton("Cancelar", null);
		Dialog d = alert.create();
		//Código para exibir o teclado ao abrir o popup
		d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return d;
	}
	
	public void onStart() {
		
		super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
		
		final AlertDialog d = (AlertDialog)getDialog();
		
		if(d != null){
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					if(TextUtils.isEmpty(nomeProduto.getText().toString())) {
						nomeProduto.setError("Insira o nome do produto");
					}else{
						
						try {
							DBHelper dbHelper = new DBHelper(getActivity());
							ProdutoDAO produtoDAO = new ProdutoDAO(dbHelper.getConnectionSource());
							produtoToRename.setNome(nomeProduto.getText().toString());
							produtoDAO.update(produtoToRename);
							d.dismiss();
							Toast.makeText(getActivity(), "Produto renomeado com sucesso.", Toast.LENGTH_SHORT).show();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
	}
	
	
	@Override
	public void onDismiss(DialogInterface dialog){
		super.onDismiss(dialog);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setCancelable(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override
	public void onCancel(DialogInterface dialog){
		super.onCancel(dialog);
	}
}
