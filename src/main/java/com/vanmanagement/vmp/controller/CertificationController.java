package com.vanmanagement.vmp.controller;

import com.vanmanagement.vmp.certification.IamportCertificationResponse;
import com.vanmanagement.vmp.certification.IamportService;
import com.vanmanagement.vmp.errors.PhoneAlreadyExistsException;
import com.vanmanagement.vmp.users.UserService;
import org.springframework.web.bind.annotation.*;

import static com.vanmanagement.vmp.utils.ApiUtils.ApiResult;
import static com.vanmanagement.vmp.utils.ApiUtils.success;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/certifications")
public class CertificationController {
    private final IamportService iamportService;


    public CertificationController(IamportService iamportService) {
        this.iamportService = iamportService;
    }

    @PostMapping("/{impUid}")
    public ApiResult<IamportCertificationResponse> getSignUpCertification(@PathVariable String impUid) {
        IamportCertificationResponse response = iamportService.getCertification(impUid);

        return success(response);
    }
}

