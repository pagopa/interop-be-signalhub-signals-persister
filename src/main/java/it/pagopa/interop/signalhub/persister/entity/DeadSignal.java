package it.pagopa.interop.signalhub.persister.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.Instant;

@Getter
@Setter
@Table
public class DeadSignal {
    @Id
    @Column("id")
    private Long id;
    @Column("signal_id")
    private Long signalId;
    @Column("object_id")
    private String objectId;
    @Column("eservice_id")
    private String eserviceId;
    @Column("object_type")
    private String objectType;
    @Column("signal_type")
    private String signalType;
    @CreatedDate
    @Column("tmst_insert")
    private Instant tmstInsert;
    @Column("error")
    private String error;
}
