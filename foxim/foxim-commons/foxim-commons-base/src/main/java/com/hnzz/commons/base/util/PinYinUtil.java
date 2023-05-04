package com.hnzz.commons.base.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * @author HB on 2023/2/16
 * TODO 用户名汉字转拼音
 */
public class PinYinUtil {
    public static String toPinyin(String chinese) {
        // 判断是否为汉字
        String regex = "[\\u4E00-\\u9FA5]+";
        if (chinese.matches(regex)) {
            StringBuilder sb = new StringBuilder();
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setVCharType(HanyuPinyinVCharType.WITH_V);
            for (int i = 0; i < chinese.length(); i++) {
                char c = chinese.charAt(i);
                // 判断是否为汉字
                if (Character.toString(c).matches(regex)) {
                    try {
                        String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
                        if (pinyin != null) {
                            sb.append(pinyin[0]);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return chinese;
        }
    }
}
