public class WordScanner {
    
    String text;
    int index;
    int lastIndex;
    
    public WordScanner(String text) {
        this.text = text;
        index = 0;
        lastIndex = 0;
        while(index < text.length() && !notSpace(text.charAt(index)))  {
            index++;
        }
    }
    
    public boolean hasNext() {
        boolean over = true;
        for (int i = index; i < text.length(); i++) {
            if (notSpace(text.charAt(i))) {
                over = false;
            }
        }
        return !over;
    }
    
    public String next() {
        lastIndex = index;
        String next = "";
        while(index < text.length() && notSpace(text.charAt(index))) {
            next = next + text.charAt(index);
            index++;
        }
        while(index < text.length() && !notSpace(text.charAt(index)))  {
            index++;
        }
        return next;
    }
    
    public static boolean notSpace(char c) {
        return (c != ' '  &&
                c != '\n' &&
                c != '\t' );
    }
}