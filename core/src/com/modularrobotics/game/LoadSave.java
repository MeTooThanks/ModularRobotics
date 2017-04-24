package com.modularrobotics.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.Model;

public class LoadSave {
	public static ArrayList<Cube> environment;
	public static ArrayList<Module> modules;
	public static ArrayList<Target> targets;
	
	public static void save(
					   ArrayList<Cube> envToSave,
					   ArrayList<Module> modToSave,
					   ArrayList<Target> tarToSave,
					   int fileNum) {
		environment = envToSave;
		modules = modToSave;
		targets = tarToSave;
		
		try {
            //Whatever the file path is.
            File fileName = new File("env" +fileNum +".txt");
            FileOutputStream is = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            for (int i = 0; i < envToSave.size(); i++) {
	            w.write(String.valueOf(i+1));
	            w.write(", ");
	            w.write(String.valueOf((int) envToSave.get(i).position.x));
	            w.write(", ");
	            w.write(String.valueOf((int) envToSave.get(i).position.y));
	            w.write(", ");
	            w.write(String.valueOf((int) envToSave.get(i).position.z));
	            w.write(System.lineSeparator());
            }
            w.close();
        } catch (IOException e) {
            System.err.println("G");
        }
		try {
            //Whatever the file path is.
            File fileName = new File("mod" +fileNum +".txt");
            FileOutputStream is = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            for (int i = 0; i < modToSave.size(); i++) {
	            w.write(String.valueOf(i+1));
	            w.write(", ");
	            w.write(String.valueOf((int) modToSave.get(i).position.x));
	            w.write(", ");
	            w.write(String.valueOf((int) modToSave.get(i).position.y));
	            w.write(", ");
	            w.write(String.valueOf((int) modToSave.get(i).position.z));
	            w.write(System.lineSeparator());
            }        
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
		try {
            //Whatever the file path is.
            File fileName = new File("tar" +fileNum +".txt");
            FileOutputStream is = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            for (int i = 0; i < tarToSave.size(); i++) {
	            w.write(String.valueOf(i+1));
	            w.write(", ");
	            w.write(String.valueOf((int) tarToSave.get(i).position.x));
	            w.write(", ");
	            w.write(String.valueOf((int) tarToSave.get(i).position.y));
	            w.write(", ");
	            w.write(String.valueOf((int) tarToSave.get(i).position.z));
	            w.write(System.lineSeparator());
            }
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
        
	}
	
	public static void savePaths(int fileNum) {
		try {
            //Whatever the file path is.
            File fileName = new File("paths" +fileNum +".txt");
            FileOutputStream is = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            for (Module mod : modules) {
	            for (int i = 0; i < mod.originalPath.size()-1; i++) {
		            w.write(String.valueOf(i+1));
		            w.write(", ");
		            w.write(String.valueOf((int)  mod.originalPath.get(i).x));
		            w.write(", ");
		            w.write(String.valueOf((int)  mod.originalPath.get(i).y));
		            w.write(", ");
		            w.write(String.valueOf((int)  mod.originalPath.get(i).z));
		            w.write(", ");
		            w.write(String.valueOf((int)  mod.originalPath.get(i+1).x));
		            w.write(", ");
		            w.write(String.valueOf((int)  mod.originalPath.get(i+1).y));
		            w.write(", ");
		            w.write(String.valueOf((int)  mod.originalPath.get(i+1).z));
		            w.write(System.lineSeparator());
	            }
            }
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
	}
	
	public static void load(int fileNum, Model envModel, Model modModel, Model tarModel) {
		BufferedReader reader = null;
		
		try {
		    File file = new File("env" +fileNum +".txt");
		    reader = new BufferedReader(new FileReader(file));
		    environment = new ArrayList<Cube>();
		    String line;
		    while ((line = reader.readLine()) != null) {
		       String[] data = line.split(", ");
		       String x = data[1];
		       String y = data[2];
		       String z = data[3];
		       
		       environment.add(new Cube(envModel,
		    		   Integer.parseInt(x),
		    		   Integer.parseInt(y),
		    		   Integer.parseInt(z)));
		    }

		} catch (IOException e) {
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		    }
		}
		try {
		    File file = new File("mod" +fileNum +".txt");
		    reader = new BufferedReader(new FileReader(file));
		    modules = new ArrayList<Module>();
		    String line;
		    while ((line = reader.readLine()) != null) {
		       String[] data = line.split(", ");
		       String x = data[1];
		       String y = data[2];
		       String z = data[3];
		       
		       modules.add(new Module(modModel,
		    		   Integer.parseInt(x),
		    		   Integer.parseInt(y),
		    		   Integer.parseInt(z)));
		    }

		} catch (IOException e) {
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		    }
		}
		try {
		    File file = new File("tar" +fileNum +".txt");
		    reader = new BufferedReader(new FileReader(file));
		    targets = new ArrayList<Target>();
		    String line;
		    while ((line = reader.readLine()) != null) {
		       String[] data = line.split(", ");
		       String x = data[1];
		       String y = data[2];
		       String z = data[3];
		       
		       targets.add(new Target(tarModel,
		    		   Integer.parseInt(x),
		    		   Integer.parseInt(y),
		    		   Integer.parseInt(z)));
		    }

		} catch (IOException e) {
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		    }
		}
	}
}
