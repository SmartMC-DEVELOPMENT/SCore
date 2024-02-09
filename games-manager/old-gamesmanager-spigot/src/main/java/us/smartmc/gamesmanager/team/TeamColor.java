package us.smartmc.gamesmanager.team;

import lombok.Getter;

@Getter
public enum TeamColor {
  RED("c"),
  BLUE("9"),
  GREEN("a"),
  YELLOW("e");

  private final String color;

  TeamColor(String color) {
    this.color = color;
  }
}
