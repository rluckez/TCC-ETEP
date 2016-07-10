package br.com.luckez.tcc.dialog;

import java.sql.SQLException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.activity.CompraActivity;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoPorCompraDAO;
import br.com.luckez.tcc.dao.UnidadeDAO;
import br.com.luckez.tcc.entity.ProdutoPorCompra;
import br.com.luckez.tcc.entity.Unidade;

public class DialogInsereProduto extends DialogFragment {
	
	private ImageButton incrementa;
	private ImageButton decrementa;
	private EditText quantidadePicker;
	private EditText precoField;
	private TextView menorValor;
	private TextView maiorValor;
	private TextView tvNomeProduto;
	private ProdutoPorCompra produtoPorCompraToEdit;
	private Boolean editOrInsert;
	private	DBHelper dbHelper;
	private ProdutoPorCompraDAO produtoPorCompraDAO;
    private UnidadeDAO unidadeDAO;
	private List<Unidade> unidades;
	private ArrayAdapter<Unidade> mAdapter;
	private Spinner unidadesSpinner;
			
	public DialogInsereProduto(ProdutoPorCompra produtoPorCompraToEdit, Boolean editOrInsert) {
		this.produtoPorCompraToEdit = produtoPorCompraToEdit;
		this.editOrInsert = editOrInsert;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setCancelable(true);
		
		dbHelper = new DBHelper(this.getActivity());
		try {
			produtoPorCompraDAO = new ProdutoPorCompraDAO(dbHelper.getConnectionSource());
			unidadeDAO = new UnidadeDAO(dbHelper.getConnectionSource());
			
			unidades = unidadeDAO.queryForAll();
			
			/**
			 * Código responsável por fazer que o item selecinado no spinner seja exibido como sigla e não o nome inteiro
			 */
			mAdapter = new ArrayAdapter<Unidade>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, unidades){
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
			           View v = super.getView(position, convertView, parent); 
			           /**
			            * O TextView usado no layout simple_spinner_dropdown_item tem o id "text1, com isso foi possível alterar
			            * a maneira como o TextView é populado
			            */
			           TextView selectedItemLabel = (TextView) v.findViewById(android.R.id.text1);
			           selectedItemLabel.setText(unidades.get(position).getCode());
			           return v;
			        }
			};

		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//		super.onCreateView(inflater, container, savedInstanceState);
//		
//		View view = inflater.inflate(R.layout.dialog_insere_produto_compra, container);
//		
//		Button botaoCancelar = (Button) view.findViewById(R.id.dialog_insere_produto_botao_cancela);
//		botaoCancelar.setOnClickListener(new Button.OnClickListener(){
//		
//			@Override
//			public void onClick(View v) {
////				dismiss();
//				
//				((CompraActivity) getActivity()).dismissDialog();
//			}
//		});
//		quantidadePicker = (NumberPicker) view.findViewById(R.id.dialog_insere_produto_quantidade_number_picker);
//		Button botaoAdicionar = (Button) view.findViewById(R.id.dialog_insere_produto_botao_add);
//		botaoAdicionar.setOnClickListener(new Button.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				
//				int quantidade = quantidadePicker.getValue();				
//		        
//		        CompraActivity callingActivity = (CompraActivity) getActivity();
//		        callingActivity.teste(quantidade);
//		        callingActivity.dismissDialog();
//			}
//			
//		});
//		
//		NumberPicker quantidadePicker = (NumberPicker) view.findViewById(R.id.dialog_insere_produto_quantidade_number_picker);
//		quantidadePicker.setMaxValue(100);
//		quantidadePicker.setMinValue(1);
//		quantidadePicker.setWrapSelectorWheel(false);
//		quantidadePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//			
//			@Override
//			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		return view;
//	}
	
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
	
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		/**
		 * Inicializa os campos da view
		 */
		View view = inflater.inflate(R.layout.dialog_insere_produto_compra, null);
		menorValor = (TextView) view.findViewById(R.id.dialog_insere_produto_menor_valor);
		maiorValor = (TextView) view.findViewById(R.id.dialog_insere_produto_maior_valor);
		tvNomeProduto = (TextView) view.findViewById(R.id.dialog_insere_produto_nome_produto);
		tvNomeProduto.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		tvNomeProduto.setSingleLine(true);
		tvNomeProduto.setMarqueeRepeatLimit(-1);
		tvNomeProduto.setSelected(true);
		
