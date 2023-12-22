package us.smartmc.gamesmanager.util;

public class StringUtils {

  public static String toSnakeCase(String camelCase) {
    final StringBuilder key = new StringBuilder();
    for (String letter : camelCase.split("")) {
      if (letter.equals(letter.toUpperCase())) {
        key.append("_").append(letter.toLowerCase());
      }
      key.append(letter);
    }
    return key.toString();
  }

  public static String toCamelCase(String snakeCase) {
    final StringBuilder value = new StringBuilder();
    for (String letter : snakeCase.split("_")) {
      value.append(letter.substring(0, 1).toUpperCase()).append(letter.substring(1));
    }
    return value.toString();
  }
}
