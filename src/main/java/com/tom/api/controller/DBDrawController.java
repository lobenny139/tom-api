package com.tom.api.controller;

import com.tom.db.exception.UnSupportException;
import com.tom.db.service.IStockDataService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@RestController
@Api(tags = "Database領取操作", value = "Database領取操作 API 說明")
public class DBDrawController {
    private static Logger logger = LoggerFactory.getLogger(DBDrawController.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Autowired(required=true)
    @Qualifier("stockDataService")
    private IStockDataService service;

    @ApiOperation(value="X個人領Y個獎品")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=404, message="找不到"),
            @ApiResponse(code=405, message="錯誤參數"),
            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/db/{x}/get/{y}")
    public void get(    @ApiParam(required=true, value="請傳入人數")
                        @PathVariable String x,
                        @ApiParam(required=true, value="請傳入獎品數")
                        @PathVariable String y,
                        HttpServletResponse response)  {
        try{
            if ( !NumberUtils.isNumber(x)){
                throw new UnSupportException("Unsupported format[" + x +"].");
            }
            if ( !NumberUtils.isNumber(y)){
                throw new UnSupportException("Unsupported format[" + y +"].");
            }

//            logger.info("設定產品數為"+y+"個");
//            getService().update("prod-1234", Integer.parseInt(y));

            logger.info("開始" + x + "個人搶" + y+"個獎品");
            for(int i = 0; i < Integer.parseInt(x); i++){
                new Thread(new MyThread(System.currentTimeMillis())).start();
            }

        }catch(Exception e){
            //500
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    class MyThread implements Runnable {
        long startTime;
        public MyThread(long time){
            this.startTime = time;
        }
        @Override
        public void run() {
            long left;
            long endTime = System.currentTimeMillis();
            if( (left = getService().update("prod-1234")) >= 0){
                logger.info(Thread.currentThread().getName() + " 在 " + getSimpleDateFormat().format(new Date(startTime)) + " 參加領獎, 在 " + getSimpleDateFormat().format(new Date(endTime) ) +   " 領到, 剩" + left +"個, 耗時 " +  (endTime - startTime) + "ms <<<<<<<<" )  ;
            }
            else{
                logger.info(Thread.currentThread().getName() + " 在 " + getSimpleDateFormat().format(new Date(startTime)) + " 參加領獎, 在 " + getSimpleDateFormat().format(new Date(endTime) ) +   " 沒領到, 剩" + left + "個, 耗時 " +  (endTime - startTime) + "ms")  ;
            }
        }
    }
}