		precoField = (EditText) view.findViewById(R.id.dialog_insere_produto_valor);
		/**
		 * Necessário para formatar o input de moeda e exibir o símbolo monetário e usar apenas 2 decimals
		 */
//		precoField.setRawInputType(Configuration.KEYBOARD_12KEY);
		precoField.addTextChangedListener(new TextWatcher() {
	        public void afterTextChanged(Editable s) {}
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	            if(!s.toString().matches("^[R]\\$(\\d{1,3}(\\.\\d{3})*|(\\d+))(\\,\\d{2})?$"))
	            {
	                String userInput= "" + s.toString().replaceAll("[^\\d]", "");
	                StringBuilder cashAmountBuilder = new StringBuilder(userInput);

	                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
	                    cashAmountBuilder.deleteCharAt(0);
	                }
	                while (cashAmountBuilder.length() < 3) {
	                    cashAmountBuilder.insert(0, '0');
	                }
	                cashAmountBuilder.insert(cashAmountBuilder.length()-2, ',');
	                cashAmountBuilder.insert(0, "R$");

	                precoField.setText(cashAmountBuilder.toString());
	                // keeps the cursor always to the right
	                Selection.setSelection(precoField.getText(), cashAmountBuilder.toString().length());

	            }

	        }
	    });
		
		unidadesSpinner = (Spinner) view.findViewById(R.id.dialog_insere_produto_spinner_unidade);
		unidadesSpinner.setAdapter(mAdapter);
		/**
		 * Troca o label para informar o usuário se o preço é unitário, por kg, dúzia, etc.
		 */
		final TextView labelDoPreco = (TextView) view.findViewById(R.id.dialog_insere_produto_preco_unidade_label);
		unidadesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(unidades.size() != 0){
					labelDoPreco.setText("Preço por " + unidades.get(position).getName());
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		quantidadePicker = (EditText) view.findViewById(R.id.dialog_insere_produto_quantidade_number_picker);
		quantidadePicker.setText(Integer.valueOf(1).toString());
		
		/**
		 * Cria os métodos responsáveis por aumentar e diminuir o valor da quantidade
		 */
		incrementa = (ImageButton) view.findViewById(R.id.dialog_insere_produto_incrementa_quantidade_button);
		incrementa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String quantidade = quantidadePicker.getText().toString();
				if(quantidade.equalsIgnoreCase("")){
					quantidadePicker.setText("1.0");
				}else{
					quantidadePicker.setText(Double.valueOf((Double.parseDouble(quantidade) + 1.0)).toString());
				}
			}
		});
		
		decrementa = (ImageButton) view.findViewById(R.id.dialog_insere_produto_decrementa_quantidade_button);
		decrementa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String quantidade = quantidadePicker.getText().toString();
				if(quantidade.equalsIgnoreCase("")){
					quantidadePicker.setText("1.0");
				}else if(Double.valueOf(quantidadePicker.getText().toString()) > 1.0){
					quantidadePicker.setText(Double.valueOf((Double.parseDouble(quantidade) - 1.0)).toString());
				}
			}
		});
		
		/**
		 * Faz a pré seleção do spinner de acordo com a unidade que o produto editado tem
		 */
//		if(editOrInsert == CompraActivity.EDIT){
//			for(int i = 0; i< mAdapter.getCount(); i++){
//				if(mAdapter.getItem(i).getId() == produtoPorCompraToEdit.getUnidade().getId()){
//					unidadesSpinner.setSelection(i);
//					break;
//				}
//			}
//		}
		
		tvNomeProduto.setText(produtoPorCompraToEdit.getProduto().getNome());
		
		String produtoNaoComprado = "Produto nunca comprado";
		
		Double maiorPreco = produtoPorCompraDAO.selectMaxPreco(dbHelper, produtoPorCompraToEdit.getProduto().getId());
		Double menorPreco = produtoPorCompraDAO.selectMinPreco(dbHelper, produtoPorCompraToEdit.getProduto().getId());
		
		if(maiorPreco != null){
			maiorValor.setText("R$" + maiorPreco.toString());
		}else{
			maiorValor.setText(produtoNaoComprado);
		}
		
		if(menorPreco != null){
			menorValor.setText("R$" + menorPreco.toString());
		}else{
			menorValor.setText(produtoNaoComprado);
		}
		
		quantidadePicker.setText(produtoPorCompraToEdit.getQuantidade().toString());
