package org.practice.streamsandfunctions.streams;

import java.util.List;
import java.util.stream.Stream;

public class StreamTraverseDemo {
    public static void main(String[] args) {
        List<String> names = List.of("John", "Jane", "Jack", "Jill");
        Stream<String> stream = names.stream();
        stream.forEach(System.out::println);
        // stream.forEach(System.out::println); // This would throw an IllegalStateException
        // stream has already been operated upon or closed

        /*
        // Intermediate operations (filter, map, skip, limit, sorted...)
// - Do not traverse the stream immediately
// - Return a new Stream
// - Build the processing pipeline
// - Actual traversal starts only when a terminal operation is invoked
         */
        // On top of that, we can only invoke a single terminal operation on a Stream object.
        // we need to create a new stream to traverse the elements again for second terminal operations

        //We can't invoke multiple terminal operations on top of a single Stream object like intermediate operations.
        /*
        The reason is very obvious

terminal means it is going to terminate your Stream, and it is going to give you the output based upon

your instructions.

So once you terminate it, if you try to invoke one more terminal operation, it is it is not going

to make any sense.

That's why inside streams API, we will be able to invoke the terminal operation only once on a given

Stream object.
         */
        /*
        // After a terminal operation, the Stream is consumed.
// To process the same data again, create a new Stream.
// Terminal operation:
// - Executes the stream pipeline
// - Traverses the stream from start to end
// - Consumes (closes) the stream
// - The same Stream cannot be reused
// - To process the data again, create a new Stream from the source
         */
        Stream<String> stream1 = names.stream();
        stream1.forEach(System.out::println);
    }
}
