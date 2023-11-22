package dev.emmily.bekin.plugin.message;

import dev.emmily.sigma.api.Model;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a long list of messages split into a fixed number of pages
 * that players can navigate through.
 */
public class PaginatedMessage
  implements Model {
  private final String id;
  private final List<List<String>> pages;
  private final Navigation navigation;
  private final Map<String, Integer> audience;

  public PaginatedMessage(String id,
                          List<String> message,
                          int linesPerPage,
                          Navigation navigation) {
    this.id = id;
    this.navigation = navigation;
    this.pages = new ArrayList<>();
    this.audience = new HashMap<>();

    int totalElements = message.size();
    int startIndex = 0;

    while (startIndex < totalElements) {
      int endIndex = Math.min(startIndex + linesPerPage, totalElements);
      pages.add(message.subList(startIndex, endIndex));

      startIndex = endIndex;
    }
  }

  public List<String> getCurrentPage(Player player) {
    int currentPage = audience.computeIfAbsent(player.getUniqueId().toString(), k -> 0);

    if (currentPage >= 0 && currentPage < pages.size()) {
      return pages.get(currentPage);
    }

    return new ArrayList<>();
  }

  public boolean hasNextPage(Player player) {
    int currentPage = audience.computeIfAbsent(player.getUniqueId().toString(), k -> 0);

    return currentPage < pages.size() - 1;
  }

  /**
   * Advances to the next page, if possible.
   */
  public void nextPage(Player player) {
    int currentPage = audience.computeIfAbsent(player.getUniqueId().toString(), k -> 0);

    if (hasNextPage(player)) {
      audience.put(player.getUniqueId().toString(), ++currentPage);
    }
  }

  public boolean hasPreviousPage(Player player) {
    int currentPage = audience.computeIfAbsent(player.getUniqueId().toString(), k -> 0);

    return currentPage > 0;
  }

  /**
   * Goes back to the previous page, if possible.
   */
  public void previousPage(Player player) {
    int currentPage = audience.computeIfAbsent(player.getUniqueId().toString(), k -> 0);

    if (hasPreviousPage(player)) {
      audience.put(player.getUniqueId().toString(), ++currentPage);
    }
  }

  public Navigation getNavigation() {
    return navigation;
  }

  @Override
  public String getId() {
    return id;
  }

  public static class Navigation {
    private static final Pattern FOOTER_PATTERN = Pattern.compile(".*?(%previous%)(.*?)(%next%).*");

    private final Component previousPage;
    private final Component nextPage;
    private final Component footer;

    public Navigation(Component previousPage,
                      Component nextPage,
                      String footer) {
      this.previousPage = previousPage;
      this.nextPage = nextPage;

      Matcher matcher = FOOTER_PATTERN.matcher(footer);

      if (matcher.find()) {
        String previousPlaceholder = matcher.group(1);
        String nextPlaceholder = matcher.group(3);

        this.footer = Component.text(footer
          .replace(previousPlaceholder, LegacyComponentSerializer.legacyAmpersand().serialize(previousPage))
          .replace(nextPlaceholder, LegacyComponentSerializer.legacySection().serialize(nextPage))
        );
      } else {
        throw new IllegalArgumentException("Invalid footer format");
      }
    }

    public Component getPreviousPage() {
      return previousPage;
    }

    public Component getNextPage() {
      return nextPage;
    }

    public Component getFooter() {
      return footer;
    }
  }
}
