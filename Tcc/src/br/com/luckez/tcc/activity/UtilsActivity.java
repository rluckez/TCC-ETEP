package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.R.id;
import br.com.luckez.tcc.R.layout;
import br.com.luckez.tcc.R.menu;
import br.com.luckez.tcc.dao.CompraDAO;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoDAO;
import br.com.luckez.tcc.dao.ProdutoPorCompraDAO;
import br.com.luckez.tcc.entity.Compra;
import br.com.luckez.tcc.entity.Produto;
import br.com.luckez.tcc.entity.ProdutoPorCompra;
import br.com.luckez.tcc.utils.BarcodeImageGenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.j256.ormlite.support.ConnectionSource;

public class UtilsActivity extends Activity {

	private DBHelper dbHelper;
	private ProdutoDAO produtoDAO;
	private CompraDAO compraDAO;
	private ProdutoPorCompraDAO ProdutoPorCompraDAO;
	private Toast toast;
	// barcode image
	Bitmap bitmap;
	ImageView iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_utils);

		dbHelper = new DBHelper(this);
				
		// barcode data
		String barcode_data = "123";

		// barcode image
		bitmap = null;
		iv = (ImageView) findViewById(R.id.iv_barcode);

		try {
			bitmap = BarcodeImageGenerator.encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
			iv.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		} 

		// barcode text
		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setText(barcode_data);

		try {
			ConnectionSource cs = dbHelper.getConnectionSource();
			produtoDAO = new ProdutoDAO(cs);
			compraDAO = new CompraDAO(cs);
			ProdutoPorCompraDAO = new ProdutoPorCompraDAO(cs);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateBarcode(View v){
		String text = "1";
		Random r = new Random();
		for(int i = 0; i < Math.random() * 10; i++){
			try {
				bitmap = BarcodeImageGenerator.encodeAsBitmap(text, BarcodeFormat.CODE_128, 600, 300);
				iv.setImageBitmap(bitmap);
				text += r.nextInt(9);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.utils, menu);
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

	public void deletaProdutos(View view) {
		List<Compra> compras = new ArrayList<Compra>();
		List<Produto> produtos = new ArrayList<Produto>();
		List<ProdutoPorCompra> ppcs = new ArrayList<ProdutoPorCompra>();

		List<Integer> ids = new ArrayList<Integer>();

		try {
			compras = compraDAO.queryForAll();
			for (Compra compra : compras) {
				ids.add(compra.getId());
			}
			compraDAO.deleteIds(ids);
			ids = new ArrayList<Integer>();

			ppcs = ProdutoPorCompraDAO.queryForAll();
			for (ProdutoPorCompra ppc : ppcs) {
				ids.add(ppc.getId());
			}
			ProdutoPorCompraDAO.deleteIds(ids);
			ids = new ArrayList<Integer>();

			produtos = produtoDAO.queryForAll();
			for (Produto produto : produtos) {
				ids.add(produto.getId());
			}
			produtoDAO.deleteIds(ids);
			ids = new ArrayList<Integer>();

			toast = Toast.makeText(getApplicationContext(),
					"Produtos deletados com sucesso!", Toast.LENGTH_LONG);
			toast.show();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
