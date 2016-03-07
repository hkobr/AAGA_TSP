package algorithms;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;

public class TestBedEvaluator {
  private static String testFolder="tests";
  private static int edgeThreshold=55;
  public static void main(String[] args) {
    for (int i=0;i<args.length;i++){
      if (args[i].charAt(0)=='-'){
        if (args[i+1].charAt(0)=='-'){
          System.err.println("Option "+args[i]+" expects an argument but received none");
          return;
        }
        switch (args[i]){
          case "-test":
            try {
              testFolder = args[i+1];
            } catch (Exception e){
              System.err.println("Invalid argument for option "+args[i]+": TestBed folder expected");
              return;
            }
            break;
          case "-edgeThreshold":
            try {
              edgeThreshold=Integer.parseInt(args[i+1]);
            } catch (Exception e){
              System.err.println("Invalid argument for option "+args[i]+": Integer type expected");
              return;
            }
            break;
          default:
            System.err.println("Unknown option "+args[i]);
            return;
        }
        i++;
      }
    }

    double result=evalFiles();
    System.out.println("Total score: " + result);
    return;
  }

  private static double evalFiles() {
    double result =0;

    for (int index=0;index<200;index++){

      String line;
      String[] coordinates;
      ArrayList<Point> points=new ArrayList<Point>();
      try {
        BufferedReader input = new BufferedReader(
            new InputStreamReader(new FileInputStream(testFolder+"/input"+index+".points"))
            );
        try {
          while ((line=input.readLine())!=null) {
            coordinates=line.split("\\s+");
            points.add(new Point(Integer.parseInt(coordinates[0]),
                  Integer.parseInt(coordinates[1])));
          }
          ArrayList<Point> hitPoints = new ArrayList<Point>();
          for (int i=points.size()/20;i<2*points.size()/20;i++) hitPoints.add((Point)points.get(i).clone());

          System.out.println("Input "+index+" successfully read. Computing...");
          ArrayList<Point> pts = new DefaultTeam().calculAngularTSP(points,edgeThreshold,hitPoints);
          System.out.println("   >>> Computation completed. Evaluating... ");
          result+=Evaluator.score(pts);
          System.out.println("   >>> Evaluation completed. Score so far = "+result);
        } catch (IOException e) {
          System.err.println("Exception: interrupted I/O.");
        } finally {
          try {
            input.close();
          } catch (IOException e) {
            System.err.println("I/O exception: unable to close files.");
          }
        }
      } catch (FileNotFoundException e) {
        System.err.println("Input file not found.");
      }
    }
    return result;
  }
}
