package com.fanfixiv.auth.utils;

import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RandomProvider {

  private static Random random = new Random();

  public static String getRandomNumber() {
    return (String.valueOf(random.nextInt(10))
        + String.valueOf(random.nextInt(10))
        + String.valueOf(random.nextInt(10))
        + String.valueOf(random.nextInt(10)));
  }

  public static String getUUID() {
    return UUID.randomUUID().toString();
  }
}
