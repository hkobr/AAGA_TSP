package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

public class DefaultTeam {
	public ArrayList<Point> calculAngularTSP(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		PlusCourtChemin pcc = new PlusCourtChemin(points, edgeThreshold);
		int[][] path = calculPlusCourtsChemins(points, edgeThreshold);

		double distMin = Double.MAX_VALUE, dist;
		ArrayList<Point> list = null;

		for (Point origin : hitPoints) {
			ArrayList<Point> tmp = voyage(origin, points, hitPoints, path);
			dist = Evaluator.score(tmp);
			if (dist < distMin) {
				distMin = dist;
				list = tmp;
			}
		}

		//System.out.println("Ancienne taille : " + distanceTotale(list));
		list = ajoutPointsBleus(list, points, pcc);
		return list;
	}

	public static int findPointIdInList(Point p, final ArrayList<Point> points) {
		for (int i = 0; i < points.size(); i++) {
			Point tmp = points.get(i);
			if (tmp.equals(p))
				return i;
		}
		//System.out.println("Point " + p.toString() + " not in list !!");
		return -1;
	}

	public static ArrayList<Integer> searchPoints(int origin, int destination, int[][] path) {
		ArrayList<Integer> res = new ArrayList<>();
		int k = origin;
		while (k != destination) {
			k = path[k][destination];
			res.add(k);
		}
		return res;
	}

	public static ArrayList<Point> searchPoints(Point pOrigin, Point pDestination, int[][] path,
			ArrayList<Point> points) {
		int origin = findPointIdInList(pOrigin, points);
		int destination = findPointIdInList(pDestination, points);
		ArrayList<Point> res = new ArrayList<>();
		int k = origin;
		while (k != destination) {
			k = path[k][destination];
			res.add(points.get(k));
		}
		return res;
	}

	public static ArrayList<Point> ajoutPointsBleus(ArrayList<Point> trajet, ArrayList<Point> points,
			PlusCourtChemin pcc) {
		ArrayList<Point> res = new ArrayList<>();
		for (int i = 0; i < trajet.size() - 1; i++) {
			Point p1 = trajet.get(i);
			Point p2 = trajet.get(i + 1);

			res.add(p1);
			res.addAll(searchPoints(p1, p2, pcc.getPath(), points));
		}
		if (trajet.size() > 1)
			res.addAll(searchPoints(trajet.get(trajet.size() - 1), trajet.get(0), pcc.getPath(), points));
		return res;
	}

	static double distanceTotale(ArrayList<Point> points) {
		if (points.size() == 0)
			return 0;
		double distance;
		Point p0 = points.get(0);
		distance = p0.distance(points.get(points.size() - 1));
		for (int i = 1; i < points.size(); i++) {
			distance += p0.distance(points.get(i));
			p0 = points.get(i);
		}
		return distance;
	}

	public static ArrayList<Point> voyage(Point origin, ArrayList<Point> points, ArrayList<Point> hitPoints,
			int[][] path) {
		ArrayList<Point> cycle = new ArrayList<>(2);
		cycle.add(origin);
		cycle.add(origin);
		HashSet<Point> visites = new HashSet<>(hitPoints.size());
		visites.add(origin);
		while (visites.size() < hitPoints.size()) {
			cycle = voyage(cycle, points, hitPoints, visites, path);
		}

		for (Point p : visites) {
			if (!hitPoints.contains(p)) {
				System.out.println("Grosse ERREUR");
			}
		}
		return cycle;
	}

	private static ArrayList<Point> voyage(ArrayList<Point> cycle, ArrayList<Point> points, ArrayList<Point> hitPoints,
			HashSet<Point> visites, int[][] path) {
		ArrayList<Point> extensionList = null, extensionTmp;
		Point extensionPoint = null;
		int extensionPosition;
		double distance = Double.POSITIVE_INFINITY, distanceTmp;
		/*
		 * On cherche le hitpoint et la position dans le cycle permettant de
		 * minimiser l'extension de la longueur du cycle
		 */
		for (Point p : hitPoints) {
			if (!visites.contains(p)) {
				for (int i = 1; i < cycle.size(); i++) {
					/*
					 * essai avec extension2 qui n'ajoute pas les points bleus
					 */
					extensionTmp = extension2(p, i, cycle, points, path);
					if ((distanceTmp = Evaluator.score(extensionTmp)) < distance) {
						distance = distanceTmp;
						extensionList = (ArrayList<Point>) extensionTmp.clone();
						extensionPoint = p;
						extensionPosition = i;
					}
				}
			}
		}
		visites.add(extensionPoint);
		return extensionList;

	}

	private static ArrayList<Point> extension2(Point extensionPoint, int extensionPosition, ArrayList<Point> cycle,
			ArrayList<Point> points, int[][] path) {
		ArrayList<Point> res = (ArrayList<Point>) cycle.clone();
		res.add(extensionPosition, extensionPoint);

		return res;
	}

	public static int[][] calculPlusCourtsChemins(ArrayList<Point> points, int edgeThreshold) {
		int[][] path = new int[points.size()][points.size()];
		double[][] distance = new double[points.size()][points.size()];
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

		return path;
	}
}
