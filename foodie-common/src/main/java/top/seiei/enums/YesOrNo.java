package top.seiei.enums;

/**
 * 是与否的枚举
 */
public enum YesOrNo {
    Yes(1, "是"),
    No(0, "否");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value){
        this.type = type;
        this.value = value;
    }
}
