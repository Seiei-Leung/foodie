package top.seiei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.seiei.pojo.Users;
import top.seiei.pojo.vo.UserVO;
import top.seiei.resource.FileUpload;
import top.seiei.service.FdfsService;
import top.seiei.service.center.CenterUserService;
import top.seiei.utils.CookieUtils;
import top.seiei.utils.JsonUtils;
import top.seiei.utils.ServerResponse;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "用户中心模块", tags = {"用于用户中心模块的相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Resource
    private CenterUserService centerUserService;

    @Resource
    private FdfsService fdfsService;

    @Resource
    private FileUpload fileUpload;

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
        if (pictureFile != null) {
            // 图片文件后缀名
            String ext = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
            // 判断文件格式是否为图片
            if (!ext.equalsIgnoreCase("jpg")) {
                return ServerResponse.createdByError("文件格式不正确");
            }

            // 上传到 FastDFS
            String path = fdfsService.uploadFace(pictureFile, ext);
            if (StringUtils.isBlank(path)) {
                return ServerResponse.createdByError("上传时，FastDFS发生错误！");
            }

            // 拼接 url
            String urlOfFace = fileUpload.getImageUserFaceUrl() +  path;

            // 保存 url 到数据库
            Users user = centerUserService.updateUserFace(userId, urlOfFace);

            // 更新 cookies
            user = this.setNullProperty(user);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            String userUniqueToken = JsonUtils.jsonToPojo(CookieUtils.getCookieValue(request, "user", true), UserVO.class).getUserUniqueToken();
            userVO.setUserUniqueToken(userUniqueToken);
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);
            return ServerResponse.createdBySuccess(JsonUtils.objectToJson(userVO));
        } else {
            return ServerResponse.createdByError("文件不能为空");
        }
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
