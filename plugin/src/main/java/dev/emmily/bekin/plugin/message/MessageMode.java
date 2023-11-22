package dev.emmily.bekin.plugin.message;

public interface MessageMode {
  /**
   * Represents an information message,
   * whose sound will be a click.
   */
  String INFO = "info";
  /**
   * Represents an information message
   * whose purpose is telling the target
   * that an operation was successfully
   * completed. The played sound will be
   * an experience level-up.
   */
  String SUCCESS = "success";
  /**
   * Represents an information message
   * whose purpose is, in contrast to {@link #SUCCESS},
   * telling the target that an operation
   * failed to complete. The played sound
   * will be a note bass.
   */
  String ERROR = "warning";
}
