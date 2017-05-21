import java.math.BigInteger;
import java.security.*;
import java.util.Arrays;

class Main {

  public static void main(String[] args) {

    reductionFunction("29c3eea3f305d6b823f562ac4be35217", 0);
  }



  /**
   * Reduces given String to 7 Chars. Follows ReductionFunction on Slide 3.27.
   *
   * @param hash Hash to reduce.
   * @param stufe Step, in that the fuction is called, influences the result.
   * @return the reduced String.
   */
  public static String reductionFunction(String hash, int stufe) {
    StringBuilder output = new StringBuilder("0000000");

    //Create array of all Chars
    char[] signs = new char[36];
    for (int i = 0; i < 10; i++) {
      signs[i] = (char) (i + 48);
    }
    for (int i = 10; i < 36; i++) {
      signs[i] = (char) (i + 87);
    }

    BigInteger step = BigInteger.valueOf(stufe);
    BigInteger arrLength = BigInteger.valueOf(signs.length);

    //TODO: Gegebener HAshwert als natürliche Zahl.
    BigInteger hashNumber = new BigInteger(hash);


    hashNumber.add(step);

    for (int i = 6; i >= 0; i--) {

      char toset = (char) hashNumber.mod(arrLength).intValue();
      output.setCharAt(i, toset);

      hashNumber = hashNumber.divide(arrLength);
    }

    return output.toString();
  }



  /**
   * Generates the i'st password in the list.
   *
   * @param i n- of what password to generate.
   * @return the password.
   */
  public static String pwGenerator(int i) {
    String pw = "0000000";
    for (int j = 0; j < i; j++) {
      pw = raiseChar(pw, 6);
    }
    return pw;
  }



  /**
   * Raises the passwords chars according to dfinition [0-9][a-z] recursive.
   *
   * @param text inputs the pw to raise.
   * @param pos  char position that has to be risen.
   * @return raised pw.
   */
  private static String raiseChar(String text, int pos) {
    StringBuilder pw = new StringBuilder(text);
    int actual = pw.charAt(pos);

    if (actual >= 48 && actual <= 56) {
      pw.setCharAt(pos, (char) (actual + 1));
    } else if (actual == 57) {
      pw.setCharAt(pos, 'a');
    } else if (actual >= 97 && actual <= 121) {
      pw.setCharAt(pos, (char) (actual + 1));
    } else if (actual == 122) {
      String received = raiseChar(pw.toString(), (pos - 1));
      pw.delete(0, 7).append(received);
      pw.setCharAt(pos, '0');
    }
    return pw.toString();
  }




  /**
   * Callculates a MD5 hash to a string.
   * copied from: http://www.asjava.com/core-java/java-md5-example/
   *
   * @param input String to create hash from.
   * @return MD5 hash from String.
   */
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