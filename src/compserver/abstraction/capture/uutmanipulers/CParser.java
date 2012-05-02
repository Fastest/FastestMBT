package compserver.abstraction.capture.uutmanipulers;


import java.util.Set;
import java.util.HashSet;
import compserver.abstraction.types.spectypes.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.*;
import java.util.ArrayList;
import java.util.List;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
import java.util.HashMap;
/** ANSI C ANTLR v3 grammar

Translated from Jutta Degener's 1995 ANSI C yacc grammar by Terence Parr
July 2006.  The lexical rules were taken from the Java grammar.

Jutta says: "In 1985, Jeff Lee published his Yacc grammar (which
is accompanied by a matching Lex specification) for the April 30, 1985 draft
version of the ANSI C standard.  Tom Stockfisch reposted it to net.sources in
1987; that original, as mentioned in the answer to question 17.25 of the
comp.lang.c FAQ, can be ftp'ed from ftp.uu.net,
   file usenet/net.sources/ansi.c.grammar.Z.
I intend to keep this version as close to the current C Standard grammar as
possible; please let me know if you discover discrepancies. Jutta Degener, 1995"

Generally speaking, you need symbol table info to parse C; typedefs
define types and then IDENTIFIERS are either types or plain IDs.  I'm doing
the min necessary here tracking only type names.  This is a good example
of the global scope (called Symbols).  Every rule that declares its usage
of Symbols pushes a new copy on the stack effectively creating a new
symbol scope.  Also note rule declaration declares a rule scope that
lets any invoked rule see isTypedef boolean.  It's much easier than
passing that info down as parameters.  Very clean.  Rule
direct_declarator can then easily determine whether the IDENTIFIER
should be declared as a type name.

I have only tested this on a single file, though it is 3500 lines.

This grammar requires ANTLR v3.0.1 or higher.

Terence Parr
July 2006
*/
public class CParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "IDENTIFIER", "HEX_LITERAL", "OCTAL_LITERAL", "DECIMAL_LITERAL", "CHARACTER_LITERAL", "STRING_LITERAL", "FLOATING_POINT_LITERAL", "LETTER", "EscapeSequence", "HexDigit", "IntegerTypeSuffix", "Exponent", "FloatTypeSuffix", "OctalEscape", "UnicodeEscape", "WS", "COMMENT", "LINE_COMMENT", "LINE_COMMAND", "'typedef'", "';'", "','", "'='", "'extern'", "'static'", "'auto'", "'register'", "'void'", "'char'", "'short'", "'int'", "'long'", "'float'", "'double'", "'signed'", "'unsigned'", "'{'", "'}'", "'struct'", "'union'", "':'", "'enum'", "'const'", "'volatile'", "'('", "')'", "'['", "']'", "'*'", "'...'", "'+'", "'-'", "'/'", "'%'", "'++'", "'--'", "'sizeof'", "'.'", "'->'", "'&'", "'~'", "'!'", "'*='", "'/='", "'%='", "'+='", "'-='", "'<<='", "'>>='", "'&='", "'^='", "'|='", "'?'", "'||'", "'&&'", "'|'", "'^'", "'=='", "'!='", "'<'", "'>'", "'<='", "'>='", "'<<'", "'>>'", "'case'", "'default'", "'if'", "'else'", "'switch'", "'while'", "'do'", "'for'", "'goto'", "'continue'", "'break'", "'return'"
    };
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int FloatTypeSuffix=16;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int LETTER=11;
    public static final int EOF=-1;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int STRING_LITERAL=9;
    public static final int T__90=90;
    public static final int FLOATING_POINT_LITERAL=10;
    public static final int COMMENT=20;
    public static final int T__99=99;
    public static final int T__98=98;
    public static final int T__97=97;
    public static final int T__96=96;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int LINE_COMMENT=21;
    public static final int IntegerTypeSuffix=14;
    public static final int CHARACTER_LITERAL=8;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int WS=19;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__70=70;
    public static final int LINE_COMMAND=22;
    public static final int T__76=76;
    public static final int T__75=75;
    public static final int T__74=74;
    public static final int T__73=73;
    public static final int DECIMAL_LITERAL=7;
    public static final int EscapeSequence=12;
    public static final int T__79=79;
    public static final int T__78=78;
    public static final int T__77=77;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int Exponent=15;
    public static final int T__61=61;
    public static final int T__60=60;
    public static final int HexDigit=13;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int IDENTIFIER=4;
    public static final int T__59=59;
    public static final int HEX_LITERAL=5;
    public static final int T__50=50;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int OCTAL_LITERAL=6;
    public static final int T__100=100;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int UnicodeEscape=18;
    public static final int OctalEscape=17;

    // delegates
    // delegators

    protected static class Symbols_scope {
        Set types;
    }
    protected Stack Symbols_stack = new Stack();


        public CParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public CParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
            this.state.ruleMemo = new HashMap[213+1];
             
             
        }
        
    protected StringTemplateGroup templateLib =
      new StringTemplateGroup("CParserTemplates", AngleBracketTemplateLexer.class);

    public void setTemplateLib(StringTemplateGroup templateLib) {
      this.templateLib = templateLib;
    }
    public StringTemplateGroup getTemplateLib() {
      return templateLib;
    }
    /** allows convenient multi-value initialization:
     *  "new STAttrMap().put(...).put(...)"
     */
    public static class STAttrMap extends HashMap {
      public STAttrMap put(String attrName, Object value) {
        super.put(attrName, value);
        return this;
      }
      public STAttrMap put(String attrName, int value) {
        super.put(attrName, new Integer(value));
        return this;
      }
    }

    public String[] getTokenNames() { return CParser.tokenNames; }
    public String getGrammarFileName() { return "C.g"; }


    	List<String> monitoredVars;
    	boolean isTypeName(String name) {
    		for (int i = Symbols_stack.size()-1; i>=0; i--) {
    			Symbols_scope scope = (Symbols_scope)Symbols_stack.get(i);
    			if ( scope.types.contains(name) ) {
    				return true;
    			}
    		}
    		return false;
    	}


    public static class makePublic_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "makePublic"
    // C.g:69:1: makePublic[List<ImplNode> vars] : ( external_declaration )+ ;
    public final CParser.makePublic_return makePublic(List<ImplNode> vars) throws RecognitionException {
        Symbols_stack.push(new Symbols_scope());

        CParser.makePublic_return retval = new CParser.makePublic_return();
        retval.start = input.LT(1);
        int makePublic_StartIndex = input.index();

          ((Symbols_scope)Symbols_stack.peek()).types = new HashSet();
        	monitoredVars = new ArrayList<String>();
        	for(int i=0;i<vars.size();i++){
        		ImplNode auxNode = vars.get(i);
        		//System.out.println("El nombre de la variable es: "+auxNode.getImplID());
        		monitoredVars.add(auxNode.getImplID());
        	}

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return retval; }
            // C.g:80:2: ( ( external_declaration )+ )
            // C.g:80:4: ( external_declaration )+
            {
            // C.g:80:4: ( external_declaration )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                alt1 = dfa1.predict(input);
                switch (alt1) {
            	case 1 :
            	    // C.g:0:0: external_declaration
            	    {
            	    pushFollow(FOLLOW_external_declaration_in_makePublic99);
            	    external_declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 1, makePublic_StartIndex); }
            Symbols_stack.pop();

        }
        return retval;
    }
    // $ANTLR end "makePublic"

    public static class external_declaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "external_declaration"
    // C.g:83:1: external_declaration options {k=1; } : ( ( ( declaration_specifiers )? declarator ( declaration )* '{' )=> function_definition | declaration );
    public final CParser.external_declaration_return external_declaration() throws RecognitionException {
        CParser.external_declaration_return retval = new CParser.external_declaration_return();
        retval.start = input.LT(1);
        int external_declaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return retval; }
            // C.g:99:2: ( ( ( declaration_specifiers )? declarator ( declaration )* '{' )=> function_definition | declaration )
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // C.g:99:4: ( ( declaration_specifiers )? declarator ( declaration )* '{' )=> function_definition
                    {
                    pushFollow(FOLLOW_function_definition_in_external_declaration135);
                    function_definition();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:100:4: declaration
                    {
                    pushFollow(FOLLOW_declaration_in_external_declaration140);
                    declaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 2, external_declaration_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "external_declaration"

    public static class function_definition_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "function_definition"
    // C.g:103:1: function_definition : ( declaration_specifiers )? declarator ( ( declaration )+ compound_statement | compound_statement ) -> {$function_definition.text.contains(\"static \")}? template(funcReplaced=$function_definition.text.replace(\"static \",\"\")) \"/*Funcion reemplazada*/ <funcReplaced>\" -> template(funcOriginal=$function_definition.text) \"<funcOriginal>\";
    public final CParser.function_definition_return function_definition() throws RecognitionException {
        Symbols_stack.push(new Symbols_scope());

        CParser.function_definition_return retval = new CParser.function_definition_return();
        retval.start = input.LT(1);
        int function_definition_StartIndex = input.index();

          ((Symbols_scope)Symbols_stack.peek()).types = new HashSet();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return retval; }
            // C.g:108:2: ( ( declaration_specifiers )? declarator ( ( declaration )+ compound_statement | compound_statement ) -> {$function_definition.text.contains(\"static \")}? template(funcReplaced=$function_definition.text.replace(\"static \",\"\")) \"/*Funcion reemplazada*/ <funcReplaced>\" -> template(funcOriginal=$function_definition.text) \"<funcOriginal>\")
            // C.g:108:4: ( declaration_specifiers )? declarator ( ( declaration )+ compound_statement | compound_statement )
            {
            // C.g:108:4: ( declaration_specifiers )?
            int alt3=2;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // C.g:0:0: declaration_specifiers
                    {
                    pushFollow(FOLLOW_declaration_specifiers_in_function_definition162);
                    declaration_specifiers();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }

            pushFollow(FOLLOW_declarator_in_function_definition165);
            declarator();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:109:3: ( ( declaration )+ compound_statement | compound_statement )
            int alt5=2;
            alt5 = dfa5.predict(input);
            switch (alt5) {
                case 1 :
                    // C.g:109:5: ( declaration )+ compound_statement
                    {
                    // C.g:109:5: ( declaration )+
                    int cnt4=0;
                    loop4:
                    do {
                        int alt4=2;
                        alt4 = dfa4.predict(input);
                        switch (alt4) {
                    	case 1 :
                    	    // C.g:0:0: declaration
                    	    {
                    	    pushFollow(FOLLOW_declaration_in_function_definition171);
                    	    declaration();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt4 >= 1 ) break loop4;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);

                    pushFollow(FOLLOW_compound_statement_in_function_definition174);
                    compound_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:110:5: compound_statement
                    {
                    pushFollow(FOLLOW_compound_statement_in_function_definition181);
                    compound_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }



            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 111:5: -> {$function_definition.text.contains(\"static \")}? template(funcReplaced=$function_definition.text.replace(\"static \",\"\")) \"/*Funcion reemplazada*/ <funcReplaced>\"
              if (input.toString(retval.start,input.LT(-1)).contains("static ")) {
                  retval.st = new StringTemplate(templateLib, "/*Funcion reemplazada*/ <funcReplaced>",
                new STAttrMap().put("funcReplaced", input.toString(retval.start,input.LT(-1)).replace("static ","")));
              }
              else // 113:3: -> template(funcOriginal=$function_definition.text) \"<funcOriginal>\"
              {
                  retval.st = new StringTemplate(templateLib, "<funcOriginal>",
                new STAttrMap().put("funcOriginal", input.toString(retval.start,input.LT(-1))));
              }

              ((TokenRewriteStream)input).replace(
                ((Token)retval.start).getTokenIndex(),
                input.LT(-1).getTokenIndex(),
                retval.st);
            }
            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 3, function_definition_StartIndex); }
            Symbols_stack.pop();

        }
        return retval;
    }
    // $ANTLR end "function_definition"

    protected static class declaration_scope {
        boolean isTypedef;
    }
    protected Stack declaration_stack = new Stack();

    public static class declaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "declaration"
    // C.g:117:1: declaration : ( 'typedef' ( declaration_specifiers )? init_declarator_list ';' | declaration_specifiers ( init_declarator_list )? ';' -> {$declaration.text.contains(\"static \") && $init_declarator_list.underMonitoring}? template(declReplaced=$declaration.text.replace(\"static \",\"\")) \"<declReplaced>\" -> template(declOriginal=$declaration.text) \"<declOriginal>\");
    public final CParser.declaration_return declaration() throws RecognitionException {
        declaration_stack.push(new declaration_scope());
        CParser.declaration_return retval = new CParser.declaration_return();
        retval.start = input.LT(1);
        int declaration_StartIndex = input.index();
        CParser.init_declarator_list_return init_declarator_list1 = null;



          ((declaration_scope)declaration_stack.peek()).isTypedef = false;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return retval; }
            // C.g:124:2: ( 'typedef' ( declaration_specifiers )? init_declarator_list ';' | declaration_specifiers ( init_declarator_list )? ';' -> {$declaration.text.contains(\"static \") && $init_declarator_list.underMonitoring}? template(declReplaced=$declaration.text.replace(\"static \",\"\")) \"<declReplaced>\" -> template(declOriginal=$declaration.text) \"<declOriginal>\")
            int alt8=2;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // C.g:124:4: 'typedef' ( declaration_specifiers )? init_declarator_list ';'
                    {
                    match(input,23,FOLLOW_23_in_declaration247); if (state.failed) return retval;
                    // C.g:124:14: ( declaration_specifiers )?
                    int alt6=2;
                    alt6 = dfa6.predict(input);
                    switch (alt6) {
                        case 1 :
                            // C.g:0:0: declaration_specifiers
                            {
                            pushFollow(FOLLOW_declaration_specifiers_in_declaration249);
                            declaration_specifiers();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {
                      ((declaration_scope)declaration_stack.peek()).isTypedef =true; 
                    }
                    pushFollow(FOLLOW_init_declarator_list_in_declaration257);
                    init_declarator_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,24,FOLLOW_24_in_declaration260); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:126:4: declaration_specifiers ( init_declarator_list )? ';'
                    {
                    pushFollow(FOLLOW_declaration_specifiers_in_declaration266);
                    declaration_specifiers();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C.g:126:27: ( init_declarator_list )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==IDENTIFIER||LA7_0==48||LA7_0==52) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // C.g:0:0: init_declarator_list
                            {
                            pushFollow(FOLLOW_init_declarator_list_in_declaration268);
                            init_declarator_list1=init_declarator_list();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {
                      if((init_declarator_list1!=null?init_declarator_list1.underMonitoring:false))
                      		System.out.println("Esta siendo monitoreado: "+(init_declarator_list1!=null?input.toString(init_declarator_list1.start,init_declarator_list1.stop):null));
                      	
                    }
                    match(input,24,FOLLOW_24_in_declaration272); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 128:7: -> {$declaration.text.contains(\"static \") && $init_declarator_list.underMonitoring}? template(declReplaced=$declaration.text.replace(\"static \",\"\")) \"<declReplaced>\"
                      if (input.toString(retval.start,input.LT(-1)).contains("static ") && (init_declarator_list1!=null?init_declarator_list1.underMonitoring:false)) {
                          retval.st = new StringTemplate(templateLib, "<declReplaced>",
                        new STAttrMap().put("declReplaced", input.toString(retval.start,input.LT(-1)).replace("static ","")));
                      }
                      else // 130:3: -> template(declOriginal=$declaration.text) \"<declOriginal>\"
                      {
                          retval.st = new StringTemplate(templateLib, "<declOriginal>",
                        new STAttrMap().put("declOriginal", input.toString(retval.start,input.LT(-1))));
                      }

                      ((TokenRewriteStream)input).replace(
                        ((Token)retval.start).getTokenIndex(),
                        input.LT(-1).getTokenIndex(),
                        retval.st);
                    }
                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 4, declaration_StartIndex); }
            declaration_stack.pop();
        }
        return retval;
    }
    // $ANTLR end "declaration"

    public static class declaration_specifiers_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "declaration_specifiers"
    // C.g:134:1: declaration_specifiers : ( storage_class_specifier | type_specifier | type_qualifier )+ ;
    public final CParser.declaration_specifiers_return declaration_specifiers() throws RecognitionException {
        CParser.declaration_specifiers_return retval = new CParser.declaration_specifiers_return();
        retval.start = input.LT(1);
        int declaration_specifiers_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return retval; }
            // C.g:135:2: ( ( storage_class_specifier | type_specifier | type_qualifier )+ )
            // C.g:135:6: ( storage_class_specifier | type_specifier | type_qualifier )+
            {
            // C.g:135:6: ( storage_class_specifier | type_specifier | type_qualifier )+
            int cnt9=0;
            loop9:
            do {
                int alt9=4;
                alt9 = dfa9.predict(input);
                switch (alt9) {
            	case 1 :
            	    // C.g:135:10: storage_class_specifier
            	    {
            	    pushFollow(FOLLOW_storage_class_specifier_in_declaration_specifiers327);
            	    storage_class_specifier();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;
            	case 2 :
            	    // C.g:136:7: type_specifier
            	    {
            	    pushFollow(FOLLOW_type_specifier_in_declaration_specifiers335);
            	    type_specifier();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;
            	case 3 :
            	    // C.g:137:13: type_qualifier
            	    {
            	    pushFollow(FOLLOW_type_qualifier_in_declaration_specifiers349);
            	    type_qualifier();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 5, declaration_specifiers_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "declaration_specifiers"

    public static class init_declarator_list_return extends ParserRuleReturnScope {
        public boolean underMonitoring;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "init_declarator_list"
    // C.g:141:1: init_declarator_list returns [boolean underMonitoring] : init1= init_declarator ( ',' init2= init_declarator )* ;
    public final CParser.init_declarator_list_return init_declarator_list() throws RecognitionException {
        CParser.init_declarator_list_return retval = new CParser.init_declarator_list_return();
        retval.start = input.LT(1);
        int init_declarator_list_StartIndex = input.index();
        CParser.init_declarator_return init1 = null;

        CParser.init_declarator_return init2 = null;



        	retval.underMonitoring = false;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return retval; }
            // C.g:145:2: (init1= init_declarator ( ',' init2= init_declarator )* )
            // C.g:145:4: init1= init_declarator ( ',' init2= init_declarator )*
            {
            pushFollow(FOLLOW_init_declarator_in_init_declarator_list383);
            init1=init_declarator();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) {
              if((init1!=null?init1.underMonitoring:false))
              				retval.underMonitoring = true;
              			
            }
            // C.g:147:5: ( ',' init2= init_declarator )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==25) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // C.g:147:6: ',' init2= init_declarator
            	    {
            	    match(input,25,FOLLOW_25_in_init_declarator_list387); if (state.failed) return retval;
            	    pushFollow(FOLLOW_init_declarator_in_init_declarator_list393);
            	    init2=init_declarator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	      if((init2!=null?init2.underMonitoring:false))
            	      				retval.underMonitoring = true;
            	      			
            	    }

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 6, init_declarator_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "init_declarator_list"

    public static class init_declarator_return extends ParserRuleReturnScope {
        public boolean underMonitoring;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "init_declarator"
    // C.g:152:1: init_declarator returns [boolean underMonitoring] : declarator ( '=' initializer )? ;
    public final CParser.init_declarator_return init_declarator() throws RecognitionException {
        CParser.init_declarator_return retval = new CParser.init_declarator_return();
        retval.start = input.LT(1);
        int init_declarator_StartIndex = input.index();
        CParser.declarator_return declarator2 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return retval; }
            // C.g:153:2: ( declarator ( '=' initializer )? )
            // C.g:153:4: declarator ( '=' initializer )?
            {
            pushFollow(FOLLOW_declarator_in_init_declarator412);
            declarator2=declarator();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:153:15: ( '=' initializer )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==26) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // C.g:153:16: '=' initializer
                    {
                    match(input,26,FOLLOW_26_in_init_declarator415); if (state.failed) return retval;
                    pushFollow(FOLLOW_initializer_in_init_declarator417);
                    initializer();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
              retval.underMonitoring = (declarator2!=null?declarator2.underMonitoring:false);
            }

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 7, init_declarator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "init_declarator"

    public static class storage_class_specifier_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "storage_class_specifier"
    // C.g:156:1: storage_class_specifier : ( 'extern' | 'static' | 'auto' | 'register' );
    public final CParser.storage_class_specifier_return storage_class_specifier() throws RecognitionException {
        CParser.storage_class_specifier_return retval = new CParser.storage_class_specifier_return();
        retval.start = input.LT(1);
        int storage_class_specifier_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return retval; }
            // C.g:157:2: ( 'extern' | 'static' | 'auto' | 'register' )
            // C.g:
            {
            if ( (input.LA(1)>=27 && input.LA(1)<=30) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 8, storage_class_specifier_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "storage_class_specifier"

    public static class type_specifier_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "type_specifier"
    // C.g:163:1: type_specifier : ( 'void' | 'char' | 'short' | 'int' | 'long' | 'float' | 'double' | 'signed' | 'unsigned' | struct_or_union_specifier | enum_specifier | type_id );
    public final CParser.type_specifier_return type_specifier() throws RecognitionException {
        CParser.type_specifier_return retval = new CParser.type_specifier_return();
        retval.start = input.LT(1);
        int type_specifier_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return retval; }
            // C.g:164:2: ( 'void' | 'char' | 'short' | 'int' | 'long' | 'float' | 'double' | 'signed' | 'unsigned' | struct_or_union_specifier | enum_specifier | type_id )
            int alt12=12;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // C.g:164:4: 'void'
                    {
                    match(input,31,FOLLOW_31_in_type_specifier458); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:165:4: 'char'
                    {
                    match(input,32,FOLLOW_32_in_type_specifier463); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:166:4: 'short'
                    {
                    match(input,33,FOLLOW_33_in_type_specifier468); if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C.g:167:4: 'int'
                    {
                    match(input,34,FOLLOW_34_in_type_specifier473); if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C.g:168:4: 'long'
                    {
                    match(input,35,FOLLOW_35_in_type_specifier478); if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // C.g:169:4: 'float'
                    {
                    match(input,36,FOLLOW_36_in_type_specifier483); if (state.failed) return retval;

                    }
                    break;
                case 7 :
                    // C.g:170:4: 'double'
                    {
                    match(input,37,FOLLOW_37_in_type_specifier488); if (state.failed) return retval;

                    }
                    break;
                case 8 :
                    // C.g:171:4: 'signed'
                    {
                    match(input,38,FOLLOW_38_in_type_specifier493); if (state.failed) return retval;

                    }
                    break;
                case 9 :
                    // C.g:172:4: 'unsigned'
                    {
                    match(input,39,FOLLOW_39_in_type_specifier498); if (state.failed) return retval;

                    }
                    break;
                case 10 :
                    // C.g:173:4: struct_or_union_specifier
                    {
                    pushFollow(FOLLOW_struct_or_union_specifier_in_type_specifier503);
                    struct_or_union_specifier();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 11 :
                    // C.g:174:4: enum_specifier
                    {
                    pushFollow(FOLLOW_enum_specifier_in_type_specifier508);
                    enum_specifier();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 12 :
                    // C.g:175:4: type_id
                    {
                    pushFollow(FOLLOW_type_id_in_type_specifier513);
                    type_id();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 9, type_specifier_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "type_specifier"

    public static class type_id_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "type_id"
    // C.g:178:1: type_id : {...}? IDENTIFIER ;
    public final CParser.type_id_return type_id() throws RecognitionException {
        CParser.type_id_return retval = new CParser.type_id_return();
        retval.start = input.LT(1);
        int type_id_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return retval; }
            // C.g:179:5: ({...}? IDENTIFIER )
            // C.g:179:9: {...}? IDENTIFIER
            {
            if ( !((isTypeName(input.LT(1).getText()))) ) {
                if (state.backtracking>0) {state.failed=true; return retval;}
                throw new FailedPredicateException(input, "type_id", "isTypeName(input.LT(1).getText())");
            }
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_type_id531); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 10, type_id_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "type_id"

    public static class struct_or_union_specifier_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "struct_or_union_specifier"
    // C.g:183:1: struct_or_union_specifier options {k=3; } : ( struct_or_union ( IDENTIFIER )? '{' struct_declaration_list '}' | struct_or_union IDENTIFIER );
    public final CParser.struct_or_union_specifier_return struct_or_union_specifier() throws RecognitionException {
        Symbols_stack.push(new Symbols_scope());

        CParser.struct_or_union_specifier_return retval = new CParser.struct_or_union_specifier_return();
        retval.start = input.LT(1);
        int struct_or_union_specifier_StartIndex = input.index();

          ((Symbols_scope)Symbols_stack.peek()).types = new HashSet();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return retval; }
            // C.g:189:2: ( struct_or_union ( IDENTIFIER )? '{' struct_declaration_list '}' | struct_or_union IDENTIFIER )
            int alt14=2;
            alt14 = dfa14.predict(input);
            switch (alt14) {
                case 1 :
                    // C.g:189:4: struct_or_union ( IDENTIFIER )? '{' struct_declaration_list '}'
                    {
                    pushFollow(FOLLOW_struct_or_union_in_struct_or_union_specifier564);
                    struct_or_union();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C.g:189:20: ( IDENTIFIER )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==IDENTIFIER) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C.g:0:0: IDENTIFIER
                            {
                            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_struct_or_union_specifier566); if (state.failed) return retval;

                            }
                            break;

                    }

                    match(input,40,FOLLOW_40_in_struct_or_union_specifier569); if (state.failed) return retval;
                    pushFollow(FOLLOW_struct_declaration_list_in_struct_or_union_specifier571);
                    struct_declaration_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,41,FOLLOW_41_in_struct_or_union_specifier573); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:190:4: struct_or_union IDENTIFIER
                    {
                    pushFollow(FOLLOW_struct_or_union_in_struct_or_union_specifier578);
                    struct_or_union();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_struct_or_union_specifier580); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 11, struct_or_union_specifier_StartIndex); }
            Symbols_stack.pop();

        }
        return retval;
    }
    // $ANTLR end "struct_or_union_specifier"

    public static class struct_or_union_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "struct_or_union"
    // C.g:193:1: struct_or_union : ( 'struct' | 'union' );
    public final CParser.struct_or_union_return struct_or_union() throws RecognitionException {
        CParser.struct_or_union_return retval = new CParser.struct_or_union_return();
        retval.start = input.LT(1);
        int struct_or_union_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return retval; }
            // C.g:194:2: ( 'struct' | 'union' )
            // C.g:
            {
            if ( (input.LA(1)>=42 && input.LA(1)<=43) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 12, struct_or_union_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "struct_or_union"

    public static class struct_declaration_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "struct_declaration_list"
    // C.g:198:1: struct_declaration_list : ( struct_declaration )+ ;
    public final CParser.struct_declaration_list_return struct_declaration_list() throws RecognitionException {
        CParser.struct_declaration_list_return retval = new CParser.struct_declaration_list_return();
        retval.start = input.LT(1);
        int struct_declaration_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return retval; }
            // C.g:199:2: ( ( struct_declaration )+ )
            // C.g:199:4: ( struct_declaration )+
            {
            // C.g:199:4: ( struct_declaration )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                alt15 = dfa15.predict(input);
                switch (alt15) {
            	case 1 :
            	    // C.g:0:0: struct_declaration
            	    {
            	    pushFollow(FOLLOW_struct_declaration_in_struct_declaration_list607);
            	    struct_declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt15 >= 1 ) break loop15;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 13, struct_declaration_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "struct_declaration_list"

    public static class struct_declaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "struct_declaration"
    // C.g:202:1: struct_declaration : specifier_qualifier_list struct_declarator_list ';' ;
    public final CParser.struct_declaration_return struct_declaration() throws RecognitionException {
        CParser.struct_declaration_return retval = new CParser.struct_declaration_return();
        retval.start = input.LT(1);
        int struct_declaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return retval; }
            // C.g:203:2: ( specifier_qualifier_list struct_declarator_list ';' )
            // C.g:203:4: specifier_qualifier_list struct_declarator_list ';'
            {
            pushFollow(FOLLOW_specifier_qualifier_list_in_struct_declaration619);
            specifier_qualifier_list();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_struct_declarator_list_in_struct_declaration621);
            struct_declarator_list();

            state._fsp--;
            if (state.failed) return retval;
            match(input,24,FOLLOW_24_in_struct_declaration623); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 14, struct_declaration_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "struct_declaration"

    public static class specifier_qualifier_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "specifier_qualifier_list"
    // C.g:206:1: specifier_qualifier_list : ( type_qualifier | type_specifier )+ ;
    public final CParser.specifier_qualifier_list_return specifier_qualifier_list() throws RecognitionException {
        CParser.specifier_qualifier_list_return retval = new CParser.specifier_qualifier_list_return();
        retval.start = input.LT(1);
        int specifier_qualifier_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return retval; }
            // C.g:207:2: ( ( type_qualifier | type_specifier )+ )
            // C.g:207:4: ( type_qualifier | type_specifier )+
            {
            // C.g:207:4: ( type_qualifier | type_specifier )+
            int cnt16=0;
            loop16:
            do {
                int alt16=3;
                alt16 = dfa16.predict(input);
                switch (alt16) {
            	case 1 :
            	    // C.g:207:6: type_qualifier
            	    {
            	    pushFollow(FOLLOW_type_qualifier_in_specifier_qualifier_list636);
            	    type_qualifier();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;
            	case 2 :
            	    // C.g:207:23: type_specifier
            	    {
            	    pushFollow(FOLLOW_type_specifier_in_specifier_qualifier_list640);
            	    type_specifier();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt16 >= 1 ) break loop16;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(16, input);
                        throw eee;
                }
                cnt16++;
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 15, specifier_qualifier_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "specifier_qualifier_list"

    public static class struct_declarator_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "struct_declarator_list"
    // C.g:210:1: struct_declarator_list : struct_declarator ( ',' struct_declarator )* ;
    public final CParser.struct_declarator_list_return struct_declarator_list() throws RecognitionException {
        CParser.struct_declarator_list_return retval = new CParser.struct_declarator_list_return();
        retval.start = input.LT(1);
        int struct_declarator_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return retval; }
            // C.g:211:2: ( struct_declarator ( ',' struct_declarator )* )
            // C.g:211:4: struct_declarator ( ',' struct_declarator )*
            {
            pushFollow(FOLLOW_struct_declarator_in_struct_declarator_list654);
            struct_declarator();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:211:22: ( ',' struct_declarator )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==25) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // C.g:211:23: ',' struct_declarator
            	    {
            	    match(input,25,FOLLOW_25_in_struct_declarator_list657); if (state.failed) return retval;
            	    pushFollow(FOLLOW_struct_declarator_in_struct_declarator_list659);
            	    struct_declarator();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 16, struct_declarator_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "struct_declarator_list"

    public static class struct_declarator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "struct_declarator"
    // C.g:214:1: struct_declarator : ( declarator ( ':' constant_expression )? | ':' constant_expression );
    public final CParser.struct_declarator_return struct_declarator() throws RecognitionException {
        CParser.struct_declarator_return retval = new CParser.struct_declarator_return();
        retval.start = input.LT(1);
        int struct_declarator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return retval; }
            // C.g:215:2: ( declarator ( ':' constant_expression )? | ':' constant_expression )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==IDENTIFIER||LA19_0==48||LA19_0==52) ) {
                alt19=1;
            }
            else if ( (LA19_0==44) ) {
                alt19=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // C.g:215:4: declarator ( ':' constant_expression )?
                    {
                    pushFollow(FOLLOW_declarator_in_struct_declarator672);
                    declarator();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C.g:215:15: ( ':' constant_expression )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==44) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // C.g:215:16: ':' constant_expression
                            {
                            match(input,44,FOLLOW_44_in_struct_declarator675); if (state.failed) return retval;
                            pushFollow(FOLLOW_constant_expression_in_struct_declarator677);
                            constant_expression();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C.g:216:4: ':' constant_expression
                    {
                    match(input,44,FOLLOW_44_in_struct_declarator684); if (state.failed) return retval;
                    pushFollow(FOLLOW_constant_expression_in_struct_declarator686);
                    constant_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 17, struct_declarator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "struct_declarator"

    public static class enum_specifier_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "enum_specifier"
    // C.g:219:1: enum_specifier options {k=3; } : ( 'enum' '{' enumerator_list '}' | 'enum' IDENTIFIER '{' enumerator_list '}' | 'enum' IDENTIFIER );
    public final CParser.enum_specifier_return enum_specifier() throws RecognitionException {
        CParser.enum_specifier_return retval = new CParser.enum_specifier_return();
        retval.start = input.LT(1);
        int enum_specifier_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return retval; }
            // C.g:221:2: ( 'enum' '{' enumerator_list '}' | 'enum' IDENTIFIER '{' enumerator_list '}' | 'enum' IDENTIFIER )
            int alt20=3;
            alt20 = dfa20.predict(input);
            switch (alt20) {
                case 1 :
                    // C.g:221:4: 'enum' '{' enumerator_list '}'
                    {
                    match(input,45,FOLLOW_45_in_enum_specifier704); if (state.failed) return retval;
                    match(input,40,FOLLOW_40_in_enum_specifier706); if (state.failed) return retval;
                    pushFollow(FOLLOW_enumerator_list_in_enum_specifier708);
                    enumerator_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,41,FOLLOW_41_in_enum_specifier710); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:222:4: 'enum' IDENTIFIER '{' enumerator_list '}'
                    {
                    match(input,45,FOLLOW_45_in_enum_specifier715); if (state.failed) return retval;
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enum_specifier717); if (state.failed) return retval;
                    match(input,40,FOLLOW_40_in_enum_specifier719); if (state.failed) return retval;
                    pushFollow(FOLLOW_enumerator_list_in_enum_specifier721);
                    enumerator_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,41,FOLLOW_41_in_enum_specifier723); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:223:4: 'enum' IDENTIFIER
                    {
                    match(input,45,FOLLOW_45_in_enum_specifier728); if (state.failed) return retval;
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enum_specifier730); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 18, enum_specifier_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "enum_specifier"

    public static class enumerator_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "enumerator_list"
    // C.g:226:1: enumerator_list : enumerator ( ',' enumerator )* ;
    public final CParser.enumerator_list_return enumerator_list() throws RecognitionException {
        CParser.enumerator_list_return retval = new CParser.enumerator_list_return();
        retval.start = input.LT(1);
        int enumerator_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return retval; }
            // C.g:227:2: ( enumerator ( ',' enumerator )* )
            // C.g:227:4: enumerator ( ',' enumerator )*
            {
            pushFollow(FOLLOW_enumerator_in_enumerator_list741);
            enumerator();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:227:15: ( ',' enumerator )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==25) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // C.g:227:16: ',' enumerator
            	    {
            	    match(input,25,FOLLOW_25_in_enumerator_list744); if (state.failed) return retval;
            	    pushFollow(FOLLOW_enumerator_in_enumerator_list746);
            	    enumerator();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 19, enumerator_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "enumerator_list"

    public static class enumerator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "enumerator"
    // C.g:230:1: enumerator : IDENTIFIER ( '=' constant_expression )? ;
    public final CParser.enumerator_return enumerator() throws RecognitionException {
        CParser.enumerator_return retval = new CParser.enumerator_return();
        retval.start = input.LT(1);
        int enumerator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return retval; }
            // C.g:231:2: ( IDENTIFIER ( '=' constant_expression )? )
            // C.g:231:4: IDENTIFIER ( '=' constant_expression )?
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enumerator759); if (state.failed) return retval;
            // C.g:231:15: ( '=' constant_expression )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==26) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // C.g:231:16: '=' constant_expression
                    {
                    match(input,26,FOLLOW_26_in_enumerator762); if (state.failed) return retval;
                    pushFollow(FOLLOW_constant_expression_in_enumerator764);
                    constant_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 20, enumerator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "enumerator"

    public static class type_qualifier_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "type_qualifier"
    // C.g:234:1: type_qualifier : ( 'const' | 'volatile' );
    public final CParser.type_qualifier_return type_qualifier() throws RecognitionException {
        CParser.type_qualifier_return retval = new CParser.type_qualifier_return();
        retval.start = input.LT(1);
        int type_qualifier_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return retval; }
            // C.g:235:2: ( 'const' | 'volatile' )
            // C.g:
            {
            if ( (input.LA(1)>=46 && input.LA(1)<=47) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 21, type_qualifier_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "type_qualifier"

    public static class declarator_return extends ParserRuleReturnScope {
        public boolean underMonitoring;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "declarator"
    // C.g:239:1: declarator returns [boolean underMonitoring] : ( ( pointer )? direct_declarator | pointer );
    public final CParser.declarator_return declarator() throws RecognitionException {
        CParser.declarator_return retval = new CParser.declarator_return();
        retval.start = input.LT(1);
        int declarator_StartIndex = input.index();
        CParser.direct_declarator_return direct_declarator3 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return retval; }
            // C.g:240:2: ( ( pointer )? direct_declarator | pointer )
            int alt24=2;
            alt24 = dfa24.predict(input);
            switch (alt24) {
                case 1 :
                    // C.g:240:4: ( pointer )? direct_declarator
                    {
                    // C.g:240:4: ( pointer )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==52) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // C.g:0:0: pointer
                            {
                            pushFollow(FOLLOW_pointer_in_declarator797);
                            pointer();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }

                    pushFollow(FOLLOW_direct_declarator_in_declarator800);
                    direct_declarator3=direct_declarator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                      retval.underMonitoring = (direct_declarator3!=null?direct_declarator3.underMonitoring:false);
                    }

                    }
                    break;
                case 2 :
                    // C.g:241:4: pointer
                    {
                    pushFollow(FOLLOW_pointer_in_declarator807);
                    pointer();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                      retval.underMonitoring = false;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 22, declarator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "declarator"

    public static class direct_declarator_return extends ParserRuleReturnScope {
        public boolean underMonitoring;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "direct_declarator"
    // C.g:244:1: direct_declarator returns [boolean underMonitoring] : ( IDENTIFIER | '(' declarator ')' ) ( declarator_suffix )* ;
    public final CParser.direct_declarator_return direct_declarator() throws RecognitionException {
        CParser.direct_declarator_return retval = new CParser.direct_declarator_return();
        retval.start = input.LT(1);
        int direct_declarator_StartIndex = input.index();
        Token IDENTIFIER4=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return retval; }
            // C.g:245:2: ( ( IDENTIFIER | '(' declarator ')' ) ( declarator_suffix )* )
            // C.g:245:6: ( IDENTIFIER | '(' declarator ')' ) ( declarator_suffix )*
            {
            // C.g:245:6: ( IDENTIFIER | '(' declarator ')' )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==IDENTIFIER) ) {
                alt25=1;
            }
            else if ( (LA25_0==48) ) {
                alt25=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // C.g:245:8: IDENTIFIER
                    {
                    IDENTIFIER4=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_direct_declarator828); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {

                      			if (declaration_stack.size()>0&&((declaration_scope)declaration_stack.peek()).isTypedef) {
                      				((Symbols_scope)Symbols_stack.peek()).types.add((IDENTIFIER4!=null?IDENTIFIER4.getText():null));
                      			}
                      			retval.underMonitoring = false;
                      			for(int i=0;i<monitoredVars.size() && !retval.underMonitoring;i++)
                      				if(monitoredVars.get(i).equals((IDENTIFIER4!=null?IDENTIFIER4.getText():null))){
                      					System.out.println("Encontro a :"+monitoredVars.get(i));
                      					retval.underMonitoring = true;
                      				}
                      			
                    }

                    }
                    break;
                case 2 :
                    // C.g:257:5: '(' declarator ')'
                    {
                    match(input,48,FOLLOW_48_in_direct_declarator839); if (state.failed) return retval;
                    pushFollow(FOLLOW_declarator_in_direct_declarator841);
                    declarator();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_direct_declarator843); if (state.failed) return retval;

                    }
                    break;

            }

            // C.g:259:9: ( declarator_suffix )*
            loop26:
            do {
                int alt26=2;
                alt26 = dfa26.predict(input);
                switch (alt26) {
            	case 1 :
            	    // C.g:0:0: declarator_suffix
            	    {
            	    pushFollow(FOLLOW_declarator_suffix_in_direct_declarator857);
            	    declarator_suffix();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 23, direct_declarator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "direct_declarator"

    public static class declarator_suffix_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "declarator_suffix"
    // C.g:262:1: declarator_suffix : ( '[' constant_expression ']' | '[' ']' | '(' parameter_type_list ')' | '(' identifier_list ')' | '(' ')' );
    public final CParser.declarator_suffix_return declarator_suffix() throws RecognitionException {
        CParser.declarator_suffix_return retval = new CParser.declarator_suffix_return();
        retval.start = input.LT(1);
        int declarator_suffix_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return retval; }
            // C.g:263:2: ( '[' constant_expression ']' | '[' ']' | '(' parameter_type_list ')' | '(' identifier_list ')' | '(' ')' )
            int alt27=5;
            alt27 = dfa27.predict(input);
            switch (alt27) {
                case 1 :
                    // C.g:263:6: '[' constant_expression ']'
                    {
                    match(input,50,FOLLOW_50_in_declarator_suffix871); if (state.failed) return retval;
                    pushFollow(FOLLOW_constant_expression_in_declarator_suffix873);
                    constant_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,51,FOLLOW_51_in_declarator_suffix875); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:264:9: '[' ']'
                    {
                    match(input,50,FOLLOW_50_in_declarator_suffix885); if (state.failed) return retval;
                    match(input,51,FOLLOW_51_in_declarator_suffix887); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:265:9: '(' parameter_type_list ')'
                    {
                    match(input,48,FOLLOW_48_in_declarator_suffix897); if (state.failed) return retval;
                    pushFollow(FOLLOW_parameter_type_list_in_declarator_suffix899);
                    parameter_type_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_declarator_suffix901); if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C.g:266:9: '(' identifier_list ')'
                    {
                    match(input,48,FOLLOW_48_in_declarator_suffix911); if (state.failed) return retval;
                    pushFollow(FOLLOW_identifier_list_in_declarator_suffix913);
                    identifier_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_declarator_suffix915); if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C.g:267:9: '(' ')'
                    {
                    match(input,48,FOLLOW_48_in_declarator_suffix925); if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_declarator_suffix927); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 24, declarator_suffix_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "declarator_suffix"

    public static class pointer_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "pointer"
    // C.g:270:1: pointer : ( '*' ( type_qualifier )+ ( pointer )? | '*' pointer | '*' );
    public final CParser.pointer_return pointer() throws RecognitionException {
        CParser.pointer_return retval = new CParser.pointer_return();
        retval.start = input.LT(1);
        int pointer_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return retval; }
            // C.g:271:2: ( '*' ( type_qualifier )+ ( pointer )? | '*' pointer | '*' )
            int alt30=3;
            alt30 = dfa30.predict(input);
            switch (alt30) {
                case 1 :
                    // C.g:271:4: '*' ( type_qualifier )+ ( pointer )?
                    {
                    match(input,52,FOLLOW_52_in_pointer938); if (state.failed) return retval;
                    // C.g:271:8: ( type_qualifier )+
                    int cnt28=0;
                    loop28:
                    do {
                        int alt28=2;
                        alt28 = dfa28.predict(input);
                        switch (alt28) {
                    	case 1 :
                    	    // C.g:0:0: type_qualifier
                    	    {
                    	    pushFollow(FOLLOW_type_qualifier_in_pointer940);
                    	    type_qualifier();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt28 >= 1 ) break loop28;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(28, input);
                                throw eee;
                        }
                        cnt28++;
                    } while (true);

                    // C.g:271:24: ( pointer )?
                    int alt29=2;
                    alt29 = dfa29.predict(input);
                    switch (alt29) {
                        case 1 :
                            // C.g:0:0: pointer
                            {
                            pushFollow(FOLLOW_pointer_in_pointer943);
                            pointer();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C.g:272:4: '*' pointer
                    {
                    match(input,52,FOLLOW_52_in_pointer949); if (state.failed) return retval;
                    pushFollow(FOLLOW_pointer_in_pointer951);
                    pointer();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:273:4: '*'
                    {
                    match(input,52,FOLLOW_52_in_pointer956); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 25, pointer_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "pointer"

    public static class parameter_type_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameter_type_list"
    // C.g:276:1: parameter_type_list : parameter_list ( ',' '...' )? ;
    public final CParser.parameter_type_list_return parameter_type_list() throws RecognitionException {
        CParser.parameter_type_list_return retval = new CParser.parameter_type_list_return();
        retval.start = input.LT(1);
        int parameter_type_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return retval; }
            // C.g:277:2: ( parameter_list ( ',' '...' )? )
            // C.g:277:4: parameter_list ( ',' '...' )?
            {
            pushFollow(FOLLOW_parameter_list_in_parameter_type_list967);
            parameter_list();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:277:19: ( ',' '...' )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==25) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // C.g:277:20: ',' '...'
                    {
                    match(input,25,FOLLOW_25_in_parameter_type_list970); if (state.failed) return retval;
                    match(input,53,FOLLOW_53_in_parameter_type_list972); if (state.failed) return retval;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 26, parameter_type_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "parameter_type_list"

    public static class parameter_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameter_list"
    // C.g:280:1: parameter_list : parameter_declaration ( ',' parameter_declaration )* ;
    public final CParser.parameter_list_return parameter_list() throws RecognitionException {
        CParser.parameter_list_return retval = new CParser.parameter_list_return();
        retval.start = input.LT(1);
        int parameter_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return retval; }
            // C.g:281:2: ( parameter_declaration ( ',' parameter_declaration )* )
            // C.g:281:4: parameter_declaration ( ',' parameter_declaration )*
            {
            pushFollow(FOLLOW_parameter_declaration_in_parameter_list985);
            parameter_declaration();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:281:26: ( ',' parameter_declaration )*
            loop32:
            do {
                int alt32=2;
                alt32 = dfa32.predict(input);
                switch (alt32) {
            	case 1 :
            	    // C.g:281:27: ',' parameter_declaration
            	    {
            	    match(input,25,FOLLOW_25_in_parameter_list988); if (state.failed) return retval;
            	    pushFollow(FOLLOW_parameter_declaration_in_parameter_list990);
            	    parameter_declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop32;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 27, parameter_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "parameter_list"

    public static class parameter_declaration_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "parameter_declaration"
    // C.g:284:1: parameter_declaration : declaration_specifiers ( declarator | abstract_declarator )* ;
    public final CParser.parameter_declaration_return parameter_declaration() throws RecognitionException {
        CParser.parameter_declaration_return retval = new CParser.parameter_declaration_return();
        retval.start = input.LT(1);
        int parameter_declaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return retval; }
            // C.g:285:2: ( declaration_specifiers ( declarator | abstract_declarator )* )
            // C.g:285:4: declaration_specifiers ( declarator | abstract_declarator )*
            {
            pushFollow(FOLLOW_declaration_specifiers_in_parameter_declaration1003);
            declaration_specifiers();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:285:27: ( declarator | abstract_declarator )*
            loop33:
            do {
                int alt33=3;
                alt33 = dfa33.predict(input);
                switch (alt33) {
            	case 1 :
            	    // C.g:285:28: declarator
            	    {
            	    pushFollow(FOLLOW_declarator_in_parameter_declaration1006);
            	    declarator();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;
            	case 2 :
            	    // C.g:285:39: abstract_declarator
            	    {
            	    pushFollow(FOLLOW_abstract_declarator_in_parameter_declaration1008);
            	    abstract_declarator();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop33;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 28, parameter_declaration_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "parameter_declaration"

    public static class identifier_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "identifier_list"
    // C.g:288:1: identifier_list : IDENTIFIER ( ',' IDENTIFIER )* ;
    public final CParser.identifier_list_return identifier_list() throws RecognitionException {
        CParser.identifier_list_return retval = new CParser.identifier_list_return();
        retval.start = input.LT(1);
        int identifier_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 29) ) { return retval; }
            // C.g:289:2: ( IDENTIFIER ( ',' IDENTIFIER )* )
            // C.g:289:4: IDENTIFIER ( ',' IDENTIFIER )*
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_identifier_list1021); if (state.failed) return retval;
            // C.g:289:15: ( ',' IDENTIFIER )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==25) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // C.g:289:16: ',' IDENTIFIER
            	    {
            	    match(input,25,FOLLOW_25_in_identifier_list1024); if (state.failed) return retval;
            	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_identifier_list1026); if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 29, identifier_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "identifier_list"

    public static class type_name_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "type_name"
    // C.g:292:1: type_name : specifier_qualifier_list ( abstract_declarator )? ;
    public final CParser.type_name_return type_name() throws RecognitionException {
        CParser.type_name_return retval = new CParser.type_name_return();
        retval.start = input.LT(1);
        int type_name_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 30) ) { return retval; }
            // C.g:293:2: ( specifier_qualifier_list ( abstract_declarator )? )
            // C.g:293:4: specifier_qualifier_list ( abstract_declarator )?
            {
            pushFollow(FOLLOW_specifier_qualifier_list_in_type_name1039);
            specifier_qualifier_list();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:293:29: ( abstract_declarator )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==48||LA35_0==50||LA35_0==52) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // C.g:0:0: abstract_declarator
                    {
                    pushFollow(FOLLOW_abstract_declarator_in_type_name1041);
                    abstract_declarator();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 30, type_name_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "type_name"

    public static class abstract_declarator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "abstract_declarator"
    // C.g:296:1: abstract_declarator : ( pointer ( direct_abstract_declarator )? | direct_abstract_declarator );
    public final CParser.abstract_declarator_return abstract_declarator() throws RecognitionException {
        CParser.abstract_declarator_return retval = new CParser.abstract_declarator_return();
        retval.start = input.LT(1);
        int abstract_declarator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 31) ) { return retval; }
            // C.g:297:2: ( pointer ( direct_abstract_declarator )? | direct_abstract_declarator )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==52) ) {
                alt37=1;
            }
            else if ( (LA37_0==48||LA37_0==50) ) {
                alt37=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // C.g:297:4: pointer ( direct_abstract_declarator )?
                    {
                    pushFollow(FOLLOW_pointer_in_abstract_declarator1053);
                    pointer();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C.g:297:12: ( direct_abstract_declarator )?
                    int alt36=2;
                    alt36 = dfa36.predict(input);
                    switch (alt36) {
                        case 1 :
                            // C.g:0:0: direct_abstract_declarator
                            {
                            pushFollow(FOLLOW_direct_abstract_declarator_in_abstract_declarator1055);
                            direct_abstract_declarator();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C.g:298:4: direct_abstract_declarator
                    {
                    pushFollow(FOLLOW_direct_abstract_declarator_in_abstract_declarator1061);
                    direct_abstract_declarator();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 31, abstract_declarator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "abstract_declarator"

    public static class direct_abstract_declarator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "direct_abstract_declarator"
    // C.g:301:1: direct_abstract_declarator : ( '(' abstract_declarator ')' | abstract_declarator_suffix ) ( abstract_declarator_suffix )* ;
    public final CParser.direct_abstract_declarator_return direct_abstract_declarator() throws RecognitionException {
        CParser.direct_abstract_declarator_return retval = new CParser.direct_abstract_declarator_return();
        retval.start = input.LT(1);
        int direct_abstract_declarator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 32) ) { return retval; }
            // C.g:302:2: ( ( '(' abstract_declarator ')' | abstract_declarator_suffix ) ( abstract_declarator_suffix )* )
            // C.g:302:4: ( '(' abstract_declarator ')' | abstract_declarator_suffix ) ( abstract_declarator_suffix )*
            {
            // C.g:302:4: ( '(' abstract_declarator ')' | abstract_declarator_suffix )
            int alt38=2;
            alt38 = dfa38.predict(input);
            switch (alt38) {
                case 1 :
                    // C.g:302:6: '(' abstract_declarator ')'
                    {
                    match(input,48,FOLLOW_48_in_direct_abstract_declarator1074); if (state.failed) return retval;
                    pushFollow(FOLLOW_abstract_declarator_in_direct_abstract_declarator1076);
                    abstract_declarator();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_direct_abstract_declarator1078); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:302:36: abstract_declarator_suffix
                    {
                    pushFollow(FOLLOW_abstract_declarator_suffix_in_direct_abstract_declarator1082);
                    abstract_declarator_suffix();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }

            // C.g:302:65: ( abstract_declarator_suffix )*
            loop39:
            do {
                int alt39=2;
                alt39 = dfa39.predict(input);
                switch (alt39) {
            	case 1 :
            	    // C.g:0:0: abstract_declarator_suffix
            	    {
            	    pushFollow(FOLLOW_abstract_declarator_suffix_in_direct_abstract_declarator1086);
            	    abstract_declarator_suffix();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop39;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 32, direct_abstract_declarator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "direct_abstract_declarator"

    public static class abstract_declarator_suffix_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "abstract_declarator_suffix"
    // C.g:305:1: abstract_declarator_suffix : ( '[' ']' | '[' constant_expression ']' | '(' ')' | '(' parameter_type_list ')' );
    public final CParser.abstract_declarator_suffix_return abstract_declarator_suffix() throws RecognitionException {
        CParser.abstract_declarator_suffix_return retval = new CParser.abstract_declarator_suffix_return();
        retval.start = input.LT(1);
        int abstract_declarator_suffix_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 33) ) { return retval; }
            // C.g:306:2: ( '[' ']' | '[' constant_expression ']' | '(' ')' | '(' parameter_type_list ')' )
            int alt40=4;
            alt40 = dfa40.predict(input);
            switch (alt40) {
                case 1 :
                    // C.g:306:4: '[' ']'
                    {
                    match(input,50,FOLLOW_50_in_abstract_declarator_suffix1098); if (state.failed) return retval;
                    match(input,51,FOLLOW_51_in_abstract_declarator_suffix1100); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:307:4: '[' constant_expression ']'
                    {
                    match(input,50,FOLLOW_50_in_abstract_declarator_suffix1105); if (state.failed) return retval;
                    pushFollow(FOLLOW_constant_expression_in_abstract_declarator_suffix1107);
                    constant_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,51,FOLLOW_51_in_abstract_declarator_suffix1109); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:308:4: '(' ')'
                    {
                    match(input,48,FOLLOW_48_in_abstract_declarator_suffix1114); if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_abstract_declarator_suffix1116); if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C.g:309:4: '(' parameter_type_list ')'
                    {
                    match(input,48,FOLLOW_48_in_abstract_declarator_suffix1121); if (state.failed) return retval;
                    pushFollow(FOLLOW_parameter_type_list_in_abstract_declarator_suffix1123);
                    parameter_type_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_abstract_declarator_suffix1125); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 33, abstract_declarator_suffix_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "abstract_declarator_suffix"

    public static class initializer_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "initializer"
    // C.g:312:1: initializer : ( assignment_expression | '{' initializer_list ( ',' )? '}' );
    public final CParser.initializer_return initializer() throws RecognitionException {
        CParser.initializer_return retval = new CParser.initializer_return();
        retval.start = input.LT(1);
        int initializer_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 34) ) { return retval; }
            // C.g:313:2: ( assignment_expression | '{' initializer_list ( ',' )? '}' )
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( ((LA42_0>=IDENTIFIER && LA42_0<=FLOATING_POINT_LITERAL)||LA42_0==48||LA42_0==52||(LA42_0>=54 && LA42_0<=55)||(LA42_0>=58 && LA42_0<=60)||(LA42_0>=63 && LA42_0<=65)) ) {
                alt42=1;
            }
            else if ( (LA42_0==40) ) {
                alt42=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;
            }
            switch (alt42) {
                case 1 :
                    // C.g:313:4: assignment_expression
                    {
                    pushFollow(FOLLOW_assignment_expression_in_initializer1137);
                    assignment_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:314:4: '{' initializer_list ( ',' )? '}'
                    {
                    match(input,40,FOLLOW_40_in_initializer1142); if (state.failed) return retval;
                    pushFollow(FOLLOW_initializer_list_in_initializer1144);
                    initializer_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C.g:314:25: ( ',' )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==25) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // C.g:0:0: ','
                            {
                            match(input,25,FOLLOW_25_in_initializer1146); if (state.failed) return retval;

                            }
                            break;

                    }

                    match(input,41,FOLLOW_41_in_initializer1149); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 34, initializer_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "initializer"

    public static class initializer_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "initializer_list"
    // C.g:317:1: initializer_list : initializer ( ',' initializer )* ;
    public final CParser.initializer_list_return initializer_list() throws RecognitionException {
        CParser.initializer_list_return retval = new CParser.initializer_list_return();
        retval.start = input.LT(1);
        int initializer_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 35) ) { return retval; }
            // C.g:318:2: ( initializer ( ',' initializer )* )
            // C.g:318:4: initializer ( ',' initializer )*
            {
            pushFollow(FOLLOW_initializer_in_initializer_list1160);
            initializer();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:318:16: ( ',' initializer )*
            loop43:
            do {
                int alt43=2;
                alt43 = dfa43.predict(input);
                switch (alt43) {
            	case 1 :
            	    // C.g:318:17: ',' initializer
            	    {
            	    match(input,25,FOLLOW_25_in_initializer_list1163); if (state.failed) return retval;
            	    pushFollow(FOLLOW_initializer_in_initializer_list1165);
            	    initializer();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 35, initializer_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "initializer_list"

    public static class argument_expression_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "argument_expression_list"
    // C.g:323:1: argument_expression_list : assignment_expression ( ',' assignment_expression )* ;
    public final CParser.argument_expression_list_return argument_expression_list() throws RecognitionException {
        CParser.argument_expression_list_return retval = new CParser.argument_expression_list_return();
        retval.start = input.LT(1);
        int argument_expression_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 36) ) { return retval; }
            // C.g:324:2: ( assignment_expression ( ',' assignment_expression )* )
            // C.g:324:6: assignment_expression ( ',' assignment_expression )*
            {
            pushFollow(FOLLOW_assignment_expression_in_argument_expression_list1182);
            assignment_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:324:28: ( ',' assignment_expression )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==25) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // C.g:324:29: ',' assignment_expression
            	    {
            	    match(input,25,FOLLOW_25_in_argument_expression_list1185); if (state.failed) return retval;
            	    pushFollow(FOLLOW_assignment_expression_in_argument_expression_list1187);
            	    assignment_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop44;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 36, argument_expression_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "argument_expression_list"

    public static class additive_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "additive_expression"
    // C.g:327:1: additive_expression : ( multiplicative_expression ) ( '+' multiplicative_expression | '-' multiplicative_expression )* ;
    public final CParser.additive_expression_return additive_expression() throws RecognitionException {
        CParser.additive_expression_return retval = new CParser.additive_expression_return();
        retval.start = input.LT(1);
        int additive_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 37) ) { return retval; }
            // C.g:328:2: ( ( multiplicative_expression ) ( '+' multiplicative_expression | '-' multiplicative_expression )* )
            // C.g:328:4: ( multiplicative_expression ) ( '+' multiplicative_expression | '-' multiplicative_expression )*
            {
            // C.g:328:4: ( multiplicative_expression )
            // C.g:328:5: multiplicative_expression
            {
            pushFollow(FOLLOW_multiplicative_expression_in_additive_expression1201);
            multiplicative_expression();

            state._fsp--;
            if (state.failed) return retval;

            }

            // C.g:328:32: ( '+' multiplicative_expression | '-' multiplicative_expression )*
            loop45:
            do {
                int alt45=3;
                alt45 = dfa45.predict(input);
                switch (alt45) {
            	case 1 :
            	    // C.g:328:33: '+' multiplicative_expression
            	    {
            	    match(input,54,FOLLOW_54_in_additive_expression1205); if (state.failed) return retval;
            	    pushFollow(FOLLOW_multiplicative_expression_in_additive_expression1207);
            	    multiplicative_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;
            	case 2 :
            	    // C.g:328:65: '-' multiplicative_expression
            	    {
            	    match(input,55,FOLLOW_55_in_additive_expression1211); if (state.failed) return retval;
            	    pushFollow(FOLLOW_multiplicative_expression_in_additive_expression1213);
            	    multiplicative_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 37, additive_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "additive_expression"

    public static class multiplicative_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "multiplicative_expression"
    // C.g:331:1: multiplicative_expression : ( cast_expression ) ( '*' cast_expression | '/' cast_expression | '%' cast_expression )* ;
    public final CParser.multiplicative_expression_return multiplicative_expression() throws RecognitionException {
        CParser.multiplicative_expression_return retval = new CParser.multiplicative_expression_return();
        retval.start = input.LT(1);
        int multiplicative_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 38) ) { return retval; }
            // C.g:332:2: ( ( cast_expression ) ( '*' cast_expression | '/' cast_expression | '%' cast_expression )* )
            // C.g:332:4: ( cast_expression ) ( '*' cast_expression | '/' cast_expression | '%' cast_expression )*
            {
            // C.g:332:4: ( cast_expression )
            // C.g:332:5: cast_expression
            {
            pushFollow(FOLLOW_cast_expression_in_multiplicative_expression1227);
            cast_expression();

            state._fsp--;
            if (state.failed) return retval;

            }

            // C.g:332:22: ( '*' cast_expression | '/' cast_expression | '%' cast_expression )*
            loop46:
            do {
                int alt46=4;
                alt46 = dfa46.predict(input);
                switch (alt46) {
            	case 1 :
            	    // C.g:332:23: '*' cast_expression
            	    {
            	    match(input,52,FOLLOW_52_in_multiplicative_expression1231); if (state.failed) return retval;
            	    pushFollow(FOLLOW_cast_expression_in_multiplicative_expression1233);
            	    cast_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;
            	case 2 :
            	    // C.g:332:45: '/' cast_expression
            	    {
            	    match(input,56,FOLLOW_56_in_multiplicative_expression1237); if (state.failed) return retval;
            	    pushFollow(FOLLOW_cast_expression_in_multiplicative_expression1239);
            	    cast_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;
            	case 3 :
            	    // C.g:332:67: '%' cast_expression
            	    {
            	    match(input,57,FOLLOW_57_in_multiplicative_expression1243); if (state.failed) return retval;
            	    pushFollow(FOLLOW_cast_expression_in_multiplicative_expression1245);
            	    cast_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop46;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 38, multiplicative_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "multiplicative_expression"

    public static class cast_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "cast_expression"
    // C.g:335:1: cast_expression : ( '(' type_name ')' cast_expression | unary_expression );
    public final CParser.cast_expression_return cast_expression() throws RecognitionException {
        CParser.cast_expression_return retval = new CParser.cast_expression_return();
        retval.start = input.LT(1);
        int cast_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 39) ) { return retval; }
            // C.g:336:2: ( '(' type_name ')' cast_expression | unary_expression )
            int alt47=2;
            alt47 = dfa47.predict(input);
            switch (alt47) {
                case 1 :
                    // C.g:336:4: '(' type_name ')' cast_expression
                    {
                    match(input,48,FOLLOW_48_in_cast_expression1258); if (state.failed) return retval;
                    pushFollow(FOLLOW_type_name_in_cast_expression1260);
                    type_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_cast_expression1262); if (state.failed) return retval;
                    pushFollow(FOLLOW_cast_expression_in_cast_expression1264);
                    cast_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:337:4: unary_expression
                    {
                    pushFollow(FOLLOW_unary_expression_in_cast_expression1269);
                    unary_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 39, cast_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "cast_expression"

    public static class unary_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "unary_expression"
    // C.g:340:1: unary_expression : ( postfix_expression | '++' unary_expression | '--' unary_expression | unary_operator cast_expression | 'sizeof' unary_expression | 'sizeof' '(' type_name ')' );
    public final CParser.unary_expression_return unary_expression() throws RecognitionException {
        CParser.unary_expression_return retval = new CParser.unary_expression_return();
        retval.start = input.LT(1);
        int unary_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 40) ) { return retval; }
            // C.g:341:2: ( postfix_expression | '++' unary_expression | '--' unary_expression | unary_operator cast_expression | 'sizeof' unary_expression | 'sizeof' '(' type_name ')' )
            int alt48=6;
            alt48 = dfa48.predict(input);
            switch (alt48) {
                case 1 :
                    // C.g:341:4: postfix_expression
                    {
                    pushFollow(FOLLOW_postfix_expression_in_unary_expression1280);
                    postfix_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:342:4: '++' unary_expression
                    {
                    match(input,58,FOLLOW_58_in_unary_expression1285); if (state.failed) return retval;
                    pushFollow(FOLLOW_unary_expression_in_unary_expression1287);
                    unary_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:343:4: '--' unary_expression
                    {
                    match(input,59,FOLLOW_59_in_unary_expression1292); if (state.failed) return retval;
                    pushFollow(FOLLOW_unary_expression_in_unary_expression1294);
                    unary_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C.g:344:4: unary_operator cast_expression
                    {
                    pushFollow(FOLLOW_unary_operator_in_unary_expression1299);
                    unary_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_cast_expression_in_unary_expression1301);
                    cast_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C.g:345:4: 'sizeof' unary_expression
                    {
                    match(input,60,FOLLOW_60_in_unary_expression1306); if (state.failed) return retval;
                    pushFollow(FOLLOW_unary_expression_in_unary_expression1308);
                    unary_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // C.g:346:4: 'sizeof' '(' type_name ')'
                    {
                    match(input,60,FOLLOW_60_in_unary_expression1313); if (state.failed) return retval;
                    match(input,48,FOLLOW_48_in_unary_expression1315); if (state.failed) return retval;
                    pushFollow(FOLLOW_type_name_in_unary_expression1317);
                    type_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_unary_expression1319); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 40, unary_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "unary_expression"

    public static class postfix_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "postfix_expression"
    // C.g:349:1: postfix_expression : primary_expression ( '[' expression ']' | '(' ')' | '(' argument_expression_list ')' | '.' IDENTIFIER | '->' IDENTIFIER | '++' | '--' )* ;
    public final CParser.postfix_expression_return postfix_expression() throws RecognitionException {
        CParser.postfix_expression_return retval = new CParser.postfix_expression_return();
        retval.start = input.LT(1);
        int postfix_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 41) ) { return retval; }
            // C.g:350:2: ( primary_expression ( '[' expression ']' | '(' ')' | '(' argument_expression_list ')' | '.' IDENTIFIER | '->' IDENTIFIER | '++' | '--' )* )
            // C.g:350:6: primary_expression ( '[' expression ']' | '(' ')' | '(' argument_expression_list ')' | '.' IDENTIFIER | '->' IDENTIFIER | '++' | '--' )*
            {
            pushFollow(FOLLOW_primary_expression_in_postfix_expression1332);
            primary_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:351:9: ( '[' expression ']' | '(' ')' | '(' argument_expression_list ')' | '.' IDENTIFIER | '->' IDENTIFIER | '++' | '--' )*
            loop49:
            do {
                int alt49=8;
                alt49 = dfa49.predict(input);
                switch (alt49) {
            	case 1 :
            	    // C.g:351:13: '[' expression ']'
            	    {
            	    match(input,50,FOLLOW_50_in_postfix_expression1346); if (state.failed) return retval;
            	    pushFollow(FOLLOW_expression_in_postfix_expression1348);
            	    expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    match(input,51,FOLLOW_51_in_postfix_expression1350); if (state.failed) return retval;

            	    }
            	    break;
            	case 2 :
            	    // C.g:352:13: '(' ')'
            	    {
            	    match(input,48,FOLLOW_48_in_postfix_expression1364); if (state.failed) return retval;
            	    match(input,49,FOLLOW_49_in_postfix_expression1366); if (state.failed) return retval;

            	    }
            	    break;
            	case 3 :
            	    // C.g:353:13: '(' argument_expression_list ')'
            	    {
            	    match(input,48,FOLLOW_48_in_postfix_expression1380); if (state.failed) return retval;
            	    pushFollow(FOLLOW_argument_expression_list_in_postfix_expression1382);
            	    argument_expression_list();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    match(input,49,FOLLOW_49_in_postfix_expression1384); if (state.failed) return retval;

            	    }
            	    break;
            	case 4 :
            	    // C.g:354:13: '.' IDENTIFIER
            	    {
            	    match(input,61,FOLLOW_61_in_postfix_expression1398); if (state.failed) return retval;
            	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_postfix_expression1400); if (state.failed) return retval;

            	    }
            	    break;
            	case 5 :
            	    // C.g:355:13: '->' IDENTIFIER
            	    {
            	    match(input,62,FOLLOW_62_in_postfix_expression1414); if (state.failed) return retval;
            	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_postfix_expression1416); if (state.failed) return retval;

            	    }
            	    break;
            	case 6 :
            	    // C.g:356:13: '++'
            	    {
            	    match(input,58,FOLLOW_58_in_postfix_expression1430); if (state.failed) return retval;

            	    }
            	    break;
            	case 7 :
            	    // C.g:357:13: '--'
            	    {
            	    match(input,59,FOLLOW_59_in_postfix_expression1444); if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop49;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 41, postfix_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "postfix_expression"

    public static class unary_operator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "unary_operator"
    // C.g:361:1: unary_operator : ( '&' | '*' | '+' | '-' | '~' | '!' );
    public final CParser.unary_operator_return unary_operator() throws RecognitionException {
        CParser.unary_operator_return retval = new CParser.unary_operator_return();
        retval.start = input.LT(1);
        int unary_operator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 42) ) { return retval; }
            // C.g:362:2: ( '&' | '*' | '+' | '-' | '~' | '!' )
            // C.g:
            {
            if ( input.LA(1)==52||(input.LA(1)>=54 && input.LA(1)<=55)||(input.LA(1)>=63 && input.LA(1)<=65) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 42, unary_operator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "unary_operator"

    public static class primary_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "primary_expression"
    // C.g:370:1: primary_expression : ( IDENTIFIER | constant | '(' expression ')' );
    public final CParser.primary_expression_return primary_expression() throws RecognitionException {
        CParser.primary_expression_return retval = new CParser.primary_expression_return();
        retval.start = input.LT(1);
        int primary_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 43) ) { return retval; }
            // C.g:371:2: ( IDENTIFIER | constant | '(' expression ')' )
            int alt50=3;
            switch ( input.LA(1) ) {
            case IDENTIFIER:
                {
                alt50=1;
                }
                break;
            case HEX_LITERAL:
            case OCTAL_LITERAL:
            case DECIMAL_LITERAL:
            case CHARACTER_LITERAL:
            case STRING_LITERAL:
            case FLOATING_POINT_LITERAL:
                {
                alt50=2;
                }
                break;
            case 48:
                {
                alt50=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }

            switch (alt50) {
                case 1 :
                    // C.g:371:4: IDENTIFIER
                    {
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary_expression1502); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:372:4: constant
                    {
                    pushFollow(FOLLOW_constant_in_primary_expression1507);
                    constant();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:373:4: '(' expression ')'
                    {
                    match(input,48,FOLLOW_48_in_primary_expression1512); if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_in_primary_expression1514);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_primary_expression1516); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 43, primary_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "primary_expression"

    public static class constant_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "constant"
    // C.g:376:1: constant : ( HEX_LITERAL | OCTAL_LITERAL | DECIMAL_LITERAL | CHARACTER_LITERAL | STRING_LITERAL | FLOATING_POINT_LITERAL );
    public final CParser.constant_return constant() throws RecognitionException {
        CParser.constant_return retval = new CParser.constant_return();
        retval.start = input.LT(1);
        int constant_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 44) ) { return retval; }
            // C.g:377:5: ( HEX_LITERAL | OCTAL_LITERAL | DECIMAL_LITERAL | CHARACTER_LITERAL | STRING_LITERAL | FLOATING_POINT_LITERAL )
            // C.g:
            {
            if ( (input.LA(1)>=HEX_LITERAL && input.LA(1)<=FLOATING_POINT_LITERAL) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 44, constant_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "constant"

    public static class expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "expression"
    // C.g:387:1: expression : assignment_expression ( ',' assignment_expression )* ;
    public final CParser.expression_return expression() throws RecognitionException {
        CParser.expression_return retval = new CParser.expression_return();
        retval.start = input.LT(1);
        int expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 45) ) { return retval; }
            // C.g:388:2: ( assignment_expression ( ',' assignment_expression )* )
            // C.g:388:4: assignment_expression ( ',' assignment_expression )*
            {
            pushFollow(FOLLOW_assignment_expression_in_expression1591);
            assignment_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:388:26: ( ',' assignment_expression )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==25) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // C.g:388:27: ',' assignment_expression
            	    {
            	    match(input,25,FOLLOW_25_in_expression1594); if (state.failed) return retval;
            	    pushFollow(FOLLOW_assignment_expression_in_expression1596);
            	    assignment_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 45, expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class constant_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "constant_expression"
    // C.g:391:1: constant_expression : conditional_expression ;
    public final CParser.constant_expression_return constant_expression() throws RecognitionException {
        CParser.constant_expression_return retval = new CParser.constant_expression_return();
        retval.start = input.LT(1);
        int constant_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 46) ) { return retval; }
            // C.g:392:2: ( conditional_expression )
            // C.g:392:4: conditional_expression
            {
            pushFollow(FOLLOW_conditional_expression_in_constant_expression1609);
            conditional_expression();

            state._fsp--;
            if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 46, constant_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "constant_expression"

    public static class assignment_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "assignment_expression"
    // C.g:395:1: assignment_expression : ( lvalue assignment_operator assignment_expression | conditional_expression );
    public final CParser.assignment_expression_return assignment_expression() throws RecognitionException {
        CParser.assignment_expression_return retval = new CParser.assignment_expression_return();
        retval.start = input.LT(1);
        int assignment_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 47) ) { return retval; }
            // C.g:396:2: ( lvalue assignment_operator assignment_expression | conditional_expression )
            int alt52=2;
            alt52 = dfa52.predict(input);
            switch (alt52) {
                case 1 :
                    // C.g:396:4: lvalue assignment_operator assignment_expression
                    {
                    pushFollow(FOLLOW_lvalue_in_assignment_expression1620);
                    lvalue();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_assignment_operator_in_assignment_expression1622);
                    assignment_operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_assignment_expression_in_assignment_expression1624);
                    assignment_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:397:4: conditional_expression
                    {
                    pushFollow(FOLLOW_conditional_expression_in_assignment_expression1629);
                    conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 47, assignment_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "assignment_expression"

    public static class lvalue_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "lvalue"
    // C.g:400:1: lvalue : unary_expression ;
    public final CParser.lvalue_return lvalue() throws RecognitionException {
        CParser.lvalue_return retval = new CParser.lvalue_return();
        retval.start = input.LT(1);
        int lvalue_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 48) ) { return retval; }
            // C.g:401:2: ( unary_expression )
            // C.g:401:4: unary_expression
            {
            pushFollow(FOLLOW_unary_expression_in_lvalue1641);
            unary_expression();

            state._fsp--;
            if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 48, lvalue_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "lvalue"

    public static class assignment_operator_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "assignment_operator"
    // C.g:404:1: assignment_operator : ( '=' | '*=' | '/=' | '%=' | '+=' | '-=' | '<<=' | '>>=' | '&=' | '^=' | '|=' );
    public final CParser.assignment_operator_return assignment_operator() throws RecognitionException {
        CParser.assignment_operator_return retval = new CParser.assignment_operator_return();
        retval.start = input.LT(1);
        int assignment_operator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 49) ) { return retval; }
            // C.g:405:2: ( '=' | '*=' | '/=' | '%=' | '+=' | '-=' | '<<=' | '>>=' | '&=' | '^=' | '|=' )
            // C.g:
            {
            if ( input.LA(1)==26||(input.LA(1)>=66 && input.LA(1)<=75) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 49, assignment_operator_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "assignment_operator"

    public static class conditional_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "conditional_expression"
    // C.g:418:1: conditional_expression : logical_or_expression ( '?' expression ':' conditional_expression )? ;
    public final CParser.conditional_expression_return conditional_expression() throws RecognitionException {
        CParser.conditional_expression_return retval = new CParser.conditional_expression_return();
        retval.start = input.LT(1);
        int conditional_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 50) ) { return retval; }
            // C.g:419:2: ( logical_or_expression ( '?' expression ':' conditional_expression )? )
            // C.g:419:4: logical_or_expression ( '?' expression ':' conditional_expression )?
            {
            pushFollow(FOLLOW_logical_or_expression_in_conditional_expression1713);
            logical_or_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:419:26: ( '?' expression ':' conditional_expression )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==76) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // C.g:419:27: '?' expression ':' conditional_expression
                    {
                    match(input,76,FOLLOW_76_in_conditional_expression1716); if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_in_conditional_expression1718);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,44,FOLLOW_44_in_conditional_expression1720); if (state.failed) return retval;
                    pushFollow(FOLLOW_conditional_expression_in_conditional_expression1722);
                    conditional_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 50, conditional_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "conditional_expression"

    public static class logical_or_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "logical_or_expression"
    // C.g:422:1: logical_or_expression : logical_and_expression ( '||' logical_and_expression )* ;
    public final CParser.logical_or_expression_return logical_or_expression() throws RecognitionException {
        CParser.logical_or_expression_return retval = new CParser.logical_or_expression_return();
        retval.start = input.LT(1);
        int logical_or_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 51) ) { return retval; }
            // C.g:423:2: ( logical_and_expression ( '||' logical_and_expression )* )
            // C.g:423:4: logical_and_expression ( '||' logical_and_expression )*
            {
            pushFollow(FOLLOW_logical_and_expression_in_logical_or_expression1735);
            logical_and_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:423:27: ( '||' logical_and_expression )*
            loop54:
            do {
                int alt54=2;
                alt54 = dfa54.predict(input);
                switch (alt54) {
            	case 1 :
            	    // C.g:423:28: '||' logical_and_expression
            	    {
            	    match(input,77,FOLLOW_77_in_logical_or_expression1738); if (state.failed) return retval;
            	    pushFollow(FOLLOW_logical_and_expression_in_logical_or_expression1740);
            	    logical_and_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop54;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 51, logical_or_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "logical_or_expression"

    public static class logical_and_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "logical_and_expression"
    // C.g:426:1: logical_and_expression : inclusive_or_expression ( '&&' inclusive_or_expression )* ;
    public final CParser.logical_and_expression_return logical_and_expression() throws RecognitionException {
        CParser.logical_and_expression_return retval = new CParser.logical_and_expression_return();
        retval.start = input.LT(1);
        int logical_and_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 52) ) { return retval; }
            // C.g:427:2: ( inclusive_or_expression ( '&&' inclusive_or_expression )* )
            // C.g:427:4: inclusive_or_expression ( '&&' inclusive_or_expression )*
            {
            pushFollow(FOLLOW_inclusive_or_expression_in_logical_and_expression1753);
            inclusive_or_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:427:28: ( '&&' inclusive_or_expression )*
            loop55:
            do {
                int alt55=2;
                alt55 = dfa55.predict(input);
                switch (alt55) {
            	case 1 :
            	    // C.g:427:29: '&&' inclusive_or_expression
            	    {
            	    match(input,78,FOLLOW_78_in_logical_and_expression1756); if (state.failed) return retval;
            	    pushFollow(FOLLOW_inclusive_or_expression_in_logical_and_expression1758);
            	    inclusive_or_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop55;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 52, logical_and_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "logical_and_expression"

    public static class inclusive_or_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "inclusive_or_expression"
    // C.g:430:1: inclusive_or_expression : exclusive_or_expression ( '|' exclusive_or_expression )* ;
    public final CParser.inclusive_or_expression_return inclusive_or_expression() throws RecognitionException {
        CParser.inclusive_or_expression_return retval = new CParser.inclusive_or_expression_return();
        retval.start = input.LT(1);
        int inclusive_or_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 53) ) { return retval; }
            // C.g:431:2: ( exclusive_or_expression ( '|' exclusive_or_expression )* )
            // C.g:431:4: exclusive_or_expression ( '|' exclusive_or_expression )*
            {
            pushFollow(FOLLOW_exclusive_or_expression_in_inclusive_or_expression1771);
            exclusive_or_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:431:28: ( '|' exclusive_or_expression )*
            loop56:
            do {
                int alt56=2;
                alt56 = dfa56.predict(input);
                switch (alt56) {
            	case 1 :
            	    // C.g:431:29: '|' exclusive_or_expression
            	    {
            	    match(input,79,FOLLOW_79_in_inclusive_or_expression1774); if (state.failed) return retval;
            	    pushFollow(FOLLOW_exclusive_or_expression_in_inclusive_or_expression1776);
            	    exclusive_or_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop56;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 53, inclusive_or_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "inclusive_or_expression"

    public static class exclusive_or_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "exclusive_or_expression"
    // C.g:434:1: exclusive_or_expression : and_expression ( '^' and_expression )* ;
    public final CParser.exclusive_or_expression_return exclusive_or_expression() throws RecognitionException {
        CParser.exclusive_or_expression_return retval = new CParser.exclusive_or_expression_return();
        retval.start = input.LT(1);
        int exclusive_or_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 54) ) { return retval; }
            // C.g:435:2: ( and_expression ( '^' and_expression )* )
            // C.g:435:4: and_expression ( '^' and_expression )*
            {
            pushFollow(FOLLOW_and_expression_in_exclusive_or_expression1789);
            and_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:435:19: ( '^' and_expression )*
            loop57:
            do {
                int alt57=2;
                alt57 = dfa57.predict(input);
                switch (alt57) {
            	case 1 :
            	    // C.g:435:20: '^' and_expression
            	    {
            	    match(input,80,FOLLOW_80_in_exclusive_or_expression1792); if (state.failed) return retval;
            	    pushFollow(FOLLOW_and_expression_in_exclusive_or_expression1794);
            	    and_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 54, exclusive_or_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "exclusive_or_expression"

    public static class and_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "and_expression"
    // C.g:438:1: and_expression : equality_expression ( '&' equality_expression )* ;
    public final CParser.and_expression_return and_expression() throws RecognitionException {
        CParser.and_expression_return retval = new CParser.and_expression_return();
        retval.start = input.LT(1);
        int and_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 55) ) { return retval; }
            // C.g:439:2: ( equality_expression ( '&' equality_expression )* )
            // C.g:439:4: equality_expression ( '&' equality_expression )*
            {
            pushFollow(FOLLOW_equality_expression_in_and_expression1807);
            equality_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:439:24: ( '&' equality_expression )*
            loop58:
            do {
                int alt58=2;
                alt58 = dfa58.predict(input);
                switch (alt58) {
            	case 1 :
            	    // C.g:439:25: '&' equality_expression
            	    {
            	    match(input,63,FOLLOW_63_in_and_expression1810); if (state.failed) return retval;
            	    pushFollow(FOLLOW_equality_expression_in_and_expression1812);
            	    equality_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop58;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 55, and_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "and_expression"

    public static class equality_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "equality_expression"
    // C.g:441:1: equality_expression : relational_expression ( ( '==' | '!=' ) relational_expression )* ;
    public final CParser.equality_expression_return equality_expression() throws RecognitionException {
        CParser.equality_expression_return retval = new CParser.equality_expression_return();
        retval.start = input.LT(1);
        int equality_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 56) ) { return retval; }
            // C.g:442:2: ( relational_expression ( ( '==' | '!=' ) relational_expression )* )
            // C.g:442:4: relational_expression ( ( '==' | '!=' ) relational_expression )*
            {
            pushFollow(FOLLOW_relational_expression_in_equality_expression1824);
            relational_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:442:26: ( ( '==' | '!=' ) relational_expression )*
            loop59:
            do {
                int alt59=2;
                alt59 = dfa59.predict(input);
                switch (alt59) {
            	case 1 :
            	    // C.g:442:27: ( '==' | '!=' ) relational_expression
            	    {
            	    if ( (input.LA(1)>=81 && input.LA(1)<=82) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_relational_expression_in_equality_expression1833);
            	    relational_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 56, equality_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "equality_expression"

    public static class relational_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "relational_expression"
    // C.g:445:1: relational_expression : shift_expression ( ( '<' | '>' | '<=' | '>=' ) shift_expression )* ;
    public final CParser.relational_expression_return relational_expression() throws RecognitionException {
        CParser.relational_expression_return retval = new CParser.relational_expression_return();
        retval.start = input.LT(1);
        int relational_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 57) ) { return retval; }
            // C.g:446:2: ( shift_expression ( ( '<' | '>' | '<=' | '>=' ) shift_expression )* )
            // C.g:446:4: shift_expression ( ( '<' | '>' | '<=' | '>=' ) shift_expression )*
            {
            pushFollow(FOLLOW_shift_expression_in_relational_expression1846);
            shift_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:446:21: ( ( '<' | '>' | '<=' | '>=' ) shift_expression )*
            loop60:
            do {
                int alt60=2;
                alt60 = dfa60.predict(input);
                switch (alt60) {
            	case 1 :
            	    // C.g:446:22: ( '<' | '>' | '<=' | '>=' ) shift_expression
            	    {
            	    if ( (input.LA(1)>=83 && input.LA(1)<=86) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_shift_expression_in_relational_expression1859);
            	    shift_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop60;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 57, relational_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "relational_expression"

    public static class shift_expression_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "shift_expression"
    // C.g:449:1: shift_expression : additive_expression ( ( '<<' | '>>' ) additive_expression )* ;
    public final CParser.shift_expression_return shift_expression() throws RecognitionException {
        CParser.shift_expression_return retval = new CParser.shift_expression_return();
        retval.start = input.LT(1);
        int shift_expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 58) ) { return retval; }
            // C.g:450:2: ( additive_expression ( ( '<<' | '>>' ) additive_expression )* )
            // C.g:450:4: additive_expression ( ( '<<' | '>>' ) additive_expression )*
            {
            pushFollow(FOLLOW_additive_expression_in_shift_expression1872);
            additive_expression();

            state._fsp--;
            if (state.failed) return retval;
            // C.g:450:24: ( ( '<<' | '>>' ) additive_expression )*
            loop61:
            do {
                int alt61=2;
                alt61 = dfa61.predict(input);
                switch (alt61) {
            	case 1 :
            	    // C.g:450:25: ( '<<' | '>>' ) additive_expression
            	    {
            	    if ( (input.LA(1)>=87 && input.LA(1)<=88) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_additive_expression_in_shift_expression1881);
            	    additive_expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop61;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 58, shift_expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "shift_expression"

    public static class statement_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "statement"
    // C.g:455:1: statement : ( labeled_statement | compound_statement | expression_statement | selection_statement | iteration_statement | jump_statement );
    public final CParser.statement_return statement() throws RecognitionException {
        CParser.statement_return retval = new CParser.statement_return();
        retval.start = input.LT(1);
        int statement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 59) ) { return retval; }
            // C.g:456:2: ( labeled_statement | compound_statement | expression_statement | selection_statement | iteration_statement | jump_statement )
            int alt62=6;
            alt62 = dfa62.predict(input);
            switch (alt62) {
                case 1 :
                    // C.g:456:4: labeled_statement
                    {
                    pushFollow(FOLLOW_labeled_statement_in_statement1896);
                    labeled_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:457:4: compound_statement
                    {
                    pushFollow(FOLLOW_compound_statement_in_statement1901);
                    compound_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:458:4: expression_statement
                    {
                    pushFollow(FOLLOW_expression_statement_in_statement1906);
                    expression_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C.g:459:4: selection_statement
                    {
                    pushFollow(FOLLOW_selection_statement_in_statement1911);
                    selection_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C.g:460:4: iteration_statement
                    {
                    pushFollow(FOLLOW_iteration_statement_in_statement1916);
                    iteration_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // C.g:461:4: jump_statement
                    {
                    pushFollow(FOLLOW_jump_statement_in_statement1921);
                    jump_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 59, statement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "statement"

    public static class labeled_statement_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "labeled_statement"
    // C.g:464:1: labeled_statement : ( IDENTIFIER ':' statement | 'case' constant_expression ':' statement | 'default' ':' statement );
    public final CParser.labeled_statement_return labeled_statement() throws RecognitionException {
        CParser.labeled_statement_return retval = new CParser.labeled_statement_return();
        retval.start = input.LT(1);
        int labeled_statement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 60) ) { return retval; }
            // C.g:465:2: ( IDENTIFIER ':' statement | 'case' constant_expression ':' statement | 'default' ':' statement )
            int alt63=3;
            switch ( input.LA(1) ) {
            case IDENTIFIER:
                {
                alt63=1;
                }
                break;
            case 89:
                {
                alt63=2;
                }
                break;
            case 90:
                {
                alt63=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 63, 0, input);

                throw nvae;
            }

            switch (alt63) {
                case 1 :
                    // C.g:465:4: IDENTIFIER ':' statement
                    {
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_labeled_statement1932); if (state.failed) return retval;
                    match(input,44,FOLLOW_44_in_labeled_statement1934); if (state.failed) return retval;
                    pushFollow(FOLLOW_statement_in_labeled_statement1936);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:466:4: 'case' constant_expression ':' statement
                    {
                    match(input,89,FOLLOW_89_in_labeled_statement1941); if (state.failed) return retval;
                    pushFollow(FOLLOW_constant_expression_in_labeled_statement1943);
                    constant_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,44,FOLLOW_44_in_labeled_statement1945); if (state.failed) return retval;
                    pushFollow(FOLLOW_statement_in_labeled_statement1947);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:467:4: 'default' ':' statement
                    {
                    match(input,90,FOLLOW_90_in_labeled_statement1952); if (state.failed) return retval;
                    match(input,44,FOLLOW_44_in_labeled_statement1954); if (state.failed) return retval;
                    pushFollow(FOLLOW_statement_in_labeled_statement1956);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 60, labeled_statement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "labeled_statement"

    public static class compound_statement_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "compound_statement"
    // C.g:470:1: compound_statement : '{' ( declaration )* ( statement_list )? '}' ;
    public final CParser.compound_statement_return compound_statement() throws RecognitionException {
        Symbols_stack.push(new Symbols_scope());

        CParser.compound_statement_return retval = new CParser.compound_statement_return();
        retval.start = input.LT(1);
        int compound_statement_StartIndex = input.index();

          ((Symbols_scope)Symbols_stack.peek()).types = new HashSet();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 61) ) { return retval; }
            // C.g:475:2: ( '{' ( declaration )* ( statement_list )? '}' )
            // C.g:475:4: '{' ( declaration )* ( statement_list )? '}'
            {
            match(input,40,FOLLOW_40_in_compound_statement1978); if (state.failed) return retval;
            // C.g:475:8: ( declaration )*
            loop64:
            do {
                int alt64=2;
                alt64 = dfa64.predict(input);
                switch (alt64) {
            	case 1 :
            	    // C.g:0:0: declaration
            	    {
            	    pushFollow(FOLLOW_declaration_in_compound_statement1980);
            	    declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop64;
                }
            } while (true);

            // C.g:475:21: ( statement_list )?
            int alt65=2;
            alt65 = dfa65.predict(input);
            switch (alt65) {
                case 1 :
                    // C.g:0:0: statement_list
                    {
                    pushFollow(FOLLOW_statement_list_in_compound_statement1983);
                    statement_list();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }

            match(input,41,FOLLOW_41_in_compound_statement1986); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 61, compound_statement_StartIndex); }
            Symbols_stack.pop();

        }
        return retval;
    }
    // $ANTLR end "compound_statement"

    public static class statement_list_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "statement_list"
    // C.g:478:1: statement_list : ( statement )+ ;
    public final CParser.statement_list_return statement_list() throws RecognitionException {
        CParser.statement_list_return retval = new CParser.statement_list_return();
        retval.start = input.LT(1);
        int statement_list_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 62) ) { return retval; }
            // C.g:479:2: ( ( statement )+ )
            // C.g:479:4: ( statement )+
            {
            // C.g:479:4: ( statement )+
            int cnt66=0;
            loop66:
            do {
                int alt66=2;
                alt66 = dfa66.predict(input);
                switch (alt66) {
            	case 1 :
            	    // C.g:0:0: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_statement_list1997);
            	    statement();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt66 >= 1 ) break loop66;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(66, input);
                        throw eee;
                }
                cnt66++;
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 62, statement_list_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "statement_list"

    public static class expression_statement_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "expression_statement"
    // C.g:482:1: expression_statement : ( ';' | expression ';' );
    public final CParser.expression_statement_return expression_statement() throws RecognitionException {
        CParser.expression_statement_return retval = new CParser.expression_statement_return();
        retval.start = input.LT(1);
        int expression_statement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 63) ) { return retval; }
            // C.g:483:2: ( ';' | expression ';' )
            int alt67=2;
            int LA67_0 = input.LA(1);

            if ( (LA67_0==24) ) {
                alt67=1;
            }
            else if ( ((LA67_0>=IDENTIFIER && LA67_0<=FLOATING_POINT_LITERAL)||LA67_0==48||LA67_0==52||(LA67_0>=54 && LA67_0<=55)||(LA67_0>=58 && LA67_0<=60)||(LA67_0>=63 && LA67_0<=65)) ) {
                alt67=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 67, 0, input);

                throw nvae;
            }
            switch (alt67) {
                case 1 :
                    // C.g:483:4: ';'
                    {
                    match(input,24,FOLLOW_24_in_expression_statement2009); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:484:4: expression ';'
                    {
                    pushFollow(FOLLOW_expression_in_expression_statement2014);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,24,FOLLOW_24_in_expression_statement2016); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 63, expression_statement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "expression_statement"

    public static class selection_statement_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "selection_statement"
    // C.g:487:1: selection_statement : ( 'if' '(' expression ')' statement ( options {k=1; backtrack=false; } : 'else' statement )? | 'switch' '(' expression ')' statement );
    public final CParser.selection_statement_return selection_statement() throws RecognitionException {
        CParser.selection_statement_return retval = new CParser.selection_statement_return();
        retval.start = input.LT(1);
        int selection_statement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 64) ) { return retval; }
            // C.g:488:2: ( 'if' '(' expression ')' statement ( options {k=1; backtrack=false; } : 'else' statement )? | 'switch' '(' expression ')' statement )
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==91) ) {
                alt69=1;
            }
            else if ( (LA69_0==93) ) {
                alt69=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;
            }
            switch (alt69) {
                case 1 :
                    // C.g:488:4: 'if' '(' expression ')' statement ( options {k=1; backtrack=false; } : 'else' statement )?
                    {
                    match(input,91,FOLLOW_91_in_selection_statement2027); if (state.failed) return retval;
                    match(input,48,FOLLOW_48_in_selection_statement2029); if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_in_selection_statement2031);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_selection_statement2033); if (state.failed) return retval;
                    pushFollow(FOLLOW_statement_in_selection_statement2035);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C.g:488:38: ( options {k=1; backtrack=false; } : 'else' statement )?
                    int alt68=2;
                    int LA68_0 = input.LA(1);

                    if ( (LA68_0==92) ) {
                        int LA68_2 = input.LA(2);

                        if ( (true) ) {
                            alt68=1;
                        }
                    }
                    switch (alt68) {
                        case 1 :
                            // C.g:488:71: 'else' statement
                            {
                            match(input,92,FOLLOW_92_in_selection_statement2050); if (state.failed) return retval;
                            pushFollow(FOLLOW_statement_in_selection_statement2052);
                            statement();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C.g:489:4: 'switch' '(' expression ')' statement
                    {
                    match(input,93,FOLLOW_93_in_selection_statement2059); if (state.failed) return retval;
                    match(input,48,FOLLOW_48_in_selection_statement2061); if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_in_selection_statement2063);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_selection_statement2065); if (state.failed) return retval;
                    pushFollow(FOLLOW_statement_in_selection_statement2067);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 64, selection_statement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "selection_statement"

    public static class iteration_statement_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "iteration_statement"
    // C.g:492:1: iteration_statement : ( 'while' '(' expression ')' statement | 'do' statement 'while' '(' expression ')' ';' | 'for' '(' expression_statement expression_statement ( expression )? ')' statement );
    public final CParser.iteration_statement_return iteration_statement() throws RecognitionException {
        CParser.iteration_statement_return retval = new CParser.iteration_statement_return();
        retval.start = input.LT(1);
        int iteration_statement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 65) ) { return retval; }
            // C.g:493:2: ( 'while' '(' expression ')' statement | 'do' statement 'while' '(' expression ')' ';' | 'for' '(' expression_statement expression_statement ( expression )? ')' statement )
            int alt71=3;
            switch ( input.LA(1) ) {
            case 94:
                {
                alt71=1;
                }
                break;
            case 95:
                {
                alt71=2;
                }
                break;
            case 96:
                {
                alt71=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }

            switch (alt71) {
                case 1 :
                    // C.g:493:4: 'while' '(' expression ')' statement
                    {
                    match(input,94,FOLLOW_94_in_iteration_statement2078); if (state.failed) return retval;
                    match(input,48,FOLLOW_48_in_iteration_statement2080); if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_in_iteration_statement2082);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_iteration_statement2084); if (state.failed) return retval;
                    pushFollow(FOLLOW_statement_in_iteration_statement2086);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:494:4: 'do' statement 'while' '(' expression ')' ';'
                    {
                    match(input,95,FOLLOW_95_in_iteration_statement2091); if (state.failed) return retval;
                    pushFollow(FOLLOW_statement_in_iteration_statement2093);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,94,FOLLOW_94_in_iteration_statement2095); if (state.failed) return retval;
                    match(input,48,FOLLOW_48_in_iteration_statement2097); if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_in_iteration_statement2099);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,49,FOLLOW_49_in_iteration_statement2101); if (state.failed) return retval;
                    match(input,24,FOLLOW_24_in_iteration_statement2103); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:495:4: 'for' '(' expression_statement expression_statement ( expression )? ')' statement
                    {
                    match(input,96,FOLLOW_96_in_iteration_statement2108); if (state.failed) return retval;
                    match(input,48,FOLLOW_48_in_iteration_statement2110); if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_statement_in_iteration_statement2112);
                    expression_statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_statement_in_iteration_statement2114);
                    expression_statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C.g:495:56: ( expression )?
                    int alt70=2;
                    int LA70_0 = input.LA(1);

                    if ( ((LA70_0>=IDENTIFIER && LA70_0<=FLOATING_POINT_LITERAL)||LA70_0==48||LA70_0==52||(LA70_0>=54 && LA70_0<=55)||(LA70_0>=58 && LA70_0<=60)||(LA70_0>=63 && LA70_0<=65)) ) {
                        alt70=1;
                    }
                    switch (alt70) {
                        case 1 :
                            // C.g:0:0: expression
                            {
                            pushFollow(FOLLOW_expression_in_iteration_statement2116);
                            expression();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }

                    match(input,49,FOLLOW_49_in_iteration_statement2119); if (state.failed) return retval;
                    pushFollow(FOLLOW_statement_in_iteration_statement2121);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 65, iteration_statement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "iteration_statement"

    public static class jump_statement_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "jump_statement"
    // C.g:498:1: jump_statement : ( 'goto' IDENTIFIER ';' | 'continue' ';' | 'break' ';' | 'return' ';' | 'return' expression ';' );
    public final CParser.jump_statement_return jump_statement() throws RecognitionException {
        CParser.jump_statement_return retval = new CParser.jump_statement_return();
        retval.start = input.LT(1);
        int jump_statement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 66) ) { return retval; }
            // C.g:499:2: ( 'goto' IDENTIFIER ';' | 'continue' ';' | 'break' ';' | 'return' ';' | 'return' expression ';' )
            int alt72=5;
            alt72 = dfa72.predict(input);
            switch (alt72) {
                case 1 :
                    // C.g:499:4: 'goto' IDENTIFIER ';'
                    {
                    match(input,97,FOLLOW_97_in_jump_statement2132); if (state.failed) return retval;
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_jump_statement2134); if (state.failed) return retval;
                    match(input,24,FOLLOW_24_in_jump_statement2136); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C.g:500:4: 'continue' ';'
                    {
                    match(input,98,FOLLOW_98_in_jump_statement2141); if (state.failed) return retval;
                    match(input,24,FOLLOW_24_in_jump_statement2143); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C.g:501:4: 'break' ';'
                    {
                    match(input,99,FOLLOW_99_in_jump_statement2148); if (state.failed) return retval;
                    match(input,24,FOLLOW_24_in_jump_statement2150); if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C.g:502:4: 'return' ';'
                    {
                    match(input,100,FOLLOW_100_in_jump_statement2155); if (state.failed) return retval;
                    match(input,24,FOLLOW_24_in_jump_statement2157); if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C.g:503:4: 'return' expression ';'
                    {
                    match(input,100,FOLLOW_100_in_jump_statement2162); if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_in_jump_statement2164);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,24,FOLLOW_24_in_jump_statement2166); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 66, jump_statement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "jump_statement"

    // $ANTLR start synpred2_C
    public final void synpred2_C_fragment() throws RecognitionException {   
        // C.g:99:6: ( declaration_specifiers )
        // C.g:99:6: declaration_specifiers
        {
        pushFollow(FOLLOW_declaration_specifiers_in_synpred2_C122);
        declaration_specifiers();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_C

    // $ANTLR start synpred4_C
    public final void synpred4_C_fragment() throws RecognitionException {   
        // C.g:99:4: ( ( declaration_specifiers )? declarator ( declaration )* '{' )
        // C.g:99:6: ( declaration_specifiers )? declarator ( declaration )* '{'
        {
        // C.g:99:6: ( declaration_specifiers )?
        int alt73=2;
        alt73 = dfa73.predict(input);
        switch (alt73) {
            case 1 :
                // C.g:0:0: declaration_specifiers
                {
                pushFollow(FOLLOW_declaration_specifiers_in_synpred4_C122);
                declaration_specifiers();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_declarator_in_synpred4_C125);
        declarator();

        state._fsp--;
        if (state.failed) return ;
        // C.g:99:41: ( declaration )*
        loop74:
        do {
            int alt74=2;
            alt74 = dfa74.predict(input);
            switch (alt74) {
        	case 1 :
        	    // C.g:0:0: declaration
        	    {
        	    pushFollow(FOLLOW_declaration_in_synpred4_C127);
        	    declaration();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop74;
            }
        } while (true);

        match(input,40,FOLLOW_40_in_synpred4_C130); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred4_C

    // $ANTLR start synpred5_C
    public final void synpred5_C_fragment() throws RecognitionException {   
        // C.g:108:4: ( declaration_specifiers )
        // C.g:108:4: declaration_specifiers
        {
        pushFollow(FOLLOW_declaration_specifiers_in_synpred5_C162);
        declaration_specifiers();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred5_C

    // $ANTLR start synpred8_C
    public final void synpred8_C_fragment() throws RecognitionException {   
        // C.g:124:14: ( declaration_specifiers )
        // C.g:124:14: declaration_specifiers
        {
        pushFollow(FOLLOW_declaration_specifiers_in_synpred8_C249);
        declaration_specifiers();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred8_C

    // $ANTLR start synpred12_C
    public final void synpred12_C_fragment() throws RecognitionException {   
        // C.g:136:7: ( type_specifier )
        // C.g:136:7: type_specifier
        {
        pushFollow(FOLLOW_type_specifier_in_synpred12_C335);
        type_specifier();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_C

    // $ANTLR start synpred35_C
    public final void synpred35_C_fragment() throws RecognitionException {   
        // C.g:207:23: ( type_specifier )
        // C.g:207:23: type_specifier
        {
        pushFollow(FOLLOW_type_specifier_in_synpred35_C640);
        type_specifier();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred35_C

    // $ANTLR start synpred45_C
    public final void synpred45_C_fragment() throws RecognitionException {   
        // C.g:240:4: ( ( pointer )? direct_declarator )
        // C.g:240:4: ( pointer )? direct_declarator
        {
        // C.g:240:4: ( pointer )?
        int alt79=2;
        int LA79_0 = input.LA(1);

        if ( (LA79_0==52) ) {
            alt79=1;
        }
        switch (alt79) {
            case 1 :
                // C.g:0:0: pointer
                {
                pushFollow(FOLLOW_pointer_in_synpred45_C797);
                pointer();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_direct_declarator_in_synpred45_C800);
        direct_declarator();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred45_C

    // $ANTLR start synpred47_C
    public final void synpred47_C_fragment() throws RecognitionException {   
        // C.g:259:9: ( declarator_suffix )
        // C.g:259:9: declarator_suffix
        {
        pushFollow(FOLLOW_declarator_suffix_in_synpred47_C857);
        declarator_suffix();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred47_C

    // $ANTLR start synpred50_C
    public final void synpred50_C_fragment() throws RecognitionException {   
        // C.g:265:9: ( '(' parameter_type_list ')' )
        // C.g:265:9: '(' parameter_type_list ')'
        {
        match(input,48,FOLLOW_48_in_synpred50_C897); if (state.failed) return ;
        pushFollow(FOLLOW_parameter_type_list_in_synpred50_C899);
        parameter_type_list();

        state._fsp--;
        if (state.failed) return ;
        match(input,49,FOLLOW_49_in_synpred50_C901); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred50_C

    // $ANTLR start synpred51_C
    public final void synpred51_C_fragment() throws RecognitionException {   
        // C.g:266:9: ( '(' identifier_list ')' )
        // C.g:266:9: '(' identifier_list ')'
        {
        match(input,48,FOLLOW_48_in_synpred51_C911); if (state.failed) return ;
        pushFollow(FOLLOW_identifier_list_in_synpred51_C913);
        identifier_list();

        state._fsp--;
        if (state.failed) return ;
        match(input,49,FOLLOW_49_in_synpred51_C915); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred51_C

    // $ANTLR start synpred52_C
    public final void synpred52_C_fragment() throws RecognitionException {   
        // C.g:271:8: ( type_qualifier )
        // C.g:271:8: type_qualifier
        {
        pushFollow(FOLLOW_type_qualifier_in_synpred52_C940);
        type_qualifier();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred52_C

    // $ANTLR start synpred53_C
    public final void synpred53_C_fragment() throws RecognitionException {   
        // C.g:271:24: ( pointer )
        // C.g:271:24: pointer
        {
        pushFollow(FOLLOW_pointer_in_synpred53_C943);
        pointer();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred53_C

    // $ANTLR start synpred54_C
    public final void synpred54_C_fragment() throws RecognitionException {   
        // C.g:271:4: ( '*' ( type_qualifier )+ ( pointer )? )
        // C.g:271:4: '*' ( type_qualifier )+ ( pointer )?
        {
        match(input,52,FOLLOW_52_in_synpred54_C938); if (state.failed) return ;
        // C.g:271:8: ( type_qualifier )+
        int cnt80=0;
        loop80:
        do {
            int alt80=2;
            int LA80_0 = input.LA(1);

            if ( ((LA80_0>=46 && LA80_0<=47)) ) {
                alt80=1;
            }


            switch (alt80) {
        	case 1 :
        	    // C.g:0:0: type_qualifier
        	    {
        	    pushFollow(FOLLOW_type_qualifier_in_synpred54_C940);
        	    type_qualifier();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt80 >= 1 ) break loop80;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(80, input);
                    throw eee;
            }
            cnt80++;
        } while (true);

        // C.g:271:24: ( pointer )?
        int alt81=2;
        int LA81_0 = input.LA(1);

        if ( (LA81_0==52) ) {
            alt81=1;
        }
        switch (alt81) {
            case 1 :
                // C.g:0:0: pointer
                {
                pushFollow(FOLLOW_pointer_in_synpred54_C943);
                pointer();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred54_C

    // $ANTLR start synpred55_C
    public final void synpred55_C_fragment() throws RecognitionException {   
        // C.g:272:4: ( '*' pointer )
        // C.g:272:4: '*' pointer
        {
        match(input,52,FOLLOW_52_in_synpred55_C949); if (state.failed) return ;
        pushFollow(FOLLOW_pointer_in_synpred55_C951);
        pointer();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred55_C

    // $ANTLR start synpred58_C
    public final void synpred58_C_fragment() throws RecognitionException {   
        // C.g:285:28: ( declarator )
        // C.g:285:28: declarator
        {
        pushFollow(FOLLOW_declarator_in_synpred58_C1006);
        declarator();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred58_C

    // $ANTLR start synpred59_C
    public final void synpred59_C_fragment() throws RecognitionException {   
        // C.g:285:39: ( abstract_declarator )
        // C.g:285:39: abstract_declarator
        {
        pushFollow(FOLLOW_abstract_declarator_in_synpred59_C1008);
        abstract_declarator();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred59_C

    // $ANTLR start synpred62_C
    public final void synpred62_C_fragment() throws RecognitionException {   
        // C.g:297:12: ( direct_abstract_declarator )
        // C.g:297:12: direct_abstract_declarator
        {
        pushFollow(FOLLOW_direct_abstract_declarator_in_synpred62_C1055);
        direct_abstract_declarator();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred62_C

    // $ANTLR start synpred65_C
    public final void synpred65_C_fragment() throws RecognitionException {   
        // C.g:302:65: ( abstract_declarator_suffix )
        // C.g:302:65: abstract_declarator_suffix
        {
        pushFollow(FOLLOW_abstract_declarator_suffix_in_synpred65_C1086);
        abstract_declarator_suffix();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred65_C

    // $ANTLR start synpred78_C
    public final void synpred78_C_fragment() throws RecognitionException {   
        // C.g:336:4: ( '(' type_name ')' cast_expression )
        // C.g:336:4: '(' type_name ')' cast_expression
        {
        match(input,48,FOLLOW_48_in_synpred78_C1258); if (state.failed) return ;
        pushFollow(FOLLOW_type_name_in_synpred78_C1260);
        type_name();

        state._fsp--;
        if (state.failed) return ;
        match(input,49,FOLLOW_49_in_synpred78_C1262); if (state.failed) return ;
        pushFollow(FOLLOW_cast_expression_in_synpred78_C1264);
        cast_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred78_C

    // $ANTLR start synpred83_C
    public final void synpred83_C_fragment() throws RecognitionException {   
        // C.g:345:4: ( 'sizeof' unary_expression )
        // C.g:345:4: 'sizeof' unary_expression
        {
        match(input,60,FOLLOW_60_in_synpred83_C1306); if (state.failed) return ;
        pushFollow(FOLLOW_unary_expression_in_synpred83_C1308);
        unary_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred83_C

    // $ANTLR start synpred104_C
    public final void synpred104_C_fragment() throws RecognitionException {   
        // C.g:396:4: ( lvalue assignment_operator assignment_expression )
        // C.g:396:4: lvalue assignment_operator assignment_expression
        {
        pushFollow(FOLLOW_lvalue_in_synpred104_C1620);
        lvalue();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_assignment_operator_in_synpred104_C1622);
        assignment_operator();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_assignment_expression_in_synpred104_C1624);
        assignment_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred104_C

    // $ANTLR start synpred136_C
    public final void synpred136_C_fragment() throws RecognitionException {   
        // C.g:475:8: ( declaration )
        // C.g:475:8: declaration
        {
        pushFollow(FOLLOW_declaration_in_synpred136_C1980);
        declaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred136_C

    // Delegated rules

    public final boolean synpred65_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred65_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred47_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred47_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred136_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred136_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred50_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred50_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred62_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred62_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred35_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred35_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred104_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred104_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred55_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred55_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred45_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred45_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred51_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred51_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred54_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred54_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred78_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred78_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred59_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred59_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred58_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred58_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred52_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred52_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred83_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred83_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred53_C() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred53_C_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA1 dfa1 = new DFA1(this);
    protected DFA2 dfa2 = new DFA2(this);
    protected DFA3 dfa3 = new DFA3(this);
    protected DFA5 dfa5 = new DFA5(this);
    protected DFA4 dfa4 = new DFA4(this);
    protected DFA8 dfa8 = new DFA8(this);
    protected DFA6 dfa6 = new DFA6(this);
    protected DFA9 dfa9 = new DFA9(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA14 dfa14 = new DFA14(this);
    protected DFA15 dfa15 = new DFA15(this);
    protected DFA16 dfa16 = new DFA16(this);
    protected DFA20 dfa20 = new DFA20(this);
    protected DFA24 dfa24 = new DFA24(this);
    protected DFA26 dfa26 = new DFA26(this);
    protected DFA27 dfa27 = new DFA27(this);
    protected DFA30 dfa30 = new DFA30(this);
    protected DFA28 dfa28 = new DFA28(this);
    protected DFA29 dfa29 = new DFA29(this);
    protected DFA32 dfa32 = new DFA32(this);
    protected DFA33 dfa33 = new DFA33(this);
    protected DFA36 dfa36 = new DFA36(this);
    protected DFA38 dfa38 = new DFA38(this);
    protected DFA39 dfa39 = new DFA39(this);
    protected DFA40 dfa40 = new DFA40(this);
    protected DFA43 dfa43 = new DFA43(this);
    protected DFA45 dfa45 = new DFA45(this);
    protected DFA46 dfa46 = new DFA46(this);
    protected DFA47 dfa47 = new DFA47(this);
    protected DFA48 dfa48 = new DFA48(this);
    protected DFA49 dfa49 = new DFA49(this);
    protected DFA52 dfa52 = new DFA52(this);
    protected DFA54 dfa54 = new DFA54(this);
    protected DFA55 dfa55 = new DFA55(this);
    protected DFA56 dfa56 = new DFA56(this);
    protected DFA57 dfa57 = new DFA57(this);
    protected DFA58 dfa58 = new DFA58(this);
    protected DFA59 dfa59 = new DFA59(this);
    protected DFA60 dfa60 = new DFA60(this);
    protected DFA61 dfa61 = new DFA61(this);
    protected DFA62 dfa62 = new DFA62(this);
    protected DFA64 dfa64 = new DFA64(this);
    protected DFA65 dfa65 = new DFA65(this);
    protected DFA66 dfa66 = new DFA66(this);
    protected DFA72 dfa72 = new DFA72(this);
    protected DFA73 dfa73 = new DFA73(this);
    protected DFA74 dfa74 = new DFA74(this);
    static final String DFA1_eotS =
        "\23\uffff";
    static final String DFA1_eofS =
        "\1\1\22\uffff";
    static final String DFA1_minS =
        "\1\4\22\uffff";
    static final String DFA1_maxS =
        "\1\64\22\uffff";
    static final String DFA1_acceptS =
        "\1\uffff\1\2\1\1\20\uffff";
    static final String DFA1_specialS =
        "\23\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\2\22\uffff\1\2\3\uffff\15\2\2\uffff\2\2\1\uffff\4\2\3\uffff"+
            "\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "()+ loopback of 80:4: ( external_declaration )+";
        }
    }
    static final String DFA2_eotS =
        "\22\uffff";
    static final String DFA2_eofS =
        "\22\uffff";
    static final String DFA2_minS =
        "\1\4\16\0\3\uffff";
    static final String DFA2_maxS =
        "\1\64\16\0\3\uffff";
    static final String DFA2_acceptS =
        "\17\uffff\2\1\1\2";
    static final String DFA2_specialS =
        "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1"+
        "\16\3\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\15\22\uffff\1\21\3\uffff\4\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10"+
            "\1\11\1\12\2\uffff\2\13\1\uffff\1\14\2\16\1\20\3\uffff\1\17",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "83:1: external_declaration options {k=1; } : ( ( ( declaration_specifiers )? declarator ( declaration )* '{' )=> function_definition | declaration );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA2_0 = input.LA(1);

                         
                        int index2_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA2_0>=27 && LA2_0<=30)) ) {s = 1;}

                        else if ( (LA2_0==31) ) {s = 2;}

                        else if ( (LA2_0==32) ) {s = 3;}

                        else if ( (LA2_0==33) ) {s = 4;}

                        else if ( (LA2_0==34) ) {s = 5;}

                        else if ( (LA2_0==35) ) {s = 6;}

                        else if ( (LA2_0==36) ) {s = 7;}

                        else if ( (LA2_0==37) ) {s = 8;}

                        else if ( (LA2_0==38) ) {s = 9;}

                        else if ( (LA2_0==39) ) {s = 10;}

                        else if ( ((LA2_0>=42 && LA2_0<=43)) ) {s = 11;}

                        else if ( (LA2_0==45) ) {s = 12;}

                        else if ( (LA2_0==IDENTIFIER) ) {s = 13;}

                        else if ( ((LA2_0>=46 && LA2_0<=47)) ) {s = 14;}

                        else if ( (LA2_0==52) && (synpred4_C())) {s = 15;}

                        else if ( (LA2_0==48) && (synpred4_C())) {s = 16;}

                        else if ( (LA2_0==23) ) {s = 17;}

                         
                        input.seek(index2_0);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA2_1 = input.LA(1);

                         
                        int index2_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA2_2 = input.LA(1);

                         
                        int index2_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_2);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA2_3 = input.LA(1);

                         
                        int index2_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_3);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA2_4 = input.LA(1);

                         
                        int index2_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_4);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA2_5 = input.LA(1);

                         
                        int index2_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_5);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA2_6 = input.LA(1);

                         
                        int index2_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_6);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA2_7 = input.LA(1);

                         
                        int index2_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_7);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA2_8 = input.LA(1);

                         
                        int index2_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_8);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA2_9 = input.LA(1);

                         
                        int index2_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_9);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA2_10 = input.LA(1);

                         
                        int index2_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_10);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA2_11 = input.LA(1);

                         
                        int index2_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_11);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA2_12 = input.LA(1);

                         
                        int index2_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_12);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA2_13 = input.LA(1);

                         
                        int index2_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (((synpred4_C()&&(isTypeName(input.LT(1).getText())))||synpred4_C())) ) {s = 16;}

                        else if ( ((isTypeName(input.LT(1).getText()))) ) {s = 17;}

                         
                        input.seek(index2_13);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA2_14 = input.LA(1);

                         
                        int index2_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_C()) ) {s = 16;}

                        else if ( (true) ) {s = 17;}

                         
                        input.seek(index2_14);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 2, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA3_eotS =
        "\44\uffff";
    static final String DFA3_eofS =
        "\44\uffff";
    static final String DFA3_minS =
        "\1\4\14\uffff\1\4\4\uffff\17\0\3\uffff";
    static final String DFA3_maxS =
        "\1\64\14\uffff\1\64\4\uffff\17\0\3\uffff";
    static final String DFA3_acceptS =
        "\1\uffff\1\1\15\uffff\1\2\24\uffff";
    static final String DFA3_specialS =
        "\22\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\3\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\15\26\uffff\15\1\2\uffff\2\1\1\uffff\3\1\1\17\3\uffff\1\17",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\22\22\uffff\1\17\3\uffff\4\24\1\25\1\26\1\27\1\30\1\31\1"+
            "\32\1\33\1\34\1\35\1\17\1\uffff\2\36\1\uffff\1\37\2\40\1\23"+
            "\1\uffff\1\17\1\uffff\1\1",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "108:4: ( declaration_specifiers )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA3_18 = input.LA(1);

                         
                        int index3_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_18);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA3_19 = input.LA(1);

                         
                        int index3_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_19);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA3_20 = input.LA(1);

                         
                        int index3_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_20);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA3_21 = input.LA(1);

                         
                        int index3_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_21);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA3_22 = input.LA(1);

                         
                        int index3_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_22);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA3_23 = input.LA(1);

                         
                        int index3_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_23);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA3_24 = input.LA(1);

                         
                        int index3_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_24);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA3_25 = input.LA(1);

                         
                        int index3_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_25);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA3_26 = input.LA(1);

                         
                        int index3_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_26);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA3_27 = input.LA(1);

                         
                        int index3_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_27);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA3_28 = input.LA(1);

                         
                        int index3_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_28);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA3_29 = input.LA(1);

                         
                        int index3_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_29);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA3_30 = input.LA(1);

                         
                        int index3_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_30);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA3_31 = input.LA(1);

                         
                        int index3_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_31);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA3_32 = input.LA(1);

                         
                        int index3_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred5_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index3_32);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 3, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA5_eotS =
        "\21\uffff";
    static final String DFA5_eofS =
        "\21\uffff";
    static final String DFA5_minS =
        "\1\4\20\uffff";
    static final String DFA5_maxS =
        "\1\57\20\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\1\16\uffff\1\2";
    static final String DFA5_specialS =
        "\21\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\1\22\uffff\1\1\3\uffff\15\1\1\20\1\uffff\2\1\1\uffff\3\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "109:3: ( ( declaration )+ compound_statement | compound_statement )";
        }
    }
    static final String DFA4_eotS =
        "\21\uffff";
    static final String DFA4_eofS =
        "\21\uffff";
    static final String DFA4_minS =
        "\1\4\20\uffff";
    static final String DFA4_maxS =
        "\1\57\20\uffff";
    static final String DFA4_acceptS =
        "\1\uffff\1\2\1\1\16\uffff";
    static final String DFA4_specialS =
        "\21\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\2\22\uffff\1\2\3\uffff\15\2\1\1\1\uffff\2\2\1\uffff\3\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "()+ loopback of 109:5: ( declaration )+";
        }
    }
    static final String DFA8_eotS =
        "\20\uffff";
    static final String DFA8_eofS =
        "\20\uffff";
    static final String DFA8_minS =
        "\1\4\17\uffff";
    static final String DFA8_maxS =
        "\1\57\17\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\1\2\15\uffff";
    static final String DFA8_specialS =
        "\20\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\2\22\uffff\1\1\3\uffff\15\2\2\uffff\2\2\1\uffff\3\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "117:1: declaration : ( 'typedef' ( declaration_specifiers )? init_declarator_list ';' | declaration_specifiers ( init_declarator_list )? ';' -> {$declaration.text.contains(\"static \") && $init_declarator_list.underMonitoring}? template(declReplaced=$declaration.text.replace(\"static \",\"\")) \"<declReplaced>\" -> template(declOriginal=$declaration.text) \"<declOriginal>\");";
        }
    }
    static final String DFA6_eotS =
        "\45\uffff";
    static final String DFA6_eofS =
        "\45\uffff";
    static final String DFA6_minS =
        "\1\4\14\uffff\1\4\5\uffff\1\0\21\uffff";
    static final String DFA6_maxS =
        "\1\64\14\uffff\1\64\5\uffff\1\0\21\uffff";
    static final String DFA6_acceptS =
        "\1\uffff\1\1\15\uffff\1\2\25\uffff";
    static final String DFA6_specialS =
        "\23\uffff\1\0\21\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\15\26\uffff\15\1\2\uffff\2\1\1\uffff\3\1\1\17\3\uffff\1\17",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\1\23\uffff\3\17\15\1\2\uffff\2\1\1\uffff\3\1\1\23\1\uffff"+
            "\1\17\1\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "124:14: ( declaration_specifiers )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA6_19 = input.LA(1);

                         
                        int index6_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred8_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index6_19);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 6, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA9_eotS =
        "\56\uffff";
    static final String DFA9_eofS =
        "\1\1\55\uffff";
    static final String DFA9_minS =
        "\1\4\1\uffff\1\0\53\uffff";
    static final String DFA9_maxS =
        "\1\64\1\uffff\1\0\53\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\4\7\uffff\1\1\1\2\12\uffff\1\3\30\uffff";
    static final String DFA9_specialS =
        "\2\uffff\1\0\53\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\2\23\uffff\2\1\1\uffff\4\11\11\12\2\uffff\2\12\1\uffff\1"+
            "\12\2\25\3\1\1\uffff\1\1",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "()+ loopback of 135:6: ( storage_class_specifier | type_specifier | type_qualifier )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA9_2 = input.LA(1);

                         
                        int index9_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred12_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 10;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index9_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 9, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA12_eotS =
        "\15\uffff";
    static final String DFA12_eofS =
        "\15\uffff";
    static final String DFA12_minS =
        "\1\4\14\uffff";
    static final String DFA12_maxS =
        "\1\55\14\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14";
    static final String DFA12_specialS =
        "\15\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\14\32\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\2\uffff"+
            "\2\12\1\uffff\1\13",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "163:1: type_specifier : ( 'void' | 'char' | 'short' | 'int' | 'long' | 'float' | 'double' | 'signed' | 'unsigned' | struct_or_union_specifier | enum_specifier | type_id );";
        }
    }
    static final String DFA14_eotS =
        "\33\uffff";
    static final String DFA14_eofS =
        "\2\uffff\1\4\30\uffff";
    static final String DFA14_minS =
        "\1\52\2\4\30\uffff";
    static final String DFA14_maxS =
        "\1\53\1\50\1\64\30\uffff";
    static final String DFA14_acceptS =
        "\3\uffff\1\1\1\2\26\uffff";
    static final String DFA14_specialS =
        "\33\uffff}>";
    static final String[] DFA14_transitionS = {
            "\2\1",
            "\1\2\43\uffff\1\3",
            "\1\4\23\uffff\2\4\1\uffff\15\4\1\3\1\uffff\11\4\1\uffff\1\4",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
    static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
    static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
    static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
    static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
    static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
    static final short[][] DFA14_transition;

    static {
        int numStates = DFA14_transitionS.length;
        DFA14_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
        }
    }

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = DFA14_eot;
            this.eof = DFA14_eof;
            this.min = DFA14_min;
            this.max = DFA14_max;
            this.accept = DFA14_accept;
            this.special = DFA14_special;
            this.transition = DFA14_transition;
        }
        public String getDescription() {
            return "183:1: struct_or_union_specifier options {k=3; } : ( struct_or_union ( IDENTIFIER )? '{' struct_declaration_list '}' | struct_or_union IDENTIFIER );";
        }
    }
    static final String DFA15_eotS =
        "\17\uffff";
    static final String DFA15_eofS =
        "\17\uffff";
    static final String DFA15_minS =
        "\1\4\16\uffff";
    static final String DFA15_maxS =
        "\1\57\16\uffff";
    static final String DFA15_acceptS =
        "\1\uffff\1\2\1\1\14\uffff";
    static final String DFA15_specialS =
        "\17\uffff}>";
    static final String[] DFA15_transitionS = {
            "\1\2\32\uffff\11\2\1\uffff\1\1\2\2\1\uffff\3\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "()+ loopback of 199:4: ( struct_declaration )+";
        }
    }
    static final String DFA16_eotS =
        "\47\uffff";
    static final String DFA16_eofS =
        "\47\uffff";
    static final String DFA16_minS =
        "\1\4\1\uffff\1\4\20\uffff\3\0\21\uffff";
    static final String DFA16_maxS =
        "\1\64\1\uffff\1\64\20\uffff\3\0\21\uffff";
    static final String DFA16_acceptS =
        "\1\uffff\1\3\5\uffff\1\1\1\2\36\uffff";
    static final String DFA16_specialS =
        "\23\uffff\1\0\1\1\1\2\21\uffff}>";
    static final String[] DFA16_transitionS = {
            "\1\2\32\uffff\11\10\2\uffff\2\10\1\1\1\10\2\7\3\1\1\uffff\1"+
            "\1",
            "",
            "\1\10\23\uffff\2\1\5\uffff\11\10\2\uffff\2\10\1\25\3\10\1\24"+
            "\1\10\1\23\1\uffff\1\10",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "()+ loopback of 207:4: ( type_qualifier | type_specifier )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA16_19 = input.LA(1);

                         
                        int index16_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred35_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 8;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index16_19);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA16_20 = input.LA(1);

                         
                        int index16_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred35_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 8;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index16_20);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA16_21 = input.LA(1);

                         
                        int index16_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred35_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 8;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index16_21);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 16, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA20_eotS =
        "\33\uffff";
    static final String DFA20_eofS =
        "\3\uffff\1\5\27\uffff";
    static final String DFA20_minS =
        "\1\55\1\4\1\uffff\1\4\27\uffff";
    static final String DFA20_maxS =
        "\1\55\1\50\1\uffff\1\64\27\uffff";
    static final String DFA20_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\1\3\25\uffff";
    static final String DFA20_specialS =
        "\33\uffff}>";
    static final String[] DFA20_transitionS = {
            "\1\1",
            "\1\3\43\uffff\1\2",
            "",
            "\1\5\23\uffff\2\5\1\uffff\15\5\1\4\1\uffff\11\5\1\uffff\1\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "219:1: enum_specifier options {k=3; } : ( 'enum' '{' enumerator_list '}' | 'enum' IDENTIFIER '{' enumerator_list '}' | 'enum' IDENTIFIER );";
        }
    }
    static final String DFA24_eotS =
        "\36\uffff";
    static final String DFA24_eofS =
        "\36\uffff";
    static final String DFA24_minS =
        "\1\4\1\0\34\uffff";
    static final String DFA24_maxS =
        "\1\64\1\0\34\uffff";
    static final String DFA24_acceptS =
        "\2\uffff\1\1\32\uffff\1\2";
    static final String DFA24_specialS =
        "\1\uffff\1\0\34\uffff}>";
    static final String[] DFA24_transitionS = {
            "\1\2\53\uffff\1\2\3\uffff\1\1",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA24_eot = DFA.unpackEncodedString(DFA24_eotS);
    static final short[] DFA24_eof = DFA.unpackEncodedString(DFA24_eofS);
    static final char[] DFA24_min = DFA.unpackEncodedStringToUnsignedChars(DFA24_minS);
    static final char[] DFA24_max = DFA.unpackEncodedStringToUnsignedChars(DFA24_maxS);
    static final short[] DFA24_accept = DFA.unpackEncodedString(DFA24_acceptS);
    static final short[] DFA24_special = DFA.unpackEncodedString(DFA24_specialS);
    static final short[][] DFA24_transition;

    static {
        int numStates = DFA24_transitionS.length;
        DFA24_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA24_transition[i] = DFA.unpackEncodedString(DFA24_transitionS[i]);
        }
    }

    class DFA24 extends DFA {

        public DFA24(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 24;
            this.eot = DFA24_eot;
            this.eof = DFA24_eof;
            this.min = DFA24_min;
            this.max = DFA24_max;
            this.accept = DFA24_accept;
            this.special = DFA24_special;
            this.transition = DFA24_transition;
        }
        public String getDescription() {
            return "239:1: declarator returns [boolean underMonitoring] : ( ( pointer )? direct_declarator | pointer );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA24_1 = input.LA(1);

                         
                        int index24_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_C()) ) {s = 2;}

                        else if ( (true) ) {s = 29;}

                         
                        input.seek(index24_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 24, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA26_eotS =
        "\65\uffff";
    static final String DFA26_eofS =
        "\1\1\64\uffff";
    static final String DFA26_minS =
        "\1\4\27\uffff\2\4\1\0\3\uffff\26\0\1\uffff";
    static final String DFA26_maxS =
        "\1\64\27\uffff\1\64\1\101\1\0\3\uffff\26\0\1\uffff";
    static final String DFA26_acceptS =
        "\1\uffff\1\2\62\uffff\1\1";
    static final String DFA26_specialS =
        "\32\uffff\1\0\3\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12"+
        "\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff}>";
    static final String[] DFA26_transitionS = {
            "\1\1\22\uffff\22\1\1\uffff\6\1\1\30\1\1\1\31\1\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\52\26\uffff\4\36\1\37\1\40\1\41\1\42\1\43\1\44\1\45\1\46"+
            "\1\47\2\uffff\2\50\1\uffff\1\51\2\53\1\1\1\32\1\1\1\uffff\1"+
            "\1",
            "\1\56\6\57\45\uffff\1\55\2\uffff\1\54\1\62\1\uffff\2\62\2\uffff"+
            "\1\60\1\61\1\63\2\uffff\3\62",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA26_eot = DFA.unpackEncodedString(DFA26_eotS);
    static final short[] DFA26_eof = DFA.unpackEncodedString(DFA26_eofS);
    static final char[] DFA26_min = DFA.unpackEncodedStringToUnsignedChars(DFA26_minS);
    static final char[] DFA26_max = DFA.unpackEncodedStringToUnsignedChars(DFA26_maxS);
    static final short[] DFA26_accept = DFA.unpackEncodedString(DFA26_acceptS);
    static final short[] DFA26_special = DFA.unpackEncodedString(DFA26_specialS);
    static final short[][] DFA26_transition;

    static {
        int numStates = DFA26_transitionS.length;
        DFA26_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA26_transition[i] = DFA.unpackEncodedString(DFA26_transitionS[i]);
        }
    }

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = DFA26_eot;
            this.eof = DFA26_eof;
            this.min = DFA26_min;
            this.max = DFA26_max;
            this.accept = DFA26_accept;
            this.special = DFA26_special;
            this.transition = DFA26_transition;
        }
        public String getDescription() {
            return "()* loopback of 259:9: ( declarator_suffix )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA26_26 = input.LA(1);

                         
                        int index26_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_26);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA26_30 = input.LA(1);

                         
                        int index26_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_30);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA26_31 = input.LA(1);

                         
                        int index26_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_31);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA26_32 = input.LA(1);

                         
                        int index26_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_32);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA26_33 = input.LA(1);

                         
                        int index26_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_33);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA26_34 = input.LA(1);

                         
                        int index26_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_34);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA26_35 = input.LA(1);

                         
                        int index26_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_35);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA26_36 = input.LA(1);

                         
                        int index26_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_36);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA26_37 = input.LA(1);

                         
                        int index26_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_37);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA26_38 = input.LA(1);

                         
                        int index26_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_38);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA26_39 = input.LA(1);

                         
                        int index26_39 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_39);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA26_40 = input.LA(1);

                         
                        int index26_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_40);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA26_41 = input.LA(1);

                         
                        int index26_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_41);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA26_42 = input.LA(1);

                         
                        int index26_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_42);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA26_43 = input.LA(1);

                         
                        int index26_43 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_43);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA26_44 = input.LA(1);

                         
                        int index26_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_44);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA26_45 = input.LA(1);

                         
                        int index26_45 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_45);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA26_46 = input.LA(1);

                         
                        int index26_46 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_46);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA26_47 = input.LA(1);

                         
                        int index26_47 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_47);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA26_48 = input.LA(1);

                         
                        int index26_48 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_48);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA26_49 = input.LA(1);

                         
                        int index26_49 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_49);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA26_50 = input.LA(1);

                         
                        int index26_50 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_50);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA26_51 = input.LA(1);

                         
                        int index26_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred47_C()) ) {s = 52;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index26_51);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 26, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA27_eotS =
        "\33\uffff";
    static final String DFA27_eofS =
        "\33\uffff";
    static final String DFA27_minS =
        "\1\60\2\4\25\uffff\1\0\2\uffff";
    static final String DFA27_maxS =
        "\1\62\1\101\1\61\25\uffff\1\0\2\uffff";
    static final String DFA27_acceptS =
        "\3\uffff\1\2\1\1\6\uffff\1\5\1\3\15\uffff\1\4";
    static final String DFA27_specialS =
        "\30\uffff\1\0\2\uffff}>";
    static final String[] DFA27_transitionS = {
            "\1\2\1\uffff\1\1",
            "\7\4\45\uffff\1\4\2\uffff\1\3\1\4\1\uffff\2\4\2\uffff\3\4\2"+
            "\uffff\3\4",
            "\1\30\26\uffff\15\14\2\uffff\2\14\1\uffff\3\14\1\uffff\1\13",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA27_eot = DFA.unpackEncodedString(DFA27_eotS);
    static final short[] DFA27_eof = DFA.unpackEncodedString(DFA27_eofS);
    static final char[] DFA27_min = DFA.unpackEncodedStringToUnsignedChars(DFA27_minS);
    static final char[] DFA27_max = DFA.unpackEncodedStringToUnsignedChars(DFA27_maxS);
    static final short[] DFA27_accept = DFA.unpackEncodedString(DFA27_acceptS);
    static final short[] DFA27_special = DFA.unpackEncodedString(DFA27_specialS);
    static final short[][] DFA27_transition;

    static {
        int numStates = DFA27_transitionS.length;
        DFA27_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA27_transition[i] = DFA.unpackEncodedString(DFA27_transitionS[i]);
        }
    }

    class DFA27 extends DFA {

        public DFA27(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 27;
            this.eot = DFA27_eot;
            this.eof = DFA27_eof;
            this.min = DFA27_min;
            this.max = DFA27_max;
            this.accept = DFA27_accept;
            this.special = DFA27_special;
            this.transition = DFA27_transition;
        }
        public String getDescription() {
            return "262:1: declarator_suffix : ( '[' constant_expression ']' | '[' ']' | '(' parameter_type_list ')' | '(' identifier_list ')' | '(' ')' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA27_24 = input.LA(1);

                         
                        int index27_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_C()) ) {s = 12;}

                        else if ( (synpred51_C()) ) {s = 26;}

                         
                        input.seek(index27_24);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 27, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA30_eotS =
        "\35\uffff";
    static final String DFA30_eofS =
        "\1\uffff\1\3\33\uffff";
    static final String DFA30_minS =
        "\1\64\1\4\1\0\26\uffff\1\0\3\uffff";
    static final String DFA30_maxS =
        "\2\64\1\0\26\uffff\1\0\3\uffff";
    static final String DFA30_acceptS =
        "\3\uffff\1\3\27\uffff\1\1\1\2";
    static final String DFA30_specialS =
        "\2\uffff\1\0\26\uffff\1\1\3\uffff}>";
    static final String[] DFA30_transitionS = {
            "\1\1",
            "\1\3\22\uffff\22\3\1\uffff\4\3\2\2\3\3\1\uffff\1\31",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA30_eot = DFA.unpackEncodedString(DFA30_eotS);
    static final short[] DFA30_eof = DFA.unpackEncodedString(DFA30_eofS);
    static final char[] DFA30_min = DFA.unpackEncodedStringToUnsignedChars(DFA30_minS);
    static final char[] DFA30_max = DFA.unpackEncodedStringToUnsignedChars(DFA30_maxS);
    static final short[] DFA30_accept = DFA.unpackEncodedString(DFA30_acceptS);
    static final short[] DFA30_special = DFA.unpackEncodedString(DFA30_specialS);
    static final short[][] DFA30_transition;

    static {
        int numStates = DFA30_transitionS.length;
        DFA30_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA30_transition[i] = DFA.unpackEncodedString(DFA30_transitionS[i]);
        }
    }

    class DFA30 extends DFA {

        public DFA30(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 30;
            this.eot = DFA30_eot;
            this.eof = DFA30_eof;
            this.min = DFA30_min;
            this.max = DFA30_max;
            this.accept = DFA30_accept;
            this.special = DFA30_special;
            this.transition = DFA30_transition;
        }
        public String getDescription() {
            return "270:1: pointer : ( '*' ( type_qualifier )+ ( pointer )? | '*' pointer | '*' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA30_2 = input.LA(1);

                         
                        int index30_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred54_C()) ) {s = 27;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index30_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA30_25 = input.LA(1);

                         
                        int index30_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred55_C()) ) {s = 28;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index30_25);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 30, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA28_eotS =
        "\64\uffff";
    static final String DFA28_eofS =
        "\1\1\63\uffff";
    static final String DFA28_minS =
        "\1\4\20\uffff\1\0\42\uffff";
    static final String DFA28_maxS =
        "\1\64\20\uffff\1\0\42\uffff";
    static final String DFA28_acceptS =
        "\1\uffff\1\2\61\uffff\1\1";
    static final String DFA28_specialS =
        "\21\uffff\1\0\42\uffff}>";
    static final String[] DFA28_transitionS = {
            "\1\1\22\uffff\22\1\1\uffff\4\1\2\21\3\1\1\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA28_eot = DFA.unpackEncodedString(DFA28_eotS);
    static final short[] DFA28_eof = DFA.unpackEncodedString(DFA28_eofS);
    static final char[] DFA28_min = DFA.unpackEncodedStringToUnsignedChars(DFA28_minS);
    static final char[] DFA28_max = DFA.unpackEncodedStringToUnsignedChars(DFA28_maxS);
    static final short[] DFA28_accept = DFA.unpackEncodedString(DFA28_acceptS);
    static final short[] DFA28_special = DFA.unpackEncodedString(DFA28_specialS);
    static final short[][] DFA28_transition;

    static {
        int numStates = DFA28_transitionS.length;
        DFA28_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA28_transition[i] = DFA.unpackEncodedString(DFA28_transitionS[i]);
        }
    }

    class DFA28 extends DFA {

        public DFA28(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 28;
            this.eot = DFA28_eot;
            this.eof = DFA28_eof;
            this.min = DFA28_min;
            this.max = DFA28_max;
            this.accept = DFA28_accept;
            this.special = DFA28_special;
            this.transition = DFA28_transition;
        }
        public String getDescription() {
            return "()+ loopback of 271:8: ( type_qualifier )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA28_17 = input.LA(1);

                         
                        int index28_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred52_C()) ) {s = 51;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index28_17);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 28, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA29_eotS =
        "\64\uffff";
    static final String DFA29_eofS =
        "\1\2\63\uffff";
    static final String DFA29_minS =
        "\1\4\1\0\62\uffff";
    static final String DFA29_maxS =
        "\1\64\1\0\62\uffff";
    static final String DFA29_acceptS =
        "\2\uffff\1\2\60\uffff\1\1";
    static final String DFA29_specialS =
        "\1\uffff\1\0\62\uffff}>";
    static final String[] DFA29_transitionS = {
            "\1\2\22\uffff\22\2\1\uffff\11\2\1\uffff\1\1",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA29_eot = DFA.unpackEncodedString(DFA29_eotS);
    static final short[] DFA29_eof = DFA.unpackEncodedString(DFA29_eofS);
    static final char[] DFA29_min = DFA.unpackEncodedStringToUnsignedChars(DFA29_minS);
    static final char[] DFA29_max = DFA.unpackEncodedStringToUnsignedChars(DFA29_maxS);
    static final short[] DFA29_accept = DFA.unpackEncodedString(DFA29_acceptS);
    static final short[] DFA29_special = DFA.unpackEncodedString(DFA29_specialS);
    static final short[][] DFA29_transition;

    static {
        int numStates = DFA29_transitionS.length;
        DFA29_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA29_transition[i] = DFA.unpackEncodedString(DFA29_transitionS[i]);
        }
    }

    class DFA29 extends DFA {

        public DFA29(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 29;
            this.eot = DFA29_eot;
            this.eof = DFA29_eof;
            this.min = DFA29_min;
            this.max = DFA29_max;
            this.accept = DFA29_accept;
            this.special = DFA29_special;
            this.transition = DFA29_transition;
        }
        public String getDescription() {
            return "271:24: ( pointer )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA29_1 = input.LA(1);

                         
                        int index29_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred53_C()) ) {s = 51;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index29_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 29, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA32_eotS =
        "\22\uffff";
    static final String DFA32_eofS =
        "\22\uffff";
    static final String DFA32_minS =
        "\1\31\1\4\20\uffff";
    static final String DFA32_maxS =
        "\1\61\1\65\20\uffff";
    static final String DFA32_acceptS =
        "\2\uffff\1\2\1\uffff\1\1\15\uffff";
    static final String DFA32_specialS =
        "\22\uffff}>";
    static final String[] DFA32_transitionS = {
            "\1\1\27\uffff\1\2",
            "\1\4\26\uffff\15\4\2\uffff\2\4\1\uffff\3\4\5\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }
        public String getDescription() {
            return "()* loopback of 281:26: ( ',' parameter_declaration )*";
        }
    }
    static final String DFA33_eotS =
        "\42\uffff";
    static final String DFA33_eofS =
        "\1\1\41\uffff";
    static final String DFA33_minS =
        "\1\4\3\uffff\1\0\1\uffff\1\4\12\uffff\2\0\1\uffff\1\0\15\uffff";
    static final String DFA33_maxS =
        "\1\64\3\uffff\1\0\1\uffff\1\64\12\uffff\2\0\1\uffff\1\0\15\uffff";
    static final String DFA33_acceptS =
        "\1\uffff\1\3\3\uffff\1\1\1\uffff\1\2\32\uffff";
    static final String DFA33_specialS =
        "\4\uffff\1\0\14\uffff\1\1\1\2\1\uffff\1\3\15\uffff}>";
    static final String[] DFA33_transitionS = {
            "\1\5\24\uffff\1\1\26\uffff\1\6\1\1\1\7\1\uffff\1\4",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "\1\24\26\uffff\15\7\2\uffff\2\7\1\uffff\3\7\1\22\2\7\1\uffff"+
            "\1\21",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA33_eot = DFA.unpackEncodedString(DFA33_eotS);
    static final short[] DFA33_eof = DFA.unpackEncodedString(DFA33_eofS);
    static final char[] DFA33_min = DFA.unpackEncodedStringToUnsignedChars(DFA33_minS);
    static final char[] DFA33_max = DFA.unpackEncodedStringToUnsignedChars(DFA33_maxS);
    static final short[] DFA33_accept = DFA.unpackEncodedString(DFA33_acceptS);
    static final short[] DFA33_special = DFA.unpackEncodedString(DFA33_specialS);
    static final short[][] DFA33_transition;

    static {
        int numStates = DFA33_transitionS.length;
        DFA33_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA33_transition[i] = DFA.unpackEncodedString(DFA33_transitionS[i]);
        }
    }

    class DFA33 extends DFA {

        public DFA33(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 33;
            this.eot = DFA33_eot;
            this.eof = DFA33_eof;
            this.min = DFA33_min;
            this.max = DFA33_max;
            this.accept = DFA33_accept;
            this.special = DFA33_special;
            this.transition = DFA33_transition;
        }
        public String getDescription() {
            return "()* loopback of 285:27: ( declarator | abstract_declarator )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA33_4 = input.LA(1);

                         
                        int index33_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred58_C()) ) {s = 5;}

                        else if ( (synpred59_C()) ) {s = 7;}

                         
                        input.seek(index33_4);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA33_17 = input.LA(1);

                         
                        int index33_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred58_C()) ) {s = 5;}

                        else if ( (synpred59_C()) ) {s = 7;}

                         
                        input.seek(index33_17);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA33_18 = input.LA(1);

                         
                        int index33_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred58_C()) ) {s = 5;}

                        else if ( (synpred59_C()) ) {s = 7;}

                         
                        input.seek(index33_18);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA33_20 = input.LA(1);

                         
                        int index33_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred58_C()) ) {s = 5;}

                        else if ( (synpred59_C()) ) {s = 7;}

                         
                        input.seek(index33_20);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 33, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA36_eotS =
        "\43\uffff";
    static final String DFA36_eofS =
        "\1\3\42\uffff";
    static final String DFA36_minS =
        "\3\4\5\uffff\32\0\1\uffff";
    static final String DFA36_maxS =
        "\2\64\1\101\5\uffff\32\0\1\uffff";
    static final String DFA36_acceptS =
        "\3\uffff\1\2\36\uffff\1\1";
    static final String DFA36_specialS =
        "\10\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30"+
        "\1\31\1\uffff}>";
    static final String[] DFA36_transitionS = {
            "\1\3\24\uffff\1\3\26\uffff\1\1\1\3\1\2\1\uffff\1\3",
            "\1\14\26\uffff\4\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25"+
            "\1\26\2\uffff\2\27\1\uffff\1\30\2\31\1\12\1\10\1\13\1\uffff"+
            "\1\11",
            "\1\34\6\35\45\uffff\1\33\2\uffff\1\32\1\40\1\uffff\2\40\2\uffff"+
            "\1\36\1\37\1\41\2\uffff\3\40",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA36_eot = DFA.unpackEncodedString(DFA36_eotS);
    static final short[] DFA36_eof = DFA.unpackEncodedString(DFA36_eofS);
    static final char[] DFA36_min = DFA.unpackEncodedStringToUnsignedChars(DFA36_minS);
    static final char[] DFA36_max = DFA.unpackEncodedStringToUnsignedChars(DFA36_maxS);
    static final short[] DFA36_accept = DFA.unpackEncodedString(DFA36_acceptS);
    static final short[] DFA36_special = DFA.unpackEncodedString(DFA36_specialS);
    static final short[][] DFA36_transition;

    static {
        int numStates = DFA36_transitionS.length;
        DFA36_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA36_transition[i] = DFA.unpackEncodedString(DFA36_transitionS[i]);
        }
    }

    class DFA36 extends DFA {

        public DFA36(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 36;
            this.eot = DFA36_eot;
            this.eof = DFA36_eof;
            this.min = DFA36_min;
            this.max = DFA36_max;
            this.accept = DFA36_accept;
            this.special = DFA36_special;
            this.transition = DFA36_transition;
        }
        public String getDescription() {
            return "297:12: ( direct_abstract_declarator )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA36_8 = input.LA(1);

                         
                        int index36_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_8);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA36_9 = input.LA(1);

                         
                        int index36_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_9);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA36_10 = input.LA(1);

                         
                        int index36_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_10);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA36_11 = input.LA(1);

                         
                        int index36_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_11);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA36_12 = input.LA(1);

                         
                        int index36_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_12);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA36_13 = input.LA(1);

                         
                        int index36_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_13);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA36_14 = input.LA(1);

                         
                        int index36_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_14);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA36_15 = input.LA(1);

                         
                        int index36_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_15);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA36_16 = input.LA(1);

                         
                        int index36_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_16);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA36_17 = input.LA(1);

                         
                        int index36_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_17);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA36_18 = input.LA(1);

                         
                        int index36_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_18);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA36_19 = input.LA(1);

                         
                        int index36_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_19);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA36_20 = input.LA(1);

                         
                        int index36_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_20);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA36_21 = input.LA(1);

                         
                        int index36_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_21);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA36_22 = input.LA(1);

                         
                        int index36_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_22);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA36_23 = input.LA(1);

                         
                        int index36_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_23);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA36_24 = input.LA(1);

                         
                        int index36_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_24);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA36_25 = input.LA(1);

                         
                        int index36_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_25);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA36_26 = input.LA(1);

                         
                        int index36_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_26);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA36_27 = input.LA(1);

                         
                        int index36_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_27);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA36_28 = input.LA(1);

                         
                        int index36_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_28);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA36_29 = input.LA(1);

                         
                        int index36_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_29);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA36_30 = input.LA(1);

                         
                        int index36_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_30);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA36_31 = input.LA(1);

                         
                        int index36_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_31);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA36_32 = input.LA(1);

                         
                        int index36_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_32);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA36_33 = input.LA(1);

                         
                        int index36_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred62_C()) ) {s = 34;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index36_33);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 36, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA38_eotS =
        "\25\uffff";
    static final String DFA38_eofS =
        "\25\uffff";
    static final String DFA38_minS =
        "\1\60\1\4\23\uffff";
    static final String DFA38_maxS =
        "\1\62\1\64\23\uffff";
    static final String DFA38_acceptS =
        "\2\uffff\1\2\17\uffff\1\1\2\uffff";
    static final String DFA38_specialS =
        "\25\uffff}>";
    static final String[] DFA38_transitionS = {
            "\1\1\1\uffff\1\2",
            "\1\2\26\uffff\15\2\2\uffff\2\2\1\uffff\3\2\1\22\1\2\1\22\1"+
            "\uffff\1\22",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA38_eot = DFA.unpackEncodedString(DFA38_eotS);
    static final short[] DFA38_eof = DFA.unpackEncodedString(DFA38_eofS);
    static final char[] DFA38_min = DFA.unpackEncodedStringToUnsignedChars(DFA38_minS);
    static final char[] DFA38_max = DFA.unpackEncodedStringToUnsignedChars(DFA38_maxS);
    static final short[] DFA38_accept = DFA.unpackEncodedString(DFA38_acceptS);
    static final short[] DFA38_special = DFA.unpackEncodedString(DFA38_specialS);
    static final short[][] DFA38_transition;

    static {
        int numStates = DFA38_transitionS.length;
        DFA38_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA38_transition[i] = DFA.unpackEncodedString(DFA38_transitionS[i]);
        }
    }

    class DFA38 extends DFA {

        public DFA38(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 38;
            this.eot = DFA38_eot;
            this.eof = DFA38_eof;
            this.min = DFA38_min;
            this.max = DFA38_max;
            this.accept = DFA38_accept;
            this.special = DFA38_special;
            this.transition = DFA38_transition;
        }
        public String getDescription() {
            return "302:4: ( '(' abstract_declarator ')' | abstract_declarator_suffix )";
        }
    }
    static final String DFA39_eotS =
        "\43\uffff";
    static final String DFA39_eofS =
        "\1\1\42\uffff";
    static final String DFA39_minS =
        "\1\4\5\uffff\2\4\1\0\3\uffff\26\0\1\uffff";
    static final String DFA39_maxS =
        "\1\64\5\uffff\1\64\1\101\1\0\3\uffff\26\0\1\uffff";
    static final String DFA39_acceptS =
        "\1\uffff\1\2\40\uffff\1\1";
    static final String DFA39_specialS =
        "\10\uffff\1\0\3\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12"+
        "\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff}>";
    static final String[] DFA39_transitionS = {
            "\1\1\24\uffff\1\1\26\uffff\1\6\1\1\1\7\1\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "\1\30\26\uffff\4\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24"+
            "\1\25\2\uffff\2\26\1\uffff\1\27\2\31\1\1\1\10\1\1\1\uffff\1"+
            "\1",
            "\1\34\6\35\45\uffff\1\33\2\uffff\1\32\1\40\1\uffff\2\40\2\uffff"+
            "\1\36\1\37\1\41\2\uffff\3\40",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA39_eot = DFA.unpackEncodedString(DFA39_eotS);
    static final short[] DFA39_eof = DFA.unpackEncodedString(DFA39_eofS);
    static final char[] DFA39_min = DFA.unpackEncodedStringToUnsignedChars(DFA39_minS);
    static final char[] DFA39_max = DFA.unpackEncodedStringToUnsignedChars(DFA39_maxS);
    static final short[] DFA39_accept = DFA.unpackEncodedString(DFA39_acceptS);
    static final short[] DFA39_special = DFA.unpackEncodedString(DFA39_specialS);
    static final short[][] DFA39_transition;

    static {
        int numStates = DFA39_transitionS.length;
        DFA39_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA39_transition[i] = DFA.unpackEncodedString(DFA39_transitionS[i]);
        }
    }

    class DFA39 extends DFA {

        public DFA39(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 39;
            this.eot = DFA39_eot;
            this.eof = DFA39_eof;
            this.min = DFA39_min;
            this.max = DFA39_max;
            this.accept = DFA39_accept;
            this.special = DFA39_special;
            this.transition = DFA39_transition;
        }
        public String getDescription() {
            return "()* loopback of 302:65: ( abstract_declarator_suffix )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA39_8 = input.LA(1);

                         
                        int index39_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_8);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA39_12 = input.LA(1);

                         
                        int index39_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_12);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA39_13 = input.LA(1);

                         
                        int index39_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_13);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA39_14 = input.LA(1);

                         
                        int index39_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_14);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA39_15 = input.LA(1);

                         
                        int index39_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_15);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA39_16 = input.LA(1);

                         
                        int index39_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_16);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA39_17 = input.LA(1);

                         
                        int index39_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_17);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA39_18 = input.LA(1);

                         
                        int index39_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_18);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA39_19 = input.LA(1);

                         
                        int index39_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_19);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA39_20 = input.LA(1);

                         
                        int index39_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_20);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA39_21 = input.LA(1);

                         
                        int index39_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_21);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA39_22 = input.LA(1);

                         
                        int index39_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_22);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA39_23 = input.LA(1);

                         
                        int index39_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_23);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA39_24 = input.LA(1);

                         
                        int index39_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_24);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA39_25 = input.LA(1);

                         
                        int index39_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_25);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA39_26 = input.LA(1);

                         
                        int index39_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_26);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA39_27 = input.LA(1);

                         
                        int index39_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_27);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA39_28 = input.LA(1);

                         
                        int index39_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_28);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA39_29 = input.LA(1);

                         
                        int index39_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_29);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA39_30 = input.LA(1);

                         
                        int index39_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_30);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA39_31 = input.LA(1);

                         
                        int index39_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_31);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA39_32 = input.LA(1);

                         
                        int index39_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_32);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA39_33 = input.LA(1);

                         
                        int index39_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_C()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index39_33);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 39, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA40_eotS =
        "\32\uffff";
    static final String DFA40_eofS =
        "\32\uffff";
    static final String DFA40_minS =
        "\1\60\2\4\27\uffff";
    static final String DFA40_maxS =
        "\1\62\1\101\1\61\27\uffff";
    static final String DFA40_acceptS =
        "\3\uffff\1\1\1\2\6\uffff\1\3\1\4\15\uffff";
    static final String DFA40_specialS =
        "\32\uffff}>";
    static final String[] DFA40_transitionS = {
            "\1\2\1\uffff\1\1",
            "\7\4\45\uffff\1\4\2\uffff\1\3\1\4\1\uffff\2\4\2\uffff\3\4\2"+
            "\uffff\3\4",
            "\1\14\26\uffff\15\14\2\uffff\2\14\1\uffff\3\14\1\uffff\1\13",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA40_eot = DFA.unpackEncodedString(DFA40_eotS);
    static final short[] DFA40_eof = DFA.unpackEncodedString(DFA40_eofS);
    static final char[] DFA40_min = DFA.unpackEncodedStringToUnsignedChars(DFA40_minS);
    static final char[] DFA40_max = DFA.unpackEncodedStringToUnsignedChars(DFA40_maxS);
    static final short[] DFA40_accept = DFA.unpackEncodedString(DFA40_acceptS);
    static final short[] DFA40_special = DFA.unpackEncodedString(DFA40_specialS);
    static final short[][] DFA40_transition;

    static {
        int numStates = DFA40_transitionS.length;
        DFA40_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA40_transition[i] = DFA.unpackEncodedString(DFA40_transitionS[i]);
        }
    }

    class DFA40 extends DFA {

        public DFA40(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 40;
            this.eot = DFA40_eot;
            this.eof = DFA40_eof;
            this.min = DFA40_min;
            this.max = DFA40_max;
            this.accept = DFA40_accept;
            this.special = DFA40_special;
            this.transition = DFA40_transition;
        }
        public String getDescription() {
            return "305:1: abstract_declarator_suffix : ( '[' ']' | '[' constant_expression ']' | '(' ')' | '(' parameter_type_list ')' );";
        }
    }
    static final String DFA43_eotS =
        "\14\uffff";
    static final String DFA43_eofS =
        "\14\uffff";
    static final String DFA43_minS =
        "\1\31\1\4\12\uffff";
    static final String DFA43_maxS =
        "\1\51\1\101\12\uffff";
    static final String DFA43_acceptS =
        "\2\uffff\1\2\1\1\10\uffff";
    static final String DFA43_specialS =
        "\14\uffff}>";
    static final String[] DFA43_transitionS = {
            "\1\1\17\uffff\1\2",
            "\7\3\35\uffff\1\3\1\2\6\uffff\1\3\3\uffff\1\3\1\uffff\2\3\2"+
            "\uffff\3\3\2\uffff\3\3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA43_eot = DFA.unpackEncodedString(DFA43_eotS);
    static final short[] DFA43_eof = DFA.unpackEncodedString(DFA43_eofS);
    static final char[] DFA43_min = DFA.unpackEncodedStringToUnsignedChars(DFA43_minS);
    static final char[] DFA43_max = DFA.unpackEncodedStringToUnsignedChars(DFA43_maxS);
    static final short[] DFA43_accept = DFA.unpackEncodedString(DFA43_acceptS);
    static final short[] DFA43_special = DFA.unpackEncodedString(DFA43_specialS);
    static final short[][] DFA43_transition;

    static {
        int numStates = DFA43_transitionS.length;
        DFA43_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA43_transition[i] = DFA.unpackEncodedString(DFA43_transitionS[i]);
        }
    }

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = DFA43_eot;
            this.eof = DFA43_eof;
            this.min = DFA43_min;
            this.max = DFA43_max;
            this.accept = DFA43_accept;
            this.special = DFA43_special;
            this.transition = DFA43_transition;
        }
        public String getDescription() {
            return "()* loopback of 318:16: ( ',' initializer )*";
        }
    }
    static final String DFA45_eotS =
        "\23\uffff";
    static final String DFA45_eofS =
        "\1\1\22\uffff";
    static final String DFA45_minS =
        "\1\30\22\uffff";
    static final String DFA45_maxS =
        "\1\130\22\uffff";
    static final String DFA45_acceptS =
        "\1\uffff\1\3\17\uffff\1\1\1\2";
    static final String DFA45_specialS =
        "\23\uffff}>";
    static final String[] DFA45_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\2\uffff"+
            "\1\21\1\22\7\uffff\1\1\14\uffff\15\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA45_eot = DFA.unpackEncodedString(DFA45_eotS);
    static final short[] DFA45_eof = DFA.unpackEncodedString(DFA45_eofS);
    static final char[] DFA45_min = DFA.unpackEncodedStringToUnsignedChars(DFA45_minS);
    static final char[] DFA45_max = DFA.unpackEncodedStringToUnsignedChars(DFA45_maxS);
    static final short[] DFA45_accept = DFA.unpackEncodedString(DFA45_acceptS);
    static final short[] DFA45_special = DFA.unpackEncodedString(DFA45_specialS);
    static final short[][] DFA45_transition;

    static {
        int numStates = DFA45_transitionS.length;
        DFA45_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA45_transition[i] = DFA.unpackEncodedString(DFA45_transitionS[i]);
        }
    }

    class DFA45 extends DFA {

        public DFA45(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 45;
            this.eot = DFA45_eot;
            this.eof = DFA45_eof;
            this.min = DFA45_min;
            this.max = DFA45_max;
            this.accept = DFA45_accept;
            this.special = DFA45_special;
            this.transition = DFA45_transition;
        }
        public String getDescription() {
            return "()* loopback of 328:32: ( '+' multiplicative_expression | '-' multiplicative_expression )*";
        }
    }
    static final String DFA46_eotS =
        "\26\uffff";
    static final String DFA46_eofS =
        "\1\1\25\uffff";
    static final String DFA46_minS =
        "\1\30\25\uffff";
    static final String DFA46_maxS =
        "\1\130\25\uffff";
    static final String DFA46_acceptS =
        "\1\uffff\1\4\21\uffff\1\1\1\2\1\3";
    static final String DFA46_specialS =
        "\26\uffff}>";
    static final String[] DFA46_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\1\23\1"+
            "\uffff\2\1\1\24\1\25\5\uffff\1\1\14\uffff\15\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA46_eot = DFA.unpackEncodedString(DFA46_eotS);
    static final short[] DFA46_eof = DFA.unpackEncodedString(DFA46_eofS);
    static final char[] DFA46_min = DFA.unpackEncodedStringToUnsignedChars(DFA46_minS);
    static final char[] DFA46_max = DFA.unpackEncodedStringToUnsignedChars(DFA46_maxS);
    static final short[] DFA46_accept = DFA.unpackEncodedString(DFA46_acceptS);
    static final short[] DFA46_special = DFA.unpackEncodedString(DFA46_specialS);
    static final short[][] DFA46_transition;

    static {
        int numStates = DFA46_transitionS.length;
        DFA46_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA46_transition[i] = DFA.unpackEncodedString(DFA46_transitionS[i]);
        }
    }

    class DFA46 extends DFA {

        public DFA46(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 46;
            this.eot = DFA46_eot;
            this.eof = DFA46_eof;
            this.min = DFA46_min;
            this.max = DFA46_max;
            this.accept = DFA46_accept;
            this.special = DFA46_special;
            this.transition = DFA46_transition;
        }
        public String getDescription() {
            return "()* loopback of 332:22: ( '*' cast_expression | '/' cast_expression | '%' cast_expression )*";
        }
    }
    static final String DFA47_eotS =
        "\33\uffff";
    static final String DFA47_eofS =
        "\33\uffff";
    static final String DFA47_minS =
        "\2\4\22\uffff\1\0\6\uffff";
    static final String DFA47_maxS =
        "\2\101\22\uffff\1\0\6\uffff";
    static final String DFA47_acceptS =
        "\2\uffff\1\2\5\uffff\1\1\22\uffff";
    static final String DFA47_specialS =
        "\24\uffff\1\0\6\uffff}>";
    static final String[] DFA47_transitionS = {
            "\7\2\45\uffff\1\1\3\uffff\1\2\1\uffff\2\2\2\uffff\3\2\2\uffff"+
            "\3\2",
            "\1\24\6\2\24\uffff\11\10\2\uffff\2\10\1\uffff\3\10\1\2\3\uffff"+
            "\1\2\1\uffff\2\2\2\uffff\3\2\2\uffff\3\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA47_eot = DFA.unpackEncodedString(DFA47_eotS);
    static final short[] DFA47_eof = DFA.unpackEncodedString(DFA47_eofS);
    static final char[] DFA47_min = DFA.unpackEncodedStringToUnsignedChars(DFA47_minS);
    static final char[] DFA47_max = DFA.unpackEncodedStringToUnsignedChars(DFA47_maxS);
    static final short[] DFA47_accept = DFA.unpackEncodedString(DFA47_acceptS);
    static final short[] DFA47_special = DFA.unpackEncodedString(DFA47_specialS);
    static final short[][] DFA47_transition;

    static {
        int numStates = DFA47_transitionS.length;
        DFA47_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA47_transition[i] = DFA.unpackEncodedString(DFA47_transitionS[i]);
        }
    }

    class DFA47 extends DFA {

        public DFA47(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 47;
            this.eot = DFA47_eot;
            this.eof = DFA47_eof;
            this.min = DFA47_min;
            this.max = DFA47_max;
            this.accept = DFA47_accept;
            this.special = DFA47_special;
            this.transition = DFA47_transition;
        }
        public String getDescription() {
            return "335:1: cast_expression : ( '(' type_name ')' cast_expression | unary_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA47_20 = input.LA(1);

                         
                        int index47_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred78_C()) ) {s = 8;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index47_20);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 47, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA48_eotS =
        "\20\uffff";
    static final String DFA48_eofS =
        "\20\uffff";
    static final String DFA48_minS =
        "\1\4\6\uffff\1\4\1\0\7\uffff";
    static final String DFA48_maxS =
        "\1\101\6\uffff\1\101\1\0\7\uffff";
    static final String DFA48_acceptS =
        "\1\uffff\1\1\2\uffff\1\2\1\3\1\4\2\uffff\1\5\5\uffff\1\6";
    static final String DFA48_specialS =
        "\10\uffff\1\0\7\uffff}>";
    static final String[] DFA48_transitionS = {
            "\7\1\45\uffff\1\1\3\uffff\1\6\1\uffff\2\6\2\uffff\1\4\1\5\1"+
            "\7\2\uffff\3\6",
            "",
            "",
            "",
            "",
            "",
            "",
            "\7\11\45\uffff\1\10\3\uffff\1\11\1\uffff\2\11\2\uffff\3\11"+
            "\2\uffff\3\11",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA48_eot = DFA.unpackEncodedString(DFA48_eotS);
    static final short[] DFA48_eof = DFA.unpackEncodedString(DFA48_eofS);
    static final char[] DFA48_min = DFA.unpackEncodedStringToUnsignedChars(DFA48_minS);
    static final char[] DFA48_max = DFA.unpackEncodedStringToUnsignedChars(DFA48_maxS);
    static final short[] DFA48_accept = DFA.unpackEncodedString(DFA48_acceptS);
    static final short[] DFA48_special = DFA.unpackEncodedString(DFA48_specialS);
    static final short[][] DFA48_transition;

    static {
        int numStates = DFA48_transitionS.length;
        DFA48_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA48_transition[i] = DFA.unpackEncodedString(DFA48_transitionS[i]);
        }
    }

    class DFA48 extends DFA {

        public DFA48(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 48;
            this.eot = DFA48_eot;
            this.eof = DFA48_eof;
            this.min = DFA48_min;
            this.max = DFA48_max;
            this.accept = DFA48_accept;
            this.special = DFA48_special;
            this.transition = DFA48_transition;
        }
        public String getDescription() {
            return "340:1: unary_expression : ( postfix_expression | '++' unary_expression | '--' unary_expression | unary_operator cast_expression | 'sizeof' unary_expression | 'sizeof' '(' type_name ')' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA48_8 = input.LA(1);

                         
                        int index48_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred83_C()) ) {s = 9;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index48_8);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 48, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA49_eotS =
        "\45\uffff";
    static final String DFA49_eofS =
        "\1\1\44\uffff";
    static final String DFA49_minS =
        "\1\30\27\uffff\1\4\14\uffff";
    static final String DFA49_maxS =
        "\1\130\27\uffff\1\101\14\uffff";
    static final String DFA49_acceptS =
        "\1\uffff\1\10\25\uffff\1\1\1\uffff\1\4\1\5\1\6\1\7\1\2\1\3\6\uffff";
    static final String DFA49_specialS =
        "\45\uffff}>";
    static final String[] DFA49_transitionS = {
            "\3\1\16\uffff\1\1\2\uffff\1\1\3\uffff\1\30\1\1\1\27\2\1\1\uffff"+
            "\4\1\1\33\1\34\1\uffff\1\31\1\32\1\1\2\uffff\27\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\7\36\45\uffff\1\36\1\35\2\uffff\1\36\1\uffff\2\36\2\uffff"+
            "\3\36\2\uffff\3\36",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA49_eot = DFA.unpackEncodedString(DFA49_eotS);
    static final short[] DFA49_eof = DFA.unpackEncodedString(DFA49_eofS);
    static final char[] DFA49_min = DFA.unpackEncodedStringToUnsignedChars(DFA49_minS);
    static final char[] DFA49_max = DFA.unpackEncodedStringToUnsignedChars(DFA49_maxS);
    static final short[] DFA49_accept = DFA.unpackEncodedString(DFA49_acceptS);
    static final short[] DFA49_special = DFA.unpackEncodedString(DFA49_specialS);
    static final short[][] DFA49_transition;

    static {
        int numStates = DFA49_transitionS.length;
        DFA49_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA49_transition[i] = DFA.unpackEncodedString(DFA49_transitionS[i]);
        }
    }

    class DFA49 extends DFA {

        public DFA49(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 49;
            this.eot = DFA49_eot;
            this.eof = DFA49_eof;
            this.min = DFA49_min;
            this.max = DFA49_max;
            this.accept = DFA49_accept;
            this.special = DFA49_special;
            this.transition = DFA49_transition;
        }
        public String getDescription() {
            return "()* loopback of 351:9: ( '[' expression ']' | '(' ')' | '(' argument_expression_list ')' | '.' IDENTIFIER | '->' IDENTIFIER | '++' | '--' )*";
        }
    }
    static final String DFA52_eotS =
        "\157\uffff";
    static final String DFA52_eofS =
        "\1\uffff\2\17\154\uffff";
    static final String DFA52_minS =
        "\1\4\2\30\5\4\6\0\26\uffff\6\0\42\uffff\43\0";
    static final String DFA52_maxS =
        "\1\101\2\130\5\101\6\0\26\uffff\6\0\42\uffff\43\0";
    static final String DFA52_acceptS =
        "\16\uffff\1\1\1\2\137\uffff";
    static final String DFA52_specialS =
        "\10\uffff\1\0\1\1\1\2\1\3\1\4\1\5\26\uffff\1\6\1\7\1\10\1\11\1\12"+
        "\1\13\42\uffff\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25"+
        "\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1\36\1\37\1\40\1\41\1\42"+
        "\1\43\1\44\1\45\1\46\1\47\1\50\1\51\1\52\1\53\1\54\1\55\1\56}>";
    static final String[] DFA52_transitionS = {
            "\1\1\6\2\45\uffff\1\3\3\uffff\1\6\1\uffff\2\6\2\uffff\1\4\1"+
            "\5\1\7\2\uffff\3\6",
            "\2\17\1\16\16\uffff\1\17\2\uffff\1\17\3\uffff\1\11\1\17\1\10"+
            "\2\17\1\uffff\4\17\1\14\1\15\1\uffff\1\12\1\13\1\17\2\uffff"+
            "\12\16\15\17",
            "\2\17\1\16\16\uffff\1\17\2\uffff\1\17\3\uffff\1\45\1\17\1\44"+
            "\2\17\1\uffff\4\17\1\50\1\51\1\uffff\1\46\1\47\1\17\2\uffff"+
            "\12\16\15\17",
            "\1\114\6\115\24\uffff\11\17\2\uffff\2\17\1\uffff\3\17\1\116"+
            "\3\uffff\1\121\1\uffff\2\121\2\uffff\1\117\1\120\1\122\2\uffff"+
            "\3\121",
            "\1\123\6\124\45\uffff\1\125\3\uffff\1\130\1\uffff\2\130\2\uffff"+
            "\1\126\1\127\1\131\2\uffff\3\130",
            "\1\132\6\133\45\uffff\1\134\3\uffff\1\137\1\uffff\2\137\2\uffff"+
            "\1\135\1\136\1\140\2\uffff\3\137",
            "\1\142\6\143\45\uffff\1\141\3\uffff\1\146\1\uffff\2\146\2\uffff"+
            "\1\144\1\145\1\147\2\uffff\3\146",
            "\1\151\6\152\45\uffff\1\150\3\uffff\1\155\1\uffff\2\155\2\uffff"+
            "\1\153\1\154\1\156\2\uffff\3\155",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff"
    };

    static final short[] DFA52_eot = DFA.unpackEncodedString(DFA52_eotS);
    static final short[] DFA52_eof = DFA.unpackEncodedString(DFA52_eofS);
    static final char[] DFA52_min = DFA.unpackEncodedStringToUnsignedChars(DFA52_minS);
    static final char[] DFA52_max = DFA.unpackEncodedStringToUnsignedChars(DFA52_maxS);
    static final short[] DFA52_accept = DFA.unpackEncodedString(DFA52_acceptS);
    static final short[] DFA52_special = DFA.unpackEncodedString(DFA52_specialS);
    static final short[][] DFA52_transition;

    static {
        int numStates = DFA52_transitionS.length;
        DFA52_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA52_transition[i] = DFA.unpackEncodedString(DFA52_transitionS[i]);
        }
    }

    class DFA52 extends DFA {

        public DFA52(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 52;
            this.eot = DFA52_eot;
            this.eof = DFA52_eof;
            this.min = DFA52_min;
            this.max = DFA52_max;
            this.accept = DFA52_accept;
            this.special = DFA52_special;
            this.transition = DFA52_transition;
        }
        public String getDescription() {
            return "395:1: assignment_expression : ( lvalue assignment_operator assignment_expression | conditional_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA52_8 = input.LA(1);

                         
                        int index52_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_8);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA52_9 = input.LA(1);

                         
                        int index52_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_9);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA52_10 = input.LA(1);

                         
                        int index52_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_10);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA52_11 = input.LA(1);

                         
                        int index52_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_11);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA52_12 = input.LA(1);

                         
                        int index52_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_12);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA52_13 = input.LA(1);

                         
                        int index52_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_13);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA52_36 = input.LA(1);

                         
                        int index52_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_36);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA52_37 = input.LA(1);

                         
                        int index52_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_37);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA52_38 = input.LA(1);

                         
                        int index52_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_38);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA52_39 = input.LA(1);

                         
                        int index52_39 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_39);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA52_40 = input.LA(1);

                         
                        int index52_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_40);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA52_41 = input.LA(1);

                         
                        int index52_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_41);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA52_76 = input.LA(1);

                         
                        int index52_76 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_76);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA52_77 = input.LA(1);

                         
                        int index52_77 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_77);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA52_78 = input.LA(1);

                         
                        int index52_78 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_78);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA52_79 = input.LA(1);

                         
                        int index52_79 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_79);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA52_80 = input.LA(1);

                         
                        int index52_80 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_80);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA52_81 = input.LA(1);

                         
                        int index52_81 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_81);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA52_82 = input.LA(1);

                         
                        int index52_82 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_82);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA52_83 = input.LA(1);

                         
                        int index52_83 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_83);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA52_84 = input.LA(1);

                         
                        int index52_84 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_84);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA52_85 = input.LA(1);

                         
                        int index52_85 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_85);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA52_86 = input.LA(1);

                         
                        int index52_86 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_86);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA52_87 = input.LA(1);

                         
                        int index52_87 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_87);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA52_88 = input.LA(1);

                         
                        int index52_88 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_88);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA52_89 = input.LA(1);

                         
                        int index52_89 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_89);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA52_90 = input.LA(1);

                         
                        int index52_90 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_90);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA52_91 = input.LA(1);

                         
                        int index52_91 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_91);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA52_92 = input.LA(1);

                         
                        int index52_92 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_92);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA52_93 = input.LA(1);

                         
                        int index52_93 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_93);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA52_94 = input.LA(1);

                         
                        int index52_94 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_94);
                        if ( s>=0 ) return s;
                        break;
                    case 31 : 
                        int LA52_95 = input.LA(1);

                         
                        int index52_95 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_95);
                        if ( s>=0 ) return s;
                        break;
                    case 32 : 
                        int LA52_96 = input.LA(1);

                         
                        int index52_96 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_96);
                        if ( s>=0 ) return s;
                        break;
                    case 33 : 
                        int LA52_97 = input.LA(1);

                         
                        int index52_97 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_97);
                        if ( s>=0 ) return s;
                        break;
                    case 34 : 
                        int LA52_98 = input.LA(1);

                         
                        int index52_98 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_98);
                        if ( s>=0 ) return s;
                        break;
                    case 35 : 
                        int LA52_99 = input.LA(1);

                         
                        int index52_99 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_99);
                        if ( s>=0 ) return s;
                        break;
                    case 36 : 
                        int LA52_100 = input.LA(1);

                         
                        int index52_100 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_100);
                        if ( s>=0 ) return s;
                        break;
                    case 37 : 
                        int LA52_101 = input.LA(1);

                         
                        int index52_101 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_101);
                        if ( s>=0 ) return s;
                        break;
                    case 38 : 
                        int LA52_102 = input.LA(1);

                         
                        int index52_102 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_102);
                        if ( s>=0 ) return s;
                        break;
                    case 39 : 
                        int LA52_103 = input.LA(1);

                         
                        int index52_103 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_103);
                        if ( s>=0 ) return s;
                        break;
                    case 40 : 
                        int LA52_104 = input.LA(1);

                         
                        int index52_104 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_104);
                        if ( s>=0 ) return s;
                        break;
                    case 41 : 
                        int LA52_105 = input.LA(1);

                         
                        int index52_105 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_105);
                        if ( s>=0 ) return s;
                        break;
                    case 42 : 
                        int LA52_106 = input.LA(1);

                         
                        int index52_106 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_106);
                        if ( s>=0 ) return s;
                        break;
                    case 43 : 
                        int LA52_107 = input.LA(1);

                         
                        int index52_107 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_107);
                        if ( s>=0 ) return s;
                        break;
                    case 44 : 
                        int LA52_108 = input.LA(1);

                         
                        int index52_108 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_108);
                        if ( s>=0 ) return s;
                        break;
                    case 45 : 
                        int LA52_109 = input.LA(1);

                         
                        int index52_109 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_109);
                        if ( s>=0 ) return s;
                        break;
                    case 46 : 
                        int LA52_110 = input.LA(1);

                         
                        int index52_110 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_C()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index52_110);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 52, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA54_eotS =
        "\12\uffff";
    static final String DFA54_eofS =
        "\1\1\11\uffff";
    static final String DFA54_minS =
        "\1\30\11\uffff";
    static final String DFA54_maxS =
        "\1\115\11\uffff";
    static final String DFA54_acceptS =
        "\1\uffff\1\2\7\uffff\1\1";
    static final String DFA54_specialS =
        "\12\uffff}>";
    static final String[] DFA54_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\30\uffff"+
            "\1\1\1\11",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA54_eot = DFA.unpackEncodedString(DFA54_eotS);
    static final short[] DFA54_eof = DFA.unpackEncodedString(DFA54_eofS);
    static final char[] DFA54_min = DFA.unpackEncodedStringToUnsignedChars(DFA54_minS);
    static final char[] DFA54_max = DFA.unpackEncodedStringToUnsignedChars(DFA54_maxS);
    static final short[] DFA54_accept = DFA.unpackEncodedString(DFA54_acceptS);
    static final short[] DFA54_special = DFA.unpackEncodedString(DFA54_specialS);
    static final short[][] DFA54_transition;

    static {
        int numStates = DFA54_transitionS.length;
        DFA54_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA54_transition[i] = DFA.unpackEncodedString(DFA54_transitionS[i]);
        }
    }

    class DFA54 extends DFA {

        public DFA54(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 54;
            this.eot = DFA54_eot;
            this.eof = DFA54_eof;
            this.min = DFA54_min;
            this.max = DFA54_max;
            this.accept = DFA54_accept;
            this.special = DFA54_special;
            this.transition = DFA54_transition;
        }
        public String getDescription() {
            return "()* loopback of 423:27: ( '||' logical_and_expression )*";
        }
    }
    static final String DFA55_eotS =
        "\13\uffff";
    static final String DFA55_eofS =
        "\1\1\12\uffff";
    static final String DFA55_minS =
        "\1\30\12\uffff";
    static final String DFA55_maxS =
        "\1\116\12\uffff";
    static final String DFA55_acceptS =
        "\1\uffff\1\2\10\uffff\1\1";
    static final String DFA55_specialS =
        "\13\uffff}>";
    static final String[] DFA55_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\30\uffff"+
            "\2\1\1\12",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA55_eot = DFA.unpackEncodedString(DFA55_eotS);
    static final short[] DFA55_eof = DFA.unpackEncodedString(DFA55_eofS);
    static final char[] DFA55_min = DFA.unpackEncodedStringToUnsignedChars(DFA55_minS);
    static final char[] DFA55_max = DFA.unpackEncodedStringToUnsignedChars(DFA55_maxS);
    static final short[] DFA55_accept = DFA.unpackEncodedString(DFA55_acceptS);
    static final short[] DFA55_special = DFA.unpackEncodedString(DFA55_specialS);
    static final short[][] DFA55_transition;

    static {
        int numStates = DFA55_transitionS.length;
        DFA55_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA55_transition[i] = DFA.unpackEncodedString(DFA55_transitionS[i]);
        }
    }

    class DFA55 extends DFA {

        public DFA55(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 55;
            this.eot = DFA55_eot;
            this.eof = DFA55_eof;
            this.min = DFA55_min;
            this.max = DFA55_max;
            this.accept = DFA55_accept;
            this.special = DFA55_special;
            this.transition = DFA55_transition;
        }
        public String getDescription() {
            return "()* loopback of 427:28: ( '&&' inclusive_or_expression )*";
        }
    }
    static final String DFA56_eotS =
        "\14\uffff";
    static final String DFA56_eofS =
        "\1\1\13\uffff";
    static final String DFA56_minS =
        "\1\30\13\uffff";
    static final String DFA56_maxS =
        "\1\117\13\uffff";
    static final String DFA56_acceptS =
        "\1\uffff\1\2\11\uffff\1\1";
    static final String DFA56_specialS =
        "\14\uffff}>";
    static final String[] DFA56_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\30\uffff"+
            "\3\1\1\13",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA56_eot = DFA.unpackEncodedString(DFA56_eotS);
    static final short[] DFA56_eof = DFA.unpackEncodedString(DFA56_eofS);
    static final char[] DFA56_min = DFA.unpackEncodedStringToUnsignedChars(DFA56_minS);
    static final char[] DFA56_max = DFA.unpackEncodedStringToUnsignedChars(DFA56_maxS);
    static final short[] DFA56_accept = DFA.unpackEncodedString(DFA56_acceptS);
    static final short[] DFA56_special = DFA.unpackEncodedString(DFA56_specialS);
    static final short[][] DFA56_transition;

    static {
        int numStates = DFA56_transitionS.length;
        DFA56_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA56_transition[i] = DFA.unpackEncodedString(DFA56_transitionS[i]);
        }
    }

    class DFA56 extends DFA {

        public DFA56(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 56;
            this.eot = DFA56_eot;
            this.eof = DFA56_eof;
            this.min = DFA56_min;
            this.max = DFA56_max;
            this.accept = DFA56_accept;
            this.special = DFA56_special;
            this.transition = DFA56_transition;
        }
        public String getDescription() {
            return "()* loopback of 431:28: ( '|' exclusive_or_expression )*";
        }
    }
    static final String DFA57_eotS =
        "\15\uffff";
    static final String DFA57_eofS =
        "\1\1\14\uffff";
    static final String DFA57_minS =
        "\1\30\14\uffff";
    static final String DFA57_maxS =
        "\1\120\14\uffff";
    static final String DFA57_acceptS =
        "\1\uffff\1\2\12\uffff\1\1";
    static final String DFA57_specialS =
        "\15\uffff}>";
    static final String[] DFA57_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\30\uffff"+
            "\4\1\1\14",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA57_eot = DFA.unpackEncodedString(DFA57_eotS);
    static final short[] DFA57_eof = DFA.unpackEncodedString(DFA57_eofS);
    static final char[] DFA57_min = DFA.unpackEncodedStringToUnsignedChars(DFA57_minS);
    static final char[] DFA57_max = DFA.unpackEncodedStringToUnsignedChars(DFA57_maxS);
    static final short[] DFA57_accept = DFA.unpackEncodedString(DFA57_acceptS);
    static final short[] DFA57_special = DFA.unpackEncodedString(DFA57_specialS);
    static final short[][] DFA57_transition;

    static {
        int numStates = DFA57_transitionS.length;
        DFA57_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA57_transition[i] = DFA.unpackEncodedString(DFA57_transitionS[i]);
        }
    }

    class DFA57 extends DFA {

        public DFA57(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 57;
            this.eot = DFA57_eot;
            this.eof = DFA57_eof;
            this.min = DFA57_min;
            this.max = DFA57_max;
            this.accept = DFA57_accept;
            this.special = DFA57_special;
            this.transition = DFA57_transition;
        }
        public String getDescription() {
            return "()* loopback of 435:19: ( '^' and_expression )*";
        }
    }
    static final String DFA58_eotS =
        "\16\uffff";
    static final String DFA58_eofS =
        "\1\1\15\uffff";
    static final String DFA58_minS =
        "\1\30\15\uffff";
    static final String DFA58_maxS =
        "\1\120\15\uffff";
    static final String DFA58_acceptS =
        "\1\uffff\1\2\13\uffff\1\1";
    static final String DFA58_specialS =
        "\16\uffff}>";
    static final String[] DFA58_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\13\uffff"+
            "\1\15\14\uffff\5\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA58_eot = DFA.unpackEncodedString(DFA58_eotS);
    static final short[] DFA58_eof = DFA.unpackEncodedString(DFA58_eofS);
    static final char[] DFA58_min = DFA.unpackEncodedStringToUnsignedChars(DFA58_minS);
    static final char[] DFA58_max = DFA.unpackEncodedStringToUnsignedChars(DFA58_maxS);
    static final short[] DFA58_accept = DFA.unpackEncodedString(DFA58_acceptS);
    static final short[] DFA58_special = DFA.unpackEncodedString(DFA58_specialS);
    static final short[][] DFA58_transition;

    static {
        int numStates = DFA58_transitionS.length;
        DFA58_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA58_transition[i] = DFA.unpackEncodedString(DFA58_transitionS[i]);
        }
    }

    class DFA58 extends DFA {

        public DFA58(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 58;
            this.eot = DFA58_eot;
            this.eof = DFA58_eof;
            this.min = DFA58_min;
            this.max = DFA58_max;
            this.accept = DFA58_accept;
            this.special = DFA58_special;
            this.transition = DFA58_transition;
        }
        public String getDescription() {
            return "()* loopback of 439:24: ( '&' equality_expression )*";
        }
    }
    static final String DFA59_eotS =
        "\17\uffff";
    static final String DFA59_eofS =
        "\1\1\16\uffff";
    static final String DFA59_minS =
        "\1\30\16\uffff";
    static final String DFA59_maxS =
        "\1\122\16\uffff";
    static final String DFA59_acceptS =
        "\1\uffff\1\2\14\uffff\1\1";
    static final String DFA59_specialS =
        "\17\uffff}>";
    static final String[] DFA59_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\13\uffff"+
            "\1\1\14\uffff\5\1\2\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA59_eot = DFA.unpackEncodedString(DFA59_eotS);
    static final short[] DFA59_eof = DFA.unpackEncodedString(DFA59_eofS);
    static final char[] DFA59_min = DFA.unpackEncodedStringToUnsignedChars(DFA59_minS);
    static final char[] DFA59_max = DFA.unpackEncodedStringToUnsignedChars(DFA59_maxS);
    static final short[] DFA59_accept = DFA.unpackEncodedString(DFA59_acceptS);
    static final short[] DFA59_special = DFA.unpackEncodedString(DFA59_specialS);
    static final short[][] DFA59_transition;

    static {
        int numStates = DFA59_transitionS.length;
        DFA59_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA59_transition[i] = DFA.unpackEncodedString(DFA59_transitionS[i]);
        }
    }

    class DFA59 extends DFA {

        public DFA59(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 59;
            this.eot = DFA59_eot;
            this.eof = DFA59_eof;
            this.min = DFA59_min;
            this.max = DFA59_max;
            this.accept = DFA59_accept;
            this.special = DFA59_special;
            this.transition = DFA59_transition;
        }
        public String getDescription() {
            return "()* loopback of 442:26: ( ( '==' | '!=' ) relational_expression )*";
        }
    }
    static final String DFA60_eotS =
        "\20\uffff";
    static final String DFA60_eofS =
        "\1\1\17\uffff";
    static final String DFA60_minS =
        "\1\30\17\uffff";
    static final String DFA60_maxS =
        "\1\126\17\uffff";
    static final String DFA60_acceptS =
        "\1\uffff\1\2\15\uffff\1\1";
    static final String DFA60_specialS =
        "\20\uffff}>";
    static final String[] DFA60_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\13\uffff"+
            "\1\1\14\uffff\7\1\4\17",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA60_eot = DFA.unpackEncodedString(DFA60_eotS);
    static final short[] DFA60_eof = DFA.unpackEncodedString(DFA60_eofS);
    static final char[] DFA60_min = DFA.unpackEncodedStringToUnsignedChars(DFA60_minS);
    static final char[] DFA60_max = DFA.unpackEncodedStringToUnsignedChars(DFA60_maxS);
    static final short[] DFA60_accept = DFA.unpackEncodedString(DFA60_acceptS);
    static final short[] DFA60_special = DFA.unpackEncodedString(DFA60_specialS);
    static final short[][] DFA60_transition;

    static {
        int numStates = DFA60_transitionS.length;
        DFA60_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA60_transition[i] = DFA.unpackEncodedString(DFA60_transitionS[i]);
        }
    }

    class DFA60 extends DFA {

        public DFA60(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 60;
            this.eot = DFA60_eot;
            this.eof = DFA60_eof;
            this.min = DFA60_min;
            this.max = DFA60_max;
            this.accept = DFA60_accept;
            this.special = DFA60_special;
            this.transition = DFA60_transition;
        }
        public String getDescription() {
            return "()* loopback of 446:21: ( ( '<' | '>' | '<=' | '>=' ) shift_expression )*";
        }
    }
    static final String DFA61_eotS =
        "\21\uffff";
    static final String DFA61_eofS =
        "\1\1\20\uffff";
    static final String DFA61_minS =
        "\1\30\20\uffff";
    static final String DFA61_maxS =
        "\1\130\20\uffff";
    static final String DFA61_acceptS =
        "\1\uffff\1\2\16\uffff\1\1";
    static final String DFA61_specialS =
        "\21\uffff}>";
    static final String[] DFA61_transitionS = {
            "\2\1\17\uffff\1\1\2\uffff\1\1\4\uffff\1\1\1\uffff\1\1\13\uffff"+
            "\1\1\14\uffff\13\1\2\20",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA61_eot = DFA.unpackEncodedString(DFA61_eotS);
    static final short[] DFA61_eof = DFA.unpackEncodedString(DFA61_eofS);
    static final char[] DFA61_min = DFA.unpackEncodedStringToUnsignedChars(DFA61_minS);
    static final char[] DFA61_max = DFA.unpackEncodedStringToUnsignedChars(DFA61_maxS);
    static final short[] DFA61_accept = DFA.unpackEncodedString(DFA61_acceptS);
    static final short[] DFA61_special = DFA.unpackEncodedString(DFA61_specialS);
    static final short[][] DFA61_transition;

    static {
        int numStates = DFA61_transitionS.length;
        DFA61_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA61_transition[i] = DFA.unpackEncodedString(DFA61_transitionS[i]);
        }
    }

    class DFA61 extends DFA {

        public DFA61(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 61;
            this.eot = DFA61_eot;
            this.eof = DFA61_eof;
            this.min = DFA61_min;
            this.max = DFA61_max;
            this.accept = DFA61_accept;
            this.special = DFA61_special;
            this.transition = DFA61_transition;
        }
        public String getDescription() {
            return "()* loopback of 450:24: ( ( '<<' | '>>' ) additive_expression )*";
        }
    }
    static final String DFA62_eotS =
        "\55\uffff";
    static final String DFA62_eofS =
        "\55\uffff";
    static final String DFA62_minS =
        "\1\4\1\30\53\uffff";
    static final String DFA62_maxS =
        "\1\144\1\130\53\uffff";
    static final String DFA62_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\1\3\6\uffff\1\4\1\uffff\1\5\2\uffff\1\6"+
        "\33\uffff";
    static final String DFA62_specialS =
        "\55\uffff}>";
    static final String[] DFA62_transitionS = {
            "\1\1\6\5\15\uffff\1\5\17\uffff\1\4\7\uffff\1\5\3\uffff\1\5\1"+
            "\uffff\2\5\2\uffff\3\5\2\uffff\3\5\27\uffff\2\2\1\14\1\uffff"+
            "\1\14\3\16\4\21",
            "\3\5\21\uffff\1\2\3\uffff\1\5\1\uffff\1\5\1\uffff\1\5\1\uffff"+
            "\6\5\1\uffff\3\5\2\uffff\27\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA62_eot = DFA.unpackEncodedString(DFA62_eotS);
    static final short[] DFA62_eof = DFA.unpackEncodedString(DFA62_eofS);
    static final char[] DFA62_min = DFA.unpackEncodedStringToUnsignedChars(DFA62_minS);
    static final char[] DFA62_max = DFA.unpackEncodedStringToUnsignedChars(DFA62_maxS);
    static final short[] DFA62_accept = DFA.unpackEncodedString(DFA62_acceptS);
    static final short[] DFA62_special = DFA.unpackEncodedString(DFA62_specialS);
    static final short[][] DFA62_transition;

    static {
        int numStates = DFA62_transitionS.length;
        DFA62_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA62_transition[i] = DFA.unpackEncodedString(DFA62_transitionS[i]);
        }
    }

    class DFA62 extends DFA {

        public DFA62(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 62;
            this.eot = DFA62_eot;
            this.eof = DFA62_eof;
            this.min = DFA62_min;
            this.max = DFA62_max;
            this.accept = DFA62_accept;
            this.special = DFA62_special;
            this.transition = DFA62_transition;
        }
        public String getDescription() {
            return "455:1: statement : ( labeled_statement | compound_statement | expression_statement | selection_statement | iteration_statement | jump_statement );";
        }
    }
    static final String DFA64_eotS =
        "\112\uffff";
    static final String DFA64_eofS =
        "\112\uffff";
    static final String DFA64_minS =
        "\2\4\44\uffff\1\0\5\uffff\1\0\16\uffff\1\0\16\uffff";
    static final String DFA64_maxS =
        "\1\144\1\130\44\uffff\1\0\5\uffff\1\0\16\uffff\1\0\16\uffff";
    static final String DFA64_acceptS =
        "\2\uffff\1\2\23\uffff\1\1\63\uffff";
    static final String DFA64_specialS =
        "\46\uffff\1\0\5\uffff\1\1\16\uffff\1\2\16\uffff}>";
    static final String[] DFA64_transitionS = {
            "\1\1\6\2\14\uffff\1\26\1\2\2\uffff\15\26\2\2\2\26\1\uffff\3"+
            "\26\1\2\3\uffff\1\2\1\uffff\2\2\2\uffff\3\2\2\uffff\3\2\27\uffff"+
            "\3\2\1\uffff\10\2",
            "\1\26\23\uffff\1\73\2\2\15\26\2\uffff\2\26\1\2\3\26\1\46\1"+
            "\uffff\1\2\1\uffff\1\54\1\uffff\6\2\1\uffff\3\2\2\uffff\27\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA64_eot = DFA.unpackEncodedString(DFA64_eotS);
    static final short[] DFA64_eof = DFA.unpackEncodedString(DFA64_eofS);
    static final char[] DFA64_min = DFA.unpackEncodedStringToUnsignedChars(DFA64_minS);
    static final char[] DFA64_max = DFA.unpackEncodedStringToUnsignedChars(DFA64_maxS);
    static final short[] DFA64_accept = DFA.unpackEncodedString(DFA64_acceptS);
    static final short[] DFA64_special = DFA.unpackEncodedString(DFA64_specialS);
    static final short[][] DFA64_transition;

    static {
        int numStates = DFA64_transitionS.length;
        DFA64_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA64_transition[i] = DFA.unpackEncodedString(DFA64_transitionS[i]);
        }
    }

    class DFA64 extends DFA {

        public DFA64(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 64;
            this.eot = DFA64_eot;
            this.eof = DFA64_eof;
            this.min = DFA64_min;
            this.max = DFA64_max;
            this.accept = DFA64_accept;
            this.special = DFA64_special;
            this.transition = DFA64_transition;
        }
        public String getDescription() {
            return "()* loopback of 475:8: ( declaration )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA64_38 = input.LA(1);

                         
                        int index64_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred136_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 22;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index64_38);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA64_44 = input.LA(1);

                         
                        int index64_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred136_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 22;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index64_44);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA64_59 = input.LA(1);

                         
                        int index64_59 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred136_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 22;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index64_59);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 64, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA65_eotS =
        "\26\uffff";
    static final String DFA65_eofS =
        "\26\uffff";
    static final String DFA65_minS =
        "\1\4\25\uffff";
    static final String DFA65_maxS =
        "\1\144\25\uffff";
    static final String DFA65_acceptS =
        "\1\uffff\1\1\23\uffff\1\2";
    static final String DFA65_specialS =
        "\26\uffff}>";
    static final String[] DFA65_transitionS = {
            "\7\1\15\uffff\1\1\17\uffff\1\1\1\25\6\uffff\1\1\3\uffff\1\1"+
            "\1\uffff\2\1\2\uffff\3\1\2\uffff\3\1\27\uffff\3\1\1\uffff\10"+
            "\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA65_eot = DFA.unpackEncodedString(DFA65_eotS);
    static final short[] DFA65_eof = DFA.unpackEncodedString(DFA65_eofS);
    static final char[] DFA65_min = DFA.unpackEncodedStringToUnsignedChars(DFA65_minS);
    static final char[] DFA65_max = DFA.unpackEncodedStringToUnsignedChars(DFA65_maxS);
    static final short[] DFA65_accept = DFA.unpackEncodedString(DFA65_acceptS);
    static final short[] DFA65_special = DFA.unpackEncodedString(DFA65_specialS);
    static final short[][] DFA65_transition;

    static {
        int numStates = DFA65_transitionS.length;
        DFA65_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA65_transition[i] = DFA.unpackEncodedString(DFA65_transitionS[i]);
        }
    }

    class DFA65 extends DFA {

        public DFA65(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 65;
            this.eot = DFA65_eot;
            this.eof = DFA65_eof;
            this.min = DFA65_min;
            this.max = DFA65_max;
            this.accept = DFA65_accept;
            this.special = DFA65_special;
            this.transition = DFA65_transition;
        }
        public String getDescription() {
            return "475:21: ( statement_list )?";
        }
    }
    static final String DFA66_eotS =
        "\27\uffff";
    static final String DFA66_eofS =
        "\1\1\26\uffff";
    static final String DFA66_minS =
        "\1\4\26\uffff";
    static final String DFA66_maxS =
        "\1\144\26\uffff";
    static final String DFA66_acceptS =
        "\1\uffff\1\2\1\uffff\1\1\23\uffff";
    static final String DFA66_specialS =
        "\27\uffff}>";
    static final String[] DFA66_transitionS = {
            "\7\3\15\uffff\1\3\17\uffff\1\3\1\1\6\uffff\1\3\3\uffff\1\3\1"+
            "\uffff\2\3\2\uffff\3\3\2\uffff\3\3\27\uffff\3\3\1\uffff\10\3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA66_eot = DFA.unpackEncodedString(DFA66_eotS);
    static final short[] DFA66_eof = DFA.unpackEncodedString(DFA66_eofS);
    static final char[] DFA66_min = DFA.unpackEncodedStringToUnsignedChars(DFA66_minS);
    static final char[] DFA66_max = DFA.unpackEncodedStringToUnsignedChars(DFA66_maxS);
    static final short[] DFA66_accept = DFA.unpackEncodedString(DFA66_acceptS);
    static final short[] DFA66_special = DFA.unpackEncodedString(DFA66_specialS);
    static final short[][] DFA66_transition;

    static {
        int numStates = DFA66_transitionS.length;
        DFA66_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA66_transition[i] = DFA.unpackEncodedString(DFA66_transitionS[i]);
        }
    }

    class DFA66 extends DFA {

        public DFA66(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 66;
            this.eot = DFA66_eot;
            this.eof = DFA66_eof;
            this.min = DFA66_min;
            this.max = DFA66_max;
            this.accept = DFA66_accept;
            this.special = DFA66_special;
            this.transition = DFA66_transition;
        }
        public String getDescription() {
            return "()+ loopback of 479:4: ( statement )+";
        }
    }
    static final String DFA72_eotS =
        "\15\uffff";
    static final String DFA72_eofS =
        "\15\uffff";
    static final String DFA72_minS =
        "\1\141\3\uffff\1\4\10\uffff";
    static final String DFA72_maxS =
        "\1\144\3\uffff\1\101\10\uffff";
    static final String DFA72_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\uffff\1\4\1\5\6\uffff";
    static final String DFA72_specialS =
        "\15\uffff}>";
    static final String[] DFA72_transitionS = {
            "\1\1\1\2\1\3\1\4",
            "",
            "",
            "",
            "\7\6\15\uffff\1\5\27\uffff\1\6\3\uffff\1\6\1\uffff\2\6\2\uffff"+
            "\3\6\2\uffff\3\6",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA72_eot = DFA.unpackEncodedString(DFA72_eotS);
    static final short[] DFA72_eof = DFA.unpackEncodedString(DFA72_eofS);
    static final char[] DFA72_min = DFA.unpackEncodedStringToUnsignedChars(DFA72_minS);
    static final char[] DFA72_max = DFA.unpackEncodedStringToUnsignedChars(DFA72_maxS);
    static final short[] DFA72_accept = DFA.unpackEncodedString(DFA72_acceptS);
    static final short[] DFA72_special = DFA.unpackEncodedString(DFA72_specialS);
    static final short[][] DFA72_transition;

    static {
        int numStates = DFA72_transitionS.length;
        DFA72_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA72_transition[i] = DFA.unpackEncodedString(DFA72_transitionS[i]);
        }
    }

    class DFA72 extends DFA {

        public DFA72(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 72;
            this.eot = DFA72_eot;
            this.eof = DFA72_eof;
            this.min = DFA72_min;
            this.max = DFA72_max;
            this.accept = DFA72_accept;
            this.special = DFA72_special;
            this.transition = DFA72_transition;
        }
        public String getDescription() {
            return "498:1: jump_statement : ( 'goto' IDENTIFIER ';' | 'continue' ';' | 'break' ';' | 'return' ';' | 'return' expression ';' );";
        }
    }
    static final String DFA73_eotS =
        "\44\uffff";
    static final String DFA73_eofS =
        "\44\uffff";
    static final String DFA73_minS =
        "\1\4\14\uffff\1\4\4\uffff\17\0\3\uffff";
    static final String DFA73_maxS =
        "\1\64\14\uffff\1\64\4\uffff\17\0\3\uffff";
    static final String DFA73_acceptS =
        "\1\uffff\1\1\15\uffff\1\2\24\uffff";
    static final String DFA73_specialS =
        "\22\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\3\uffff}>";
    static final String[] DFA73_transitionS = {
            "\1\15\26\uffff\15\1\2\uffff\2\1\1\uffff\3\1\1\17\3\uffff\1\17",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\22\22\uffff\1\17\3\uffff\4\24\1\25\1\26\1\27\1\30\1\31\1"+
            "\32\1\33\1\34\1\35\1\17\1\uffff\2\36\1\uffff\1\37\2\40\1\23"+
            "\1\uffff\1\17\1\uffff\1\1",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA73_eot = DFA.unpackEncodedString(DFA73_eotS);
    static final short[] DFA73_eof = DFA.unpackEncodedString(DFA73_eofS);
    static final char[] DFA73_min = DFA.unpackEncodedStringToUnsignedChars(DFA73_minS);
    static final char[] DFA73_max = DFA.unpackEncodedStringToUnsignedChars(DFA73_maxS);
    static final short[] DFA73_accept = DFA.unpackEncodedString(DFA73_acceptS);
    static final short[] DFA73_special = DFA.unpackEncodedString(DFA73_specialS);
    static final short[][] DFA73_transition;

    static {
        int numStates = DFA73_transitionS.length;
        DFA73_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA73_transition[i] = DFA.unpackEncodedString(DFA73_transitionS[i]);
        }
    }

    class DFA73 extends DFA {

        public DFA73(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 73;
            this.eot = DFA73_eot;
            this.eof = DFA73_eof;
            this.min = DFA73_min;
            this.max = DFA73_max;
            this.accept = DFA73_accept;
            this.special = DFA73_special;
            this.transition = DFA73_transition;
        }
        public String getDescription() {
            return "99:6: ( declaration_specifiers )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA73_18 = input.LA(1);

                         
                        int index73_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_18);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA73_19 = input.LA(1);

                         
                        int index73_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_19);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA73_20 = input.LA(1);

                         
                        int index73_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_20);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA73_21 = input.LA(1);

                         
                        int index73_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_21);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA73_22 = input.LA(1);

                         
                        int index73_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_22);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA73_23 = input.LA(1);

                         
                        int index73_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_23);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA73_24 = input.LA(1);

                         
                        int index73_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_24);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA73_25 = input.LA(1);

                         
                        int index73_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_25);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA73_26 = input.LA(1);

                         
                        int index73_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_26);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA73_27 = input.LA(1);

                         
                        int index73_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_27);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA73_28 = input.LA(1);

                         
                        int index73_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_28);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA73_29 = input.LA(1);

                         
                        int index73_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_29);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA73_30 = input.LA(1);

                         
                        int index73_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_30);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA73_31 = input.LA(1);

                         
                        int index73_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_31);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA73_32 = input.LA(1);

                         
                        int index73_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred2_C()&&(isTypeName(input.LT(1).getText())))) ) {s = 1;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index73_32);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 73, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA74_eotS =
        "\21\uffff";
    static final String DFA74_eofS =
        "\21\uffff";
    static final String DFA74_minS =
        "\1\4\20\uffff";
    static final String DFA74_maxS =
        "\1\57\20\uffff";
    static final String DFA74_acceptS =
        "\1\uffff\1\2\1\1\16\uffff";
    static final String DFA74_specialS =
        "\21\uffff}>";
    static final String[] DFA74_transitionS = {
            "\1\2\22\uffff\1\2\3\uffff\15\2\1\1\1\uffff\2\2\1\uffff\3\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA74_eot = DFA.unpackEncodedString(DFA74_eotS);
    static final short[] DFA74_eof = DFA.unpackEncodedString(DFA74_eofS);
    static final char[] DFA74_min = DFA.unpackEncodedStringToUnsignedChars(DFA74_minS);
    static final char[] DFA74_max = DFA.unpackEncodedStringToUnsignedChars(DFA74_maxS);
    static final short[] DFA74_accept = DFA.unpackEncodedString(DFA74_acceptS);
    static final short[] DFA74_special = DFA.unpackEncodedString(DFA74_specialS);
    static final short[][] DFA74_transition;

    static {
        int numStates = DFA74_transitionS.length;
        DFA74_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA74_transition[i] = DFA.unpackEncodedString(DFA74_transitionS[i]);
        }
    }

    class DFA74 extends DFA {

        public DFA74(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 74;
            this.eot = DFA74_eot;
            this.eof = DFA74_eof;
            this.min = DFA74_min;
            this.max = DFA74_max;
            this.accept = DFA74_accept;
            this.special = DFA74_special;
            this.transition = DFA74_transition;
        }
        public String getDescription() {
            return "()* loopback of 99:41: ( declaration )*";
        }
    }
 

    public static final BitSet FOLLOW_external_declaration_in_makePublic99 = new BitSet(new long[]{0x0011ECFFF8800012L});
    public static final BitSet FOLLOW_function_definition_in_external_declaration135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_in_external_declaration140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_specifiers_in_function_definition162 = new BitSet(new long[]{0x0011ECFFF8000010L});
    public static final BitSet FOLLOW_declarator_in_function_definition165 = new BitSet(new long[]{0x0011EDFFF8800010L});
    public static final BitSet FOLLOW_declaration_in_function_definition171 = new BitSet(new long[]{0x0011EDFFF8800010L});
    public static final BitSet FOLLOW_compound_statement_in_function_definition174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compound_statement_in_function_definition181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_declaration247 = new BitSet(new long[]{0x0011ECFFF8000010L});
    public static final BitSet FOLLOW_declaration_specifiers_in_declaration249 = new BitSet(new long[]{0x0011ECFFF8000010L});
    public static final BitSet FOLLOW_init_declarator_list_in_declaration257 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_declaration260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_specifiers_in_declaration266 = new BitSet(new long[]{0x0011ECFFF9000010L});
    public static final BitSet FOLLOW_init_declarator_list_in_declaration268 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_declaration272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_storage_class_specifier_in_declaration_specifiers327 = new BitSet(new long[]{0x0000ECFFF8000012L});
    public static final BitSet FOLLOW_type_specifier_in_declaration_specifiers335 = new BitSet(new long[]{0x0000ECFFF8000012L});
    public static final BitSet FOLLOW_type_qualifier_in_declaration_specifiers349 = new BitSet(new long[]{0x0000ECFFF8000012L});
    public static final BitSet FOLLOW_init_declarator_in_init_declarator_list383 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_init_declarator_list387 = new BitSet(new long[]{0x0011ECFFF8000010L});
    public static final BitSet FOLLOW_init_declarator_in_init_declarator_list393 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_declarator_in_init_declarator412 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_init_declarator415 = new BitSet(new long[]{0x9CD10100000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_initializer_in_init_declarator417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_storage_class_specifier0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_type_specifier458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_type_specifier463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_type_specifier468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_type_specifier473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_type_specifier478 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_type_specifier483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_type_specifier488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_type_specifier493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_type_specifier498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_struct_or_union_specifier_in_type_specifier503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_specifier_in_type_specifier508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_id_in_type_specifier513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_type_id531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_struct_or_union_in_struct_or_union_specifier564 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_struct_or_union_specifier566 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_struct_or_union_specifier569 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_struct_declaration_list_in_struct_or_union_specifier571 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_struct_or_union_specifier573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_struct_or_union_in_struct_or_union_specifier578 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_struct_or_union_specifier580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_struct_or_union0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_struct_declaration_in_struct_declaration_list607 = new BitSet(new long[]{0x0000ECFFF8000012L});
    public static final BitSet FOLLOW_specifier_qualifier_list_in_struct_declaration619 = new BitSet(new long[]{0x0011FCFFF8000010L});
    public static final BitSet FOLLOW_struct_declarator_list_in_struct_declaration621 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_struct_declaration623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_qualifier_in_specifier_qualifier_list636 = new BitSet(new long[]{0x0000ECFFF8000012L});
    public static final BitSet FOLLOW_type_specifier_in_specifier_qualifier_list640 = new BitSet(new long[]{0x0000ECFFF8000012L});
    public static final BitSet FOLLOW_struct_declarator_in_struct_declarator_list654 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_struct_declarator_list657 = new BitSet(new long[]{0x0011FCFFF8000010L});
    public static final BitSet FOLLOW_struct_declarator_in_struct_declarator_list659 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_declarator_in_struct_declarator672 = new BitSet(new long[]{0x0000100000000002L});
    public static final BitSet FOLLOW_44_in_struct_declarator675 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_constant_expression_in_struct_declarator677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_struct_declarator684 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_constant_expression_in_struct_declarator686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_enum_specifier704 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_enum_specifier706 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_enumerator_list_in_enum_specifier708 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_enum_specifier710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_enum_specifier715 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enum_specifier717 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_enum_specifier719 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_enumerator_list_in_enum_specifier721 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_enum_specifier723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_enum_specifier728 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enum_specifier730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumerator_in_enumerator_list741 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_enumerator_list744 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_enumerator_in_enumerator_list746 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enumerator759 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_enumerator762 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_constant_expression_in_enumerator764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_type_qualifier0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pointer_in_declarator797 = new BitSet(new long[]{0x0011000000000010L});
    public static final BitSet FOLLOW_direct_declarator_in_declarator800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pointer_in_declarator807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_direct_declarator828 = new BitSet(new long[]{0x0005000000000002L});
    public static final BitSet FOLLOW_48_in_direct_declarator839 = new BitSet(new long[]{0x0011ECFFF8000010L});
    public static final BitSet FOLLOW_declarator_in_direct_declarator841 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_direct_declarator843 = new BitSet(new long[]{0x0005000000000002L});
    public static final BitSet FOLLOW_declarator_suffix_in_direct_declarator857 = new BitSet(new long[]{0x0005000000000002L});
    public static final BitSet FOLLOW_50_in_declarator_suffix871 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_constant_expression_in_declarator_suffix873 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_declarator_suffix875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_declarator_suffix885 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_declarator_suffix887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_declarator_suffix897 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_parameter_type_list_in_declarator_suffix899 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_declarator_suffix901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_declarator_suffix911 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_identifier_list_in_declarator_suffix913 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_declarator_suffix915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_declarator_suffix925 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_declarator_suffix927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_pointer938 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_type_qualifier_in_pointer940 = new BitSet(new long[]{0x0010ECFFF8000012L});
    public static final BitSet FOLLOW_pointer_in_pointer943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_pointer949 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_pointer_in_pointer951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_pointer956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameter_list_in_parameter_type_list967 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_parameter_type_list970 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_parameter_type_list972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parameter_declaration_in_parameter_list985 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_parameter_list988 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_parameter_declaration_in_parameter_list990 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_declaration_specifiers_in_parameter_declaration1003 = new BitSet(new long[]{0x0015ECFFF8000012L});
    public static final BitSet FOLLOW_declarator_in_parameter_declaration1006 = new BitSet(new long[]{0x0015ECFFF8000012L});
    public static final BitSet FOLLOW_abstract_declarator_in_parameter_declaration1008 = new BitSet(new long[]{0x0015ECFFF8000012L});
    public static final BitSet FOLLOW_IDENTIFIER_in_identifier_list1021 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_identifier_list1024 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_identifier_list1026 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_specifier_qualifier_list_in_type_name1039 = new BitSet(new long[]{0x0015ECFFF8000012L});
    public static final BitSet FOLLOW_abstract_declarator_in_type_name1041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pointer_in_abstract_declarator1053 = new BitSet(new long[]{0x0015ECFFF8000012L});
    public static final BitSet FOLLOW_direct_abstract_declarator_in_abstract_declarator1055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_direct_abstract_declarator_in_abstract_declarator1061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_direct_abstract_declarator1074 = new BitSet(new long[]{0x0015ECFFF8000010L});
    public static final BitSet FOLLOW_abstract_declarator_in_direct_abstract_declarator1076 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_direct_abstract_declarator1078 = new BitSet(new long[]{0x0015ECFFF8000012L});
    public static final BitSet FOLLOW_abstract_declarator_suffix_in_direct_abstract_declarator1082 = new BitSet(new long[]{0x0015ECFFF8000012L});
    public static final BitSet FOLLOW_abstract_declarator_suffix_in_direct_abstract_declarator1086 = new BitSet(new long[]{0x0015ECFFF8000012L});
    public static final BitSet FOLLOW_50_in_abstract_declarator_suffix1098 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_abstract_declarator_suffix1100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_abstract_declarator_suffix1105 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_constant_expression_in_abstract_declarator_suffix1107 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_abstract_declarator_suffix1109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_abstract_declarator_suffix1114 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_abstract_declarator_suffix1116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_abstract_declarator_suffix1121 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_parameter_type_list_in_abstract_declarator_suffix1123 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_abstract_declarator_suffix1125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignment_expression_in_initializer1137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_initializer1142 = new BitSet(new long[]{0x9CD10100000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_initializer_list_in_initializer1144 = new BitSet(new long[]{0x0000020002000000L});
    public static final BitSet FOLLOW_25_in_initializer1146 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_initializer1149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_initializer_in_initializer_list1160 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_initializer_list1163 = new BitSet(new long[]{0x9CD10100000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_initializer_in_initializer_list1165 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_assignment_expression_in_argument_expression_list1182 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_argument_expression_list1185 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_assignment_expression_in_argument_expression_list1187 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_multiplicative_expression_in_additive_expression1201 = new BitSet(new long[]{0x00C0000000000002L});
    public static final BitSet FOLLOW_54_in_additive_expression1205 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_multiplicative_expression_in_additive_expression1207 = new BitSet(new long[]{0x00C0000000000002L});
    public static final BitSet FOLLOW_55_in_additive_expression1211 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_multiplicative_expression_in_additive_expression1213 = new BitSet(new long[]{0x00C0000000000002L});
    public static final BitSet FOLLOW_cast_expression_in_multiplicative_expression1227 = new BitSet(new long[]{0x0310000000000002L});
    public static final BitSet FOLLOW_52_in_multiplicative_expression1231 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_cast_expression_in_multiplicative_expression1233 = new BitSet(new long[]{0x0310000000000002L});
    public static final BitSet FOLLOW_56_in_multiplicative_expression1237 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_cast_expression_in_multiplicative_expression1239 = new BitSet(new long[]{0x0310000000000002L});
    public static final BitSet FOLLOW_57_in_multiplicative_expression1243 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_cast_expression_in_multiplicative_expression1245 = new BitSet(new long[]{0x0310000000000002L});
    public static final BitSet FOLLOW_48_in_cast_expression1258 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_type_name_in_cast_expression1260 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_cast_expression1262 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_cast_expression_in_cast_expression1264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_expression_in_cast_expression1269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_postfix_expression_in_unary_expression1280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_unary_expression1285 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_unary_expression_in_unary_expression1287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_unary_expression1292 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_unary_expression_in_unary_expression1294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_operator_in_unary_expression1299 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_cast_expression_in_unary_expression1301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_unary_expression1306 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_unary_expression_in_unary_expression1308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_unary_expression1313 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_unary_expression1315 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_type_name_in_unary_expression1317 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_unary_expression1319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_expression_in_postfix_expression1332 = new BitSet(new long[]{0x6C05000000000002L});
    public static final BitSet FOLLOW_50_in_postfix_expression1346 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_postfix_expression1348 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_postfix_expression1350 = new BitSet(new long[]{0x6C05000000000002L});
    public static final BitSet FOLLOW_48_in_postfix_expression1364 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_postfix_expression1366 = new BitSet(new long[]{0x6C05000000000002L});
    public static final BitSet FOLLOW_48_in_postfix_expression1380 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_argument_expression_list_in_postfix_expression1382 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_postfix_expression1384 = new BitSet(new long[]{0x6C05000000000002L});
    public static final BitSet FOLLOW_61_in_postfix_expression1398 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_postfix_expression1400 = new BitSet(new long[]{0x6C05000000000002L});
    public static final BitSet FOLLOW_62_in_postfix_expression1414 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_postfix_expression1416 = new BitSet(new long[]{0x6C05000000000002L});
    public static final BitSet FOLLOW_58_in_postfix_expression1430 = new BitSet(new long[]{0x6C05000000000002L});
    public static final BitSet FOLLOW_59_in_postfix_expression1444 = new BitSet(new long[]{0x6C05000000000002L});
    public static final BitSet FOLLOW_set_in_unary_operator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary_expression1502 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constant_in_primary_expression1507 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_primary_expression1512 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_primary_expression1514 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_primary_expression1516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_constant0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignment_expression_in_expression1591 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_expression1594 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_assignment_expression_in_expression1596 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_conditional_expression_in_constant_expression1609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lvalue_in_assignment_expression1620 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000FFCL});
    public static final BitSet FOLLOW_assignment_operator_in_assignment_expression1622 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_assignment_expression_in_assignment_expression1624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditional_expression_in_assignment_expression1629 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_expression_in_lvalue1641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_assignment_operator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_logical_or_expression_in_conditional_expression1713 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_conditional_expression1716 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_conditional_expression1718 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_conditional_expression1720 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_conditional_expression_in_conditional_expression1722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_logical_and_expression_in_logical_or_expression1735 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_logical_or_expression1738 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_logical_and_expression_in_logical_or_expression1740 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_inclusive_or_expression_in_logical_and_expression1753 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_78_in_logical_and_expression1756 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_inclusive_or_expression_in_logical_and_expression1758 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
    public static final BitSet FOLLOW_exclusive_or_expression_in_inclusive_or_expression1771 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_inclusive_or_expression1774 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_exclusive_or_expression_in_inclusive_or_expression1776 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_and_expression_in_exclusive_or_expression1789 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_80_in_exclusive_or_expression1792 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_and_expression_in_exclusive_or_expression1794 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_equality_expression_in_and_expression1807 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_63_in_and_expression1810 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_equality_expression_in_and_expression1812 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_relational_expression_in_equality_expression1824 = new BitSet(new long[]{0x0000000000000002L,0x0000000000060000L});
    public static final BitSet FOLLOW_set_in_equality_expression1827 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_relational_expression_in_equality_expression1833 = new BitSet(new long[]{0x0000000000000002L,0x0000000000060000L});
    public static final BitSet FOLLOW_shift_expression_in_relational_expression1846 = new BitSet(new long[]{0x0000000000000002L,0x0000000000780000L});
    public static final BitSet FOLLOW_set_in_relational_expression1849 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_shift_expression_in_relational_expression1859 = new BitSet(new long[]{0x0000000000000002L,0x0000000000780000L});
    public static final BitSet FOLLOW_additive_expression_in_shift_expression1872 = new BitSet(new long[]{0x0000000000000002L,0x0000000001800000L});
    public static final BitSet FOLLOW_set_in_shift_expression1875 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_additive_expression_in_shift_expression1881 = new BitSet(new long[]{0x0000000000000002L,0x0000000001800000L});
    public static final BitSet FOLLOW_labeled_statement_in_statement1896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compound_statement_in_statement1901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_statement_in_statement1906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selection_statement_in_statement1911 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_iteration_statement_in_statement1916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_jump_statement_in_statement1921 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_labeled_statement1932 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_labeled_statement1934 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_labeled_statement1936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_labeled_statement1941 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_constant_expression_in_labeled_statement1943 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_labeled_statement1945 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_labeled_statement1947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_labeled_statement1952 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_labeled_statement1954 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_labeled_statement1956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_compound_statement1978 = new BitSet(new long[]{0x9CD1EFFFF98007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_declaration_in_compound_statement1980 = new BitSet(new long[]{0x9CD1EFFFF98007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_list_in_compound_statement1983 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_compound_statement1986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_statement_list1997 = new BitSet(new long[]{0x9CD10100010007F2L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_24_in_expression_statement2009 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expression_statement2014 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_expression_statement2016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_selection_statement2027 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_selection_statement2029 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_selection_statement2031 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_selection_statement2033 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_selection_statement2035 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_92_in_selection_statement2050 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_selection_statement2052 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_selection_statement2059 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_selection_statement2061 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_selection_statement2063 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_selection_statement2065 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_selection_statement2067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_iteration_statement2078 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_iteration_statement2080 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_iteration_statement2082 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_iteration_statement2084 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_iteration_statement2086 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_iteration_statement2091 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_iteration_statement2093 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_94_in_iteration_statement2095 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_iteration_statement2097 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_iteration_statement2099 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_iteration_statement2101 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_iteration_statement2103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_iteration_statement2108 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_iteration_statement2110 = new BitSet(new long[]{0x9CD10000010007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_statement_in_iteration_statement2112 = new BitSet(new long[]{0x9CD10000010007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_statement_in_iteration_statement2114 = new BitSet(new long[]{0x9CD30000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_iteration_statement2116 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_iteration_statement2119 = new BitSet(new long[]{0x9CD10100010007F0L,0x0000001FEE000003L});
    public static final BitSet FOLLOW_statement_in_iteration_statement2121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_jump_statement2132 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_jump_statement2134 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_jump_statement2136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_jump_statement2141 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_jump_statement2143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_jump_statement2148 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_jump_statement2150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_jump_statement2155 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_jump_statement2157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_jump_statement2162 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_expression_in_jump_statement2164 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_jump_statement2166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_specifiers_in_synpred2_C122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_specifiers_in_synpred4_C122 = new BitSet(new long[]{0x0011ECFFF8000010L});
    public static final BitSet FOLLOW_declarator_in_synpred4_C125 = new BitSet(new long[]{0x0011EDFFF8800010L});
    public static final BitSet FOLLOW_declaration_in_synpred4_C127 = new BitSet(new long[]{0x0011EDFFF8800010L});
    public static final BitSet FOLLOW_40_in_synpred4_C130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_specifiers_in_synpred5_C162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_specifiers_in_synpred8_C249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_specifier_in_synpred12_C335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_specifier_in_synpred35_C640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pointer_in_synpred45_C797 = new BitSet(new long[]{0x0011000000000010L});
    public static final BitSet FOLLOW_direct_declarator_in_synpred45_C800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declarator_suffix_in_synpred47_C857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_synpred50_C897 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_parameter_type_list_in_synpred50_C899 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred50_C901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_synpred51_C911 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_identifier_list_in_synpred51_C913 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred51_C915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_qualifier_in_synpred52_C940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pointer_in_synpred53_C943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred54_C938 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_type_qualifier_in_synpred54_C940 = new BitSet(new long[]{0x0010ECFFF8000012L});
    public static final BitSet FOLLOW_pointer_in_synpred54_C943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred55_C949 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_pointer_in_synpred55_C951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declarator_in_synpred58_C1006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_abstract_declarator_in_synpred59_C1008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_direct_abstract_declarator_in_synpred62_C1055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_abstract_declarator_suffix_in_synpred65_C1086 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_synpred78_C1258 = new BitSet(new long[]{0x0000ECFFF8000010L});
    public static final BitSet FOLLOW_type_name_in_synpred78_C1260 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred78_C1262 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_cast_expression_in_synpred78_C1264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_synpred83_C1306 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_unary_expression_in_synpred83_C1308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lvalue_in_synpred104_C1620 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000FFCL});
    public static final BitSet FOLLOW_assignment_operator_in_synpred104_C1622 = new BitSet(new long[]{0x9CD10000000007F0L,0x0000000000000003L});
    public static final BitSet FOLLOW_assignment_expression_in_synpred104_C1624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_in_synpred136_C1980 = new BitSet(new long[]{0x0000000000000002L});

}