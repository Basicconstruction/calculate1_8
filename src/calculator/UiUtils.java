package calculator;

public class UiUtils {
    protected static boolean isDigitChar(char c){
        return c>='0'&&c<='9';
    }
    protected static String ansSerialize(String expression,String ans){
        return expression.replaceAll("ans",ans);
    }
}
