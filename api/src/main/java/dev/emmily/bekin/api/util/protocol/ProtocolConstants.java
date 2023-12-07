package dev.emmily.bekin.api.util.protocol;

import dev.emmily.bekin.api.util.version.SemanticVersion;
import org.bukkit.Bukkit;

public interface ProtocolConstants {
  String SERVER_VERSION = Bukkit
    .getServer()
    .getClass()
    .getName()
    .split("\\.")[3];
  SemanticVersion SEMANTIC_SERVER_VERSION = SemanticVersion.fromBukkit();
}
