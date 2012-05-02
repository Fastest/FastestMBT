package compserver.prunning;

import java.util.*;

public class FastestOperatorReplacer{

	public static String replace(String line)
	{
		String[] partsLine = line.split(";");
		String newLine = "";
		for(int i=0;i<partsLine.length;i++){
		String subLine = partsLine[i];
		if(subLine.contains("\\anything"))
			subLine = subLine.replaceAll("\\\\anything", "FastestReservedAnything");
		if(subLine.contains("\\se"))
			subLine = subLine.replaceAll("\\\\se\\( (.*) \\)", "FastestReservedSE$1FastestReservedSE");
		if(subLine.contains("\\sw"))
			subLine = subLine.replaceAll("\\\\sw\\( (.*) \\)", "FastestReservedSW$1FastestReservedSW");
		newLine+=subLine+";";
		}
		newLine = newLine.substring(0,newLine.length()-1);
		return newLine;
	}
	public static String recover(String line)
	{
		String[] partsLine = line.split(";");
		String newLine = "";
		for(int i=0;i<partsLine.length;i++){
		String subLine = partsLine[i];
		if(subLine.contains("FastestReservedAnything"))
			subLine = subLine.replaceAll("FastestReservedAnything", "\\\\anything");
		if(subLine.contains("FastestReservedSE"))
			subLine = subLine.replaceAll("FastestReservedSE(.*)FastestReservedSE", "\\\\se( $1 )");
		if(subLine.contains("FastestReservedSW"))
			subLine = subLine.replaceAll("FastestReservedSW(.*)FastestReservedSW", "\\\\sw( $1 )");
		newLine+=subLine+";";
		}
		newLine = newLine.substring(0,newLine.length()-1);
		return newLine;
	}

}