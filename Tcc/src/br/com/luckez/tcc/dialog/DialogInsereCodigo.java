package br.com.luckez.tcc.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.activity.CompraActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;

public class DialogInsereCodigo extends DialogFragment {
	
	private EditText etCodigo;
//	private Bitmap bitmap;
	private ImageView iv;
	
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view = inflater.inflate(R.layout.dialog_insere_codigo, null);
		
		iv = (ImageView) view.findViewById(R.id.dialog_insere_codigo_image_view_codigo);
//		try {
//			bitmap = BarcodeImageGenerator.encodeAsBitmap("123456", BarcodeFormat.CODE_128, 600, 300);
//		} catch (WriterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		iv.setImageBitmap(bitmap);	
		
		etCodigo = (EditText) view.findViewById(R.id.dialog_insere_codigo_codigo);
		etCodigo.addTextChangedListener(new TextWatcher() {
	        public void afterTextChanged(Editable s) {}
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	final AlertDialog d = (AlertDialog)getDialog();
	        	if(d!= null){
    				Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
    				if(s.length() == 13){
	        			try {
			        		BitMatrix bitMatrix = new EAN13Writer().encode(s.toString(), BarcodeFormat.EAN_13, 600, 300);
			        		int height = bitMatrix.getHeight();
			        	    int width = bitMatrix.getWidth();
			        	    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			        	    for (int x = 0; x < width; x++){
			        	        for (int y = 0; y < height; y++){
			        	            bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
			        	        }
			        	    }
	//						bitmap = BarcodeImageGenerator.encodeAsBitmap(s.toString(), BarcodeFormat.CODE_128, 600, 300);
							iv.setImageBitmap(bmp);
		    	                positiveButton.setEnabled(true);
	        			} catch (Exception e) {
						// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(getActivity(), "Código inválido", Toast.LENGTH_LONG).show();
	        			}
    				}else{
    					positiveButton.setEnabled(false);
    					iv.setImageBitmap(null);
    				}
        		}
	        }
	    });
		
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Adicionar produto");
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
					//TODO Validar se o padrão do código inserido é um EAN-13
					if(TextUtils.isEmpty(etCodigo.getText().toString())) {
						etCodigo.setError("Insira o um código de barras válido");
					}else{
						CompraActivity callingActivity = (CompraActivity) getActivity();
						callingActivity.processa(etCodigo.getText().toString());
						d.dismiss();
					}
				}
			});
	        positiveButton.setEnabled(false);
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
