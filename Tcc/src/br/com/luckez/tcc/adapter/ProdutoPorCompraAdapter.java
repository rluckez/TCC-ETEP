package br.com.luckez.tcc.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.entity.ProdutoPorCompra;

public class ProdutoPorCompraAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<ProdutoPorCompra> mLista;
	
	public ProdutoPorCompraAdapter(Context context, ArrayList<ProdutoPorCompra> lista) {
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
		return mLista.get(position).getProduto().getId();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ProdutoPorCompra produtoPorCompra = mLista.get(position);
		View layout;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.list_view_produto_por_compra_layout, null);
		}else{
			layout = convertView;
		}
		
		TextView tvProdutoNome = (TextView) layout.findViewById(R.id.list_view_produto_por_compra_produto);
		tvProdutoNome.setText(produtoPorCompra.getProduto().getNome());
		tvProdutoNome.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		tvProdutoNome.setSingleLine(true);
		tvProdutoNome.setMarqueeRepeatLimit(-1);
		tvProdutoNome.setSelected(true);
		
		TextView tvQuantidade = (TextView) layout.findViewById(R.id.list_view_produto_por_compra_quantidade);
		tvQuantidade.setText("Quantidade: " + produtoPorCompra.getQuantidade().toString() + " " + produtoPorCompra.getUnidade().getCode());
		
		TextView tvPreco = (TextView) layout.findViewById(R.id.list_view_produto_por_compra_preco);
		tvPreco.setText("Preço: R$ " + new DecimalFormat("0.00").format(produtoPorCompra.getPrecoProduto()).toString());
		
		TextView tvTotal = (TextView) layout.findViewById(R.id.list_view_produto_por_compra_total);
		tvTotal.setText("R$ " + new DecimalFormat("0.00").format((produtoPorCompra.getPrecoProduto() * produtoPorCompra.getQuantidade())));
		
		TextView tvLabelTotal = (TextView) layout.findViewById(R.id.list_view_produto_por_compra_label_total);
		tvLabelTotal.setText("Total:");
		
		return layout;
	}

}
