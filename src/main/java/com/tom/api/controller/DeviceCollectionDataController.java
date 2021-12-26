package com.tom.api.controller;

import com.tom.db.entity.DeviceCollectionData;
import com.tom.db.exception.DataException;
import com.tom.db.exception.DuplicatedException;
import com.tom.db.exception.EntityNotFoundException;
import com.tom.db.exception.UnSupportException;
import com.tom.db.service.IDeviceCollectionDataService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletResponse;


@Getter
@Setter
@RestController
@Api(tags = "DeviceCollectionData 操作", value = "DeviceCollectionData 操作 API 說明")
public class DeviceCollectionDataController {

    private static Logger logger = LoggerFactory.getLogger(DeviceCollectionDataController.class);

    @Autowired(required=true)
    @Qualifier("deviceCollectionDataService")
    private IDeviceCollectionDataService service;

    @ApiOperation(value="新增單一物件(DeviceCollectionData)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=405, message="錯誤參數"),
            @ApiResponse(code=409, message="資料重複"),
            @ApiResponse(code=400, message="無法新增"),
            @ApiResponse(code=500, message="無法新增(其他錯誤)")
    })
    @PostMapping("/deviceCollectionData")
    public DeviceCollectionData create(@ApiParam(required=true, value="請傳入物件的 JSON 格式")
                                       @RequestBody(required=true)
                                       DeviceCollectionData entity,
                                       HttpServletResponse response)  {
        DeviceCollectionData obj = null;
        try{
            if ( !NumberUtils.isNumber(entity.getDataValue())){
                throw new UnSupportException("Unsupported format[" + entity.getDataValue() + "].");
            }
            obj = getService().createEntity(entity);
        }catch(UnSupportException e){
            //405
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        }catch(DataException e){
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(Exception e){
            //500
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return obj;
    }


    @ApiOperation(value="以id取出單一物件(DeviceCollectionData)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=404, message="找不到"),
            @ApiResponse(code=405, message="錯誤參數"),
            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/deviceCollectionData/{id}")
    public DeviceCollectionData get(@ApiParam(required=true, value="請傳入物件(DeviceCollectionData)的id")
                                    @PathVariable
                                    String id,
                                    HttpServletResponse response)  {
        DeviceCollectionData obj = null;
        try{
            if ( !NumberUtils.isNumber(id)){
                throw new UnSupportException("Unsupported format[" + id +"].");
            }
            obj = getService().getEntityById(Long.parseLong(id));
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(UnSupportException e){
            //405
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return obj;
    }
}
