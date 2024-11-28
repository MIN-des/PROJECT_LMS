package com.project.lms.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "allBoardFile")
@Data
public class AllBoardFile {

    @Id
    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    private String fileType;

    @ManyToOne
    @JoinColumn(nullable = false, name = "allBno")
    private AllBoard allBoard;
}
