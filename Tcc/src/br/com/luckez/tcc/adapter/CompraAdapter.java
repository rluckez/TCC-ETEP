package br.com.luckez.tcc.adapter;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.entity.Compra;

public class CompraAdapter extends BaseAdapter{
	
	private Context context;
	private List<Compra> mLista;
	
	public CompraAdapter(Context context, List<Compra> lista) {
		this.context = context;
		this.mLista = lista;
	}

	@Override
	public int getCount() {
		return mLista.size();
	}

	@Override
	public Object getItem(int position) {
		return mLista.get(position);
	}

	@Override
	public long getItemId(int position) {
		//TODO Alterar o id que é buscado
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Compra compra = mLista.get(position);
		View layout;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.list_view_compra_layout, null);
		}else{
			layout = convertView;
		}
		
		TextView tvData = (TextView) layout.findViewById(R.id.list_view_compra_data);
		Format formatter = new SimpleDateFormat("E, dd/MM/yy - HH:mm:ss", new Locale("pt","br"));
		String s = formatter.format(compra.getDataHora());
		tvData.setText(s);
		
		TextView tvSupermercado = (TextView) layout.findViewById(R.id.list_view_compra_supermercado);
		tvSupermercado.setText(compra.getSupermercado().getNome());
		
		TextView tvQuantidade = (TextView) layout.findViewById(R.id.list_view_compra_quantidade);
		tvQuantidade.setText(compra.getNumeroDeProdutos().toString() + " produto(s)");
		
		TextView tvValor = (TextView) layout.findViewById(R.id.list_view_compra_valor);
		tvValor.setText("R$ " + new DecimalFormat("0.00").format(compra.getValor()).toString());
		
		return layout;
	}

}
