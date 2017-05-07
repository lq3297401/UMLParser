package umlparser;

import java.io.File;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;


public class ParserEngine {
	public static final String CLASS = "class";
	public static final String INTERFACE = "interface";
	final String inputDir;
	public HashMap<String,String> allClassesOrInterfaces;
	private StringBuffer allUMLString;
	private HashSet<ClassRelationship> allRelationships;
	
	public ParserEngine(String inputDir){
		this.inputDir = inputDir;
		this.allUMLString = new StringBuffer();
		this.allClassesOrInterfaces = new HashMap<>();
		this.allRelationships = new HashSet<>();
	}
	
	public void loadFile() {
		File folder = new File(this.inputDir);
		CompilationUnit compilationUnit;
		for(File file : folder.listFiles()) {
			if(file.isFile() && file.getName().endsWith(".java")){
				try {
					CompilationUnit cu = JavaParser.parse(file);
					TypeDeclaration type = cu.getType(0);
					if (type instanceof ClassOrInterfaceDeclaration) {
						System.out.println("type " + ((ClassOrInterfaceDeclaration) type).isInterface());
						if (((ClassOrInterfaceDeclaration) type).isInterface()) {
							allClassesOrInterfaces.put(file.getName().split(".java")[0],INTERFACE);
						} else {
							allClassesOrInterfaces.put(file.getName().split(".java")[0],CLASS);
						}
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for(File file : folder.listFiles()) {
			if(file.isFile() && file.getName().endsWith(".java")){
				IntermediateObject iObject = new IntermediateObject(file, allClassesOrInterfaces);
				allUMLString.append(iObject.getUMLString());
				allRelationships.addAll(iObject.relationships);
			}
		}
		System.out.println("relationship: size = " + allRelationships.size());
		for(ClassRelationship relationship : allRelationships) {
			System.out.println("relationship:" + relationship.toString());
			
			allUMLString.append(relationship.toString());
		}
		
		allUMLString.insert(0, "@startuml\n");
		allUMLString.append("@enduml");
	}
	
	public String getUMLString() {
		return allUMLString.toString();
	}
	
}
