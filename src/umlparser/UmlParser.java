package umlparser;

public class UmlParser {
	public static void main(String[] args) {
		
		ParserEngine pEngine = new ParserEngine(args[0]);
		pEngine.loadFile();
		UmlWriter umlWriter = new UmlWriter();
		umlWriter.draw(pEngine.getUMLString(), args[1]);
	}
}
