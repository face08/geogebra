package geogebra.web.gui.layout.panels;

import geogebra.common.euclidian.EuclidianConstants;
import geogebra.common.main.App;
import geogebra.common.main.settings.SpreadsheetSettings;
import geogebra.web.gui.app.SpreadsheetStyleBarPanel;
import geogebra.web.gui.app.VerticalPanelSmart;
import geogebra.web.gui.layout.DockPanelW;
import geogebra.web.gui.view.spreadsheet.MyTableW;
import geogebra.web.gui.view.spreadsheet.SpreadsheetViewW;
import geogebra.web.main.AppW;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Arpad Fekete
 * 
 * Top level GUI for the spreadsheet view
 *
 */
public class SpreadsheetDockPanelW extends DockPanelW {

	App application = null;

	SimpleLayoutPanel toplevel;

	VerticalPanelSmart ancestor;
	SpreadsheetStyleBarPanel sstylebar;
	SpreadsheetViewW sview;
	
	protected SpreadsheetDockPanelW dockPanel;

	public SpreadsheetDockPanelW(App app) {
		super(
				App.VIEW_SPREADSHEET, 		// view id
				"Spreadsheet", 						// view title phrase
				getDefaultToolbar(),				// toolbar string
				true,								// style bar?
				3, 									// menu order
				'S'									// menu shortcut
			);
		
		//initWidget(toplevel = new SimpleLayoutPanel());
		//ancestor = new VerticalPanelSmart();
		//ancestor.add(sstylebar = new SpreadsheetStyleBarPanel());
		//toplevel.add(ancestor);
		
		application = app;
		this.dockPanel = this;
	}

	protected Widget loadComponent() {
		
		sview = ((AppW)application).getGuiManager().getSpreadsheetView();
					
		return sview;
	}

	protected Widget loadStyleBar() {
		if (sstylebar == null) {
			sstylebar = new SpreadsheetStyleBarPanel();
		}
		return sstylebar;
	}

	public void onResize() {
		super.onResize();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {

				if (application != null) {

					if (sview != null) {

						int width2 = ((MyTableW) sview.getSpreadsheetTable())
						        .getOffsetWidth();
						int height2 = ((MyTableW) sview.getSpreadsheetTable())
						        .getOffsetHeight();

						sview.getFocusPanel().setWidth(width2 + "px");
						sview.getFocusPanel().setHeight(height2 + "px");


						int width = dockPanel.getComponentInteriorWidth();
						int height = dockPanel.getComponentInteriorHeight();

						if (width < 0 || height < 0) {
							return;
						}

						sview.getScrollPanel().setWidth(width + "px");
						sview.getScrollPanel().setHeight(height + "px");
					}
				}

			}
		});

	}

	public void attachApp(App app) {
		super.attachApp(app);
		this.application = app;
	}

	public SpreadsheetViewW getSpreadsheet() {
		return sview;
	}

	public void showView(boolean show) {

		if (application == null) return;

		// imperfect yet
		if (show && sview == null) {
			sview = ((AppW)application).getGuiManager().getSpreadsheetView();
			((MyTableW)sview.getSpreadsheetTable()).setRepaintAll();
			ancestor.add(sview);
			((MyTableW)sview.getSpreadsheetTable()).repaint();
			onResize();
		} else if (!show && sview != null) {
			ancestor.remove(sview);
			sview = null;
			onResize();
		}
	}

	public App getApp() {
	    return application;
    }

	private static String getDefaultToolbar() {
		StringBuilder sb = new StringBuilder();
		sb.append(EuclidianConstants.MODE_MOVE);
		sb.append(" ");		
		sb.append(EuclidianConstants.MODE_RECORD_TO_SPREADSHEET);
		
		sb.append(" || ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_ONEVARSTATS);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_TWOVARSTATS);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_MULTIVARSTATS);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_PROBABILITY_CALCULATOR);
		
		sb.append(" || ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_CREATE_LIST);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_CREATE_LISTOFPOINTS);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_CREATE_MATRIX);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_CREATE_TABLETEXT);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_CREATE_POLYLINE);
		
		sb.append(" || ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_SUM);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_AVERAGE);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_COUNT);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_MAX);
		sb.append(" , ");
		sb.append(EuclidianConstants.MODE_SPREADSHEET_MIN);
		

		return sb.toString();
	}

	@Override 
	public boolean isStyleBarVisible() {

		if (!app.isApplet()) {
			return super.isStyleBarVisible();
		}

		SpreadsheetSettings settings = app.getSettings().getSpreadsheet(); 

		// currently no GUI / XML for hiding the style-bar
		// hide in applets if the row/column headers are missing
		return super.isStyleBarVisible() && settings.showRowHeader() && settings.showColumnHeader();
	}

	@Override
	public boolean hasStyleBar() {
		SpreadsheetSettings settings = app.getSettings().getSpreadsheet();

		if (settings == null)
			return super.hasStyleBar();

		return super.hasStyleBar() && settings.showRowHeader() && settings.showColumnHeader();
	}
}
