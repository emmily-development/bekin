# bekin

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.8.8%20to%201.20.2-brightgreen)
![Spigot Version](https://img.shields.io/badge/Spigot-1.8.8%20to%201.20.2-blue)

Bekin is a lightweight and flexible library for creating holographic displays in your Spigot plugins. This library
provides an easy-to-use API for adding and managing holograms in your Minecraft server. Whether you want to display
player stats, messages, or any other information in the form of floating text, this library has got you covered.

## Features

- Support for Spigot versions 1.8.8 to 1.20.2.
- Easy-to-use API for creating and managing holograms.
- Create holographic displays with text, item frames, and other entities.
- Update holograms dynamically to reflect changing information.
- Customize hologram appearance, position, and interactions.
- High-performance and efficient hologram rendering.
- ~~Ray tracing-based interactions for the highest precision in player interactions.~~
- PR-Tree-based indexing for clickable lines, ensuring the highest precision in player interactions.

## TO-DO

- Loader API
- Loader implementations for JSON, TOML and YAML

## Getting Started

### Maven

```xml

<repositories>
  ...
  <repository>
    <id>emmily-public</id>
    <url>https://repo.emmily.dev/repository/emmily-public</url>
  </repository>
  ...
</repositories>

<dependencies>
...
<dependency>
  <groupId>dev.emmily</groupId>
  <artifactId>bekin-api</artifactId>
  <version>LATEST</version>
</dependency>
...
</dependencies>
```

## Documentation

For more detailed information on how to use the Bekin Holograms Library, please refer to
the [official documentation](https://github.com/emmily-development/bekin/wiki).

## Example Usage

Here's a simple example of creating and updating a hologram in your Spigot plugin:

```java
Hologram hologram = Hologram
  .builder()
  .id("welcome")
  .position(Vector3D.of("spawn", 10, 65, 0))
  .addLines(
    HologramLine.line("Static line"),
    ClickableHologramLine.decorate(HologramLine.line("Clickable line"), player -> player.sendMessage("Hello!"))
  )
  .renderAuthorizer(player -> player.hasPermission("holograms.welcome.view"))
  .build();
```
## Contributing

If you want to contribute to bekin, feel free to submit issues, pull requests, or enhancements on
the [GitHub repository](https://github.com/yourrepository/bekin).

## License

This library is open-source and released under the MIT License. See [LICENSE](LICENSE) for more details.

## Support

If you have any questions or need assistance, you can reach out to us
on [our Discord server](https://discord.gg/yourserver) or
by [creating an issue](https://github.com/yourrepository/bekin/issues) on GitHub.

Happy hologramming! 🌟