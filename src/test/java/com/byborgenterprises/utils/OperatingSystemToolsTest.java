package com.byborgenterprises.utils;


import com.byborgenterprises.enums.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

class OperatingSystemToolsTest {

  @Test
  @EnabledOnOs(OS.LINUX)
  void testGetOperatingSystem_Linux() {
    //given
    //when
    OperatingSystem os = OperatingSystemTools.getOperatingSystem();
    //then
    Assertions.assertEquals(OperatingSystem.LINUX, os);
  }

  @Test
  @EnabledOnOs(OS.WINDOWS)
  void testGetOperatingSystem_Windows() {
    //given
    //when
    OperatingSystem os = OperatingSystemTools.getOperatingSystem();
    //then
    Assertions.assertEquals(OperatingSystem.WINDOWS, os);
  }
}