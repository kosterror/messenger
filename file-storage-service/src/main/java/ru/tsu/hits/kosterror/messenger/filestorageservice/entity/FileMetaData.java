package ru.tsu.hits.kosterror.messenger.filestorageservice.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file_meta_data")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaData {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Long sizeInBytes;

    @Column(name = "upload_date_time")
    private LocalDateTime uploadDateTime;

    @Column(name = "author_id")
    private UUID authorId;

}
