/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Cymric
 */
public class JobStorage {

    public static void writeToStorage(TrackTimeData trackTimeData) throws PropertyException, JAXBException {
        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(TrackTimeData.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to System.out
        m.marshal(trackTimeData, getStorageFile());
    }

    public static TrackTimeData readFromStorage() throws JAXBException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(TrackTimeData.class);
        Unmarshaller um = context.createUnmarshaller();
        try {
            return (TrackTimeData) um.unmarshal(new FileReader(getStorageFile()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return new TrackTimeData();
    }

    public static File getStorageFile() {
        File file = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "trackTime.xml");
        System.out.println(file.getAbsolutePath());
        return file;
    }

    public static TrackTimeData createDefaultData() {
        List<JobDescription> jobDescriptions = new ArrayList<>();
        jobDescriptions.add(new JobDescription("HPM", "Design"));
        jobDescriptions.add(new JobDescription("HPM", "Implementierung"));
        jobDescriptions.add(new JobDescription("ePROTAS", "Beratung"));
        jobDescriptions.add(new JobDescription("IT", "Administration"));
        TrackTimeData trackTimeData = new TrackTimeData();
        trackTimeData.setJobDescriptions(jobDescriptions);
        return trackTimeData;
    }
}
