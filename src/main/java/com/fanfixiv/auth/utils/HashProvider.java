package com.fanfixiv.auth.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashProvider {
  public static String hashString(String raw, String salt) {
    String hex = "";
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(salt.getBytes());
      md.update(raw.getBytes());
      hex = String.format("%064x", new BigInteger(1, md.digest()));
    } catch (NoSuchAlgorithmException e) {
    }
    return hex;
  }

  public static String getSalt() {
    byte[] bytes = new byte[16];
    try {
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      random.nextBytes(bytes);
    } catch (NoSuchAlgorithmException e) {
    }
    return new String(Base64.getEncoder().encode(bytes));
  }
}
