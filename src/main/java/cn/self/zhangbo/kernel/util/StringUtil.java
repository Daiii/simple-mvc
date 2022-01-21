package cn.self.zhangbo.kernel.util;

public class StringUtil
{
    
    /**
     * 是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isEmpty(CharSequence str)
    {
        return str == null || str.length() == 0;
    }
    
    /**
     * 是否不为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isNotEmpty(CharSequence str)
    {
        return !isEmpty(str);
    }
    
    /**
     * 是否相等
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return boolean
     */
    public static boolean equals(CharSequence str1, CharSequence str2)
    {
        if (null == str1)
        {
            return str2 == null;
        }
        
        if (null == str2)
        {
            return Boolean.FALSE;
        }
        
        return str1.toString().contentEquals(str2);
    }
}
