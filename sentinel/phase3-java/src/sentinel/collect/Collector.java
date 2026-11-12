package sentinel.collect;

/**
 * Where the contract lines come from.
 *
 * INTERFACE, not abstract class: the three sources share no data at all. One
 * reads a pipe, one reads a file, one launches a program. They only share
 * what they can DO: hand over the next line.
 *
 * This is the JSON contract of the whole course, now inside Java: Sentinel
 * does not know, and must not know, who produces the line.
 */
public interface Collector {

    /** The next raw line of the contract, or null when the stream has ended. */
    String nextLine();

    /** Short description for the header, for example "replay muestra.txt". */
    String describe();

    /** Releases whatever this source holds: a file, a process. */
    void close();
}
