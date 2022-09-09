package spending_management_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import spending_management_project.annotation.CurrentUser;
import spending_management_project.component.ResultHelper;
import spending_management_project.domain.UserProfileDomain;
import spending_management_project.po.User;
import spending_management_project.service.FileStorageService;
import spending_management_project.tools.Assert;
import spending_management_project.vo.UploadFileVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);


    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @CurrentUser User user) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/")
                .path(fileName)
                .toUriString();
        userProfileDomain.saveAvatarUrl(fileDownloadUri,user.getId());
        return this.resultHelper.successResp(new UploadFileVO(fileName, fileDownloadUri,
                file.getContentType(), file.getSize()), HttpStatus.OK);
    }

//    @PostMapping("/uploadMultipleFiles")
//    public List<UploadFileVO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.stream(files)
//                .map(this::uploadFile)
//                .collect(Collectors.toList());
//    }

    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private final FileStorageService fileStorageService;

    private final ResultHelper resultHelper;

    private final UserProfileDomain userProfileDomain;
    @Autowired
    public FileController(FileStorageService fileStorageService, ResultHelper resultHelper, UserProfileDomain userProfileDomain) {
        Assert.defaultNotNull(fileStorageService);
        Assert.defaultNotNull(resultHelper);
        Assert.defaultNotNull(userProfileDomain);
        this.fileStorageService = fileStorageService;
        this.resultHelper = resultHelper;
        this.userProfileDomain = userProfileDomain;
    }
}
