import java.math.BigInteger;
import java.security.*;
import java.util.Arrays;

class Main {

  public static void main(String[] args) {

    for (int i = 0; i < 2000; i++) {
      System.out.println(pwGenerator(i));

    }

  }

  public static String pwGenerator(int i) {
    String pw = "0000000";

    for (int j = 0; j < i; j++) {
      pw  = raiseChar(pw, 6);
    }

    return pw;
  }


  public static String raiseChar(String text, int pos) {
    StringBuilder pw = new StringBuilder(text);
    int actual = pw.charAt(pos);

//    System.out.println(text +"  "+ pos +"  "+actual);

    if (actual >= 48 && actual <= 56) {
      pw.setCharAt(pos, (char) (actual + 1));
    } else if (actual == 57) {
      pw.setCharAt(pos, 'a');
    } else if (actual >= 97 && actual <= 121) {
      pw.setCharAt(pos, (char) (actual + 1));
    } else if (actual == 122) {
//      System.out.println("NOW: " +pw.toString());
      String received = raiseChar(pw.toString(), (pos - 1));
      pw.delete(0,7).append(received);
      pw.setCharAt(pos, '0');
    }

    return pw.toString();

  }

  public static String callculateMd5(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger number = new BigInteger(1, messageDigest);
      String hashtext = number.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}