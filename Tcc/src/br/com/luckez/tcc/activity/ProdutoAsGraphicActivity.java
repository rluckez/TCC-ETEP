package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.dao.ProdutoPorCompraDAO;
import br.com.luckez.tcc.entity.Produto;
import br.com.luckez.tcc.entity.ProdutoPorCompra;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ProdutoAsGraphicActivity extends Activity {

	private DBHelper dbHelper;
	private ProdutoPorCompraDAO produtoPorCompraDAO;
	private List<ProdutoPorCompra> produtoPorCompraList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produto_as_graphic);
		
		dbHelper = new DBHelper(this);
		if(getIntent().hasExtra("produtoSelecionado")){
			try {
				Produto produtoSelecionado = getIntent().getParcelableExtra("produtoSelecionado");
				produtoPorCompraDAO = new ProdutoPorCompraDAO(dbHelper.getConnectionSource());
				produtoPorCompraList = produtoPorCompraDAO.queryForEq("produto_id", produtoSelecionado.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			GraphView graph = (GraphView) findViewById(R.id.activity_produto_as_graphic_chart);
			ArrayList<DataPoint> points = new ArrayList<DataPoint>();
			for(ProdutoPorCompra ppc : produtoPorCompraList){
				points.add(new DataPoint(ppc.getCompra().getDataHora().getTime(), ppc.getPrecoProduto()));
			}
			DataPoint[] pointsAsArray = points.toArray(new DataPoint[0]);
			LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(pointsAsArray);
			graph.addSeries(series);
			
			// titles
	        graph.setTitle("Minhas Compras");
	        graph.getGridLabelRenderer().setVerticalAxisTitle("Valor");        
			
			// set date label formatter
	        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
	        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
	        
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.produto_as_graphic, menu);
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
