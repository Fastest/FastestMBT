package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of type of a file
 */
public class ImplNodeFile implements ImplNode{

	public ImplNodeFile(){
		// We initialize this fields because they aren't mandatory
		eol = "";
		eof = "";
	}
	public ImplNodeFile(String name, String path, String delimiter, String eol, String eof, String structure){
        this.name=name;
        this.path=path;
        this.delimiter=delimiter;
        this.eol=eol;
        this.eof=eof;
        this.structure=structure;
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setPath(String path){
		this.path = path;
	}
	public String getPath(){
		return path;
	}
	public void setDelimiter(String delimiter){
		this.delimiter = delimiter;
	}
	public String getDelimiter(){
		return delimiter;
	}
	public void setEol(String eol){
		this.eol = eol;
	}
	public String getEol(){
		return eol;
	}
	public void setEof(String eof){
		this.eof = eof;
	}
	public String getEof(){
		return eof;
	}
	public void setStructure(String structure){
		this.structure = structure;
	}
	public String getStructure(){
		return structure;
	}

	private String id;
	private String name;
	private String path;
	private String delimiter;
	private String eof;
	private String eol;
	private String structure;
}
