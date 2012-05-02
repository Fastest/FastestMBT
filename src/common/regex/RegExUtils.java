package common.regex;
import java.util.regex.*;

/**
 * Provides general utilities related to regular expresions.
 */
public class RegExUtils
{
	public static String addEscapeCharacters(String original)
	{
		original = original.replaceAll("\\\\(\\D)","\\\\\\\\$1");
		original = original.replaceAll("\\{(\\D)","\\\\\\{$1");
		//original = original.replaceAll("\\{ ","\\\\\\{ ");
		original = original.replaceAll("\\{(\\d)\\}","\\\\\\{$1\\\\\\}");
		//original = original.replaceAll(" \\? "," \\\\? ");
		original = original.replaceAll("(\\w)\\?","$1\\\\?");
		original = original.replaceAll(" \\+ "," \\\\+ ");
		original = original.replaceAll(" \\* "," \\\\* ");
		original = original.replaceAll("\\( ","\\\\( ");
		original = original.replaceAll(" \\)"," \\\\)");
		original = original.replaceAll("\\\\\\([.][*]\\\\\\)","(.*)");
		original = original.replaceAll("\\\\\\([.][+]\\\\\\)","(.+)");
		return original;
	}
	public static Pattern createVarRegEx(String varName)
	{
		//Pattern regex = Pattern.compile("^[ ]*"+varName+" | "+varName+"[ ]*$| "+varName+" | "+varName+";"+varName+" | "+varName+";|;"+varName+" | "+varName+"~|~"+varName+" ", Pattern.MULTILINE);
		Pattern regex = Pattern.compile("^[ ]*"+varName+" | "+varName+"[ ]*$|~"+varName+"[ ]*$| "+varName+" | "+varName+";"+varName+" | "+varName+";|;"+varName+" | "+varName+"~|~"+varName+" ", Pattern.MULTILINE);
		return regex;
	}
}

