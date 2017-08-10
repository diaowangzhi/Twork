package com.twork.util;

import com.twork.pojo.User;
import com.twork.vo.Result;
import org.springframework.web.bind.annotation.ResponseBody;

public class Check {
    public static @ResponseBody
    Result checkParameter(User user, Object... parameters) {

        Result result = new Result();

        if (user == null) {
            result.setCode(Code.NOLOGIN);
            return result;
        }

        for (Object parameter : parameters) {
            if (parameter == null) {
                result.setCode(Code.PARAMETER_NOT_MATCH);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }
}
