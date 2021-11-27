package net.npg.tracktime.model;

import net.npg.tracktime.data.JobDescriptionData;
import net.npg.tracktime.data.JobTimeData;
import net.npg.tracktime.data.TrackTimeData;

import java.util.List;
import java.util.stream.Collectors;

public final class ModelConversion {

    private ModelConversion() {
    }

    public static TrackTimeDataModel convert(final TrackTimeData trackTimeData) {
        final TrackTimeDataModel trackTimeDataModel = new TrackTimeDataModel();
        trackTimeData.jobDescriptions().stream()
                .map(ModelConversion::convert)
                .forEach(trackTimeDataModel::addJobDescription);
        return trackTimeDataModel;
    }

    private static JobDescriptionModel convert(final JobDescriptionData jd) {
        final JobDescriptionModel jobDescriptionModel = new JobDescriptionModel(jd.project(), jd.job());
        jd.jobTimes().stream()
                .map(jt -> new JobTimeModel(jt.startTime(), jt.endTime(), jt.activity()))
                .forEach(jobDescriptionModel::addjobTime);
        return jobDescriptionModel;
    }

    public static TrackTimeData convert(final TrackTimeDataModel data) {
        final List<JobDescriptionData> jobDescriptionData = data.getJobDescriptions().stream()
                .map(jd -> new JobDescriptionData(jd.getProject(), jd.getJob(),
                        convert(jd.getJobTimes())))
                .collect(Collectors.toList());
        return new TrackTimeData(jobDescriptionData);
    }

    private static List<JobTimeData> convert(final List<JobTimeModel> jobTimes) {
        return jobTimes.stream()
                .map(jt -> new JobTimeData(jt.getStartTime(), jt.getEndTime(), jt.getActivity()))
                .collect(Collectors.toList());
    }

}
