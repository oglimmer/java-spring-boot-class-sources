package com.oglimmer.kniffel.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter // this will generate getters for all attributes
@Setter // this will generate setter for all attributes
@ToString
public class ServerStatus {
    private long serverTime;
    private String serverVersion;
    private String serverName;
    private String serverStatus;
}
