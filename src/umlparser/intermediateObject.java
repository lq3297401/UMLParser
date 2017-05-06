package umlparser;

import java.io.File;
import java.security.PublicKey;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class intermediateObject {
	public String className;
	public String attribute;
	public String method;
	public CompilationUnit compilationUnit;
	
		
	public intermediateObject(File file){
		this.className = file.getName().split(".java")[0];
		fileToCu(file);
//		this,attribute = 
	}
	
	private void fileToCu(File file){
		try {
			this.compilationUnit = JavaParser.parse(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
