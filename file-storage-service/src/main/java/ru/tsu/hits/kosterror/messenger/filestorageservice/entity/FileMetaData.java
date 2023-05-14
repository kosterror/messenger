package ru.tsu.hits.kosterror.messenger.filestorageservice.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "upload_date_time")
    private LocalDateTime uploadDateTime;

    @Column(name = "author_id")
    private UUID authorId;

}
