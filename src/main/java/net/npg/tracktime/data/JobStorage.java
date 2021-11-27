/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Cymric
 */
public final class JobStorage {
    private JobStorage() {
    }

    public static void writeToStorage(final TrackTimeData trackTimeData) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(getStorageFile(), trackTimeData);
    }

    public static TrackTimeData readFromStorage() {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(getStorageFile(), TrackTimeData.class);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        return new TrackTimeData(new ArrayList<>());
    }

    private static File getStorageFile() {
        final File file = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "trackTime.json");
        System.out.println(file.getAbsolutePath());
        return file;
    }
}
