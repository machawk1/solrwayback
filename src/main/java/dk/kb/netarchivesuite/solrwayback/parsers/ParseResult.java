package dk.kb.netarchivesuite.solrwayback.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ParseResult {
  
  private String replaced;
  private AtomicInteger numberOfLinksReplaced = new AtomicInteger(0);
  private AtomicInteger numberOfLinksNotFound = new AtomicInteger(0);
  private final List<Timing> timings = Collections.synchronizedList(new ArrayList<>());
  
  public ParseResult(){
  }

  public String getReplaced() {
    return replaced;
  }

  public void setReplaced(String replaced) {
    this.replaced = replaced;
  }

  public int getNumberOfLinksReplaced() {
    return numberOfLinksReplaced.get();
  }


  public void setNumberOfLinksReplaced(int numberOfLinksReplaced) {
    this.numberOfLinksReplaced.set(numberOfLinksReplaced);
  }
  public void incReplaced() {
    numberOfLinksReplaced.incrementAndGet();
  }


  public int getNumberOfLinksNotFound() {
    return numberOfLinksNotFound.get();
  }


  public void setNumberOfLinksNotFound(int numberOfLinksNotFound) {
    this.numberOfLinksNotFound.set(numberOfLinksNotFound);
  }
  public void incNotFound() {
    numberOfLinksNotFound.incrementAndGet();
  }

  /**
   * Timings are used for tracking how long individual parts of the processing took.
   * Timings are expected to add up to total processing time: No overlaps!
   * @param designation a distinct part of total processing.
   * @param ms the number of milliseconds it took to handle the distinct part.
   */
  public void addTiming(String designation, long ms) {
    timings.add(new Timing(designation, ms));
  }

  public List<Timing> getTimings() {
    return timings;
  }

  /**
   * @return the added timings as a convenient Stream, usable for logging.
   */
  public String getTimingsString() {
    if (timings.isEmpty()) {
      return "N/A";
    }
    long totalMS = timings.stream().map(timing -> timing.ms).mapToLong(Long::longValue).sum();
    String details = timings.stream().map(Timing::toString).collect(Collectors.joining(", "));
    return "total:" + totalMS + "ms (" + details + ")";
  }

  public void time(String getResources, Object o) {
    // TODO: Implement this
  }

  public String toString() {
    return String.format("ParseResult(content=%d bytes, links replaced=%d, not_found=%d. Timing %s)",
                         replaced == null ? 0 : replaced.length(),
                         numberOfLinksReplaced.get(), numberOfLinksNotFound.get(), getTimingsString());
  } 

  public static class Timing {
    public String designation;
    public long ms;

    public Timing(String designation, long ms) {
      this.designation = designation;
      this.ms = ms;
    }

    public String toString() {
      return designation + ":" + ms + "ms";
    }
  }
}
