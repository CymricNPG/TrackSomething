/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import java.util.List;

/**
 * @author Cymric
 */
public record JobDescription(String project,
                             String job,
                             List<JobTime> jobTimes) {
}
