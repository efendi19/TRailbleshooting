package com.tehkotak.trailbleshooting.view;

import com.tehkotak.trailbleshooting.DataModel;

import java.util.ArrayList;
import java.util.List;

public interface ReportView {

    //belum bisa dipake model MVP nya, ini masih belum menggunakan pattern
    void setReportView(ArrayList<DataModel> reportView);
}
