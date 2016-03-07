package algorithms;

import java.awt.Point;
import java.util.ArrayList;

public class PlusCourtChemin {
	int[][] path;
	double[][] distance;

	public PlusCourtChemin(ArrayList<Point> points, int edgeThreshold) {
		path = new int[points.size()][points.size()];
		distance = new double[points.size()][points.size()];
		for (int i = 0; i < points.size(); i++) {
			for (int j = 0; j < points.size(); j++) {
				path[i][j] = j;
				double dist = points.get(i).distance(points.get(j));
				if (dist > edgeThreshold) {
					distance[i][j] = Double.POSITIVE_INFINITY;
				} else {
					distance[i][j] = dist;
				}
			}
		}
		for (int k = 0; k < points.size(); k++) {
			for (int i = 0; i < points.size(); i++) {
				for (int j = 0; j < points.size(); j++) {

					if (distance[i][k] + distance[k][j] < distance[i][j]) {
						distance[i][j] = distance[i][k] + distance[k][j];
						path[i][j] = path[i][k];
					}
				}
			}
		}
	}

	public int[][] getPath() {
		return path;
	}

	public double[][] getDistance() {
		return distance;
	}

}