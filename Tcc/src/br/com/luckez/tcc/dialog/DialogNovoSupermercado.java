package br.com.luckez.tcc.dialog;

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
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.activity.SupermercadoListActivity;

public class DialogNovoSupermercado extends DialogFragment {
	
	EditText nomeSupermercado;
	
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view = inflater.inflate(R.layout.dialog_novo_supermercado, null);
		
		nomeSupermercado = (EditText) view.findViewById(R.id.dialog_novo_supermercado_nome_supermercado);
		//Responsável por settar o editText na activity de lista de supermercado, para que o onAcitivityResult possa trocar o texto no dialog
		SupermercadoListActivity callingActivity = (SupermercadoListActivity) getActivity();
		callingActivity.setSupermercadoNameEditText(nomeSupermercado);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Novo supermercado");
			alert.setIcon(R.drawable.ic_launcher);
			alert.setView(view);
			alert.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {				
				
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
					if(TextUtils.isEmpty(nomeSupermercado.getText().toString())) {
						nomeSupermercado.setError("Insira o nome do supermercado");
					}else{
						SupermercadoListActivity callingActivity = (SupermercadoListActivity) getActivity();
						callingActivity.saveSupermercado(nomeSupermercado.getText().toString());
						d.dismiss();
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
