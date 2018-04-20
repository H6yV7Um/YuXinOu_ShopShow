package com.example.videolibrary.videoupload.impl;

import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.common.QCloudClientException;

/**
 * Created by carolsuo on 2017/10/9.
 */

public class TVCDirectCredentialProvider extends BasicLifecycleCredentialProvider {
    private String secretId;
    private String secretKey;
    private String token;
    private long expiredTime;

    public TVCDirectCredentialProvider(String secretId, String secretKey, String token, long expiredTime) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.token = token;
        this.expiredTime = expiredTime;
    }

    @Override
    protected QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException {
        return new SessionQCloudCredentials(secretId, secretKey, token, expiredTime);
    }
}
