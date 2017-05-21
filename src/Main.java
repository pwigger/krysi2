import java.math.BigInteger;
import java.security.*;
import java.util.Arrays;

class Main {
  public static String[][] rainbowbtable = new String[2][2000];

  public static void main(String[] args) {

//    Generating rainbowtable
    System.out.println("Creating Rainbowtable...");
    generateRainbowtable();
    printTable(15);

//    Search fo the hash
    System.out.println("searching for the hash...");
    String hashToFind = "1d56a37fb6b08aa709fe90e12ca59e12";
    int lineOfHash = findHashInTable(hashToFind);
    System.out.println("Found Hash in line: " + lineOfHash);

//    Reconstruct PW
    String potentialPW = rainbowbtable[0][lineOfHash];
    String finalPW;
    for (int i = 0; i < rainbowbtable[0].length; i++) {
      String hashedPW = callculateMd5(potentialPW);
      if (hashedPW.equals(hashToFind)) {
        finalPW = potentialPW;
        System.out.println("PW was: " + finalPW);
        break;
      }
      potentialPW = reductionFunction(hashedPW, i);
    }
  }


  /**
   * Searches the Rainbowtable for a specific hash.
   *
   * @param hashToFind the hash to find in the table.
   * @return line on witch the hash appears or -1.
   */
  private static int findHashInTable(String hashToFind) {
    for (int j = rainbowbtable[0].length - 1; j >= 0; j--) {
      String actual = hashToFind;
      System.out.print("Searching: " + j);

      int a = j;
      while (a < 1999) {
        actual = reductionFunction(actual, a);
        System.out.print(actual + " - ");
        actual = callculateMd5(actual);
        System.out.print(actual + " - ");
        a++;
      }

      actual = reductionFunction(actual, a);
      System.out.println(actual + " - ");
      for (int i = 0; i < rainbowbtable[1].length; i++) {
        if (rainbowbtable[1][i].equals(actual)) {
          return i;
        }
      }
    }
    return -1;
  }


  /**
   * Shows some first entries of the RainbowTable
   *
   * @param i how much entries to show.
   */
  private static void printTable(int i) {
    for (int j = 0; j < i; j++) {
      System.out.println(rainbowbtable[0][j] + "   " + rainbowbtable[1][j]);
    }
    System.out.println("  ...  " + "   " + "  ...  ");
  }


  /**
   * Fills the Rainbowbeatable.
   */
  private static void generateRainbowtable() {
    for (int i = 0; i < 2000; i++) {
      rainbowbtable[0][i] = pwGenerator(i);

      String actualItem = rainbowbtable[0][i];
      for (int j = 0; j < 2000; j++) {
        actualItem = callculateMd5(actualItem);
//        System.out.println(actualItem);
        actualItem = reductionFunction(actualItem, j);
//        System.out.println(actualItem);
      }
      rainbowbtable[1][i] = actualItem;
    }
  }


  /**
   * Reduces given String to 7 Chars. Follows ReductionFunction on Slide 3.27.
   *
   * @param hash  Hash to reduce.
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

    BigInteger hashNumber = new BigInteger(hash, 16);

    hashNumber = hashNumber.add(step);

    for (int i = 6; i >= 0; i--) {

      char toset = signs[hashNumber.mod(arrLength).intValue()];
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