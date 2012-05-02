package compserver.abstraction.types.impltypes;

/**
 * Represents a node in the AST generated with the abstraction laws that contains
 * the information of screen that is divided in fields
 */
public class ImplNodeScreenTable implements ImplNodeScreen{

	public ImplNodeScreenTable(){
		this.screenType = "table";
	}
	public void setImplID(String id){
		this.id = id;
	}
	public String getImplID(){
		return id;
	}
	public void setScreenType(String screenType){
		this.screenType = screenType;
	}
	public String getScreenType(){
		return screenType;
	}
	public void setDelimiter(String delimiter){
		this.delimiter = delimiter;
	}
	public String getDelimiter(){
		return delimiter;
	}
	public void setRowLowerBound(int rowLB){
		this.rowLB = rowLB;
	}
	public int getRowLowerBound(){
		return rowLB;
	}
	public void setRowUpperBound(int rowUB){
		this.rowUB = rowUB;
	}
	public int getRowUpperBound(){
		return rowUB;
	}
	public void setColumnLowerBound(int colLB){
		this.colLB = colLB;
	}
	public int getColumnLowerBound(){
		return colLB;
	}
	public void setColumnUpperBound(int colUB){
		this.colUB = colUB;
	}
	public int getColumnUpperBound(){
		return colUB;
	}
	public void setScreenPath(String path){
		this.path = path;
	}
	public String getScreenPath(){
		return path;
	}
	public void setEnumeration(ImplNodeEnumeration enumeration){
		this.enumeration = enumeration;
	}
	public ImplNodeEnumeration getEnumeration(){
		return enumeration;
	}
	private String screenType;
	private String id;
	private String delimiter;
	private int rowLB;
	private int rowUB;
	private int colLB;
	private int colUB;
	private String path;
	private ImplNodeEnumeration enumeration;
}