package geogebra.web.euclidian;

import geogebra.common.awt.GPoint;
import geogebra.common.euclidian.EuclidianPen;
import geogebra.common.euclidian.EuclidianView;
import geogebra.common.kernel.geos.GeoConic;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoPoint;
import geogebra.common.kernel.geos.GeoPolygon;
import geogebra.common.kernel.geos.Test;
import geogebra.common.kernel.kernelND.GeoPointND;
import geogebra.common.main.App;

import java.util.ArrayList;

public class EuclidianPenFreehand extends EuclidianPen {

	/**
	 * type that is expected to be created
	 */
	public enum ShapeType {
		circle, polygon, rigidPolygon, vectorPolygon;
	}

	private ShapeType expected = null;
	private GeoElement lastCreated = null;

	public EuclidianPenFreehand(App app, EuclidianView view) {
		super(app, view);
		super.setFreehand(true);
	}

	/**
	 * will be ignored - always freehand
	 */
	@Override
	public void setFreehand(boolean b) {
		// don't do anything
	}

	/**
	 * @param expectedType
	 *            defines the expected shape
	 */
	public void setExpected(ShapeType expectedType) {
		this.expected = expectedType;

		resetParameters();
		switch (expected) {
		case circle:
			CIRCLE_MAX_SCORE = 0.15;
			CIRCLE_MIN_DET = 0.9;
			break;
		case polygon:
		case rigidPolygon:
		case vectorPolygon:
			RECTANGLE_LINEAR_TOLERANCE = 0.25;
			POLYGON_LINEAR_TOLERANCE = 0.25;
			RECTANGLE_ANGLE_TOLERANCE = 17 * Math.PI / 180;
			break;
		}
	}

	/**
	 * @return the GeoElement that was created the last time, null in case
	 *         creating failed
	 */
	public GeoElement getCreatedShape() {
		return lastCreated;
	}

	@Override
	public void handleMouseReleasedForPenMode(boolean right, int x, int y) {
		if (this.expected == ShapeType.circle) {
			ArrayList<GeoPoint> list = new ArrayList<GeoPoint>();
			for (GPoint p : this.penPoints) {
				this.view.setHits(p, this.view.getEuclidianController()
				        .getDefaultEventType());
				if (this.view.getHits().containsGeoPoint()) {
					GeoPoint point = (GeoPoint) this.view.getHits()
					        .getFirstHit(Test.GEOPOINT);
					if (!list.contains(point)) {
						list.add(point);
					}
				}
			}

			if (list.size() >= 3) {
				this.app.getKernel().getAlgoDispatcher()
				        .Circle(null, list.get(0), list.get(1), list.get(2));
			}
		}

		lastCreated = checkShapes(x, y);

		if (lastCreated == null) {
			return;
		}

		switch (this.expected) {
		case circle:
			if (lastCreated instanceof GeoConic) {
				if (this.initialPoint != null) {
					this.initialPoint.remove();
				}

				for (GeoPointND geo : ((GeoConic) lastCreated)
				        .getPointsOnConic()) {
					if (!geo.isLabelSet()) {
						geo.setLabel(null);
					}
				}
				return;
			}
			break;
		case polygon:
			if (lastCreated instanceof GeoPolygon) {
				return;
			}
			break;
		case rigidPolygon:
			if (lastCreated instanceof GeoPolygon) {
				ArrayList<GeoPoint> list = new ArrayList<GeoPoint>();
				for (GeoPointND point : ((GeoPolygon) lastCreated).getPoints()) {
					if (point instanceof GeoPoint) {
						list.add((GeoPoint) point);
					}
				}
				if (list.size() == ((GeoPolygon) lastCreated).getPoints().length) {
					// true if all the points are GeoPoints, otherwise the
					// original Polygon will not be deleted
					lastCreated.remove();
					this.app.getKernel().RigidPolygon(null,
					        list.toArray(new GeoPoint[0]));
				}
				return;
			}
			break;
		case vectorPolygon:
			if (lastCreated instanceof GeoPolygon) {
				ArrayList<GeoPoint> list = new ArrayList<GeoPoint>();
				for (GeoPointND point : ((GeoPolygon) lastCreated).getPoints()) {
					if (point instanceof GeoPoint) {
						list.add((GeoPoint) point);
					}
				}
				if (list.size() == ((GeoPolygon) lastCreated).getPoints().length) {
					// true if all the points are GeoPoints, otherwise the
					// original Polygon will not be deleted
					lastCreated.remove();
					this.app.getKernel().VectorPolygon(null,
					        list.toArray(new GeoPoint[0]));
				}
				return;
			}
			break;
		}

		// shape is not of the expected type -> delete it
		for (GeoPointND geo : lastCreated.getParentAlgorithm()
		        .getFreeInputPoints()) {
			geo.remove();
		}
		lastCreated.remove();
		lastCreated = null;
	}

	private void resetParameters() {
		CIRCLE_MIN_DET = 0.95;
		CIRCLE_MAX_SCORE = 0.10;
		RECTANGLE_LINEAR_TOLERANCE = 0.20;
		POLYGON_LINEAR_TOLERANCE = 0.20;
		RECTANGLE_ANGLE_TOLERANCE = 15 * Math.PI / 180;
	}

}
