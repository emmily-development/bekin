package dev.emmily.bekin.api.hologram.spatial;

import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.spatial.tree.CopyOnWritePRTree;
import dev.emmily.sigma.api.repository.ModelRepository;

public class PrTreeRegistry {
  private final ModelRepository<CopyOnWritePRTree<ClickableHologramLine>> prTreeRepository;

  public PrTreeRegistry(ModelRepository<CopyOnWritePRTree<ClickableHologramLine>> prTreeRepository) {
    this.prTreeRepository = prTreeRepository;
  }

  public CopyOnWritePRTree<ClickableHologramLine> getOrCreate(String language) {
    CopyOnWritePRTree<ClickableHologramLine> prTree = prTreeRepository.find(language);

    if (prTree == null) {
      prTree = new CopyOnWritePRTree<>(language, HologramMBRConverter.forLanguage(language), 30);
      prTreeRepository.create(prTree);
    }

    return prTree;
  }
}
