package com.quinzex.service;

import com.quinzex.dto.RefreshTokenData;

public interface IrefreshTokenService {
    public String createRefreshToken(String email,int tokenVersion);
    public RefreshTokenData validate(String refreshToken);
    public void revoke (String refreshToken);

}
