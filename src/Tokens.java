
import java.util.Map;

public class Tokens {
        public enum TokenType {
                ASSIGN,
                SUM,
                REST,
                MULT,
                COMA,
                QUOTE,
                LPAR,
                RPAR,
                GREATERTHAN,
                LOWERTHAN,
                LESSTHANEQUAL,
                GREATERTHANEQUAL,
                EQUAL,
                PR,

                NUMBER, // 17
                IDENTIFIER, // 18
                INVALID_TOKEN, // 19
        }

        protected static final char EQUAL_CHAR = '=';
        protected static final char GREATER_THAN_CHAR = '>';
        protected static final char LESS_THAN_CHAR = '<';

        public static final byte ASSIGN = 1;
        public static final byte SUM = 2;
        public static final byte REST = 3;
        public static final byte MULT = 4;
        public static final byte COMA = 5;
        public static final byte QUOTE = 6;
        public static final byte LPAR = 7;
        public static final byte RPAR = 8;
        public static final byte GREATERTHAN = 9;
        public static final byte LOWERTHAN = 10;
        public static final byte LESSTHANEQUAL = 11;
        public static final byte GREATERTHANEQUAL = 12;
        public static final byte EQUAL = 13;

        public static final byte INT = 14;
        public static final byte STRING = 15;
        public static final byte BDZTOUSER = 16;

        public static final byte NUMBER = 17;
        public static final byte IDENTIFIER = 18;
        protected static final byte PR = 19;

        public static final byte INVALID_TOKEN = 20;

        protected static final Map<Character, TokenInfo> SYMBOLS = Map.of(
                        '=', new TokenInfo(TokenType.ASSIGN, ASSIGN, "="),
                        '+', new TokenInfo(TokenType.SUM, SUM, "+"),
                        '-', new TokenInfo(TokenType.REST, REST, "-"),
                        '*', new TokenInfo(TokenType.MULT, MULT, "*"),
                        ',', new TokenInfo(TokenType.COMA, COMA, ","),
                        '"', new TokenInfo(TokenType.QUOTE, QUOTE, "\""),
                        '(', new TokenInfo(TokenType.LPAR, LPAR, "("),
                        ')', new TokenInfo(TokenType.RPAR, RPAR, ")"),
                        '>', new TokenInfo(TokenType.GREATERTHAN, GREATERTHAN, ">"),
                        '<', new TokenInfo(TokenType.LOWERTHAN, LOWERTHAN, "<"));

        protected static final Map<String, TokenInfo> COMPLEX_SYMBOLS = Map.of(
                        "<=", new TokenInfo(TokenType.LESSTHANEQUAL, LESSTHANEQUAL, "<="),
                        ">=", new TokenInfo(TokenType.GREATERTHANEQUAL, GREATERTHANEQUAL, ">="),
                        "==", new TokenInfo(TokenType.EQUAL, EQUAL, "=="));

        protected static final Map<String, TokenInfo> KEYWORDS = Map.of(
                        "int", new TokenInfo(TokenType.PR, INT, "int"),
                        "String", new TokenInfo(TokenType.PR, STRING, "String"),
                        "BDZtoUser", new TokenInfo(TokenType.PR, BDZTOUSER, "BDZtoUser"));
}
