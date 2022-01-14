package com.tsystems.javaschool.tasks.calculator;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */


    public String evaluate(String statement) {
        // TODO: Implement the logic here

        if (statement == "")
            return null;
        
        try {
            List<Lexeme> lexemes = lexAnalyze(statement);
            LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
            double number = expr(lexemeBuffer);
            
            if (Double.isInfinite(number))
                    return null;
            
            if (number * 10000 == ((int) number) * 10000) { //precision
                String result = (int) number + "";
                return result;
            } else {
                String result = number + "";
                return result;
            }
        } catch (RuntimeException e) {return null;}
    }

    public enum LexemeType {
        LEFT_BRACKET, RIGHT_BRACKET,
        OP_PLUS, OP_MINUS, OP_MUL, OP_DIV,
        NUMBER,
        EOF;
    }

    public static class Lexeme {

        LexemeType type;
        String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

        @Override
        public String toString() {
            return "Lexeme{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    //итератор
    public static class LexemeBuffer {

        private int pos;

        public List<Lexeme> lexemes;

        public LexemeBuffer(List<Lexeme> lexemes) {
            this.lexemes = lexemes;
        }

        public Lexeme next() {
            return lexemes.get(pos++);
        }

        public void back() {
            pos--;
        }

        public int getPos() {
            return pos;
        }
    }

    //парсер
    public static List<Lexeme> lexAnalyze(String expText) {

        ArrayList<Lexeme> lexemes = new ArrayList<>();

        int pos = 0;
        while (pos < expText.length()) {
            char c = expText.charAt(pos);

            switch (c) {
                case ('('):
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, c));
                    pos++;
                    continue; //возврат к началу цикла
                case (')'):
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, c));
                    pos++;
                    continue;
                case ('+'):
                    lexemes.add(new Lexeme(LexemeType.OP_PLUS, c));
                    pos++;
                    continue;
                case ('-'):
                    lexemes.add(new Lexeme(LexemeType.OP_MINUS, c));
                    pos++;
                    continue;
                case ('*'):
                    lexemes.add(new Lexeme(LexemeType.OP_MUL, c));
                    pos++;
                    continue;
                case ('/'):
                    lexemes.add(new Lexeme(LexemeType.OP_DIV, c));
                    pos++;
                    continue;
                default:
                    if (c <= '9' && c >= '0' || c == '.') { //склеиваем цифры в sb
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expText.length())
                                break; //выход из do while
                            c = expText.charAt(pos);
                        } while (c <= '9' && c >= '0' || c == '.');

                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));

                    } else { //всегда должен быть пробел
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected character: " + c);
                        }
                        pos++;
                    }
            }

        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }

    // expr : plusminus * EOF
    // plusminus : multdiv ( ( '+' | '-' ) multdiv ) * ;
    // multdiv : factor ( ( '*' | '/' ) factor ) * ;
    // factor : NUMBER | '(' expr ')' ;


    public static double expr(LexemeBuffer lexemes) {

        Lexeme lexeme = lexemes.next();

        if (lexeme.type == LexemeType.EOF)
            return 0;
        else {
            lexemes.back();
            return plusminus(lexemes);
        }
    }

    public static double plusminus(LexemeBuffer lexemes) {

        double value = multdiv(lexemes);

        while (true) {

            Lexeme lexeme = lexemes.next();

            switch (lexeme.type) {
                case OP_PLUS:
                    value = value + multdiv(lexemes);
                    break;
                case OP_MINUS:
                    value = value - multdiv(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public static double multdiv(LexemeBuffer lexemes) {

        double value = factor(lexemes);

        while (true) {

            Lexeme lexeme = lexemes.next();

            switch (lexeme.type) {
                case OP_MUL:
                    value = value * factor(lexemes);
                    break;
                case OP_DIV:
                    value = value / factor(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public static double factor(LexemeBuffer lexemes) {

        Lexeme lexeme = lexemes.next();

        switch (lexeme.type) { //разобраться + точка
            case NUMBER:
                return Double.parseDouble(lexeme.value);
            case LEFT_BRACKET:
                double value = expr(lexemes); //будет далее
                lexeme = lexemes.next();
                if (lexeme.type != LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " + lexeme.value + " at position: " + lexemes.getPos());
                }
                return value; //вычесленное выражение в скобках
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value + " at position: " + lexemes.getPos());
        }
    }

}
