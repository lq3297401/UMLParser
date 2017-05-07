package umlparser;

import java.util.ArrayList;

public class ClassMethod {
	String methodName;
	String returnType;
	ArrayList<String> inputs;
	
	public ClassMethod(String name, String type, ArrayList<String> inputs) {
		this.methodName = name;
		this.returnType = type;
		this.inputs = inputs;
	}
	
	public String toString() {
		StringBuffer info = new StringBuffer();
		info.append("+" + methodName + "(");
		for(String input : inputs) {
			info.append(input);
		}
		info.append("):" + returnType + "\n");
		return info.toString();
	}
}
