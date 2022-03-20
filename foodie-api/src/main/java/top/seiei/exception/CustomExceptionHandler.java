package top.seiei.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import top.seiei.utils.ServerResponse;

/**
 * 全局捕获异常
 * @RestControllerAdvice 注解启动后，被 @ExceptionHandler、@InitBinder、@ModelAttribute 注解的方法，都会作用在 被 @RequestMapping 注解的方法上
 * @ExceptionHandler(value = Exception.class) ExceptionHandler的作用是用来捕获指定的异常。
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 上传文件超过 500 kb 异常
     * @param ex maxUploadSizeExceededException 对象
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ServerResponse handlerMaxUpLoadFile(MaxUploadSizeExceededException ex) {
        return ServerResponse.createdByError("文件上传大小不能超过500k");
    }
}
