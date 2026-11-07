package io.ethertale.findadicethymeleaf.feedbackReport.model;

public enum FeedbackReportType {
    BUG_REPORT("Bug Report"),
    FEATURE_REQUEST("Feature Request"),
    THOUGHTS("Thoughts"),
    REPORT("Report something/someone");

    public final String label;

    FeedbackReportType(String label) {
        this.label = label;
    }
}
