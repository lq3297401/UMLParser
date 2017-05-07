package umlparser;

import java.util.ArrayList;

public class ClassConstructor {
	String constructorName;
	ArrayList<String> inputs;
	
	public ClassConstructor(String name, ArrayList<String> inputs) {
		this.inputs = inputs;
		this.constructorName = name;
	}
	
	public String toString() {
		StringBuffer info = new StringBuffer();
		info.append("+" + constructorName + "(");
		for(String input : inputs) {
			info.append(input);
		}
		info.append(")\n");
		return info.toString();
	}
}
