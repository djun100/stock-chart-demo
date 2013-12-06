package org.stockchart.demo;

import org.stockchart.StockChartActivity;
import org.stockchart.core.Area;
import org.stockchart.core.Axis;
import org.stockchart.core.Axis.Side;
import org.stockchart.demo.utils.StockDataGenerator;
import org.stockchart.demo.utils.StockDataGenerator.Point;
import org.stockchart.series.LinearSeries;
import org.stockchart.series.StockSeries;

import android.os.Bundle;

public class VirtualAxisActivity extends StockChartActivity
{
	private StockSeries fPriceSeries;
	private LinearSeries fOverlaySeries;
	private Area fArea;
	
	private static final int POINTS_COUNT = 150;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		populateChart();
	}
	

	private void populateChart()
	{
		StockDataGenerator gen = new StockDataGenerator();
		double value = 0.0;
		for(int i=0;i<POINTS_COUNT;i++)
		{
			Point p = gen.getNextPoint();
		
			double sin = Math.sin(value);
			fPriceSeries.addPoint(p.o, p.h, p.l, p.c);
			fOverlaySeries.addPoint(sin);
			value+=0.1;
			
			// because our axes have auto feature disabled, we provide maximum and minimum values explicitly 
			Axis a = fArea.getAxis(fPriceSeries.getYAxisSide(),fPriceSeries.getYAxisVirtualId());
			a.getAxisRange().expandValues(p.h, p.l);
			
			a = fArea.getAxis(fOverlaySeries.getYAxisSide(),fOverlaySeries.getYAxisVirtualId());
			a.getAxisRange().expandValues(sin, sin);
		}

	}
	
	@Override
	protected void initChart()
	{
		// create the Area 
		fArea = this.getStockChartView().addArea();
		
		// add virtual axis to the right side
		int virtualAxis = fArea.addVirtualAxis(Side.RIGHT);

		// create series
		fPriceSeries = new StockSeries();
		fPriceSeries.setName("price");				
		fPriceSeries.setYAxisSide(Side.RIGHT);

		fOverlaySeries = new LinearSeries();
		fOverlaySeries.setName("overlay");
		fOverlaySeries.setYAxisSide(Side.RIGHT);
		fOverlaySeries.setYAxisVirtualId(virtualAxis);
				
		// add series to the Area
		fArea.getSeries().add(fPriceSeries);
		fArea.getSeries().add(fOverlaySeries);
		
		// Now we have to turn off the auto feature. Otherwise you'll see strange visual effects, 
		// because different series have different axes. If so, we must provide maximum and
		// minimum values to your AxisRange (see populateChart())
		Axis a = fArea.getAxis(fPriceSeries.getYAxisSide(), fPriceSeries.getYAxisVirtualId());
		a.getAxisRange().setAuto(false);
		a = fArea.getAxis(fOverlaySeries.getYAxisSide(), fOverlaySeries.getYAxisVirtualId());
		a.getAxisRange().setAuto(false);
		
		
		// set bottom axis zoomable
		fArea.getBottomAxis().getAxisRange().setZoomable(true);	
		fArea.getBottomAxis().getAxisRange().setMovable(true);
	}

	@Override
	protected void restoreChart() 
	{
		fPriceSeries = (StockSeries) this.getStockChartView().findSeriesByName("price");
		fOverlaySeries = (LinearSeries) this.getStockChartView().findSeriesByName("overlay");
	}
}
