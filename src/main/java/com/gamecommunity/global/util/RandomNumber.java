package com.gamecommunity.global.util;

public class RandomNumber {

  public static String createNumber() {
    return String.valueOf((int) (Math.random() * (90000)) + 100000);
  }

}
