package com.tom.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "系統狀態", description = "系統狀態 API 操作說明")
@Getter
@Setter
@RestController
public class StatusController {

    private static Logger logger = LoggerFactory.getLogger(StatusController.class);

    @GetMapping("/status")
    @ApiOperation(value="檢查系統狀態")
    @ApiResponses(value={
            @ApiResponse(code=200, message="系統正常運行中")
    })
    public Map<String, String> getStatus() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("message", "OK");
        logger.info("status: " + result.get("message"));
        return result;
    }
}
