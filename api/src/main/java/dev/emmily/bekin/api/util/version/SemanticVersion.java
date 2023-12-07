package dev.emmily.bekin.api.util.version;

import dev.emmily.bekin.api.util.protocol.ProtocolConstants;

public class SemanticVersion {
  public static SemanticVersion fromBukkit() {
    String[] components = ProtocolConstants
      .SERVER_VERSION
      .replaceAll("[^0-9_]", "")
      .split("_");

    return new SemanticVersion(
      Byte.parseByte(components[1]),
      Byte.parseByte(components[2]),
      (byte) 0
    );
  }

  private final byte major;
  private final byte minor;
  private final byte patch;

  public SemanticVersion(byte major,
                         byte minor,
                         byte patch) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  public byte getMajor() {
    return major;
  }

  public byte getMinor() {
    return minor;
  }

  public byte getPatch() {
    return patch;
  }
}
