/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;


import java.util.Date;

/**
 * @author Cymric
 */

public record JobTime(Date startTime,
                      Date endTime,
                      String activity) {
}
