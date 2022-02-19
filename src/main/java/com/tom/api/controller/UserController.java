package com.tom.api.controller;

import com.tom.db.entity.UserData;
import com.tom.db.exception.EntityNotFoundException;
import com.tom.db.exception.UnSupportException;
import com.tom.db.service.IUserDataService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
@RestController
@Api(tags = "UserData 操作", value = "UserData 操作 API 說明")
public class UserController {


    @Autowired(required=true)
    @Qualifier("userDataService")
    private IUserDataService service;


    @ApiOperation(value="以id取出單一物件(UserData)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=404, message="找不到"),
            @ApiResponse(code=405, message="錯誤參數"),
            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/user/{id}")
    public UserData get(@ApiParam(required=true, value="請傳入物件(UserData)的id")
                                    @PathVariable String id,
                                    HttpServletResponse response)  {
        UserData obj = null;
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
