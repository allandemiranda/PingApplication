package com.byborgenterprises.utils;

import com.byborgenterprises.enums.OperatingSystem;
import com.byborgenterprises.exceptions.OperatingSystemNotFoundException;
import java.util.Arrays;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * The OperatingSystemTools class provides utility methods for determining
 * the operating system of the current environment. This is a final utility
 * class and cannot be instantiated.
 */
@UtilityClass
public final class OperatingSystemTools {

  /**
   * The system property key used to identify the operating system.
   */
  private static final String KEY_OS = "os.name";

  /**
   * Determines the current operating system by analyzing the system property "os.name".
   *
   * @return the {@link OperatingSystem} enum representing the current operating system.
   * @throws OperatingSystemNotFoundException if the operating system cannot be identified.
   */
  @NonNull
  public static OperatingSystem getOperatingSystem() {
    String os = System.getProperty(KEY_OS).toLowerCase();
    return Arrays.stream(OperatingSystem.values()).filter(system -> os.contains(system.getCode())).findFirst()
        .orElseThrow(() -> new OperatingSystemNotFoundException("Operating System " + os + " not found."));
  }
}
