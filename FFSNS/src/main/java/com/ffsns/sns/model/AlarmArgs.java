package com.ffsns.sns.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Basic;

@Data
@AllArgsConstructor
public class AlarmArgs {

    private Integer fromUserId;

    private Integer targetId;

}
