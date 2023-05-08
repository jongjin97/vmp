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
@RequestMapping("/certifications")
public class CertificationController {
    private final IamportService iamportService;

    private final UserService userService;

    public CertificationController(IamportService iamportService, UserService userService) {
        this.iamportService = iamportService;
        this.userService = userService;
    }

    @PostMapping("/{impUid}")
    public ApiResult<IamportCertificationResponse> getSignUpCertification(@PathVariable String impUid) {
        IamportCertificationResponse response = iamportService.getCertification(impUid);

        return success(response);
    }
}

