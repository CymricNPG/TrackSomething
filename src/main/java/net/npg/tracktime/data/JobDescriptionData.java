/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import java.util.List;

/**
 * @author Cymric
 */
public record JobDescriptionData(String project,
                                 String job,
                                 List<JobTimeData> jobTimes) {
}
