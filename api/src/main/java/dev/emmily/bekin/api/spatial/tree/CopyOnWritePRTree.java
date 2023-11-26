package dev.emmily.bekin.api.spatial.tree;

import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import dev.emmily.sigma.api.Model;
import org.khelekore.prtree.MBR;
import org.khelekore.prtree.MBRConverter;
import org.khelekore.prtree.PRTree;
import org.khelekore.prtree.SimpleMBR;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Represents a wrapper class for a
 * {@link PRTree} (bulk-loaded RTree)
 * that adds some methods to insert and
 * remove entries from the tree.
 *
 * <p>All modifications cause the
 * rebuild of the tree.</p>
 *
 * @param <T> The stored type
 * @author yusshu - Original code
 * @author emmilydev - Adapted the class to work with Vector3D
 */
public final class CopyOnWritePRTree<T>
  implements Model {
  private static MBR getBoundingRegion(Vector3D min,
                                       Vector3D max) {
    return new SimpleMBR(
      min.getX(),
      max.getX(),
      min.getY(),
      max.getY(),
      min.getZ(),
      max.getZ()
    );
  }

  private final String id;

  /** Executor for bulk-loading the tree */
  private final Executor executor =
    Executors.newSingleThreadExecutor();


  /** Set containing the entries */
  private final Set<T> entries = new HashSet<>();

  /** The wrapped Priority R-Tree */
  private PRTree<T> tree;

  /** The Minimum Bounding Region converter */
  private final MBRConverter<T> converter;

  /** The branch factor of the RTree */
  private final int branchFactor;

  /** Constructs a new CopyOnWritePRTRee using the specified converter */
  public CopyOnWritePRTree(String id,
                           MBRConverter<T> converter,
                           int branchFactor) {
    this.id = id;
    this.converter = converter;
    this.branchFactor = branchFactor;
    this.tree = new PRTree<>(converter, branchFactor);
  }


  /** Executes a simple query to the Priority R-Tree using a single point */
  public Iterable<T> find(Vector3D vector) {
    return find(getBoundingRegion(vector, vector));
  }

  /** Executes a window query to the Priority R-Tree */
  public Iterable<T> find(MBR query) {
    return tree.find(query);
  }

  /** Bulk loads the tree */
  public void bulkLoad(Collection<? extends T> data) {
    this.tree.load(data);
  }

  /** Remove the specified entry from the tree */
  public void remove(T entry) {
    if (entries.remove(entry)) {
      rebuild();
    }
  }

  /** Adds the specified entry to the tree */
  public void add(T entry) {
    if (entries.add(entry)) {
      rebuild();
    }
  }

  /**
   * Rebuilds the tree, this method doesn't
   * check if the entry set was modified, so
   * it must be checked by the caller
   */
  private void rebuild() {
    executor.execute(() -> {
      PRTree<T> newTree = new PRTree<>(converter, branchFactor);
      newTree.load(entries);
      tree = newTree;
    });
  }

  @Override
  public String getId() {
    return id;
  }
}
