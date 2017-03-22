import java.util.ArrayList;

public class Denetleyici {
    static final String semboller = "’'[](){}:,-—…!.?“”‘’;/&*@\\•°?#%~$€?£\"";
    static final String sertler = "pçtk";
    static final String yumusaklar = "bcdð";
    
    private ArrayList<String> normalDict;
    private ArrayList<String> sapkaliDict;
    
    ArrayList<Integer> wordStartIndexes;
    ArrayList<Integer> wordEndIndexes;
    
    int found;
    
    public Denetleyici(ArrayList<String> normalDict, ArrayList<String> sapkaliDict) {
        this.normalDict = normalDict;
        this.sapkaliDict = sapkaliDict;
    }
    
    public ArrayList<String> denetle(String text) {
        WordScanner scan = new WordScanner(text);
        ArrayList<String> sonuclar = new ArrayList<String>();
        wordStartIndexes = new ArrayList<Integer>();
        wordEndIndexes = new ArrayList<Integer>();
        
        found = 0;
        String word;
        String rawWord;
        int match;
        int sertIndex = -1;
        while (scan.hasNext()) {
            rawWord = scan.next();
            word = clearPunctuation(rawWord.toLowerCase());
            if (!word.equals(normalize(word))) {
                sonuclar.add("Þüpheli kelime: " + rawWord);
                int start = scan.lastIndex;
                wordStartIndexes.add(start);
                wordEndIndexes.add(start + rawWord.length());
                found++;
                continue;
            }
            match = normalDict.indexOf(word);
            if (match != -1) {
                sonuclar.add("Olasý deðiþiklik: " + sapkaliDict.get(match));
                int start = scan.lastIndex;
                wordStartIndexes.add(start);
                wordEndIndexes.add(start + rawWord.length());
                found++;
            }
            else if (sapkaliDict.indexOf(word) == -1 &&
                     (word.indexOf('â') != -1 || 
                      word.indexOf('î') != -1 || 
                      word.indexOf('ô') != -1 || 
                      word.indexOf('û') != -1)) {
                match = normalDict.indexOf(normalize(word));
                if (match != -1) {
                    sonuclar.add("Olasý deðiþiklik: " + sapkaliDict.get(match));
                    int start = scan.lastIndex;
                    wordStartIndexes.add(start);
                    wordEndIndexes.add(start + rawWord.length());
                    found++;
                }
            }
            else {
                for (String subWord : normalDict) {
                    if (word.indexOf(subWord) == 0) {
                        sonuclar.add("Bulunan eþleþme: " + sapkaliDict.get(normalDict.indexOf(subWord)));
                        int start = scan.lastIndex;
                        wordStartIndexes.add(start);
                        wordEndIndexes.add(start + rawWord.length());
                        found++;
                        break;
                    }
                    else {
                        sertIndex = sertler.indexOf(subWord.substring(subWord.length() - 1));
                        if (sertIndex != -1) {
                            String yumusakSubWord = subWord.substring(0, subWord.length() - 1) + yumusaklar.charAt(sertIndex);
                            if (word.indexOf(yumusakSubWord) == 0) {
                                sonuclar.add("Bulunan eþleþme: " + sapkaliDict.get(normalDict.indexOf(subWord)));
                                int start = scan.lastIndex;
                                wordStartIndexes.add(start);
                                wordEndIndexes.add(start + rawWord.length());
                                found++;
                                break;
                            }
                        }
                    }
                }
            }
        }
        wordStartIndexes.add(-1);
        wordEndIndexes.add(-1);
        return sonuclar;
    }
    
    public static String normalize(String abnormal) {
        StringBuffer abnormalBuffer = new StringBuffer(abnormal);
        if (abnormal.indexOf("'") != -1)
            abnormalBuffer.deleteCharAt(abnormal.indexOf("'"));
        abnormal = abnormalBuffer.toString();
        String normal = "";
        for (int i = 0; i < abnormal.length(); i++) {
            if (abnormal.charAt(i) == 'â')
                normal = normal + 'a';
            else if (abnormal.charAt(i) == 'î')
                normal = normal + 'i';
            else if (abnormal.charAt(i) == 'ô')
                normal = normal + 'o';
            else if (abnormal.charAt(i) == 'û')
                normal = normal + 'u';
            else
                normal = normal + abnormal.charAt(i);
        }
        return normal;
    }
    
    public static String clearPunctuation(String word) {
        StringBuffer wordBuffer = new StringBuffer(word);
        for (int i = 0; i < wordBuffer.length(); i++) {
            if (semboller.indexOf(wordBuffer.charAt(i)) != -1) {
                wordBuffer.deleteCharAt(i);
                i--;
            }
        }
        return wordBuffer.toString();
    }
    
}