package umlparser;

public class ClassRelationship {
	public static final String ASSOCIATION = "association";
	public static final String UNDEFINEDASSOCIATION = "undefinedassociation";
	public static final String DEPENDENCY = "dependency";
	public static final String EXTENSION = "extension";
	public static final String IMPLEMENT = "implement";
	
	String classA;
	String classB;
	String relationship;

	public ClassRelationship(String A, String B, String re) {
		this.classA = A;
		this.classB = B;
		this.relationship = re;
		System.out.println("re ==" + re);
		System.out.println("new relationship : " + this.toString());
	}

	public String toString() {
		StringBuffer info = new StringBuffer();
		info.append(classA);
		String line = new String();
		switch (this.relationship) {
		case ASSOCIATION:
			line = " -- ";
			break;
		case UNDEFINEDASSOCIATION:
			line = " \"*\"-- ";
			break;
		case DEPENDENCY:
			line = " <.. ";
			break;
		case EXTENSION:
			line = " <|-- ";
			break;
		case IMPLEMENT:
			line = " <|.. ";
			break;
		}
		info.append(line);
		info.append(classB + "\n");
		return info.toString();
	}

	// make sure no duplicate
	public boolean equals(Object relationshipB) {
		ClassRelationship classRelationshipB = (ClassRelationship) relationshipB;
		if (this.classA.equals(classRelationshipB.classA) && this.classB.equals(classRelationshipB.classB)
				&& this.relationship.equals(classRelationshipB.relationship)) {
			return true;
		} else if (this.relationship.equals(DEPENDENCY) && this.classA.equals(classRelationshipB.classB)
				&& this.classB.equals(classRelationshipB.classA)
				&& this.relationship.equals(classRelationshipB.relationship)) {
			return true;
		}else if (this.relationship.equals(UNDEFINEDASSOCIATION) && this.classA.equals(classRelationshipB.classB)
				&& this.classB.equals(classRelationshipB.classA)
				&& this.relationship.equals(classRelationshipB.relationship)) {
			return true;
		} 
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return this.classA.hashCode() + this.classB.hashCode() + this.relationship.hashCode();
	}
}
