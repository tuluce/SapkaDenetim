import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SapkaDenetim extends JFrame {
    
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JTextArea jTextArea1;
    private JTextField jTextField1;
    private JScrollPane jScrollPane1;
    
    private Denetleyici denetleyici;
    private String text;
    private String sonuc;
    private String word;
    private ArrayList<String> sonuclar;
    private ArrayList<Integer> wordStartIndexes;
    private ArrayList<Integer> wordEndIndexes;
    private int sonucIndex;
    private int wordIndex;
    private int exactIndex;
    
    public SapkaDenetim() {
        setLayout(null);
        setSize(810, 610);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("Þapkalý Harf Denetimi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jLabel1 = new JLabel("Þapkalý Harf Denetimi");
        jLabel2 = new JLabel("Denetlemek istediðiniz metni aþaðý yapýþtýrýn.");
        jButton1 = new JButton("Denetle");
        jButton2 = new JButton("<");
        jButton3 = new JButton(">");
        jButton4 = new JButton("?");
        jTextArea1 = new JTextArea();
        jTextField1 = new JTextField();
        jScrollPane1 = new JScrollPane(jTextArea1);
        
        add(jButton1);
        add(jButton2);
        add(jButton3);
        add(jButton4);
        add(jLabel1);
        add(jLabel2);
        add(jTextField1);
        add(jScrollPane1);
        
        Font bigFont = new Font("Tahoma", 0, 48);
        Font normalFont = new Font("Tahoma", 0, 24);
        Font textFont = new Font("Times New Roman", 0, 24);
        
        jButton1.setFont(normalFont);
        jButton2.setFont(normalFont);
        jButton3.setFont(normalFont);
        jButton4.setFont(normalFont);
        jLabel1.setFont(bigFont);
        jLabel2.setFont(normalFont);
        jTextArea1.setFont(textFont);
        jTextField1.setFont(normalFont);
        
        resizeComponents();
        addComponentListener(new jFrame1ComponentListener());
        
        jTextField1.setEditable(false);
        jTextField1.setText( "Hoþgeldiniz...");
        
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setMargin(new Insets(10, 10, 10, 10));
        
        jButton1.addActionListener(new jButton1ActionListener());
        jButton2.addActionListener(new jButton2ActionListener());
        jButton3.addActionListener(new jButton3ActionListener());
        jButton4.addActionListener(new jButton4ActionListener());
        
        try {
            setIconImage(ImageIO.read(SapkaDenetim.class.getResource("icon.png")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Bir hata oluþtu... (ICON_LOADING_FAILED)",
                                          "IOException", JOptionPane.ERROR_MESSAGE);
        }
        
        Scanner normalDictInput = null;
        Scanner sapkaliDictInput = null;
        try {
            normalDictInput = new Scanner(SapkaDenetim.class.getResourceAsStream("NormalHaller.txt"));
            sapkaliDictInput = new Scanner(SapkaDenetim.class.getResourceAsStream("SapkaliHaller.txt"));
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Bir hata oluþtu... (DICT_LOADING_FAILED)",
                                          "IOException", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
            System.exit(0);
        }
        ArrayList<String> normalDict = new ArrayList<String>();
        ArrayList<String> sapkaliDict = new ArrayList<String>();
        while (normalDictInput.hasNext()) {
            normalDict.add(normalDictInput.next());
        }
        while (sapkaliDictInput.hasNext()) {
            sapkaliDict.add(sapkaliDictInput.next());
        }
        
        denetleyici = new Denetleyici(normalDict, sapkaliDict);
        sonuclar = new ArrayList<String>();
    }
    
    private class jFrame1ComponentListener implements ComponentListener {
        public void componentResized(ComponentEvent e) {
            resizeComponents();
            jScrollPane1.revalidate();
        }
        public void componentHidden(ComponentEvent e) {}
        public void componentMoved(ComponentEvent e) {}
        public void componentShown(ComponentEvent e) {}
    }

    private class jButton1ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            text = jTextArea1.getText().trim();
            jTextArea1.setText(text);
            if (text.equals("")) {
                jTextField1.setText("Denetlemek için bir metin girin.");
                sonuclar = new ArrayList<String>();
            }
            else {
                sonuclar = denetleyici.denetle(text);
                if (sonuclar.size() == 0) {
                    sonuclar.add("Denetim tamamlandý, þüpheli bir kelimeye rastlanmadý.");
                }
                else {
                    sonuclar.add("Denetim tamamlandý.");
                }
                wordStartIndexes = denetleyici.wordStartIndexes;
                wordEndIndexes = denetleyici.wordEndIndexes;
                sonucIndex = 0;
                inform();
            }
        }
    }
    
    private class jButton2ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            sonucIndex--;
            inform();
        }
    }
    
    private class jButton3ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            sonucIndex++;
            inform();
        }
    }
    
    private static final String INFO =
        "Bu yazýlým, girdiðiniz metindeki kelimelerden düzeltme iþareti (þapka) kullanýmýna \n" +
        "baðlý olarak yanlýþ yazýlmýþ olma ihtimali taþýyanlarý tespit eder. \n\n" +
        "Emin Bahadýr Tülüce - Mart 2017";
    private class jButton4ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            showInfo();
        }
    }
    private void showInfo() {
        JOptionPane.showMessageDialog(this, INFO,
                                      "Hakkýnda", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resizeComponents() {
        int x = (int) this.getBounds().getWidth();
        int y = (int) this.getBounds().getHeight();
        jButton1.setBounds(x - 195, 90, 150, 40);
        jButton2.setBounds(x - 195, y - 110, 70, 40);
        jButton3.setBounds(x - 115, y - 110, 70, 40);
        jButton4.setBounds(x - 95, 10, 50, 40);
        jLabel1.setBounds((x / 2) - 250, 10, 600, 58);
        jLabel2.setBounds(15, 90, 600, 39);
        jScrollPane1.setBounds(10, 135, x - 55, y - 260);
        jTextField1.setBounds(10, y - 110, x - 220, 40);
    }
    
    private void inform() {
        if (sonucIndex >= sonuclar.size()) {
            sonucIndex = sonuclar.size() - 1;
        }
        if (sonucIndex < 0) {
            sonucIndex = 0;
        }
        if (0 <= sonucIndex && sonucIndex < sonuclar.size()) {
            int start = wordStartIndexes.get(sonucIndex);
            int end = wordEndIndexes.get(sonucIndex);
            sonuc = sonuclar.get(sonucIndex);
            if (sonucIndex + 1 <= denetleyici.found) {
                sonuc = sonuc + " (" + (sonucIndex + 1) + "/" + denetleyici.found + ")";
            }
            jTextField1.setText(sonuc);
            jTextArea1.requestFocus();
            jTextArea1.select(start, end);
        }
    }
    
    public static void main(String[] args) {
        SapkaDenetim jFrame1 = new SapkaDenetim();
    }
}