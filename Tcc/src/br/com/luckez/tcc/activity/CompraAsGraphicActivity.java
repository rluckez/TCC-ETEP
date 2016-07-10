package br.com.luckez.tcc.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import br.com.luckez.tcc.R;
import br.com.luckez.tcc.dao.CompraDAO;
import br.com.luckez.tcc.dao.DBHelper;
import br.com.luckez.tcc.entity.Compra;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class CompraAsGraphicActivity extends Activity {

	private DBHelper dbHelper;
	private CompraDAO compraDAO;
	private List<Compra> mCompras;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compra_as_graphic);
		
		dbHelper = new DBHelper(this);
		try {
			compraDAO = new CompraDAO(dbHelper.getConnectionSource());
			mCompras = compraDAO.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		GraphView graph = (GraphView) findViewById(R.id.activity_compras_as_graphic_chart);
		ArrayList<DataPoint> points = new ArrayList<DataPoint>();
		for(Compra compra : mCompras){
			points.add(new DataPoint(compra.getDataHora().getTime(), compra.getValor()));
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

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(mCompras.get(0).getDataHora().getTime());
        graph.getViewport().setMaxX(mCompras.get(mCompras.size() - 1).getDataHora().getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        
        graph.onDataChanged(false, false);
        
     // enable scaling
        graph.getViewport().setScalable(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compra_as_graphic, menu);
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
