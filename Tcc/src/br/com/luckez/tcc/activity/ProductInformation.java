package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.R.id;
import br.com.luckez.tcc.R.layout;
import br.com.luckez.tcc.R.menu;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoPorCompraDAO;
import br.com.luckez.tcc.entity.Produto;

public class ProductInformation extends Activity {

	private DBHelper dbHelper;
	private ProdutoPorCompraDAO produtoPorCompraDAO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_information);
		
		dbHelper = new DBHelper(this);
		if(getIntent().hasExtra("produtoSelecionado")){
			try {
				Produto produtoSelecionado = getIntent().getParcelableExtra("produtoSelecionado");
				produtoPorCompraDAO = new ProdutoPorCompraDAO(dbHelper.getConnectionSource());
				
				TextView tvProductName = (TextView) findViewById(R.id.activity_product_information_product_name);
				tvProductName.setText(produtoSelecionado.getNome());
				tvProductName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
				tvProductName.setSingleLine(true);
				tvProductName.setMarqueeRepeatLimit(-1);
				tvProductName.setSelected(true);
				
				TextView tvProductCode = (TextView) findViewById(R.id.activity_product_information_product_code);
				StringBuilder str = new StringBuilder(produtoSelecionado.getCodigo().toString());
				str.insert(1, " ");
				str.insert(8, " ");
				tvProductCode.setText(str);
				
				TextView tvProductCount = (TextView) findViewById(R.id.activity_product_information_product_count);
				Integer count = produtoPorCompraDAO.countProdutoCompras(dbHelper, produtoSelecionado.getId());
				tvProductCount.setText(count.toString() + " vez(es)");
				
				DecimalFormat df = new DecimalFormat("#.00", DecimalFormatSymbols.getInstance(Locale.GERMAN));
				
				TextView tvProductMinValue = (TextView) findViewById(R.id.activity_product_information_product_min_price);
				Double minValue = produtoPorCompraDAO.selectMinPreco(dbHelper, produtoSelecionado.getId());
				if(minValue != null){
					tvProductMinValue.setText("R$ " + df.format(minValue));
				}else{
					tvProductMinValue.setText("Produto nunca comprado.");
				}
				
				TextView tvProductMaxValue = (TextView) findViewById(R.id.activity_product_information_product_max_price);
				Double maxValue = produtoPorCompraDAO.selectMaxPreco(dbHelper, produtoSelecionado.getId());
				if(maxValue != null){
					tvProductMaxValue.setText("R$ " + df.format(maxValue));
				}else{
					tvProductMaxValue.setText("Produto nunca comprado.");
				}
//				
				TextView tvProductTotalSpent = (TextView) findViewById(R.id.activity_product_information_product_total_spent);
				Double sum = produtoPorCompraDAO.sumtTotalCompradoPorProduto(dbHelper, produtoSelecionado.getId());
				if(sum != null){
					tvProductTotalSpent.setText("R$ " + df.format(sum));
				}else{
					tvProductTotalSpent.setText("R$ 0,00");
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_information, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
