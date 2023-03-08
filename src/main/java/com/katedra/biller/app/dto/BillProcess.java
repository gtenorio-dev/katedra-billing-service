package com.katedra.biller.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class BillProcess {

    private List<BillProcessDetail> details;
    private String error;

    public BillProcess () {
        details = new ArrayList<>();
        error = null;
    }

    public void addDetail(BillProcessDetail detail){
        getDetails().add(detail);
    }

}
