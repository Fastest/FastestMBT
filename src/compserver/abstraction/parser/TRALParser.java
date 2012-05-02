// $ANTLR 3.2 Sep 23, 2009 12:02:23 TRAL.g 2010-07-29 14:57:53

package compserver.abstraction.parser;

import compserver.abstraction.types.spectypes.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TRALParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Text", "NEWLINE", "NATURAL", "INTEGER", "Range", "WS", "'@RULES'", "':'", "','", "'==>'", "'COMPOSITION'", "'@SET'", "'AS'", "'dom'", "'ran'", "'cardinal'", "'DECOMPOSITION'", "'@CAPTURECODE'", "'BASICTYPE'", "'['", "']'", "'FREETYPE'", "'PFUN'", "'FUN'", "'REL'", "'PROD'", "'SEQUENCE'", "'POWER'", "'INT'", "'NAT'", "'PLTYPE'", "'POINTER'", "'STRUCTURE'", "'ARRAY'", "'LIST'", "'SLL'", "'DLL'", "'CLL'", "'DCLL'", "'FILE'", "'ENDOFLINE'", "'ENDOFFILE'", "'LINEAR'", "'RPL'", "'FPL'", "'DB'", "'ENUMERATION'", "'\\\"'", "'-->'", "'SCREEN'", "'plane'", "'table'", "'COLRANGE'", "'ROWRANGE'"
    };
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int EOF=-1;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__19=19;
    public static final int T__57=57;
    public static final int T__51=51;
    public static final int T__16=16;
    public static final int T__52=52;
    public static final int T__15=15;
    public static final int T__53=53;
    public static final int T__18=18;
    public static final int T__54=54;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int T__50=50;
    public static final int INTEGER=7;
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
    public static final int T__30=30;
    public static final int Range=8;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int WS=9;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int NEWLINE=5;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int Text=4;
    public static final int NATURAL=6;

    // delegates
    // delegators


        public TRALParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public TRALParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return TRALParser.tokenNames; }
    public String getGrammarFileName() { return "TRAL.g"; }


    	List<ImplNodeField> fields;
    	List<ImplNodeField> fieldsList;
    	List<AbstractionRule> rulesList;
    	List<SpecNode> specNodes;
    	List<ImplNode> implNodes;
    	String code;
    	String linkPrevNode;
    	String lastPosPointedID;
    	List<ImplNodeDBColumn> columnsList;
    	List<SpecNode> cartesianMembers;
    	Map<String,String> enumElements;
    	List<VarComposition> compositions;
    	ImplNodeEnumeration nodeEnumeration;



    // $ANTLR start "abstractionLaw"
    // TRAL.g:32:1: abstractionLaw returns [AbstractionLaw law] : Text NEWLINE rules ;
    public final AbstractionLaw abstractionLaw() throws RecognitionException {
        AbstractionLaw law = null;

        Token Text1=null;
        List<AbstractionRule> rules2 = null;


        try {
            // TRAL.g:33:1: ( Text NEWLINE rules )
            // TRAL.g:33:3: Text NEWLINE rules
            {
            Text1=(Token)match(input,Text,FOLLOW_Text_in_abstractionLaw36); 
            match(input,NEWLINE,FOLLOW_NEWLINE_in_abstractionLaw38); 
            pushFollow(FOLLOW_rules_in_abstractionLaw43);
            rules2=rules();

            state._fsp--;

            law =new AbstractionLaw((Text1!=null?Text1.getText():null),rules2);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return law;
    }
    // $ANTLR end "abstractionLaw"


    // $ANTLR start "rules"
    // TRAL.g:36:1: rules returns [List<AbstractionRule> listRules] : '@RULES' NEWLINE ( rule NEWLINE )+ ;
    public final List<AbstractionRule> rules() throws RecognitionException {
        List<AbstractionRule> listRules = null;

        AbstractionRule rule3 = null;



        	rulesList = new ArrayList<AbstractionRule>();

        try {
            // TRAL.g:40:1: ( '@RULES' NEWLINE ( rule NEWLINE )+ )
            // TRAL.g:40:3: '@RULES' NEWLINE ( rule NEWLINE )+
            {
            match(input,10,FOLLOW_10_in_rules60); 
            match(input,NEWLINE,FOLLOW_NEWLINE_in_rules62); 
            // TRAL.g:41:4: ( rule NEWLINE )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==Text) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // TRAL.g:41:5: rule NEWLINE
            	    {
            	    pushFollow(FOLLOW_rule_in_rules68);
            	    rule3=rule();

            	    state._fsp--;

            	    rulesList.add(rule3);
            	    match(input,NEWLINE,FOLLOW_NEWLINE_in_rules72); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            listRules = rulesList;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return listRules;
    }
    // $ANTLR end "rules"


    // $ANTLR start "rule"
    // TRAL.g:43:1: rule returns [AbstractionRule ruleObject] : Text ':' ruleAbstraction ;
    public final AbstractionRule rule() throws RecognitionException {
        AbstractionRule ruleObject = null;

        Token Text5=null;
        AbstractionRule ruleAbstraction4 = null;


        try {
            // TRAL.g:44:1: ( Text ':' ruleAbstraction )
            // TRAL.g:44:3: Text ':' ruleAbstraction
            {
            Text5=(Token)match(input,Text,FOLLOW_Text_in_rule88); 
            match(input,11,FOLLOW_11_in_rule90); 
            pushFollow(FOLLOW_ruleAbstraction_in_rule92);
            ruleAbstraction4=ruleAbstraction();

            state._fsp--;

            ruleObject = ruleAbstraction4; ruleObject.setRuleName((Text5!=null?Text5.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ruleObject;
    }
    // $ANTLR end "rule"


    // $ANTLR start "ruleAbstraction"
    // TRAL.g:46:1: ruleAbstraction returns [AbstractionRule ruleObject] : impId1= Text ':' impT1= implType ( ',' impId2= Text ':' impT2= implType )* '==>' specId1= Text ':' specT1= specType ( ',' specId2= Text ':' specT2= specType )* ( ',' NEWLINE composition )* ( ',' NEWLINE decomposition )* ( ',' NEWLINE captureCode )* ;
    public final AbstractionRule ruleAbstraction() throws RecognitionException {
        AbstractionRule ruleObject = null;

        Token impId1=null;
        Token impId2=null;
        Token specId1=null;
        Token specId2=null;
        ImplNode impT1 = null;

        ImplNode impT2 = null;

        SpecNode specT1 = null;

        SpecNode specT2 = null;

        List<VarComposition> composition6 = null;

        String captureCode7 = null;



        	specNodes = new ArrayList<SpecNode>();
        	implNodes = new ArrayList<ImplNode>();
        	compositions = new ArrayList<VarComposition>();
        	code = "";

        try {
            // TRAL.g:53:1: (impId1= Text ':' impT1= implType ( ',' impId2= Text ':' impT2= implType )* '==>' specId1= Text ':' specT1= specType ( ',' specId2= Text ':' specT2= specType )* ( ',' NEWLINE composition )* ( ',' NEWLINE decomposition )* ( ',' NEWLINE captureCode )* )
            // TRAL.g:53:3: impId1= Text ':' impT1= implType ( ',' impId2= Text ':' impT2= implType )* '==>' specId1= Text ':' specT1= specType ( ',' specId2= Text ':' specT2= specType )* ( ',' NEWLINE composition )* ( ',' NEWLINE decomposition )* ( ',' NEWLINE captureCode )*
            {
            impId1=(Token)match(input,Text,FOLLOW_Text_in_ruleAbstraction113); 
            match(input,11,FOLLOW_11_in_ruleAbstraction114); 
            pushFollow(FOLLOW_implType_in_ruleAbstraction119);
            impT1=implType();

            state._fsp--;

            ImplNode impNode1 = impT1; impNode1.setImplID((impId1!=null?impId1.getText():null)); implNodes.add(impNode1); 
            // TRAL.g:53:134: ( ',' impId2= Text ':' impT2= implType )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==12) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // TRAL.g:53:135: ',' impId2= Text ':' impT2= implType
            	    {
            	    match(input,12,FOLLOW_12_in_ruleAbstraction123); 
            	    impId2=(Token)match(input,Text,FOLLOW_Text_in_ruleAbstraction129); 
            	    match(input,11,FOLLOW_11_in_ruleAbstraction130); 
            	    pushFollow(FOLLOW_implType_in_ruleAbstraction135);
            	    impT2=implType();

            	    state._fsp--;

            	    ImplNode impNode2 = impT2; impNode2.setImplID((impId2!=null?impId2.getText():null)); implNodes.add(impNode2); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match(input,13,FOLLOW_13_in_ruleAbstraction141); 
            specId1=(Token)match(input,Text,FOLLOW_Text_in_ruleAbstraction147); 
            match(input,11,FOLLOW_11_in_ruleAbstraction148); 
            pushFollow(FOLLOW_specType_in_ruleAbstraction153);
            specT1=specType();

            state._fsp--;

            SpecNode specNode1 = specT1; specNode1.setSpecID((specId1!=null?specId1.getText():null)); specNodes.add(specNode1); 
            // TRAL.g:53:417: ( ',' specId2= Text ':' specT2= specType )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==12) ) {
                    int LA3_1 = input.LA(2);

                    if ( (LA3_1==Text) ) {
                        alt3=1;
                    }


                }


                switch (alt3) {
            	case 1 :
            	    // TRAL.g:53:418: ',' specId2= Text ':' specT2= specType
            	    {
            	    match(input,12,FOLLOW_12_in_ruleAbstraction157); 
            	    specId2=(Token)match(input,Text,FOLLOW_Text_in_ruleAbstraction163); 
            	    match(input,11,FOLLOW_11_in_ruleAbstraction164); 
            	    pushFollow(FOLLOW_specType_in_ruleAbstraction169);
            	    specT2=specType();

            	    state._fsp--;

            	    SpecNode specNode2 = specT2; specNode2.setSpecID((specId2!=null?specId2.getText():null)); specNodes.add(specNode2); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            // TRAL.g:53:563: ( ',' NEWLINE composition )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==12) ) {
                    int LA4_1 = input.LA(2);

                    if ( (LA4_1==NEWLINE) ) {
                        int LA4_3 = input.LA(3);

                        if ( (LA4_3==14) ) {
                            alt4=1;
                        }


                    }


                }


                switch (alt4) {
            	case 1 :
            	    // TRAL.g:53:564: ',' NEWLINE composition
            	    {
            	    match(input,12,FOLLOW_12_in_ruleAbstraction176); 
            	    match(input,NEWLINE,FOLLOW_NEWLINE_in_ruleAbstraction178); 
            	    pushFollow(FOLLOW_composition_in_ruleAbstraction180);
            	    composition6=composition();

            	    state._fsp--;

            	    compositions = composition6;

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // TRAL.g:53:637: ( ',' NEWLINE decomposition )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==12) ) {
                    int LA5_1 = input.LA(2);

                    if ( (LA5_1==NEWLINE) ) {
                        int LA5_3 = input.LA(3);

                        if ( (LA5_3==20) ) {
                            alt5=1;
                        }


                    }


                }


                switch (alt5) {
            	case 1 :
            	    // TRAL.g:53:638: ',' NEWLINE decomposition
            	    {
            	    match(input,12,FOLLOW_12_in_ruleAbstraction187); 
            	    match(input,NEWLINE,FOLLOW_NEWLINE_in_ruleAbstraction189); 
            	    pushFollow(FOLLOW_decomposition_in_ruleAbstraction191);
            	    decomposition();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // TRAL.g:53:666: ( ',' NEWLINE captureCode )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==12) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // TRAL.g:53:667: ',' NEWLINE captureCode
            	    {
            	    match(input,12,FOLLOW_12_in_ruleAbstraction196); 
            	    match(input,NEWLINE,FOLLOW_NEWLINE_in_ruleAbstraction198); 
            	    pushFollow(FOLLOW_captureCode_in_ruleAbstraction200);
            	    captureCode7=captureCode();

            	    state._fsp--;

            	    code+=captureCode7;

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            ruleObject = new AbstractionRule("",implNodes,specNodes,code,compositions); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ruleObject;
    }
    // $ANTLR end "ruleAbstraction"


    // $ANTLR start "composition"
    // TRAL.g:55:1: composition returns [List<VarComposition> varCompositions] : 'COMPOSITION' ( NEWLINE '@SET' implID= Text 'AS' preOperator specID= Text )+ ;
    public final List<VarComposition> composition() throws RecognitionException {
        List<VarComposition> varCompositions = null;

        Token implID=null;
        Token specID=null;
        TRALParser.preOperator_return preOperator8 = null;



        	varCompositions = new ArrayList<VarComposition>();

        try {
            // TRAL.g:59:1: ( 'COMPOSITION' ( NEWLINE '@SET' implID= Text 'AS' preOperator specID= Text )+ )
            // TRAL.g:59:3: 'COMPOSITION' ( NEWLINE '@SET' implID= Text 'AS' preOperator specID= Text )+
            {
            match(input,14,FOLLOW_14_in_composition221); 
            // TRAL.g:60:3: ( NEWLINE '@SET' implID= Text 'AS' preOperator specID= Text )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==NEWLINE) ) {
                    int LA7_2 = input.LA(2);

                    if ( (LA7_2==15) ) {
                        alt7=1;
                    }


                }


                switch (alt7) {
            	case 1 :
            	    // TRAL.g:60:4: NEWLINE '@SET' implID= Text 'AS' preOperator specID= Text
            	    {
            	    match(input,NEWLINE,FOLLOW_NEWLINE_in_composition227); 
            	    match(input,15,FOLLOW_15_in_composition229); 
            	    implID=(Token)match(input,Text,FOLLOW_Text_in_composition235); 
            	    match(input,16,FOLLOW_16_in_composition237); 
            	    pushFollow(FOLLOW_preOperator_in_composition239);
            	    preOperator8=preOperator();

            	    state._fsp--;

            	    specID=(Token)match(input,Text,FOLLOW_Text_in_composition245); 
            	    varCompositions.add(new VarComposition((specID!=null?specID.getText():null),(preOperator8!=null?input.toString(preOperator8.start,preOperator8.stop):null),(implID!=null?implID.getText():null))); 

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return varCompositions;
    }
    // $ANTLR end "composition"

    public static class preOperator_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "preOperator"
    // TRAL.g:62:1: preOperator : ( 'dom' | 'ran' | 'cardinal' );
    public final TRALParser.preOperator_return preOperator() throws RecognitionException {
        TRALParser.preOperator_return retval = new TRALParser.preOperator_return();
        retval.start = input.LT(1);

        try {
            // TRAL.g:63:1: ( 'dom' | 'ran' | 'cardinal' )
            // TRAL.g:
            {
            if ( (input.LA(1)>=17 && input.LA(1)<=19) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
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
        }
        return retval;
    }
    // $ANTLR end "preOperator"


    // $ANTLR start "decomposition"
    // TRAL.g:65:1: decomposition : 'DECOMPOSITION' NEWLINE ( '@SET' Text 'AS' Text NEWLINE )* ;
    public final void decomposition() throws RecognitionException {
        try {
            // TRAL.g:66:1: ( 'DECOMPOSITION' NEWLINE ( '@SET' Text 'AS' Text NEWLINE )* )
            // TRAL.g:66:3: 'DECOMPOSITION' NEWLINE ( '@SET' Text 'AS' Text NEWLINE )*
            {
            match(input,20,FOLLOW_20_in_decomposition273); 
            match(input,NEWLINE,FOLLOW_NEWLINE_in_decomposition275); 
            // TRAL.g:67:3: ( '@SET' Text 'AS' Text NEWLINE )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==15) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // TRAL.g:67:4: '@SET' Text 'AS' Text NEWLINE
            	    {
            	    match(input,15,FOLLOW_15_in_decomposition280); 
            	    match(input,Text,FOLLOW_Text_in_decomposition282); 
            	    match(input,16,FOLLOW_16_in_decomposition284); 
            	    match(input,Text,FOLLOW_Text_in_decomposition286); 
            	    match(input,NEWLINE,FOLLOW_NEWLINE_in_decomposition288); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "decomposition"


    // $ANTLR start "captureCode"
    // TRAL.g:69:1: captureCode returns [String toCapture] : '@CAPTURECODE' NEWLINE Text ;
    public final String captureCode() throws RecognitionException {
        String toCapture = null;

        Token Text9=null;

        try {
            // TRAL.g:70:1: ( '@CAPTURECODE' NEWLINE Text )
            // TRAL.g:70:3: '@CAPTURECODE' NEWLINE Text
            {
            match(input,21,FOLLOW_21_in_captureCode302); 
            match(input,NEWLINE,FOLLOW_NEWLINE_in_captureCode304); 
            Text9=(Token)match(input,Text,FOLLOW_Text_in_captureCode308); 
            toCapture = (Text9!=null?Text9.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return toCapture;
    }
    // $ANTLR end "captureCode"


    // $ANTLR start "specType"
    // TRAL.g:73:1: specType returns [SpecNode specNode] : ( basicType | freeType | pFunction | tFunction | relation | sequence | powerSet | cartesianProd | integer | nat );
    public final SpecNode specType() throws RecognitionException {
        SpecNode specNode = null;

        SpecNodeBasicType basicType10 = null;

        SpecNodeFreeType freeType11 = null;

        SpecNodePFun pFunction12 = null;

        SpecNodeFun tFunction13 = null;

        SpecNodeRel relation14 = null;

        SpecNodeSeq sequence15 = null;

        SpecNodePowerSet powerSet16 = null;

        SpecNodeCartesianProd cartesianProd17 = null;

        SpecNodeInt integer18 = null;

        SpecNodeNat nat19 = null;


        try {
            // TRAL.g:74:1: ( basicType | freeType | pFunction | tFunction | relation | sequence | powerSet | cartesianProd | integer | nat )
            int alt9=10;
            switch ( input.LA(1) ) {
            case 22:
                {
                alt9=1;
                }
                break;
            case 25:
                {
                alt9=2;
                }
                break;
            case 26:
                {
                alt9=3;
                }
                break;
            case 27:
                {
                alt9=4;
                }
                break;
            case 28:
                {
                alt9=5;
                }
                break;
            case 30:
                {
                alt9=6;
                }
                break;
            case 31:
                {
                alt9=7;
                }
                break;
            case 29:
                {
                alt9=8;
                }
                break;
            case 32:
                {
                alt9=9;
                }
                break;
            case 33:
                {
                alt9=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // TRAL.g:74:3: basicType
                    {
                    pushFollow(FOLLOW_basicType_in_specType322);
                    basicType10=basicType();

                    state._fsp--;

                    specNode = basicType10;

                    }
                    break;
                case 2 :
                    // TRAL.g:75:3: freeType
                    {
                    pushFollow(FOLLOW_freeType_in_specType328);
                    freeType11=freeType();

                    state._fsp--;

                    specNode = freeType11;

                    }
                    break;
                case 3 :
                    // TRAL.g:76:3: pFunction
                    {
                    pushFollow(FOLLOW_pFunction_in_specType334);
                    pFunction12=pFunction();

                    state._fsp--;

                    specNode = pFunction12;

                    }
                    break;
                case 4 :
                    // TRAL.g:77:3: tFunction
                    {
                    pushFollow(FOLLOW_tFunction_in_specType340);
                    tFunction13=tFunction();

                    state._fsp--;

                    specNode = tFunction13;

                    }
                    break;
                case 5 :
                    // TRAL.g:78:3: relation
                    {
                    pushFollow(FOLLOW_relation_in_specType346);
                    relation14=relation();

                    state._fsp--;

                    specNode = relation14;

                    }
                    break;
                case 6 :
                    // TRAL.g:79:3: sequence
                    {
                    pushFollow(FOLLOW_sequence_in_specType352);
                    sequence15=sequence();

                    state._fsp--;

                    specNode = sequence15;

                    }
                    break;
                case 7 :
                    // TRAL.g:80:3: powerSet
                    {
                    pushFollow(FOLLOW_powerSet_in_specType358);
                    powerSet16=powerSet();

                    state._fsp--;

                    specNode = powerSet16;

                    }
                    break;
                case 8 :
                    // TRAL.g:81:3: cartesianProd
                    {
                    pushFollow(FOLLOW_cartesianProd_in_specType364);
                    cartesianProd17=cartesianProd();

                    state._fsp--;

                    specNode = cartesianProd17;

                    }
                    break;
                case 9 :
                    // TRAL.g:82:3: integer
                    {
                    pushFollow(FOLLOW_integer_in_specType370);
                    integer18=integer();

                    state._fsp--;

                    specNode = integer18;

                    }
                    break;
                case 10 :
                    // TRAL.g:83:3: nat
                    {
                    pushFollow(FOLLOW_nat_in_specType376);
                    nat19=nat();

                    state._fsp--;

                    specNode = nat19;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "specType"


    // $ANTLR start "basicType"
    // TRAL.g:86:1: basicType returns [SpecNodeBasicType specNode] : 'BASICTYPE' '[' Text ']' ;
    public final SpecNodeBasicType basicType() throws RecognitionException {
        SpecNodeBasicType specNode = null;

        Token Text20=null;

        try {
            // TRAL.g:87:1: ( 'BASICTYPE' '[' Text ']' )
            // TRAL.g:87:3: 'BASICTYPE' '[' Text ']'
            {
            match(input,22,FOLLOW_22_in_basicType392); 
            match(input,23,FOLLOW_23_in_basicType394); 
            Text20=(Token)match(input,Text,FOLLOW_Text_in_basicType396); 
            match(input,24,FOLLOW_24_in_basicType398); 
            specNode =new SpecNodeBasicType((Text20!=null?Text20.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "basicType"


    // $ANTLR start "freeType"
    // TRAL.g:89:1: freeType returns [SpecNodeFreeType specNode] : 'FREETYPE' '[' Text ']' ;
    public final SpecNodeFreeType freeType() throws RecognitionException {
        SpecNodeFreeType specNode = null;

        Token Text21=null;

        try {
            // TRAL.g:90:1: ( 'FREETYPE' '[' Text ']' )
            // TRAL.g:90:3: 'FREETYPE' '[' Text ']'
            {
            match(input,25,FOLLOW_25_in_freeType412); 
            match(input,23,FOLLOW_23_in_freeType414); 
            Text21=(Token)match(input,Text,FOLLOW_Text_in_freeType416); 
            match(input,24,FOLLOW_24_in_freeType418); 
            specNode =new SpecNodeFreeType((Text21!=null?Text21.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "freeType"


    // $ANTLR start "pFunction"
    // TRAL.g:92:1: pFunction returns [SpecNodePFun specNode] : 'PFUN' '[' t1= specType ',' t2= specType ']' ;
    public final SpecNodePFun pFunction() throws RecognitionException {
        SpecNodePFun specNode = null;

        SpecNode t1 = null;

        SpecNode t2 = null;


        try {
            // TRAL.g:93:1: ( 'PFUN' '[' t1= specType ',' t2= specType ']' )
            // TRAL.g:93:3: 'PFUN' '[' t1= specType ',' t2= specType ']'
            {
            match(input,26,FOLLOW_26_in_pFunction432); 
            match(input,23,FOLLOW_23_in_pFunction434); 
            pushFollow(FOLLOW_specType_in_pFunction440);
            t1=specType();

            state._fsp--;

            match(input,12,FOLLOW_12_in_pFunction442); 
            pushFollow(FOLLOW_specType_in_pFunction448);
            t2=specType();

            state._fsp--;

            match(input,24,FOLLOW_24_in_pFunction450); 
            specNode =new SpecNodePFun(t1, t2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "pFunction"


    // $ANTLR start "tFunction"
    // TRAL.g:95:1: tFunction returns [SpecNodeFun specNode] : 'FUN' '[' t1= specType ',' t2= specType ']' ;
    public final SpecNodeFun tFunction() throws RecognitionException {
        SpecNodeFun specNode = null;

        SpecNode t1 = null;

        SpecNode t2 = null;


        try {
            // TRAL.g:96:1: ( 'FUN' '[' t1= specType ',' t2= specType ']' )
            // TRAL.g:96:3: 'FUN' '[' t1= specType ',' t2= specType ']'
            {
            match(input,27,FOLLOW_27_in_tFunction464); 
            match(input,23,FOLLOW_23_in_tFunction466); 
            pushFollow(FOLLOW_specType_in_tFunction472);
            t1=specType();

            state._fsp--;

            match(input,12,FOLLOW_12_in_tFunction474); 
            pushFollow(FOLLOW_specType_in_tFunction480);
            t2=specType();

            state._fsp--;

            match(input,24,FOLLOW_24_in_tFunction482); 
            specNode =new SpecNodeFun(t1, t2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "tFunction"


    // $ANTLR start "relation"
    // TRAL.g:98:1: relation returns [SpecNodeRel specNode] : 'REL' '[' t1= specType ',' t2= specType ']' ;
    public final SpecNodeRel relation() throws RecognitionException {
        SpecNodeRel specNode = null;

        SpecNode t1 = null;

        SpecNode t2 = null;


        try {
            // TRAL.g:99:1: ( 'REL' '[' t1= specType ',' t2= specType ']' )
            // TRAL.g:99:3: 'REL' '[' t1= specType ',' t2= specType ']'
            {
            match(input,28,FOLLOW_28_in_relation496); 
            match(input,23,FOLLOW_23_in_relation498); 
            pushFollow(FOLLOW_specType_in_relation504);
            t1=specType();

            state._fsp--;

            match(input,12,FOLLOW_12_in_relation506); 
            pushFollow(FOLLOW_specType_in_relation512);
            t2=specType();

            state._fsp--;

            match(input,24,FOLLOW_24_in_relation514); 
            specNode =new SpecNodeRel(t1, t2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "relation"


    // $ANTLR start "cartesianProd"
    // TRAL.g:101:1: cartesianProd returns [SpecNodeCartesianProd specNode] : 'PROD' '[' t1= specType ',' t2= specType ( ',' t3= specType )* ']' ;
    public final SpecNodeCartesianProd cartesianProd() throws RecognitionException {
        SpecNodeCartesianProd specNode = null;

        SpecNode t1 = null;

        SpecNode t2 = null;

        SpecNode t3 = null;



        	cartesianMembers = new ArrayList<SpecNode>();

        try {
            // TRAL.g:105:1: ( 'PROD' '[' t1= specType ',' t2= specType ( ',' t3= specType )* ']' )
            // TRAL.g:105:3: 'PROD' '[' t1= specType ',' t2= specType ( ',' t3= specType )* ']'
            {
            match(input,29,FOLLOW_29_in_cartesianProd532); 
            match(input,23,FOLLOW_23_in_cartesianProd534); 
            pushFollow(FOLLOW_specType_in_cartesianProd540);
            t1=specType();

            state._fsp--;

            cartesianMembers.add(t1);
            match(input,12,FOLLOW_12_in_cartesianProd544); 
            pushFollow(FOLLOW_specType_in_cartesianProd550);
            t2=specType();

            state._fsp--;

            cartesianMembers.add(t2);
            // TRAL.g:105:122: ( ',' t3= specType )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==12) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // TRAL.g:105:123: ',' t3= specType
            	    {
            	    match(input,12,FOLLOW_12_in_cartesianProd555); 
            	    pushFollow(FOLLOW_specType_in_cartesianProd561);
            	    t3=specType();

            	    state._fsp--;

            	    cartesianMembers.add(t3);

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            match(input,24,FOLLOW_24_in_cartesianProd566); 
            specNode =new SpecNodeCartesianProd(cartesianMembers); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "cartesianProd"


    // $ANTLR start "sequence"
    // TRAL.g:107:1: sequence returns [SpecNodeSeq specNode] : 'SEQUENCE' '[' specType ']' ;
    public final SpecNodeSeq sequence() throws RecognitionException {
        SpecNodeSeq specNode = null;

        SpecNode specType22 = null;


        try {
            // TRAL.g:108:1: ( 'SEQUENCE' '[' specType ']' )
            // TRAL.g:108:3: 'SEQUENCE' '[' specType ']'
            {
            match(input,30,FOLLOW_30_in_sequence580); 
            match(input,23,FOLLOW_23_in_sequence582); 
            pushFollow(FOLLOW_specType_in_sequence584);
            specType22=specType();

            state._fsp--;

            match(input,24,FOLLOW_24_in_sequence586); 
            specNode =new SpecNodeSeq(specType22); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "sequence"


    // $ANTLR start "powerSet"
    // TRAL.g:110:1: powerSet returns [SpecNodePowerSet specNode] : 'POWER' '[' specType ']' ;
    public final SpecNodePowerSet powerSet() throws RecognitionException {
        SpecNodePowerSet specNode = null;

        SpecNode specType23 = null;


        try {
            // TRAL.g:111:1: ( 'POWER' '[' specType ']' )
            // TRAL.g:111:3: 'POWER' '[' specType ']'
            {
            match(input,31,FOLLOW_31_in_powerSet600); 
            match(input,23,FOLLOW_23_in_powerSet602); 
            pushFollow(FOLLOW_specType_in_powerSet604);
            specType23=specType();

            state._fsp--;

            match(input,24,FOLLOW_24_in_powerSet606); 
            specNode =new SpecNodePowerSet(specType23); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "powerSet"


    // $ANTLR start "integer"
    // TRAL.g:113:1: integer returns [SpecNodeInt specNode] : 'INT' ;
    public final SpecNodeInt integer() throws RecognitionException {
        SpecNodeInt specNode = null;

        try {
            // TRAL.g:114:1: ( 'INT' )
            // TRAL.g:114:3: 'INT'
            {
            match(input,32,FOLLOW_32_in_integer620); 
            specNode =new SpecNodeInt();

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "integer"


    // $ANTLR start "nat"
    // TRAL.g:116:1: nat returns [SpecNodeNat specNode] : 'NAT' ;
    public final SpecNodeNat nat() throws RecognitionException {
        SpecNodeNat specNode = null;

        try {
            // TRAL.g:117:1: ( 'NAT' )
            // TRAL.g:117:3: 'NAT'
            {
            match(input,33,FOLLOW_33_in_nat634); 
            specNode =new SpecNodeNat();

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return specNode;
    }
    // $ANTLR end "nat"


    // $ANTLR start "implType"
    // TRAL.g:119:1: implType returns [ImplNode implNode] : ( plType | pointer | structure | array | list | file | db | enumeration | screen );
    public final ImplNode implType() throws RecognitionException {
        ImplNode implNode = null;

        ImplNodePLType plType24 = null;

        ImplNodePointer pointer25 = null;

        ImplNodeStructure structure26 = null;

        ImplNodeArray array27 = null;

        ImplNodeList list28 = null;

        ImplNodeFile file29 = null;

        ImplNodeDB db30 = null;

        ImplNodeEnumeration enumeration31 = null;

        ImplNodeScreen screen32 = null;


        try {
            // TRAL.g:120:1: ( plType | pointer | structure | array | list | file | db | enumeration | screen )
            int alt11=9;
            switch ( input.LA(1) ) {
            case 34:
                {
                alt11=1;
                }
                break;
            case 35:
                {
                alt11=2;
                }
                break;
            case 36:
                {
                alt11=3;
                }
                break;
            case 37:
                {
                alt11=4;
                }
                break;
            case 38:
                {
                alt11=5;
                }
                break;
            case 43:
                {
                alt11=6;
                }
                break;
            case 49:
                {
                alt11=7;
                }
                break;
            case 50:
                {
                alt11=8;
                }
                break;
            case 53:
                {
                alt11=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // TRAL.g:120:3: plType
                    {
                    pushFollow(FOLLOW_plType_in_implType648);
                    plType24=plType();

                    state._fsp--;

                    implNode = plType24;

                    }
                    break;
                case 2 :
                    // TRAL.g:121:3: pointer
                    {
                    pushFollow(FOLLOW_pointer_in_implType654);
                    pointer25=pointer();

                    state._fsp--;

                    implNode = pointer25;

                    }
                    break;
                case 3 :
                    // TRAL.g:122:3: structure
                    {
                    pushFollow(FOLLOW_structure_in_implType660);
                    structure26=structure();

                    state._fsp--;

                    implNode = structure26;

                    }
                    break;
                case 4 :
                    // TRAL.g:123:3: array
                    {
                    pushFollow(FOLLOW_array_in_implType666);
                    array27=array();

                    state._fsp--;

                    implNode = array27;

                    }
                    break;
                case 5 :
                    // TRAL.g:124:3: list
                    {
                    pushFollow(FOLLOW_list_in_implType672);
                    list28=list();

                    state._fsp--;

                    implNode = list28;

                    }
                    break;
                case 6 :
                    // TRAL.g:125:3: file
                    {
                    pushFollow(FOLLOW_file_in_implType678);
                    file29=file();

                    state._fsp--;

                    implNode = file29;

                    }
                    break;
                case 7 :
                    // TRAL.g:126:3: db
                    {
                    pushFollow(FOLLOW_db_in_implType684);
                    db30=db();

                    state._fsp--;

                    implNode = db30;

                    }
                    break;
                case 8 :
                    // TRAL.g:127:3: enumeration
                    {
                    pushFollow(FOLLOW_enumeration_in_implType690);
                    enumeration31=enumeration();

                    state._fsp--;

                    implNode = enumeration31;

                    }
                    break;
                case 9 :
                    // TRAL.g:128:3: screen
                    {
                    pushFollow(FOLLOW_screen_in_implType697);
                    screen32=screen();

                    state._fsp--;

                    implNode = screen32;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "implType"


    // $ANTLR start "plType"
    // TRAL.g:130:1: plType returns [ImplNodePLType implNode] : 'PLTYPE' '[' Text ']' ;
    public final ImplNodePLType plType() throws RecognitionException {
        ImplNodePLType implNode = null;

        Token Text33=null;

        try {
            // TRAL.g:131:1: ( 'PLTYPE' '[' Text ']' )
            // TRAL.g:131:3: 'PLTYPE' '[' Text ']'
            {
            match(input,34,FOLLOW_34_in_plType712); 
            match(input,23,FOLLOW_23_in_plType714); 
            Text33=(Token)match(input,Text,FOLLOW_Text_in_plType716); 
            match(input,24,FOLLOW_24_in_plType718); 
            implNode =new ImplNodePLType((Text33!=null?Text33.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "plType"


    // $ANTLR start "pointer"
    // TRAL.g:133:1: pointer returns [ImplNodePointer implNode] : 'POINTER' '[' implType ']' ;
    public final ImplNodePointer pointer() throws RecognitionException {
        ImplNodePointer implNode = null;

        ImplNode implType34 = null;


        try {
            // TRAL.g:134:1: ( 'POINTER' '[' implType ']' )
            // TRAL.g:134:2: 'POINTER' '[' implType ']'
            {
            match(input,35,FOLLOW_35_in_pointer732); 
            match(input,23,FOLLOW_23_in_pointer734); 
            pushFollow(FOLLOW_implType_in_pointer736);
            implType34=implType();

            state._fsp--;

            match(input,24,FOLLOW_24_in_pointer738); 
            implNode =new ImplNodePointer(implType34);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "pointer"


    // $ANTLR start "structure"
    // TRAL.g:136:1: structure returns [ImplNodeStructure implNode] : 'STRUCTURE' '[' Text ',' f1= field ( ',' f2= field )* ']' ;
    public final ImplNodeStructure structure() throws RecognitionException {
        ImplNodeStructure implNode = null;

        Token Text35=null;
        ImplNodeField f1 = null;

        ImplNodeField f2 = null;



        	fields = new ArrayList<ImplNodeField>();

        try {
            // TRAL.g:140:1: ( 'STRUCTURE' '[' Text ',' f1= field ( ',' f2= field )* ']' )
            // TRAL.g:140:2: 'STRUCTURE' '[' Text ',' f1= field ( ',' f2= field )* ']'
            {
            match(input,36,FOLLOW_36_in_structure756); 
            match(input,23,FOLLOW_23_in_structure758); 
            Text35=(Token)match(input,Text,FOLLOW_Text_in_structure760); 
            match(input,12,FOLLOW_12_in_structure762); 
            pushFollow(FOLLOW_field_in_structure768);
            f1=field();

            state._fsp--;

            fields.add(f1);
            // TRAL.g:140:65: ( ',' f2= field )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==12) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // TRAL.g:140:66: ',' f2= field
            	    {
            	    match(input,12,FOLLOW_12_in_structure772); 
            	    pushFollow(FOLLOW_field_in_structure778);
            	    f2=field();

            	    state._fsp--;

            	    fields.add(f2);

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            match(input,24,FOLLOW_24_in_structure784); 
            implNode =new ImplNodeStructure((Text35!=null?Text35.getText():null),fields); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "structure"


    // $ANTLR start "field"
    // TRAL.g:142:1: field returns [ImplNodeField implNode] : id1= Text ':' t1= implType ;
    public final ImplNodeField field() throws RecognitionException {
        ImplNodeField implNode = null;

        Token id1=null;
        ImplNode t1 = null;


        try {
            // TRAL.g:143:1: (id1= Text ':' t1= implType )
            // TRAL.g:143:3: id1= Text ':' t1= implType
            {
            id1=(Token)match(input,Text,FOLLOW_Text_in_field802); 
            match(input,11,FOLLOW_11_in_field804); 
            pushFollow(FOLLOW_implType_in_field810);
            t1=implType();

            state._fsp--;

            implNode = new ImplNodeField((id1!=null?id1.getText():null), t1);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "field"


    // $ANTLR start "array"
    // TRAL.g:145:1: array returns [ImplNodeArray implNode] : 'ARRAY' '[' implType ',' NATURAL ( ',' l2= Text )* ']' ;
    public final ImplNodeArray array() throws RecognitionException {
        ImplNodeArray implNode = null;

        Token l2=null;
        Token NATURAL37=null;
        ImplNode implType36 = null;



        	fieldsList = new ArrayList<ImplNodeField>();
        	lastPosPointedID = null;

        try {
            // TRAL.g:150:1: ( 'ARRAY' '[' implType ',' NATURAL ( ',' l2= Text )* ']' )
            // TRAL.g:150:3: 'ARRAY' '[' implType ',' NATURAL ( ',' l2= Text )* ']'
            {
            match(input,37,FOLLOW_37_in_array828); 
            match(input,23,FOLLOW_23_in_array830); 
            pushFollow(FOLLOW_implType_in_array832);
            implType36=implType();

            state._fsp--;

            match(input,12,FOLLOW_12_in_array834); 
            NATURAL37=(Token)match(input,NATURAL,FOLLOW_NATURAL_in_array836); 
            // TRAL.g:150:36: ( ',' l2= Text )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==12) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // TRAL.g:150:37: ',' l2= Text
            	    {
            	    match(input,12,FOLLOW_12_in_array839); 
            	    l2=(Token)match(input,Text,FOLLOW_Text_in_array845); 
            	    lastPosPointedID = (l2!=null?l2.getText():null);

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

            match(input,24,FOLLOW_24_in_array852); 
            implNode =new ImplNodeArray(implType36, Integer.parseInt((NATURAL37!=null?NATURAL37.getText():null)),lastPosPointedID);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "array"


    // $ANTLR start "list"
    // TRAL.g:152:1: list returns [ImplNodeList implNode] : 'LIST' '[' id1= Text ',' linkType ',' l1= Text ',' (l2= Text ',' )* f1= field ( ',' f2= field )* ']' ;
    public final ImplNodeList list() throws RecognitionException {
        ImplNodeList implNode = null;

        Token id1=null;
        Token l1=null;
        Token l2=null;
        ImplNodeField f1 = null;

        ImplNodeField f2 = null;

        TRALParser.linkType_return linkType38 = null;



        	fieldsList = new ArrayList<ImplNodeField>();
        	linkPrevNode = "";

        try {
            // TRAL.g:157:1: ( 'LIST' '[' id1= Text ',' linkType ',' l1= Text ',' (l2= Text ',' )* f1= field ( ',' f2= field )* ']' )
            // TRAL.g:157:3: 'LIST' '[' id1= Text ',' linkType ',' l1= Text ',' (l2= Text ',' )* f1= field ( ',' f2= field )* ']'
            {
            match(input,38,FOLLOW_38_in_list871); 
            match(input,23,FOLLOW_23_in_list873); 
            id1=(Token)match(input,Text,FOLLOW_Text_in_list879); 
            match(input,12,FOLLOW_12_in_list881); 
            pushFollow(FOLLOW_linkType_in_list883);
            linkType38=linkType();

            state._fsp--;

            match(input,12,FOLLOW_12_in_list885); 
            l1=(Token)match(input,Text,FOLLOW_Text_in_list891); 
            match(input,12,FOLLOW_12_in_list893); 
            // TRAL.g:157:56: (l2= Text ',' )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==Text) ) {
                    int LA14_1 = input.LA(2);

                    if ( (LA14_1==12) ) {
                        alt14=1;
                    }


                }


                switch (alt14) {
            	case 1 :
            	    // TRAL.g:157:57: l2= Text ','
            	    {
            	    l2=(Token)match(input,Text,FOLLOW_Text_in_list900); 
            	    match(input,12,FOLLOW_12_in_list902); 
            	    linkPrevNode = (l2!=null?l2.getText():null);

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            pushFollow(FOLLOW_field_in_list912);
            f1=field();

            state._fsp--;

            fieldsList.add(f1);
            // TRAL.g:157:142: ( ',' f2= field )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==12) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // TRAL.g:157:143: ',' f2= field
            	    {
            	    match(input,12,FOLLOW_12_in_list916); 
            	    pushFollow(FOLLOW_field_in_list922);
            	    f2=field();

            	    state._fsp--;

            	    fieldsList.add(f2);

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            match(input,24,FOLLOW_24_in_list927); 
            implNode = new ImplNodeList((id1!=null?id1.getText():null),(linkType38!=null?input.toString(linkType38.start,linkType38.stop):null),(l1!=null?l1.getText():null),linkPrevNode,fieldsList,"");

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "list"

    public static class linkType_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "linkType"
    // TRAL.g:159:1: linkType : ( 'SLL' | 'DLL' | 'CLL' | 'DCLL' );
    public final TRALParser.linkType_return linkType() throws RecognitionException {
        TRALParser.linkType_return retval = new TRALParser.linkType_return();
        retval.start = input.LT(1);

        try {
            // TRAL.g:160:1: ( 'SLL' | 'DLL' | 'CLL' | 'DCLL' )
            // TRAL.g:
            {
            if ( (input.LA(1)>=39 && input.LA(1)<=42) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
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
        }
        return retval;
    }
    // $ANTLR end "linkType"


    // $ANTLR start "file"
    // TRAL.g:162:1: file returns [ImplNodeFile implNode] : 'FILE' '[' n1= Text ',' p1= Text ',' d1= Text ',' ( 'ENDOFLINE' '[' e1= Text ']' ',' )* ( 'ENDOFFILE' '[' e2= Text ']' ',' )* fileStructure ']' ;
    public final ImplNodeFile file() throws RecognitionException {
        ImplNodeFile implNode = null;

        Token n1=null;
        Token p1=null;
        Token d1=null;
        Token e1=null;
        Token e2=null;
        TRALParser.fileStructure_return fileStructure39 = null;


        try {
            // TRAL.g:163:1: ( 'FILE' '[' n1= Text ',' p1= Text ',' d1= Text ',' ( 'ENDOFLINE' '[' e1= Text ']' ',' )* ( 'ENDOFFILE' '[' e2= Text ']' ',' )* fileStructure ']' )
            // TRAL.g:163:3: 'FILE' '[' n1= Text ',' p1= Text ',' d1= Text ',' ( 'ENDOFLINE' '[' e1= Text ']' ',' )* ( 'ENDOFFILE' '[' e2= Text ']' ',' )* fileStructure ']'
            {
            match(input,43,FOLLOW_43_in_file957); 
            match(input,23,FOLLOW_23_in_file959); 
            n1=(Token)match(input,Text,FOLLOW_Text_in_file965); 
            match(input,12,FOLLOW_12_in_file967); 
            p1=(Token)match(input,Text,FOLLOW_Text_in_file973); 
            match(input,12,FOLLOW_12_in_file975); 
            d1=(Token)match(input,Text,FOLLOW_Text_in_file981); 
            match(input,12,FOLLOW_12_in_file983); 
            // TRAL.g:163:56: ( 'ENDOFLINE' '[' e1= Text ']' ',' )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==44) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // TRAL.g:163:57: 'ENDOFLINE' '[' e1= Text ']' ','
            	    {
            	    match(input,44,FOLLOW_44_in_file986); 
            	    match(input,23,FOLLOW_23_in_file988); 
            	    e1=(Token)match(input,Text,FOLLOW_Text_in_file994); 
            	    match(input,24,FOLLOW_24_in_file996); 
            	    match(input,12,FOLLOW_12_in_file997); 

            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            // TRAL.g:163:92: ( 'ENDOFFILE' '[' e2= Text ']' ',' )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==45) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // TRAL.g:163:93: 'ENDOFFILE' '[' e2= Text ']' ','
            	    {
            	    match(input,45,FOLLOW_45_in_file1002); 
            	    match(input,23,FOLLOW_23_in_file1004); 
            	    e2=(Token)match(input,Text,FOLLOW_Text_in_file1010); 
            	    match(input,24,FOLLOW_24_in_file1012); 
            	    match(input,12,FOLLOW_12_in_file1013); 

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            pushFollow(FOLLOW_fileStructure_in_file1018);
            fileStructure39=fileStructure();

            state._fsp--;

            match(input,24,FOLLOW_24_in_file1020); 
            implNode = new ImplNodeFile((n1!=null?n1.getText():null), (p1!=null?p1.getText():null),(d1!=null?d1.getText():null),"","",(fileStructure39!=null?input.toString(fileStructure39.start,fileStructure39.stop):null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "file"

    public static class fileStructure_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "fileStructure"
    // TRAL.g:165:1: fileStructure : ( 'LINEAR' | 'RPL' | 'FPL' );
    public final TRALParser.fileStructure_return fileStructure() throws RecognitionException {
        TRALParser.fileStructure_return retval = new TRALParser.fileStructure_return();
        retval.start = input.LT(1);

        try {
            // TRAL.g:166:1: ( 'LINEAR' | 'RPL' | 'FPL' )
            // TRAL.g:
            {
            if ( (input.LA(1)>=46 && input.LA(1)<=48) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
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
        }
        return retval;
    }
    // $ANTLR end "fileStructure"


    // $ANTLR start "db"
    // TRAL.g:168:1: db returns [ImplNodeDB implNode] : 'DB' '[' d1= Text ',' c1= Text ',' t1= Text ',' n1= nodeDBColumn ( ',' n2= nodeDBColumn ) ']' ;
    public final ImplNodeDB db() throws RecognitionException {
        ImplNodeDB implNode = null;

        Token d1=null;
        Token c1=null;
        Token t1=null;
        ImplNodeDBColumn n1 = null;

        ImplNodeDBColumn n2 = null;



        	columnsList = new ArrayList<ImplNodeDBColumn>();

        try {
            // TRAL.g:172:1: ( 'DB' '[' d1= Text ',' c1= Text ',' t1= Text ',' n1= nodeDBColumn ( ',' n2= nodeDBColumn ) ']' )
            // TRAL.g:172:3: 'DB' '[' d1= Text ',' c1= Text ',' t1= Text ',' n1= nodeDBColumn ( ',' n2= nodeDBColumn ) ']'
            {
            match(input,49,FOLLOW_49_in_db1052); 
            match(input,23,FOLLOW_23_in_db1054); 
            d1=(Token)match(input,Text,FOLLOW_Text_in_db1060); 
            match(input,12,FOLLOW_12_in_db1062); 
            c1=(Token)match(input,Text,FOLLOW_Text_in_db1068); 
            match(input,12,FOLLOW_12_in_db1070); 
            t1=(Token)match(input,Text,FOLLOW_Text_in_db1076); 
            match(input,12,FOLLOW_12_in_db1078); 
            pushFollow(FOLLOW_nodeDBColumn_in_db1084);
            n1=nodeDBColumn();

            state._fsp--;

            columnsList.add(n1);
            // TRAL.g:172:108: ( ',' n2= nodeDBColumn )
            // TRAL.g:172:109: ',' n2= nodeDBColumn
            {
            match(input,12,FOLLOW_12_in_db1088); 
            pushFollow(FOLLOW_nodeDBColumn_in_db1094);
            n2=nodeDBColumn();

            state._fsp--;

            columnsList.add(n2);

            }

            match(input,24,FOLLOW_24_in_db1099); 
            implNode = new ImplNodeDB((d1!=null?d1.getText():null), (c1!=null?c1.getText():null), (t1!=null?t1.getText():null), columnsList);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "db"


    // $ANTLR start "nodeDBColumn"
    // TRAL.g:174:1: nodeDBColumn returns [ImplNodeDBColumn columnObject] : columnName= Text ',' columnType= Text ;
    public final ImplNodeDBColumn nodeDBColumn() throws RecognitionException {
        ImplNodeDBColumn columnObject = null;

        Token columnName=null;
        Token columnType=null;

        try {
            // TRAL.g:175:1: (columnName= Text ',' columnType= Text )
            // TRAL.g:175:3: columnName= Text ',' columnType= Text
            {
            columnName=(Token)match(input,Text,FOLLOW_Text_in_nodeDBColumn1118); 
            match(input,12,FOLLOW_12_in_nodeDBColumn1120); 
            columnType=(Token)match(input,Text,FOLLOW_Text_in_nodeDBColumn1126); 
            columnObject = new ImplNodeDBColumn((columnName!=null?columnName.getText():null), (columnType!=null?columnType.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return columnObject;
    }
    // $ANTLR end "nodeDBColumn"


    // $ANTLR start "enumeration"
    // TRAL.g:177:1: enumeration returns [ImplNodeEnumeration implNode] : 'ENUMERATION' '[' implType ( ',' '\\\"' idImpl= ( Text | INTEGER | NATURAL ) '\\\"' '-->' idSpec= Text )+ ']' ;
    public final ImplNodeEnumeration enumeration() throws RecognitionException {
        ImplNodeEnumeration implNode = null;

        Token idImpl=null;
        Token idSpec=null;
        ImplNode implType40 = null;



        	enumElements = new HashMap<String,String>();

        try {
            // TRAL.g:181:1: ( 'ENUMERATION' '[' implType ( ',' '\\\"' idImpl= ( Text | INTEGER | NATURAL ) '\\\"' '-->' idSpec= Text )+ ']' )
            // TRAL.g:181:3: 'ENUMERATION' '[' implType ( ',' '\\\"' idImpl= ( Text | INTEGER | NATURAL ) '\\\"' '-->' idSpec= Text )+ ']'
            {
            match(input,50,FOLLOW_50_in_enumeration1145); 
            match(input,23,FOLLOW_23_in_enumeration1147); 
            pushFollow(FOLLOW_implType_in_enumeration1149);
            implType40=implType();

            state._fsp--;

            // TRAL.g:181:30: ( ',' '\\\"' idImpl= ( Text | INTEGER | NATURAL ) '\\\"' '-->' idSpec= Text )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==12) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // TRAL.g:181:31: ',' '\\\"' idImpl= ( Text | INTEGER | NATURAL ) '\\\"' '-->' idSpec= Text
            	    {
            	    match(input,12,FOLLOW_12_in_enumeration1152); 
            	    match(input,51,FOLLOW_51_in_enumeration1154); 
            	    idImpl=(Token)input.LT(1);
            	    if ( input.LA(1)==Text||(input.LA(1)>=NATURAL && input.LA(1)<=INTEGER) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    match(input,51,FOLLOW_51_in_enumeration1174); 
            	    match(input,52,FOLLOW_52_in_enumeration1176); 
            	    idSpec=(Token)match(input,Text,FOLLOW_Text_in_enumeration1182); 

            	    enumElements.put((idImpl!=null?idImpl.getText():null),(idSpec!=null?idSpec.getText():null));

            	    }
            	    break;

            	default :
            	    if ( cnt18 >= 1 ) break loop18;
                        EarlyExitException eee =
                            new EarlyExitException(18, input);
                        throw eee;
                }
                cnt18++;
            } while (true);

            match(input,24,FOLLOW_24_in_enumeration1188); 
            implNode = new ImplNodeEnumeration(implType40,enumElements);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "enumeration"


    // $ANTLR start "screen"
    // TRAL.g:186:1: screen returns [ImplNodeScreen implNode] : ( screenPlane | screenTable );
    public final ImplNodeScreen screen() throws RecognitionException {
        ImplNodeScreen implNode = null;

        ImplNodeScreen screenPlane41 = null;

        ImplNodeScreenTable screenTable42 = null;


        try {
            // TRAL.g:187:1: ( screenPlane | screenTable )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==53) ) {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==23) ) {
                    int LA19_2 = input.LA(3);

                    if ( (LA19_2==54) ) {
                        alt19=1;
                    }
                    else if ( (LA19_2==55) ) {
                        alt19=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // TRAL.g:187:3: screenPlane
                    {
                    pushFollow(FOLLOW_screenPlane_in_screen1204);
                    screenPlane41=screenPlane();

                    state._fsp--;

                    implNode = screenPlane41;

                    }
                    break;
                case 2 :
                    // TRAL.g:188:3: screenTable
                    {
                    pushFollow(FOLLOW_screenTable_in_screen1210);
                    screenTable42=screenTable();

                    state._fsp--;

                    implNode = screenTable42;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "screen"


    // $ANTLR start "screenPlane"
    // TRAL.g:190:1: screenPlane returns [ImplNodeScreen implNode] : 'SCREEN' '[' 'plane' ( ',' enumeration )* ']' ;
    public final ImplNodeScreen screenPlane() throws RecognitionException {
        ImplNodeScreen implNode = null;

        ImplNodeEnumeration enumeration43 = null;


        try {
            // TRAL.g:191:1: ( 'SCREEN' '[' 'plane' ( ',' enumeration )* ']' )
            // TRAL.g:191:3: 'SCREEN' '[' 'plane' ( ',' enumeration )* ']'
            {
            match(input,53,FOLLOW_53_in_screenPlane1224); 
            match(input,23,FOLLOW_23_in_screenPlane1226); 
            match(input,54,FOLLOW_54_in_screenPlane1228); 
            // TRAL.g:191:24: ( ',' enumeration )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==12) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // TRAL.g:191:25: ',' enumeration
            	    {
            	    match(input,12,FOLLOW_12_in_screenPlane1231); 
            	    pushFollow(FOLLOW_enumeration_in_screenPlane1233);
            	    enumeration43=enumeration();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            match(input,24,FOLLOW_24_in_screenPlane1238); 
            implNode =new ImplNodeScreenPlane(); 
            implNode.setEnumeration(enumeration43);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "screenPlane"


    // $ANTLR start "screenTable"
    // TRAL.g:194:1: screenTable returns [ImplNodeScreenTable implNode] : 'SCREEN' '[' 'table' ',' delimiter= Text ( ',' 'COLRANGE' '[' columnRange= Range ']' )* ( ',' 'ROWRANGE' '[' rowRange= Range ']' )* ']' ;
    public final ImplNodeScreenTable screenTable() throws RecognitionException {
        ImplNodeScreenTable implNode = null;

        Token delimiter=null;
        Token columnRange=null;
        Token rowRange=null;

        try {
            // TRAL.g:195:1: ( 'SCREEN' '[' 'table' ',' delimiter= Text ( ',' 'COLRANGE' '[' columnRange= Range ']' )* ( ',' 'ROWRANGE' '[' rowRange= Range ']' )* ']' )
            // TRAL.g:195:3: 'SCREEN' '[' 'table' ',' delimiter= Text ( ',' 'COLRANGE' '[' columnRange= Range ']' )* ( ',' 'ROWRANGE' '[' rowRange= Range ']' )* ']'
            {
            match(input,53,FOLLOW_53_in_screenTable1253); 
            match(input,23,FOLLOW_23_in_screenTable1255); 
            match(input,55,FOLLOW_55_in_screenTable1257); 
            match(input,12,FOLLOW_12_in_screenTable1259); 
            delimiter=(Token)match(input,Text,FOLLOW_Text_in_screenTable1265); 
            // TRAL.g:195:45: ( ',' 'COLRANGE' '[' columnRange= Range ']' )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==12) ) {
                    int LA21_1 = input.LA(2);

                    if ( (LA21_1==56) ) {
                        alt21=1;
                    }


                }


                switch (alt21) {
            	case 1 :
            	    // TRAL.g:195:46: ',' 'COLRANGE' '[' columnRange= Range ']'
            	    {
            	    match(input,12,FOLLOW_12_in_screenTable1268); 
            	    match(input,56,FOLLOW_56_in_screenTable1270); 
            	    match(input,23,FOLLOW_23_in_screenTable1272); 
            	    columnRange=(Token)match(input,Range,FOLLOW_Range_in_screenTable1277); 
            	    match(input,24,FOLLOW_24_in_screenTable1279); 

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

            // TRAL.g:195:90: ( ',' 'ROWRANGE' '[' rowRange= Range ']' )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==12) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // TRAL.g:195:91: ',' 'ROWRANGE' '[' rowRange= Range ']'
            	    {
            	    match(input,12,FOLLOW_12_in_screenTable1284); 
            	    match(input,57,FOLLOW_57_in_screenTable1286); 
            	    match(input,23,FOLLOW_23_in_screenTable1288); 
            	    rowRange=(Token)match(input,Range,FOLLOW_Range_in_screenTable1293); 
            	    match(input,24,FOLLOW_24_in_screenTable1295); 

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            match(input,24,FOLLOW_24_in_screenTable1299); 
            implNode =new ImplNodeScreenTable();
            	String strColRange = (columnRange!=null?columnRange.getText():null);
            	String strRanRange = (rowRange!=null?rowRange.getText():null);
            	implNode.setDelimiter((delimiter!=null?delimiter.getText():null));
            	if(strColRange!=null && strColRange.matches("\\d+\\.\\.\\d+")){
            		String[] parts = strColRange.split("\\.\\.");
            		Integer cLB = new Integer(parts[0]);
            		int columnLB = cLB.intValue();
            		Integer cUB = new Integer(parts[1]);
            		int columnUB = cUB.intValue();
            		implNode.setRowLowerBound(columnLB);
            		implNode.setRowUpperBound(columnUB);
            	}
            	if(strRanRange!=null &&  strRanRange.matches("\\d+\\.\\.\\d+")){
            		String[] parts = strRanRange.split("\\.\\.");
            		Integer rLB = new Integer(parts[0]);
            		int rowLB = rLB.intValue();
            		Integer rUB = new Integer(parts[1]);
            		int rowUB = rUB.intValue();
            		implNode.setColumnLowerBound(rowLB);
            		implNode.setColumnUpperBound(rowUB);
            	}



            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return implNode;
    }
    // $ANTLR end "screenTable"

    // Delegated rules


 

    public static final BitSet FOLLOW_Text_in_abstractionLaw36 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_abstractionLaw38 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_rules_in_abstractionLaw43 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_10_in_rules60 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_rules62 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule_in_rules68 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_rules72 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_Text_in_rule88 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_rule90 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleAbstraction_in_rule92 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Text_in_ruleAbstraction113 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_ruleAbstraction114 = new BitSet(new long[]{0x0026087C00000000L});
    public static final BitSet FOLLOW_implType_in_ruleAbstraction119 = new BitSet(new long[]{0x0000000000003000L});
    public static final BitSet FOLLOW_12_in_ruleAbstraction123 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_ruleAbstraction129 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_ruleAbstraction130 = new BitSet(new long[]{0x0026087C00000000L});
    public static final BitSet FOLLOW_implType_in_ruleAbstraction135 = new BitSet(new long[]{0x0000000000003000L});
    public static final BitSet FOLLOW_13_in_ruleAbstraction141 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_ruleAbstraction147 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_ruleAbstraction148 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_ruleAbstraction153 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_12_in_ruleAbstraction157 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_ruleAbstraction163 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_ruleAbstraction164 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_ruleAbstraction169 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_12_in_ruleAbstraction176 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_ruleAbstraction178 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_composition_in_ruleAbstraction180 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_12_in_ruleAbstraction187 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_ruleAbstraction189 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_decomposition_in_ruleAbstraction191 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_12_in_ruleAbstraction196 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_ruleAbstraction198 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_captureCode_in_ruleAbstraction200 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_14_in_composition221 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_composition227 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_composition229 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_composition235 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_composition237 = new BitSet(new long[]{0x00000000000E0000L});
    public static final BitSet FOLLOW_preOperator_in_composition239 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_composition245 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_set_in_preOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_decomposition273 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_decomposition275 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_15_in_decomposition280 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_decomposition282 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_decomposition284 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_decomposition286 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_decomposition288 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_21_in_captureCode302 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NEWLINE_in_captureCode304 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_captureCode308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_basicType_in_specType322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_freeType_in_specType328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pFunction_in_specType334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tFunction_in_specType340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_relation_in_specType346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sequence_in_specType352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_powerSet_in_specType358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cartesianProd_in_specType364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_integer_in_specType370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nat_in_specType376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_basicType392 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_basicType394 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_basicType396 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_basicType398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_freeType412 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_freeType414 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_freeType416 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_freeType418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_pFunction432 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_pFunction434 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_pFunction440 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_pFunction442 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_pFunction448 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_pFunction450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_tFunction464 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_tFunction466 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_tFunction472 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_tFunction474 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_tFunction480 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_tFunction482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_relation496 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_relation498 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_relation504 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_relation506 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_relation512 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_relation514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_cartesianProd532 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_cartesianProd534 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_cartesianProd540 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_cartesianProd544 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_cartesianProd550 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_12_in_cartesianProd555 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_cartesianProd561 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_24_in_cartesianProd566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_sequence580 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_sequence582 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_sequence584 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_sequence586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_powerSet600 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_powerSet602 = new BitSet(new long[]{0x00000003FE400000L});
    public static final BitSet FOLLOW_specType_in_powerSet604 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_powerSet606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_integer620 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_nat634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_plType_in_implType648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pointer_in_implType654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_structure_in_implType660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_array_in_implType666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_list_in_implType672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_file_in_implType678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_db_in_implType684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumeration_in_implType690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_screen_in_implType697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_plType712 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_plType714 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_plType716 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_plType718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_pointer732 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_pointer734 = new BitSet(new long[]{0x0026087C00000000L});
    public static final BitSet FOLLOW_implType_in_pointer736 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_pointer738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_structure756 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_structure758 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_structure760 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_structure762 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_field_in_structure768 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_12_in_structure772 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_field_in_structure778 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_24_in_structure784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Text_in_field802 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_field804 = new BitSet(new long[]{0x0026087C00000000L});
    public static final BitSet FOLLOW_implType_in_field810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_array828 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_array830 = new BitSet(new long[]{0x0026087C00000000L});
    public static final BitSet FOLLOW_implType_in_array832 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_array834 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_NATURAL_in_array836 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_12_in_array839 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_array845 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_24_in_array852 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_list871 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_list873 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_list879 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_list881 = new BitSet(new long[]{0x0000078000000000L});
    public static final BitSet FOLLOW_linkType_in_list883 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_list885 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_list891 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_list893 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_list900 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_list902 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_field_in_list912 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_12_in_list916 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_field_in_list922 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_24_in_list927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_linkType0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_file957 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_file959 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_file965 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_file967 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_file973 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_file975 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_file981 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_file983 = new BitSet(new long[]{0x0001F00000000000L});
    public static final BitSet FOLLOW_44_in_file986 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_file988 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_file994 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_file996 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_file997 = new BitSet(new long[]{0x0001F00000000000L});
    public static final BitSet FOLLOW_45_in_file1002 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_file1004 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_file1010 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_file1012 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_file1013 = new BitSet(new long[]{0x0001F00000000000L});
    public static final BitSet FOLLOW_fileStructure_in_file1018 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_file1020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_fileStructure0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_db1052 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_db1054 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_db1060 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_db1062 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_db1068 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_db1070 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_db1076 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_db1078 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_nodeDBColumn_in_db1084 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_db1088 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_nodeDBColumn_in_db1094 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_db1099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Text_in_nodeDBColumn1118 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_nodeDBColumn1120 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_nodeDBColumn1126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_enumeration1145 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_enumeration1147 = new BitSet(new long[]{0x0026087C00000000L});
    public static final BitSet FOLLOW_implType_in_enumeration1149 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_enumeration1152 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_enumeration1154 = new BitSet(new long[]{0x00000000000000D0L});
    public static final BitSet FOLLOW_set_in_enumeration1160 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_enumeration1174 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_enumeration1176 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_enumeration1182 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_24_in_enumeration1188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_screenPlane_in_screen1204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_screenTable_in_screen1210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_screenPlane1224 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_screenPlane1226 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_screenPlane1228 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_12_in_screenPlane1231 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_enumeration_in_screenPlane1233 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_24_in_screenPlane1238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_screenTable1253 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_screenTable1255 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_55_in_screenTable1257 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_screenTable1259 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Text_in_screenTable1265 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_12_in_screenTable1268 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_screenTable1270 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_screenTable1272 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_Range_in_screenTable1277 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_screenTable1279 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_12_in_screenTable1284 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_screenTable1286 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_screenTable1288 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_Range_in_screenTable1293 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_screenTable1295 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_24_in_screenTable1299 = new BitSet(new long[]{0x0000000000000002L});

}