package umlparser;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.security.Policy;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.css.ElementCSSInlineStyle;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class IntermediateObject {
	private String className;
	private ArrayList<ClassAttribute> attributes;
    private ArrayList<ClassMethod> methods;
    private ArrayList<ClassConstructor> constructors;
    public ArrayList<ClassRelationship> relationships;
    private HashMap<String,String> allClassesOrInterfaces;
	private boolean isInterface;
	private StringBuffer UMLString;

	private CompilationUnit cu;

	public IntermediateObject(File file, HashMap<String,String> allClassesOrInterfaces) {
		//get class name
		this.className = file.getName().split(".java")[0];
		this.attributes = new ArrayList<>();
		this.allClassesOrInterfaces = allClassesOrInterfaces;
		this.constructors = new ArrayList<>();
		this.relationships = new ArrayList<>();
		this.UMLString = new StringBuffer();
		this.methods = new ArrayList<>();
		fileToCu(file);
		TypeDeclaration type = this.cu.getType(0);
		if (type instanceof ClassOrInterfaceDeclaration) {
			if (((ClassOrInterfaceDeclaration) type).isInterface()) {
				this.isInterface = true;
				List<ClassOrInterfaceType> extendedTypes = ((ClassOrInterfaceDeclaration) type).getExtendedTypes();
				for(ClassOrInterfaceType t : extendedTypes) {
					String extendedClass = t.getName().getIdentifier();
					if(allClassesOrInterfaces.containsKey(extendedClass) && allClassesOrInterfaces.get(extendedClass).equals(ParserEngine.CLASS)) {
						relationships.add(new ClassRelationship(extendedClass, this.className, ClassRelationship.EXTENSION));
					}
				}
				List<ClassOrInterfaceType> implementedTypes = ((ClassOrInterfaceDeclaration) type).getImplementedTypes();
	            for (ClassOrInterfaceType i : implementedTypes) {
	                String implementInterface = i.getName().getIdentifier();
	                if (allClassesOrInterfaces.containsKey(implementInterface) && allClassesOrInterfaces.get(implementInterface).equals(ParserEngine.INTERFACE)) {
	                	relationships.add(new ClassRelationship(implementInterface, className, ClassRelationship.IMPLEMENT));
	                }
	            }
			} else {
				this.isInterface = false;
			}
		}
		
		 NodeList<BodyDeclaration> allMembers = type.getMembers();
		 for (BodyDeclaration member : allMembers) {
			 //get attributes
			 if(member instanceof FieldDeclaration) {
				 List<VariableDeclarator> variables =  ((FieldDeclaration) member).getVariables();
				 Pattern MY_PATTERN = Pattern.compile("<(.*)>");
				 for (VariableDeclarator variable : variables) {
					 String vType = variable.getType().toString();
					 String pType = null;
					 Matcher matcher = MY_PATTERN.matcher(vType);
					 while (matcher.find()) {
						 pType = matcher.group(1);
					}
					 
					if(allClassesOrInterfaces.containsKey(vType)) {
						if(variable.getType().getArrayLevel() > 0 ) {
							this.relationships.add(new ClassRelationship(vType, className, ClassRelationship.UNDEFINEDASSOCIATION));
						} else {
							this.relationships.add(new ClassRelationship(vType, className, ClassRelationship.ASSOCIATION));
						}
					} else if(pType != null && allClassesOrInterfaces.containsKey(pType)) {
						this.relationships.add(new ClassRelationship(vType, className, ClassRelationship.UNDEFINEDASSOCIATION));
					} else {
						String fName = variable.getNameAsString();
						if(((FieldDeclaration) member).isPublic() || ((FieldDeclaration) member).isPrivate()) {
							this.attributes.add(new ClassAttribute(vType , fName, ((FieldDeclaration) member).isPublic()));
						}
					}
					 
				 }
			 
			 }else if (member instanceof ConstructorDeclaration) {
				//get constructor
				 ConstructorDeclaration m = (ConstructorDeclaration) member;
				 if(m.isPublic()){
					 String constructorName = m.getNameAsString();
					 ArrayList<String> parameters = new ArrayList<>();
					 for(Parameter parameter : m.getParameters()) {
						 String pType = parameter.getType().toString();
						 if(allClassesOrInterfaces.containsKey(pType)) {
							 this.relationships.add(new ClassRelationship(pType, this.className, ClassRelationship.DEPENDENCY));
						 }
						 parameters.add(parameter.getNameAsString() + ":" + pType);
					 }
					 constructors.add(new ClassConstructor(constructorName, parameters));
				 }
			 }else if (member instanceof MethodDeclaration) {
				 //get methods
				 MethodDeclaration m = (MethodDeclaration) member;
				 String methodName = m.getNameAsString();
				 String outputType = m.getType().toString();
				 ArrayList<String> parameters = new ArrayList<>();
				 for(Parameter parameter : m.getParameters()) {
					 String pType = parameter.getType().toString();
					 if(allClassesOrInterfaces.containsKey(pType) && !this.isInterface) {
						 this.relationships.add(new ClassRelationship(pType, this.className, ClassRelationship.DEPENDENCY));
					 }
					 parameters.add(parameter.getNameAsString() + ":" + pType);
				 }
				 
				 NodeList<Statement> contents = new NodeList<>();
				 Optional<BlockStmt> bStmt = m.getBody();
				 if(bStmt.isPresent()) {
					contents = m.getBody().get().getStatements();
				 }
				 for(Statement s: contents) {
					 String[] statement = s.toString().split(" ");
					 if(allClassesOrInterfaces.containsKey(statement[0]) && !this.isInterface) {
						 this.relationships.add(new ClassRelationship(statement[0], className, ClassRelationship.DEPENDENCY));
					 }
				 }
				 this.methods.add(new ClassMethod(methodName, outputType, parameters));
			 }
		 }	
	}
	
	public String getUMLString () {
		if(isInterface) {
			UMLString.append(ParserEngine.INTERFACE+" ");
		} else {
			UMLString.append(ParserEngine.CLASS +" ");
		}
		UMLString.append(this.className).append("{\n");
		
		for(ClassConstructor classConstructor : constructors) {
			UMLString.append(classConstructor.toString());
		}
		
		for(ClassAttribute attribute : attributes) {
			UMLString.append(attribute.toString());
		}
		
		for(ClassMethod method:methods) {
			UMLString.append(method.toString());
		}
		
		UMLString.append("}\n");
		
		return UMLString.toString();
	}

	private void fileToCu(File file) {
		try {
			this.cu = JavaParser.parse(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
