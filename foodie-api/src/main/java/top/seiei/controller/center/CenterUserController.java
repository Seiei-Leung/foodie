package top.seiei.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.seiei.controller.BaseController;
import top.seiei.pojo.Users;
import top.seiei.pojo.bo.center.CenterUserBO;
import top.seiei.resource.FileUpload;
import top.seiei.service.center.CenterUserService;
import top.seiei.utils.CookieUtils;
import top.seiei.utils.DateUtil;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户中心模块", tags = {"用于用户中心模块的相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    static final Logger logger = LoggerFactory.getLogger(CenterUserController.class);

    @Resource
    private CenterUserService centerUserService;

    @Resource
    private FileUpload fileUpload;

    /**
     * 更新用户消息
     * @param userId 用户 Id
     * @param centerUserBO 用户信息BO
     * @param bindingResult 检验的错误消息
     * @param request request 对象
     * @param response response 对象
     * @return
     */
    @ApiOperation(value = "更新用户消息", notes = "更新用户消息", httpMethod = "POST")
    @PostMapping("/update")
    public ServerResponse updateUserInfo(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @Valid @RequestBody CenterUserBO centerUserBO,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 使用 hibernate 检验数据的正确性
        // 限制注释必须声明在 Entity 中;
        // 在 controller 层需要使用 @Valid 注释，并需要添加 BindingResult 接收检验结果
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> errorObj = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                // 键为发生错误的属性名，值为错误消息
                errorObj.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ServerResponse.createdByError(errorObj);
        }
        // 更新 cookie
        Users users = setNullProperty(centerUserService.updateUserInfo(userId, centerUserBO));
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);

        return ServerResponse.createdBySuccess();
    }

    /**
     * 上传用户头像
     * @param userId 用户ID
     * @param pictureFile 头像
     * @return
     */
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public ServerResponse uploadFace(
            @ApiParam(name = "userId", value = "用户主键", required = true)
            @RequestParam String userId,
            @ApiParam(name = "pictureFile", value = "用户头像", required = true)
            MultipartFile pictureFile,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 组装文件存储的 path
        // 在路径上为每个用户增加一个userid文件夹，区分不同用户上传
        String filePath = fileUpload.getImageUserFaceLocation() + File.separator + userId;
        if (pictureFile != null) {
            // 图片文件后缀名
            String ext = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
            // 判断文件格式是否为图片
            if (!ext.equalsIgnoreCase("jpg")) {
                return ServerResponse.createdByError("文件格式不正确");
            }
            // 文件名：face-{userid}.png
            String newFileName = "face-" + userId + "." + ext;
            filePath = filePath + File.separator + newFileName;
            File saveFile = new File(filePath);
            // 获取文件所在的文件夹，注意 getParentFile 返回的 File 对象是否为 null
            // 并不是根据该文件夹是否已存在，而只是根据 filePath 字符串中是否还有父文件夹
            // mkdirs 方法它会有判断是否创建文件夹，成功创建文件夹会返回 true
            if (saveFile.getParentFile() != null) {
                saveFile.getParentFile().mkdirs();
            }
            // 转化文件
            try {
                // 这里直接传入 saveFile 会报错
                pictureFile.transferTo(new File(saveFile.getAbsolutePath()));
                // 更新数据库，url 最后添加时间戳防止浏览器缓存
                Users users = centerUserService.updateUserFace(userId, fileUpload.getImageUserFaceUrl() + userId + "/" + newFileName + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));
                users = this.setNullProperty(users);
                CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);
            } catch (IOException e) {
                e.printStackTrace();
                return ServerResponse.createdByError("保存文件时出错了！！");
            }
        } else {
            return ServerResponse.createdByError("文件不能为空");
        }
        return ServerResponse.createdBySuccess();
    }

    /**
     * 脱敏逻辑
     * @param users 用户
     * @return
     */
    private Users setNullProperty(Users users)
    {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setBirthday(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        return users;
    }
}
