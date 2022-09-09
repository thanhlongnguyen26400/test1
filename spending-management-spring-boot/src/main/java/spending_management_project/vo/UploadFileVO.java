package spending_management_project.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileVO {
    private String fileName;
    private String url;
    private String fileType;
    private long size;

}
