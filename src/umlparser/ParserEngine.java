package umlparser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class ParserEngine {
	final String inputDir;
	final String outputDir;
	private StringBuffer plantUmlCode;
	private ArrayList<CompilationUnit> compilationUnits;
	
	public ParserEngine(String inputDir, String outputDir){
		this.inputDir = inputDir;
		this.outputDir = outputDir;
		this.plantUmlCode = new StringBuffer();
		this.compilationUnits = new ArrayList<CompilationUnit>();
	}
	
	private void loadFile() {
		File folder = new File(this.inputDir);
		CompilationUnit compilationUnit;
		for(File file : folder.listFiles()) {
			if(file.isFile() && file.getName().endsWith(".java")){
				try {
					compilationUnit = JavaParser.parse(file);
					this.compilationUnits.add(compilationUnit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void parseCode() {
		loadFile();
		for(CompilationUnit cu:compilationUnits){
			ClassOrInterfaceDeclaration classA = cu.getInterfaceByName(interfaceName);
		}
		
	}

}
