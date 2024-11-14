package com.mycompany.minio.domainmodel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Media {
    private String title;
    private String description;
    private String url;
}
