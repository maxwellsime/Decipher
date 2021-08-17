import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

/**
 * Decipher code programmed to decipher basic Vignere ciphers
 */
public class decipher{
    // Constructor variables
    File ciphertext;
    File plaintext;
    File proof = new File("tess26.txt");
    int key_len;
    int[] key;
    // Vigenere Cipher
    double[] alphabetFreq = new double[26];
    String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public decipher(File ciphertext, File plaintext, int key_len){
        this.ciphertext = ciphertext;
        this.plaintext = plaintext;
        this.key_len = key_len;
        key = new int[key_len];
    }


    /**
     * Letter Frequencies Calculator
     * @param ea
     * @throws IOException
     */
    public void freqCalc() throws IOException{
        Scanner sc = new Scanner(proof);
        String s = sc.next();
        sc.close();
        double aSum = 0;

        for(int i = 0; i < s.length(); i++){
            char sI = s.charAt(i);
            int j = Arrays.asList(alphabet).indexOf(String.valueOf(sI));
            alphabetFreq[j] += 1;
        }
        for(int i = 0; i < 26; i++){
            aSum += alphabetFreq[i];
        }
        for(int i = 0; i < 26; i++){
            alphabetFreq[i] = alphabetFreq[i]/aSum;
        }
    }


    /**
     * Solves vigenere cipher key
     * @param ea    exercise answer File
     */
    public void vigenereSolve() throws IOException{
        Scanner sc = new Scanner(ciphertext);
        String s = sc.next();
        sc.close();

        freqCalc();
    
        for(int i = 0; i < key_len; i++){
            int[] aCount = new int[26];
            int str = 0;

            /*============================================
                        COUNT OCCURANCES 
            ==============================================*/
            for(int x = 0; ;x++){
                if(str >= s.length()){
                    break;
                }
                if (x >= key_len){      // resets x
                    x = 0;
                }
                if(i == x){             
                    // count this letter in aCount
                    char sI = s.charAt(str);
                    int j = Arrays.asList(alphabet).indexOf(String.valueOf(sI));
                    aCount[j] += 1;
                }

                str++;
            }
        
            /*=================================================
                        CALCULATE SUM OF OCCURANCES 
            ===================================================*/
            int cSum = 0;               // sum of count arr
            double[] cFreq = new double[26];
    
            for(int x = 0; x <= 25; x++){
                cSum += aCount[x];
            }
            for(int x = 0; x <=25; x++){
                cFreq[x] = (double) aCount[x] / cSum;
            }

            /*=================================================
                        CALCULATE CESAR SHIFT 
            ===================================================*/

            double hVal = 0;            // highest value
            int hShift = 0;             // highest val shift

            for(int x = 0; x <= 25; x++){   // shift value
                double tSum = 0;            // temporary sum
                for(int j = 0; j <= 25; j++){
                    int shift = j-x;
                    if(shift < 0){
                        shift = shift + 26;
                    }

                    // Sum frequencies calculation
                    tSum += cFreq[shift]*alphabetFreq[j];
                }
                if(tSum > hVal){
                    hShift = x;
                    hVal = tSum;
                }
            }
            key[i] = hShift;
        }
        outputVigenere();
    }


    /**
     * decrypts vigenere using key and outputs to "plaintext" file
     * @param ea        output file
     * @throws IOException
     */
    public void outputVigenere() throws IOException{
        FileWriter wr = new FileWriter(plaintext);
        Scanner sc = new Scanner(ciphertext);
        String s = sc.next();
        sc.close();   
        String ns = "";         // new string
        int i = 0;              // to iterate through s

        for(int k =0; ; k++){
            if(i >= s.length()){
                break;
            }
            if(k == key_len){
                k = 0;
            }
            char sI = s.charAt(i);
            int j = Arrays.asList(alphabet).indexOf(String.valueOf(sI));
            j = j+key[k];

            if(j > 25){
                j = j - 26;
            }
            ns += alphabet[j];
            
            i++;
        }
        wr.write(ns);
        wr.close();
    }
    

    /**
     * Solves simple transposition cipher then outputs to "plaintext" file
     * @param args
     * @throws IOException
     */
    public void simpleTransposition() throws IOException{
        FileWriter wr = new FileWriter(plaintext);
        Scanner sc = new Scanner(ciphertext);
        String s = sc.next();
        sc.close();

        int[] counter = new int[6];            // amount of data in columns
        int c = 0;                               // hard coded rows being different sizes

        if(key_len == 4){
            c = 210;
        }else if(key_len == 5){
            c = 168;
        }else if(key_len == 6){
            c = 140;
            counter[0] = 141;

        }

        String ns = "";         // new string
        for(int i = 0; i < c; i++){
            int k = 0;  
            for(k+=i; k < c*key_len; k+=c){
                char sI = s.charAt(k);
                ns += String.valueOf(sI);
            }
        }
        wr.write(ns);
        wr.close();
    }


    public static void main(String[] args) throws IOException{
        File e3 = new File("cexercise1.txt");
        File e3a = new File("exercise1.txt");
        decipher d3 = new decipher(e3, e3a, 6);
        d3.vigenereSolve();
        
        File e4 = new File("cexercise2.txt");
        File e4a = new File("exercise2.txt");
        decipher d4 = new decipher(e4, e4a, 6);
        d4.vigenereSolve();

        File e5 = new File("cexercise3.txt");
        File e5a = new File("exercise3.txt");
        decipher d5 = new decipher(e5, e5a, 5);
        d5.simpleTransposition();
    }
}