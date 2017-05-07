package umlparser;

public class ClassAttribute {
	String attributeName;
	String attributeType;
	boolean isPublic;
	
	public  ClassAttribute(String name, String type, Boolean isPublic) {
		this.attributeName = name;
		this.attributeType = type;
		this.isPublic = isPublic;
	}
	
	public String toString() {
		 StringBuffer info = new StringBuffer();
	        if (isPublic) {
	            info.append("+");
	        } else {
	            info.append("-");
	        }
	        info.append(attributeName + ":" + attributeType + "\n");

	        return info.toString();
	}
}

