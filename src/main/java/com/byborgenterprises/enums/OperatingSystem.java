package com.byborgenterprises.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The OperatingSystem enum represents supported operating systems.
 * Each enum constant is associated with a code representing the operating system.
 */
@Getter
@RequiredArgsConstructor
public enum OperatingSystem {

  /**
   * Represents the Linux operating system.
   */
  LINUX("linux"),

  /**
   * Represents the Windows operating system.
   */
  WINDOWS("win");

  /**
   * The code representing the operating system.
   */
  @NonNull
  private final String code;
}
