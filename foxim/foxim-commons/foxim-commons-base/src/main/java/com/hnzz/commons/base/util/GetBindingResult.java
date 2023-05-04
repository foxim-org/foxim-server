package com.hnzz.commons.base.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author HB
 * @Classname GetBindingResult
 * @Date 2023/1/9 15:01
 * @Description TODO
 */
public class GetBindingResult {
    public static String validErrorCollect(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMsgBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError e : fieldErrors) {
                String err = e.getDefaultMessage()+";\n";
                errorMsgBuilder.append(err);
            }
            return errorMsgBuilder.toString();
        }
        return null;
    }
}
