package dev.emmily.bekin.api.hologram.registry;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import dev.emmily.sigma.api.repository.CachedAsyncModelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Provides a registry for managing holograms using a CachedAsyncModelRepository.
 * It allows you to register, unregister, get, find, save, and delete holograms with ease.
 */
public class HologramRegistry {
  private final CachedAsyncModelRepository<Hologram> hologramRepository;

  /**
   * Constructs a new HologramRegistry with the specified CachedAsyncModelRepository.
   *
   * @param hologramRepository The repository for managing holograms.
   */
  public HologramRegistry(CachedAsyncModelRepository<Hologram> hologramRepository) {
    this.hologramRepository = hologramRepository;
  }

  /**
   * Registers a hologram in the repository for caching.
   *
   * @param hologram The hologram to register.
   */
  public void register(Hologram hologram) {
    hologramRepository.cache(hologram);
  }

  /**
   * Unregisters a hologram from the cache repository.
   *
   * @param hologram The hologram to unregister.
   */
  public void unregister(Hologram hologram) {
    hologramRepository.deleteCached(hologram);
  }

  /**
   * Unregisters a hologram from the cache repository using its ID.
   *
   * @param id The ID of the hologram to unregister.
   */
  public void unregister(String id) {
    hologramRepository.deleteCached(id);
  }

  /**
   * Retrieves a hologram from the repository using its ID.
   *
   * @param id The ID of the hologram to retrieve.
   * @return The hologram with the specified ID or null if not found.
   */
  public Hologram get(String id) {
    return hologramRepository.get(id);
  }

  /**
   * Asynchronously finds and retrieves a hologram from the repository using its ID.
   *
   * @param id The ID of the hologram to find.
   * @return A CompletableFuture that resolves to the hologram with the specified ID or null if not found.
   */
  public CompletableFuture<Hologram> find(String id) {
    return hologramRepository.findAsync(id);
  }

  /**
   * Asynchronously retrieves a hologram from the repository using its ID, and if not found, creates a new one.
   *
   * @param id The ID of the hologram to retrieve or create.
   * @return A CompletableFuture that resolves to the hologram with the specified ID or a new hologram if not found.
   */
  public CompletableFuture<Hologram> getOrFind(String id) {
    return hologramRepository.getOrFindAsync(id);
  }

  /**
   * Retrieves the list of cached holograms.
   *
   * @return A CompletableFuture representing the completion of the get operation.
   */
  public List<Hologram> getAll() {
    return hologramRepository.getAll();
  }

  public List<Hologram> getNearbyHolograms(Vector3D origin,
                                           double distance) {
    List<Hologram> nearbyHolograms = new ArrayList<>();

    for (Hologram hologram : getAll()) {
      System.out.println("Iterating over all holograms");
      Vector3D position = hologram.getPosition();

      if (!position.getWorld().equals(origin.getWorld())) {
        System.out.println("Different world");
        continue;
      }

      double distanceSquared = origin.distanceSquared(position);
      System.out.println("Distance squared: " + distanceSquared + ", required: " + Math.pow(distance, 2));
      if (distanceSquared <= Math.pow(distance, 2)) {
        nearbyHolograms.add(hologram);
      }
    }

    return nearbyHolograms;
  }

  /**
   * Retrieves the list of holograms in the repository.
   *
   * @return A CompletableFuture representing the completion of the find operation.
   */
  public CompletableFuture<List<Hologram>> findAll() {
    return hologramRepository.findAllAsync();
  }

  /**
   * Asynchronously saves a hologram in the repository, updating or creating it as needed.
   *
   * @param hologram The hologram to save.
   * @return A CompletableFuture representing the completion of the save operation.
   */
  public CompletableFuture<?> save(Hologram hologram) {
    return CompletableFuture.runAsync(() -> {
      hologramRepository.deleteCached(hologram);
      hologramRepository.create(hologram);
    });
  }

  /**
   * Asynchronously saves a hologram in the repository using its ID, updating or creating it as needed.
   *
   * @param id The ID of the hologram to save.
   * @return A CompletableFuture representing the completion of the save operation.
   */
  public CompletableFuture<?> save(String id) {
    return save(get(id));
  }

  /**
   * Asynchronously deletes a hologram from the repository using its ID.
   *
   * @param hologram The hologram to delete.
   * @return A CompletableFuture representing the completion of the delete operation.
   */
  public CompletableFuture<?> delete(Hologram hologram) {
    return hologramRepository.deleteAsync(hologram.getId());
  }

  /**
   * Asynchronously deletes a hologram from the repository using its ID.
   *
   * @param id The ID of the hologram to delete.
   * @return A CompletableFuture representing the completion of the delete operation.
   */
  public CompletableFuture<?> delete(String id) {
    return delete(get(id));
  }
}
