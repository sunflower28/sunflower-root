package com.sunflower.framework.token;

import java.io.Serializable;

public interface UserProfile extends Serializable {
    String getLoginId();

    String getName();
}
