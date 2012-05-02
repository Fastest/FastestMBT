package compserver.abstraction.parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TRALLexer extends Lexer {
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
    public static final int T__19=19;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__16=16;
    public static final int T__51=51;
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
    public static final int T__31=31;
    public static final int Range=8;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int WS=9;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int NEWLINE=5;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int Text=4;
    public static final int NATURAL=6;

    // delegates
    // delegators

    public TRALLexer() {;} 
    public TRALLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public TRALLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "TRAL.g"; }

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:7:7: ( '@RULES' )
            // TRAL.g:7:9: '@RULES'
            {
            match("@RULES"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:8:7: ( ':' )
            // TRAL.g:8:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:9:7: ( ',' )
            // TRAL.g:9:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:10:7: ( '==>' )
            // TRAL.g:10:9: '==>'
            {
            match("==>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:11:7: ( 'COMPOSITION' )
            // TRAL.g:11:9: 'COMPOSITION'
            {
            match("COMPOSITION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:12:7: ( '@SET' )
            // TRAL.g:12:9: '@SET'
            {
            match("@SET"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:13:7: ( 'AS' )
            // TRAL.g:13:9: 'AS'
            {
            match("AS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:14:7: ( 'dom' )
            // TRAL.g:14:9: 'dom'
            {
            match("dom"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:15:7: ( 'ran' )
            // TRAL.g:15:9: 'ran'
            {
            match("ran"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:16:7: ( 'cardinal' )
            // TRAL.g:16:9: 'cardinal'
            {
            match("cardinal"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:17:7: ( 'DECOMPOSITION' )
            // TRAL.g:17:9: 'DECOMPOSITION'
            {
            match("DECOMPOSITION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:18:7: ( '@CAPTURECODE' )
            // TRAL.g:18:9: '@CAPTURECODE'
            {
            match("@CAPTURECODE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:19:7: ( 'BASICTYPE' )
            // TRAL.g:19:9: 'BASICTYPE'
            {
            match("BASICTYPE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:20:7: ( '[' )
            // TRAL.g:20:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:21:7: ( ']' )
            // TRAL.g:21:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:22:7: ( 'FREETYPE' )
            // TRAL.g:22:9: 'FREETYPE'
            {
            match("FREETYPE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:23:7: ( 'PFUN' )
            // TRAL.g:23:9: 'PFUN'
            {
            match("PFUN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:24:7: ( 'FUN' )
            // TRAL.g:24:9: 'FUN'
            {
            match("FUN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:25:7: ( 'REL' )
            // TRAL.g:25:9: 'REL'
            {
            match("REL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:26:7: ( 'PROD' )
            // TRAL.g:26:9: 'PROD'
            {
            match("PROD"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:27:7: ( 'SEQUENCE' )
            // TRAL.g:27:9: 'SEQUENCE'
            {
            match("SEQUENCE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:28:7: ( 'POWER' )
            // TRAL.g:28:9: 'POWER'
            {
            match("POWER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:29:7: ( 'INT' )
            // TRAL.g:29:9: 'INT'
            {
            match("INT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:30:7: ( 'NAT' )
            // TRAL.g:30:9: 'NAT'
            {
            match("NAT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:31:7: ( 'PLTYPE' )
            // TRAL.g:31:9: 'PLTYPE'
            {
            match("PLTYPE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:32:7: ( 'POINTER' )
            // TRAL.g:32:9: 'POINTER'
            {
            match("POINTER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:33:7: ( 'STRUCTURE' )
            // TRAL.g:33:9: 'STRUCTURE'
            {
            match("STRUCTURE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:34:7: ( 'ARRAY' )
            // TRAL.g:34:9: 'ARRAY'
            {
            match("ARRAY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:35:7: ( 'LIST' )
            // TRAL.g:35:9: 'LIST'
            {
            match("LIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:36:7: ( 'SLL' )
            // TRAL.g:36:9: 'SLL'
            {
            match("SLL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:37:7: ( 'DLL' )
            // TRAL.g:37:9: 'DLL'
            {
            match("DLL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:38:7: ( 'CLL' )
            // TRAL.g:38:9: 'CLL'
            {
            match("CLL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:39:7: ( 'DCLL' )
            // TRAL.g:39:9: 'DCLL'
            {
            match("DCLL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:40:7: ( 'FILE' )
            // TRAL.g:40:9: 'FILE'
            {
            match("FILE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:41:7: ( 'ENDOFLINE' )
            // TRAL.g:41:9: 'ENDOFLINE'
            {
            match("ENDOFLINE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:42:7: ( 'ENDOFFILE' )
            // TRAL.g:42:9: 'ENDOFFILE'
            {
            match("ENDOFFILE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:43:7: ( 'LINEAR' )
            // TRAL.g:43:9: 'LINEAR'
            {
            match("LINEAR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:44:7: ( 'RPL' )
            // TRAL.g:44:9: 'RPL'
            {
            match("RPL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:45:7: ( 'FPL' )
            // TRAL.g:45:9: 'FPL'
            {
            match("FPL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:46:7: ( 'DB' )
            // TRAL.g:46:9: 'DB'
            {
            match("DB"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:47:7: ( 'ENUMERATION' )
            // TRAL.g:47:9: 'ENUMERATION'
            {
            match("ENUMERATION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:48:7: ( '\\\"' )
            // TRAL.g:48:9: '\\\"'
            {
            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:49:7: ( '-->' )
            // TRAL.g:49:9: '-->'
            {
            match("-->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:50:7: ( 'SCREEN' )
            // TRAL.g:50:9: 'SCREEN'
            {
            match("SCREEN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:51:7: ( 'plane' )
            // TRAL.g:51:9: 'plane'
            {
            match("plane"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:52:7: ( 'table' )
            // TRAL.g:52:9: 'table'
            {
            match("table"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:53:7: ( 'COLRANGE' )
            // TRAL.g:53:9: 'COLRANGE'
            {
            match("COLRANGE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:54:7: ( 'ROWRANGE' )
            // TRAL.g:54:9: 'ROWRANGE'
            {
            match("ROWRANGE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "Text"
    public final void mText() throws RecognitionException {
        try {
            int _type = Text;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:220:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '\\\\' | '\\.' | '\\/' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '\\\\' | '\\.' | '\\?' | '\\/' | '\\'' | '\\!' )* )
            // TRAL.g:220:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '\\\\' | '\\.' | '\\/' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '\\\\' | '\\.' | '\\?' | '\\/' | '\\'' | '\\!' )*
            {
            if ( (input.LA(1)>='.' && input.LA(1)<='/')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='\\'||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // TRAL.g:220:53: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '\\\\' | '\\.' | '\\?' | '\\/' | '\\'' | '\\!' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='!'||LA1_0=='\''||(LA1_0>='.' && LA1_0<='9')||LA1_0=='?'||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='\\'||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // TRAL.g:
            	    {
            	    if ( input.LA(1)=='!'||input.LA(1)=='\''||(input.LA(1)>='.' && input.LA(1)<='9')||input.LA(1)=='?'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='\\'||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Text"

    // $ANTLR start "Range"
    public final void mRange() throws RecognitionException {
        try {
            int _type = Range;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:223:6: ( ( '0' .. '9' )+ '..' ( '0' .. '9' )+ )
            // TRAL.g:223:8: ( '0' .. '9' )+ '..' ( '0' .. '9' )+
            {
            // TRAL.g:223:8: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // TRAL.g:223:8: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);

            match(".."); 

            // TRAL.g:223:23: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // TRAL.g:223:23: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Range"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:224:8: ( ( '\\r' | '\\n' )+ )
            // TRAL.g:224:9: ( '\\r' | '\\n' )+
            {
            // TRAL.g:224:9: ( '\\r' | '\\n' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='\n'||LA4_0=='\r') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // TRAL.g:
            	    {
            	    if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:225:9: ( '-' ( '0' .. '9' )+ )
            // TRAL.g:225:13: '-' ( '0' .. '9' )+
            {
            match('-'); 
            // TRAL.g:225:16: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // TRAL.g:225:16: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "NATURAL"
    public final void mNATURAL() throws RecognitionException {
        try {
            int _type = NATURAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:226:9: ( ( '0' .. '9' )+ )
            // TRAL.g:226:11: ( '0' .. '9' )+
            {
            // TRAL.g:226:11: ( '0' .. '9' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // TRAL.g:226:11: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NATURAL"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // TRAL.g:227:4: ( ( ' ' | '\\t' )+ )
            // TRAL.g:227:6: ( ' ' | '\\t' )+
            {
            // TRAL.g:227:6: ( ' ' | '\\t' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\t'||LA7_0==' ') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // TRAL.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


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

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // TRAL.g:1:8: ( T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | Text | Range | NEWLINE | INTEGER | NATURAL | WS )
        int alt8=54;
        alt8 = dfa8.predict(input);
        switch (alt8) {
            case 1 :
                // TRAL.g:1:10: T__10
                {
                mT__10(); 

                }
                break;
            case 2 :
                // TRAL.g:1:16: T__11
                {
                mT__11(); 

                }
                break;
            case 3 :
                // TRAL.g:1:22: T__12
                {
                mT__12(); 

                }
                break;
            case 4 :
                // TRAL.g:1:28: T__13
                {
                mT__13(); 

                }
                break;
            case 5 :
                // TRAL.g:1:34: T__14
                {
                mT__14(); 

                }
                break;
            case 6 :
                // TRAL.g:1:40: T__15
                {
                mT__15(); 

                }
                break;
            case 7 :
                // TRAL.g:1:46: T__16
                {
                mT__16(); 

                }
                break;
            case 8 :
                // TRAL.g:1:52: T__17
                {
                mT__17(); 

                }
                break;
            case 9 :
                // TRAL.g:1:58: T__18
                {
                mT__18(); 

                }
                break;
            case 10 :
                // TRAL.g:1:64: T__19
                {
                mT__19(); 

                }
                break;
            case 11 :
                // TRAL.g:1:70: T__20
                {
                mT__20(); 

                }
                break;
            case 12 :
                // TRAL.g:1:76: T__21
                {
                mT__21(); 

                }
                break;
            case 13 :
                // TRAL.g:1:82: T__22
                {
                mT__22(); 

                }
                break;
            case 14 :
                // TRAL.g:1:88: T__23
                {
                mT__23(); 

                }
                break;
            case 15 :
                // TRAL.g:1:94: T__24
                {
                mT__24(); 

                }
                break;
            case 16 :
                // TRAL.g:1:100: T__25
                {
                mT__25(); 

                }
                break;
            case 17 :
                // TRAL.g:1:106: T__26
                {
                mT__26(); 

                }
                break;
            case 18 :
                // TRAL.g:1:112: T__27
                {
                mT__27(); 

                }
                break;
            case 19 :
                // TRAL.g:1:118: T__28
                {
                mT__28(); 

                }
                break;
            case 20 :
                // TRAL.g:1:124: T__29
                {
                mT__29(); 

                }
                break;
            case 21 :
                // TRAL.g:1:130: T__30
                {
                mT__30(); 

                }
                break;
            case 22 :
                // TRAL.g:1:136: T__31
                {
                mT__31(); 

                }
                break;
            case 23 :
                // TRAL.g:1:142: T__32
                {
                mT__32(); 

                }
                break;
            case 24 :
                // TRAL.g:1:148: T__33
                {
                mT__33(); 

                }
                break;
            case 25 :
                // TRAL.g:1:154: T__34
                {
                mT__34(); 

                }
                break;
            case 26 :
                // TRAL.g:1:160: T__35
                {
                mT__35(); 

                }
                break;
            case 27 :
                // TRAL.g:1:166: T__36
                {
                mT__36(); 

                }
                break;
            case 28 :
                // TRAL.g:1:172: T__37
                {
                mT__37(); 

                }
                break;
            case 29 :
                // TRAL.g:1:178: T__38
                {
                mT__38(); 

                }
                break;
            case 30 :
                // TRAL.g:1:184: T__39
                {
                mT__39(); 

                }
                break;
            case 31 :
                // TRAL.g:1:190: T__40
                {
                mT__40(); 

                }
                break;
            case 32 :
                // TRAL.g:1:196: T__41
                {
                mT__41(); 

                }
                break;
            case 33 :
                // TRAL.g:1:202: T__42
                {
                mT__42(); 

                }
                break;
            case 34 :
                // TRAL.g:1:208: T__43
                {
                mT__43(); 

                }
                break;
            case 35 :
                // TRAL.g:1:214: T__44
                {
                mT__44(); 

                }
                break;
            case 36 :
                // TRAL.g:1:220: T__45
                {
                mT__45(); 

                }
                break;
            case 37 :
                // TRAL.g:1:226: T__46
                {
                mT__46(); 

                }
                break;
            case 38 :
                // TRAL.g:1:232: T__47
                {
                mT__47(); 

                }
                break;
            case 39 :
                // TRAL.g:1:238: T__48
                {
                mT__48(); 

                }
                break;
            case 40 :
                // TRAL.g:1:244: T__49
                {
                mT__49(); 

                }
                break;
            case 41 :
                // TRAL.g:1:250: T__50
                {
                mT__50(); 

                }
                break;
            case 42 :
                // TRAL.g:1:256: T__51
                {
                mT__51(); 

                }
                break;
            case 43 :
                // TRAL.g:1:262: T__52
                {
                mT__52(); 

                }
                break;
            case 44 :
                // TRAL.g:1:268: T__53
                {
                mT__53(); 

                }
                break;
            case 45 :
                // TRAL.g:1:274: T__54
                {
                mT__54(); 

                }
                break;
            case 46 :
                // TRAL.g:1:280: T__55
                {
                mT__55(); 

                }
                break;
            case 47 :
                // TRAL.g:1:286: T__56
                {
                mT__56(); 

                }
                break;
            case 48 :
                // TRAL.g:1:292: T__57
                {
                mT__57(); 

                }
                break;
            case 49 :
                // TRAL.g:1:298: Text
                {
                mText(); 

                }
                break;
            case 50 :
                // TRAL.g:1:303: Range
                {
                mRange(); 

                }
                break;
            case 51 :
                // TRAL.g:1:309: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 52 :
                // TRAL.g:1:317: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 53 :
                // TRAL.g:1:325: NATURAL
                {
                mNATURAL(); 

                }
                break;
            case 54 :
                // TRAL.g:1:333: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA8_eotS =
        "\5\uffff\7\32\2\uffff\10\32\2\uffff\2\32\1\uffff\1\104\5\uffff\2"+
        "\32\1\111\7\32\1\121\24\32\2\uffff\2\32\2\uffff\2\32\1\155\1\uffff"+
        "\1\32\1\157\1\160\2\32\1\163\1\32\1\uffff\2\32\1\167\1\32\1\171"+
        "\5\32\1\177\1\u0080\3\32\1\u0084\1\32\1\u0086\1\u0087\10\32\1\uffff"+
        "\1\32\2\uffff\2\32\1\uffff\1\u0093\2\32\1\uffff\1\u0096\1\uffff"+
        "\1\u0097\1\u0098\3\32\2\uffff\3\32\1\uffff\1\32\2\uffff\1\u00a0"+
        "\7\32\1\u00a8\2\32\1\uffff\2\32\3\uffff\1\u00ad\6\32\1\uffff\3\32"+
        "\1\u00b8\1\u00b9\2\32\1\uffff\4\32\1\uffff\1\32\1\u00c1\3\32\1\u00c5"+
        "\1\u00c6\3\32\2\uffff\6\32\1\u00d0\1\uffff\3\32\2\uffff\4\32\1\u00d8"+
        "\1\u00d9\2\32\1\u00dc\1\uffff\1\u00dd\1\u00de\5\32\2\uffff\1\32"+
        "\1\u00e5\3\uffff\1\u00e6\1\u00e7\1\u00e8\3\32\4\uffff\1\32\1\u00ed"+
        "\1\32\1\u00ef\1\uffff\1\32\1\uffff\1\u00f1\1\uffff";
    static final String DFA8_eofS =
        "\u00f2\uffff";
    static final String DFA8_minS =
        "\1\11\1\103\3\uffff\1\114\1\122\1\157\2\141\1\102\1\101\2\uffff"+
        "\1\111\1\106\1\105\1\103\1\116\1\101\1\111\1\116\1\uffff\1\55\1"+
        "\154\1\141\1\uffff\1\56\5\uffff\2\114\1\41\1\122\1\155\1\156\1\162"+
        "\1\103\2\114\1\41\1\123\1\105\1\116\2\114\1\125\1\117\1\111\1\124"+
        "\2\114\1\127\1\121\1\122\1\114\1\122\2\124\1\116\1\104\2\uffff\1"+
        "\141\1\142\2\uffff\1\120\1\122\1\41\1\uffff\1\101\2\41\1\144\1\117"+
        "\1\41\1\114\1\uffff\1\111\1\105\1\41\1\105\1\41\1\116\1\104\1\105"+
        "\1\116\1\131\2\41\1\122\2\125\1\41\1\105\2\41\1\124\1\105\1\117"+
        "\1\115\1\156\1\154\1\117\1\101\1\uffff\1\131\2\uffff\1\151\1\115"+
        "\1\uffff\1\41\1\103\1\124\1\uffff\1\41\1\uffff\2\41\1\122\1\124"+
        "\1\120\2\uffff\1\101\1\105\1\103\1\uffff\1\105\2\uffff\1\41\1\101"+
        "\1\106\1\105\2\145\1\123\1\116\1\41\1\156\1\120\1\uffff\1\124\1"+
        "\131\3\uffff\1\41\2\105\2\116\1\124\1\116\1\uffff\1\122\1\106\1"+
        "\122\2\41\1\111\1\107\1\uffff\1\141\1\117\1\131\1\120\1\uffff\1"+
        "\122\1\41\1\107\1\103\1\125\2\41\2\111\1\101\2\uffff\1\124\1\105"+
        "\1\154\1\123\1\120\1\105\1\41\1\uffff\2\105\1\122\2\uffff\1\116"+
        "\1\114\1\124\1\111\2\41\1\111\1\105\1\41\1\uffff\2\41\3\105\1\111"+
        "\1\117\2\uffff\1\124\1\41\3\uffff\3\41\1\117\1\116\1\111\4\uffff"+
        "\1\116\1\41\1\117\1\41\1\uffff\1\116\1\uffff\1\41\1\uffff";
    static final String DFA8_maxS =
        "\1\172\1\123\3\uffff\1\117\1\123\1\157\2\141\1\114\1\101\2\uffff"+
        "\1\125\1\122\1\120\1\124\1\116\1\101\1\111\1\116\1\uffff\1\71\1"+
        "\154\1\141\1\uffff\1\71\5\uffff\1\115\1\114\1\172\1\122\1\155\1"+
        "\156\1\162\1\103\2\114\1\172\1\123\1\105\1\116\2\114\1\125\1\117"+
        "\1\127\1\124\2\114\1\127\1\121\1\122\1\114\1\122\2\124\1\123\1\125"+
        "\2\uffff\1\141\1\142\2\uffff\1\120\1\122\1\172\1\uffff\1\101\2\172"+
        "\1\144\1\117\1\172\1\114\1\uffff\1\111\1\105\1\172\1\105\1\172\1"+
        "\116\1\104\1\105\1\116\1\131\2\172\1\122\2\125\1\172\1\105\2\172"+
        "\1\124\1\105\1\117\1\115\1\156\1\154\1\117\1\101\1\uffff\1\131\2"+
        "\uffff\1\151\1\115\1\uffff\1\172\1\103\1\124\1\uffff\1\172\1\uffff"+
        "\2\172\1\122\1\124\1\120\2\uffff\1\101\1\105\1\103\1\uffff\1\105"+
        "\2\uffff\1\172\1\101\1\106\1\105\2\145\1\123\1\116\1\172\1\156\1"+
        "\120\1\uffff\1\124\1\131\3\uffff\1\172\2\105\2\116\1\124\1\116\1"+
        "\uffff\1\122\1\114\1\122\2\172\1\111\1\107\1\uffff\1\141\1\117\1"+
        "\131\1\120\1\uffff\1\122\1\172\1\107\1\103\1\125\2\172\2\111\1\101"+
        "\2\uffff\1\124\1\105\1\154\1\123\1\120\1\105\1\172\1\uffff\2\105"+
        "\1\122\2\uffff\1\116\1\114\1\124\1\111\2\172\1\111\1\105\1\172\1"+
        "\uffff\2\172\3\105\1\111\1\117\2\uffff\1\124\1\172\3\uffff\3\172"+
        "\1\117\1\116\1\111\4\uffff\1\116\1\172\1\117\1\172\1\uffff\1\116"+
        "\1\uffff\1\172\1\uffff";
    static final String DFA8_acceptS =
        "\2\uffff\1\2\1\3\1\4\7\uffff\1\16\1\17\10\uffff\1\52\3\uffff\1\61"+
        "\1\uffff\1\63\1\66\1\1\1\6\1\14\37\uffff\1\53\1\64\2\uffff\1\65"+
        "\1\62\3\uffff\1\7\7\uffff\1\50\33\uffff\1\40\1\uffff\1\10\1\11\2"+
        "\uffff\1\37\3\uffff\1\22\1\uffff\1\47\5\uffff\1\23\1\46\3\uffff"+
        "\1\36\1\uffff\1\27\1\30\13\uffff\1\41\2\uffff\1\42\1\21\1\24\7\uffff"+
        "\1\35\7\uffff\1\34\4\uffff\1\26\12\uffff\1\55\1\56\7\uffff\1\31"+
        "\3\uffff\1\54\1\45\11\uffff\1\32\7\uffff\1\57\1\12\2\uffff\1\20"+
        "\1\60\1\25\6\uffff\1\15\1\33\1\43\1\44\4\uffff\1\5\1\uffff\1\51"+
        "\1\uffff\1\13";
    static final String DFA8_specialS =
        "\u00f2\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\35\1\34\2\uffff\1\34\22\uffff\1\35\1\uffff\1\26\11\uffff"+
            "\1\3\1\27\2\32\12\33\1\2\2\uffff\1\4\2\uffff\1\1\1\6\1\13\1"+
            "\5\1\12\1\25\1\16\2\32\1\22\2\32\1\24\1\32\1\23\1\32\1\17\1"+
            "\32\1\20\1\21\7\32\1\14\1\32\1\15\1\uffff\1\32\1\uffff\2\32"+
            "\1\11\1\7\13\32\1\30\1\32\1\10\1\32\1\31\6\32",
            "\1\40\16\uffff\1\36\1\37",
            "",
            "",
            "",
            "\1\42\2\uffff\1\41",
            "\1\44\1\43",
            "\1\45",
            "\1\46",
            "\1\47",
            "\1\53\1\52\1\uffff\1\50\6\uffff\1\51",
            "\1\54",
            "",
            "",
            "\1\57\6\uffff\1\60\1\uffff\1\55\2\uffff\1\56",
            "\1\61\5\uffff\1\64\2\uffff\1\63\2\uffff\1\62",
            "\1\65\11\uffff\1\67\1\66",
            "\1\73\1\uffff\1\70\6\uffff\1\72\7\uffff\1\71",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "",
            "\1\100\2\uffff\12\101",
            "\1\102",
            "\1\103",
            "",
            "\1\105\1\uffff\12\33",
            "",
            "",
            "",
            "",
            "",
            "\1\107\1\106",
            "\1\110",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\132\15\uffff\1\131",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\146\4\uffff\1\145",
            "\1\147\20\uffff\1\150",
            "",
            "",
            "\1\151",
            "\1\152",
            "",
            "",
            "\1\153",
            "\1\154",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "",
            "\1\156",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\161",
            "\1\162",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\164",
            "",
            "\1\165",
            "\1\166",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\170",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\172",
            "\1\173",
            "\1\174",
            "\1\175",
            "\1\176",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u0085",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "",
            "\1\u0090",
            "",
            "",
            "\1\u0091",
            "\1\u0092",
            "",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u0094",
            "\1\u0095",
            "",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "",
            "",
            "\1\u009c",
            "\1\u009d",
            "\1\u009e",
            "",
            "\1\u009f",
            "",
            "",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00a1",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00a9",
            "\1\u00aa",
            "",
            "\1\u00ab",
            "\1\u00ac",
            "",
            "",
            "",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00b0",
            "\1\u00b1",
            "\1\u00b2",
            "\1\u00b3",
            "",
            "\1\u00b4",
            "\1\u00b6\5\uffff\1\u00b5",
            "\1\u00b7",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00ba",
            "\1\u00bb",
            "",
            "\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "\1\u00bf",
            "",
            "\1\u00c0",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c4",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00c7",
            "\1\u00c8",
            "\1\u00c9",
            "",
            "",
            "\1\u00ca",
            "\1\u00cb",
            "\1\u00cc",
            "\1\u00cd",
            "\1\u00ce",
            "\1\u00cf",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "",
            "",
            "\1\u00d4",
            "\1\u00d5",
            "\1\u00d6",
            "\1\u00d7",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00da",
            "\1\u00db",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00df",
            "\1\u00e0",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "",
            "",
            "\1\u00e4",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "",
            "",
            "",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00e9",
            "\1\u00ea",
            "\1\u00eb",
            "",
            "",
            "",
            "",
            "\1\u00ec",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "\1\u00ee",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
            "",
            "\1\u00f0",
            "",
            "\1\32\5\uffff\1\32\6\uffff\14\32\5\uffff\1\32\1\uffff\32\32"+
            "\1\uffff\1\32\2\uffff\1\32\1\uffff\32\32",
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
            return "1:1: Tokens : ( T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | Text | Range | NEWLINE | INTEGER | NATURAL | WS );";
        }
    }
 

}