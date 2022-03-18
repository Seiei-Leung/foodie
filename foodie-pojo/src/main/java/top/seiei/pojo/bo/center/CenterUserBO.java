package top.seiei.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

@ApiModel(value = "用户中心用户对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
public class CenterUserBO {

    @NotEmpty(message = "用户昵称不能为空")
    @Length(max = 12, message = "昵称长度不能超过12")
    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;

    @Length(max = 12, message = "真名长度不能超过12")
    @ApiModelProperty(value = "真名", name = "realname")
    private String realname;

    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号码格式不正确")
    @ApiModelProperty(value = "手机号码", name = "mobile")
    private String mobile;

    @Email
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    @Max(value = 2, message = "性别格式不正确")
    @Min(value = 0, message = "性别格式不正确")
    @ApiModelProperty(value = "性别", name = "sex")
    private Integer sex;

    @Past
    @ApiModelProperty(value = "生日", name = "birthday")
    private Date birthday;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
