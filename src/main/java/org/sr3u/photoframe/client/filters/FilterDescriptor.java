package org.sr3u.photoframe.client.filters;

import lombok.AllArgsConstructor;
import lombok.Value;
import sr3u.streamz.streams.Streamex;

import java.util.List;

@Value
@AllArgsConstructor
public class FilterDescriptor {
    String name;
    List<String> parameters;

    @Override
    public String toString() {
        return name + " " + Streamex.ofStream(parameters.stream()).mapToString().joined(" ");
    }
}