//		precoField.setText(produtoPorCompraToEdit.getPrecoProduto().toString());
		
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		String textoBotaoOk;
		if(editOrInsert == CompraActivity.INSERT){
			alert.setTitle("Inserir produto na compra");
			textoBotaoOk = "Inserir";
			
			//Seleciona por default a unidade de quantidade de um produto inserido como Unidade
			for (int i = 0; i < mAdapter.getCount(); i++) {
				if(mAdapter.getItem(i).getName().equalsIgnoreCase("Unidade")){
					unidadesSpinner.setSelection(i);
					break;
				}
			}
		}else{
			alert.setTitle("Alterar produto da compra");
			textoBotaoOk = "Alterar";
			String preco = produtoPorCompraToEdit.getPrecoProduto().toString();
			//Código usado para verificar se o npumero possui apenas 1 casa decimal após a vírgula
			String verificaDecimal = String.valueOf(preco.charAt(preco.length() - 2));
			//Concatena um zero no final da String para que o valor exibido seja correto
			if((".").equals(verificaDecimal)){
				preco = preco + "0";
			}
			precoField.setText(preco);
			for(int i = 0; i< mAdapter.getCount(); i++){
				if(mAdapter.getItem(i).getId() == produtoPorCompraToEdit.getUnidade().getId()){
					unidadesSpinner.setSelection(i);
					break;
				}
			}
		}
		alert.setIcon(R.drawable.ic_launcher);
		alert.setView(view);
		alert.setPositiveButton(textoBotaoOk, new DialogInterface.OnClickListener() {
			
			@Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Do nothing here because we override this button later to change the close behaviour. 
                //However, we still need this because on older versions of Android unless we 
                //pass a handler the button doesn't get instantiated
            }
		});
			
		alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				((CompraActivity) getActivity()).enableListToClick();
				
			}
		});
		
		Dialog d = alert.create();
		//Método responsável por catpurar o evento de pressionar a tecla voltar e fazer o desbloqueio da lista de compras
		//TODO Ficar de olho no comportamento
		d.setOnKeyListener(new Dialog.OnKeyListener() {
	        @Override
	        public boolean onKey(DialogInterface dialog, int keyCode,
	                KeyEvent event) {
	            if (keyCode == KeyEvent.KEYCODE_BACK) {
			        ((CompraActivity) getActivity()).enableListToClick();
			        dialog.cancel();
	                return true;
	            }
	            return false;
	        }
	    });
		return d;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog){
		super.onDismiss(dialog);
	}
	
	/**
	 * Método utilizado para pegar o listener do positive button e conseguir setar erro em algum campo do dialog
	 * sem que o dialog seja fechado
	 */
	@Override
	public void onStart(){
		
		super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
		final AlertDialog d = (AlertDialog)getDialog();
		if(d != null)
		{
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					CompraActivity callingActivity = (CompraActivity) getActivity();
					/**
					 * Verifica se o preço do produto está vazio, se sim ñ fecha o dialog
					 */
					if (TextUtils.isEmpty(precoField.getText().toString())) {
						precoField.setError("Insira o preço do produto");
					}else if(TextUtils.isEmpty(quantidadePicker.getText().toString())){
						quantidadePicker.setError("Insira uma quantidade");
					}else{
						StringBuilder sb = new StringBuilder(precoField.getText().toString());
						//Troca a vírgula por ponto para usar o número
						sb.replace(sb.indexOf(","), sb.indexOf(",") + 1, ".");
						//Deleta o símbolo de moeda
						sb.delete(0, 2);
						Double preco = Double.parseDouble(sb.toString());
						//Valida se o preço está zerado
						if(preco == 0.0){
							precoField.setError("Preço não pode ser zero");
						}else{
							Double quantidade = Double.parseDouble(quantidadePicker.getText().toString());
							if(quantidade == 0.0){
								quantidadePicker.setError("Quantidade não pode ser zero");
							}else{
								if (editOrInsert == CompraActivity.INSERT) {
									ProdutoPorCompra ppc = new ProdutoPorCompra();
									ppc.setQuantidade(quantidade);
									ppc.setPrecoProduto(preco);
									ppc.setUnidade((Unidade) unidadesSpinner.getSelectedItem());
									callingActivity.insereProduto(ppc);
								} else {
									produtoPorCompraToEdit.setQuantidade(quantidade);
									produtoPorCompraToEdit.setPrecoProduto(preco);
									produtoPorCompraToEdit.setUnidade((Unidade) unidadesSpinner.getSelectedItem());
									callingActivity.salvaProdutoEditado(produtoPorCompraToEdit);
								}
								d.dismiss();
							}
						}
					}
				}
			});
		}
	}
}
